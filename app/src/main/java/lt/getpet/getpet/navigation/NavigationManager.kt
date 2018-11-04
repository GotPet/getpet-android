package lt.getpet.getpet.navigation

import android.app.Activity
import android.content.Intent
import lt.getpet.getpet.GetPetActivity
import lt.getpet.getpet.MainActivity
import lt.getpet.getpet.PetProfileActivity
import lt.getpet.getpet.authentication.AuthenticationManager
import lt.getpet.getpet.constants.ActivityConstants.Companion.EXTRA_PET
import lt.getpet.getpet.constants.ActivityConstants.Companion.EXTRA_SHOW_FAVORITE_BUTTON
import lt.getpet.getpet.constants.ActivityConstants.Companion.PET_FAVORITE
import lt.getpet.getpet.constants.ActivityConstants.Companion.RC_SIGN_IN
import lt.getpet.getpet.data.Pet

class NavigationManager(private val authenticationManager: AuthenticationManager) {

    fun navigateToMainActivity(activity: Activity) {
        val intent = Intent(activity, MainActivity::class.java)

        activity.startActivityForResult(intent, RC_SIGN_IN)
    }

    fun navigateToUserLoginActivity(activity: Activity) {
        val intent = authenticationManager.getAuthenticationActivityIntent()

        activity.startActivityForResult(intent, RC_SIGN_IN)
    }

    fun navigateToGetPetActivity(activity: Activity, pet: Pet) {
        val intent = Intent(activity, GetPetActivity::class.java).apply {
            putExtra(EXTRA_PET, pet)
        }
        activity.startActivity(intent)
    }

    fun navigateToPetProfileActivity(activity: Activity, pet: Pet, showFavoriteButton: Boolean) {
        val intent = Intent(activity, PetProfileActivity::class.java).apply {
            putExtra(EXTRA_PET, pet)
            putExtra(EXTRA_SHOW_FAVORITE_BUTTON, showFavoriteButton)
        }
        activity.startActivityForResult(intent, PET_FAVORITE)
    }
}