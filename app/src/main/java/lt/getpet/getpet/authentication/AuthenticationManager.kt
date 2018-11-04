package lt.getpet.getpet.authentication

import android.app.Activity
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
import lt.getpet.getpet.data.GuestUser
import lt.getpet.getpet.data.Provider
import lt.getpet.getpet.data.RegularUser
import lt.getpet.getpet.data.User
import timber.log.Timber

class AuthenticationManager {

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

        return user != null && user is RegularUser
    }

    fun signInAnonymously(activity: Activity): Single<GuestUser> {
        return Single.create { subscriber ->
            firebaseAuth.signInAnonymously()
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            val firebaseUser = task.result!!.user
                            val user = firebaseUser.mapToGuestUser()
                            Timber.i("Signed in anonymously $user}")

                            if (!subscriber.isDisposed) {
                                subscriber.onSuccess(user)
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
        val user = getCurrentUser()
        if (user is RegularUser) {
            authStateChangeListeners.forEach { it.onRegularUserLoggedIn(user) }
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

    private fun FirebaseUser.mapToUser(): User {
        if (this.isAnonymous) {
            return this.mapToGuestUser()
        }
        return this.mapToRegularUser()

    }

    private fun FirebaseUser.mapToGuestUser(): GuestUser {
        return GuestUser(userId = this.uid)
    }

    // TODO map to correct provider
    private fun FirebaseUser.mapToRegularUser(): RegularUser {
        return RegularUser(
                name = this.displayName!!,
                email = this.email!!,
                photo_url = this.photoUrl?.toString(),
                provider = Provider.GOOGLE
        )
    }

}