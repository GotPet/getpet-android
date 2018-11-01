package lt.getpet.getpet.navigation

import androidx.fragment.app.FragmentActivity
import com.firebase.ui.auth.AuthUI
import lt.getpet.getpet.R
import lt.getpet.getpet.constants.ActivityConstants.Companion.RC_SIGN_IN

class NavigationManager {
    fun navigateToUserLogin(activity: FragmentActivity) {
        val providers = listOf(
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.FacebookBuilder().build()
        )

        activity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setAlwaysShowSignInMethodScreen(true)
                        .setTheme(R.style.AppTheme_Auth)
                        .setLogo(R.drawable.get_pet_logo)
                        .build(),
                RC_SIGN_IN)
    }

}