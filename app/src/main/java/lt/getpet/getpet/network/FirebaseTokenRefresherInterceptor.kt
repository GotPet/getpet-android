package lt.getpet.getpet.network

import lt.getpet.getpet.authentication.AuthenticationManager
import lt.getpet.getpet.network.ApiHeadersInterceptor.Companion.HTTP_HEADER_AUTHORIZATION
import lt.getpet.getpet.preferences.AppPreferences
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.net.HttpURLConnection
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

// Adapted from https://github.com/vilnius/tvarkau-vilniu/blob/master/app/src/main/java/lt/vilnius/tvarkau/auth/OauthTokenRefresher.kt
class FirebaseTokenRefresherInterceptor @Inject constructor(
        private val appPreferences: AppPreferences,
        private val authenticationManager: AuthenticationManager,
        private val apiHeadersInterceptor: ApiHeadersInterceptor
) : Interceptor {
    private val isRefreshing = AtomicBoolean(false)

    private val lock = Object()

    private var counter = 0

    private val queue = mutableListOf<CallbackLock<CallbackStatus>>()

    private fun applyToken(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder().removeHeader(HTTP_HEADER_AUTHORIZATION)
        apiHeadersInterceptor.applyToken(requestBuilder)
        return chain.proceed(requestBuilder.build())
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        Timber.i("Called FirebaseTokenRefresherInterceptor")
        val request = chain.request()
        val response = chain.proceed(request)
        val counter = this.counter++

        if (appPreferences.firebaseApiToken.isSet() && response.code() != HttpURLConnection.HTTP_UNAUTHORIZED) {
            return response
        }

        Timber.d("error $counter")

        val localLock = synchronized(lock) {
            val isRefreshingInAnotherThread = !isRefreshing.compareAndSet(false, true)
            if (isRefreshingInAnotherThread) {
                CallbackLock<CallbackStatus>().apply {
                    queue += this
                }
            } else {
                null
            }
        }

        if (localLock != null) {
            Timber.d("is refreshing in another thread $counter")
            val result = localLock.block()
            Timber.d("unlocked: $counter with result: $result")

            return if (result == CallbackStatus.ERROR) {
                response.newBuilder().code(500).build()
            } else {
                applyToken(chain)
            }
        }

        Timber.d("refresh $counter")

        val firebaseAPIToken = authenticationManager.getFirebaseAPIToken().blockingGet()
        appPreferences.firebaseApiToken.set(firebaseAPIToken, commit = true)

        synchronized(lock) {
            isRefreshing.set(false)
            queue.forEach { it.sendAndUnlock(CallbackStatus.SUCCESS) }
            queue.clear()
        }

        return applyToken(chain)
    }

    enum class CallbackStatus {
        SUCCESS, ERROR
    }

    class CallbackLock<T : Any> {
        @Volatile
        private var result: T? = null

        private val lock = Object()

        fun sendAndUnlock(result: T) {
            synchronized(lock) {
                if (this.result != null) throw IllegalStateException("Unlocked already")
                this.result = result
                lock.notifyAll()
            }
        }

        fun block(): T {
            synchronized(lock) {
                while (result == null) {
                    try {
                        lock.wait()
                    } catch (e: InterruptedException) {
                        //InterruptedException is using to stop sleep.
                    }
                }
            }
            return result!!
        }
    }

}