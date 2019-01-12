package lt.getpet.getpet

import android.os.Bundle
import android.widget.Toast
import lt.getpet.getpet.navigation.NavigationManager
import lt.getpet.getpet.preferences.AppPreferences
import lt.getpet.getpet.services.PetsService
import timber.log.Timber
import javax.inject.Inject


class SplashActivity : BaseActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var petsService: PetsService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()

        val disposable = petsService.generatePetsToSwipe()
                .observeOn(uiScheduler)
                .subscribeOn(ioScheduler)
                .subscribe({
                    onPetsLoaded()
                }, { t ->
                    Timber.w(t)
                    Toast.makeText(this, "Error loading pets", Toast.LENGTH_LONG).show()
                })

        subscriptions.add(disposable)
    }

    private fun onPetsLoaded() {
        if (appPreferences.onboardingShown.get()) {
            navigationManager.navigateToMainActivity(this)
        } else {
            navigationManager.navigateToOnboardingActivity(this)
        }
        finish()
    }

}
