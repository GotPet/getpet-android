package lt.getpet.getpet.authentication

import android.app.Activity.RESULT_OK
import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Single
import lt.getpet.getpet.R
import lt.getpet.getpet.data.AuthenticationRequest
import lt.getpet.getpet.data.Provider
import lt.getpet.getpet.data.User
import lt.getpet.getpet.network.PetApiService
import lt.getpet.getpet.preferences.AppPreferences
import timber.log.Timber

class AuthenticationManager(
        private val petApiService: PetApiService,
        private val appPreferences: AppPreferences
) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val authStateChangeListeners = mutableListOf<AuthStateChangedListener>()

    private val firebaseProviders = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build()
    )

    fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.mapToUser()
    }

    fun addAuthStateChangeListener(listener: AuthStateChangedListener): Boolean {
        return authStateChangeListeners.add(listener)
    }

    fun removeAuthStateChangeListener(listener: AuthStateChangedListener): Boolean {
        return authStateChangeListeners.remove(listener)
    }

    fun isUserLoggedIn(): Boolean {
        val user = getCurrentUser()

        return user != null
    }

    fun isApiTokenSet(): Boolean {
        return appPreferences.apiToken.isSet()
    }

    fun refreshAndGetApiToken(): Single<ApiToken> {
        return getFirebaseAPIToken().flatMap { idToken ->
            petApiService.authenticate(AuthenticationRequest(idToken = idToken))
        }.map {
            ApiToken(token = it.key).apply {
                appPreferences.apiToken.set(this, commit = true)
            }
        }
    }

    private fun getFirebaseAPIToken(): Single<String> {
        return Single.create { subscriber ->
            firebaseAuth.getAccessToken(false).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (!subscriber.isDisposed) {
                        subscriber.onSuccess(task.result!!.token!!)
                    }
                } else {
                    if (!subscriber.isDisposed) {
                        subscriber.onError(task.exception!!)
                    }
                }
            }
        }
    }

    fun getAuthenticationActivityIntent(): Intent {
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(firebaseProviders)
                .setAlwaysShowSignInMethodScreen(true)
                .enableAnonymousUsersAutoUpgrade()
                .setTheme(R.style.AppTheme_Auth)
                .setLogo(R.drawable.get_pet_logo)
                .build()
    }

    fun onActivityResult(resultCode: Int, data: Intent?): Single<Boolean> {
        val response = IdpResponse.fromResultIntent(data)
                ?: // User pressed back button
                return Single.just(false)
        return when {
            resultCode == RESULT_OK -> {
                onUserLoggedIn()
                Single.just(true)
            }
            response.error!!.errorCode == ErrorCodes.ANONYMOUS_UPGRADE_MERGE_CONFLICT -> {
                Timber.i("Merge conflict: user already exists")
                val pendingCredential = response.credentialForLinking!!

                resolveMerge(pendingCredential)
            }
            else -> Single.error(response.error)
        }
    }

    private fun onUserLoggedIn() {
        getCurrentUser()?.let { user ->
            authStateChangeListeners.forEach { it.onUserLoggedIn(user) }
        }
    }

    private fun resolveMerge(pendingCredential: AuthCredential): Single<Boolean> {
        return Single.create { subscriber ->
            firebaseAuth.signInWithCredential(pendingCredential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onUserLoggedIn()
                            if (!subscriber.isDisposed) {
                                subscriber.onSuccess(true)
                            }
                        } else {
                            if (!subscriber.isDisposed) {
                                subscriber.onError(task.exception!!)
                            }
                        }

                    }
        }
    }

    // TODO map to correct provider
    private fun FirebaseUser.mapToUser(): User? {
        if (!this.isAnonymous) {
            return User(
                    name = this.displayName!!,
                    email = this.email!!,
                    photo_url = this.photoUrl?.toString(),
                    provider = Provider.GOOGLE
            )
        }

        return null
    }

}