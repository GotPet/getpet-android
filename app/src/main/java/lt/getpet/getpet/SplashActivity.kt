package lt.getpet.getpet

import android.os.Bundle
import android.widget.Toast
import io.reactivex.Single
import lt.getpet.getpet.navigation.NavigationManager
import lt.getpet.getpet.network.PetApiService
import lt.getpet.getpet.persistence.PetDao
import lt.getpet.getpet.preferences.AppPreferences
import timber.log.Timber
import javax.inject.Inject


class SplashActivity : BaseActivity() {

    @Inject
    lateinit var apiService: PetApiService

    @Inject
    lateinit var petsDao: PetDao

    @Inject
    lateinit var navigationManager: NavigationManager

    @Inject
    lateinit var appPreferences: AppPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()

        subscriptions.add(
                Single.fromCallable {
                    val user = authenticationManager.getCurrentUser()
                    if (user == null) {
                        authenticationManager.signInAnonymously(this)
                    } else {
                        Single.just(user)
                    }
                }.subscribeOn(ioScheduler)
                        .flatMap { it }
                        .flatMap {
                            apiService.getPets()
                        }.map { pets ->
                            petsDao.insertPets(pets)
                        }.observeOn(uiScheduler)
                        .subscribe({
                            onPetsLoaded()
                        }, { t ->
                            Timber.w(t)
                            Toast.makeText(this, "Error loading pets", Toast.LENGTH_LONG).show()
                        })
        )

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
