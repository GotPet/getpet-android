package lt.getpet.getpet.network

import lt.getpet.getpet.preferences.AppPreferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiHeadersInterceptor @Inject constructor(
        private val appPreferences: AppPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        appendToken(chain, requestBuilder)

        return chain.proceed(requestBuilder.build())
    }

    private fun appendToken(chain: Interceptor.Chain, requestBuilder: Request.Builder) {
        val containsTokenHeader = chain.request().headers().names().contains(HTTP_HEADER_AUTHORIZATION)

        if (!containsTokenHeader && appPreferences.apiToken.isSet()) {
            applyToken(requestBuilder)
        }
    }


    private fun applyToken(requestBuilder: Request.Builder) {
        val apiToken = appPreferences.apiToken.get()

        requestBuilder.addHeader(HTTP_HEADER_AUTHORIZATION, formatTokenForHeader(apiToken.token))
    }

    private fun formatTokenForHeader(token: String) = "Token $token"

    companion object {
        const val HTTP_HEADER_AUTHORIZATION = "Authorization"
    }
}