package lt.getpet.getpet.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import lt.getpet.getpet.GetPetActivity
import lt.getpet.getpet.PetProfileActivity
import lt.getpet.getpet.authentication.AuthenticationManager
import lt.getpet.getpet.constants.ActivityConstants.Companion.EXTRA_PET
import lt.getpet.getpet.constants.ActivityConstants.Companion.EXTRA_SHOW_FAVORITE_BUTTON
import lt.getpet.getpet.constants.ActivityConstants.Companion.RC_SIGN_IN
import lt.getpet.getpet.data.Pet

class NavigationManager(private val authenticationManager: AuthenticationManager) {

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

    fun navigateToPetProfileActivity(context: Context, pet: Pet, showFavoriteButton: Boolean) {
        val intent = Intent(context, PetProfileActivity::class.java).apply {
            putExtra(EXTRA_PET, pet)
            putExtra(EXTRA_SHOW_FAVORITE_BUTTON, showFavoriteButton)
        }
        context.startActivity(intent)
    }
}