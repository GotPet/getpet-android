package lt.getpet.getpet.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import lt.getpet.getpet.R
import lt.getpet.getpet.data.PetResponse
import lt.getpet.getpet.network.PetApiService

class MainActivity : AppCompatActivity() {

    private var subscription: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadPets()
    }

    fun loadPets() {
        subscription = PetApiService.create().getPetResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    showPetResponse(it)
                }, {
                    Log.e("Error", "Error loading pets", it)
                    showNoPets()
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (subscription != null) {
            subscription?.dispose()
        }
    }

    fun showPetResponse(petsList: List<PetResponse>) {
        val petMessage = "${petsList[0].id} ${petsList[0].shelter.email}"
        message.text = petMessage
    }

    fun showNoPets() {
        message.text = "Opps, something wrong happened"
    }
}

