package lt.getpet.getpet.network

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import lt.getpet.getpet.R
import lt.getpet.getpet.data.PetResponse

class GetPetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_pet)


        val pet = intent.getParcelableExtra<PetResponse>("pet")


    }
}
