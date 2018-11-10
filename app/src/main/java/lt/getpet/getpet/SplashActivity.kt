package lt.getpet.getpet

import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import io.reactivex.Single
import lt.getpet.getpet.constants.ActivityConstants.Companion.COMPLETED_ONBOARDING_PREF_NAME
import lt.getpet.getpet.fragments.OnBoardingFragment
import lt.getpet.getpet.navigation.NavigationManager
import lt.getpet.getpet.network.PetApiService
import lt.getpet.getpet.persistence.PetDao
import timber.log.Timber
import javax.inject.Inject


class SplashActivity : BaseActivity() {

    @Inject
    lateinit var apiService: PetApiService

    @Inject
    lateinit var petsDao: PetDao

    @Inject
    lateinit var navigationManager: NavigationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        subscriptions.add(
                apiService.getPets()
                        .subscribeOn(ioScheduler)
                        .map { pets ->
                            petsDao.insertPets(pets)
                        }.flatMap {
                            val user = authenticationManager.getCurrentUser()

                            if (user == null) {
                                authenticationManager.signInAnonymously(this)
                            } else {
                                Single.just(user)
                            }
                        }
                        .observeOn(uiScheduler)
                        .subscribe({
                            showOnBoardingFragment()
                        }, { t ->
                            Timber.w(t)
                            Toast.makeText(this, "Error loading pets", Toast.LENGTH_LONG).show()
                        })
        )
    }

    private fun showMainActivity() {
        navigationManager.navigateToMainActivity(this)
        finish()
    }

    private fun showOnBoardingFragment() {
        PreferenceManager.getDefaultSharedPreferences(this).apply {
            // Check if we need to display our OnboardingFragment
            if (!getBoolean(COMPLETED_ONBOARDING_PREF_NAME, false)) {
                // The user hasn't seen the OnboardingFragment yet, so show it
                navigationManager.navigateToOnboardingActivity(this@SplashActivity)
            } else showMainActivity()
        }
    }
}
