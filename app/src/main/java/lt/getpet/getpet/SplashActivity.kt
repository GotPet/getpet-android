package lt.getpet.getpet

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import io.reactivex.disposables.Disposable
import lt.getpet.getpet.network.PetApiService
import lt.getpet.getpet.persistence.PetDao
import timber.log.Timber
import javax.inject.Inject


class SplashActivity : BaseActivity() {

    @Inject
    lateinit var apiService: PetApiService

    @Inject
    lateinit var petsDao: PetDao


    private var subscription: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        subscription = apiService.getPets()
                .subscribeOn(ioScheduler)
                .map { pets ->
                    petsDao.insertPets(pets)
                }
                .observeOn(uiScheduler)
                .subscribe({
                    showMainActivity()
                }, { t ->
                    Timber.w(t)
                    Toast.makeText(this, "Error loading pets", Toast.LENGTH_LONG).show()
                })
    }

    private fun showMainActivity() {
        val loadMainActivity = Intent(this@SplashActivity, MainActivity::class.java)

        startActivity(loadMainActivity)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription?.dispose()

    }
}
