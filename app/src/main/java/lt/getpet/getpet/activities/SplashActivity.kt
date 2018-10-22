package lt.getpet.getpet.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import lt.getpet.getpet.MainActivity
import lt.getpet.getpet.R
import lt.getpet.getpet.data.PetResponse
import lt.getpet.getpet.network.PetApiService


class SplashActivity : AppCompatActivity() {

    private var subscription: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        subscription = PetApiService.create().getPetResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    showPetResponse(it)
                }, {
                    Toast.makeText(this, "Error loading pets", Toast.LENGTH_SHORT).show()
                })
    }

    private fun showPetResponse(pets: List<PetResponse>) {
        val loadMainActivity = Intent(this@SplashActivity, MainActivity::class.java)
        MainActivity.pets = pets

        startActivity(loadMainActivity)
        finish()

    }

    override fun onDestroy() {
        super.onDestroy()
        subscription?.dispose()

    }
}
