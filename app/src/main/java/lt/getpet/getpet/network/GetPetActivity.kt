package lt.getpet.getpet.network

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_get_pet.*
import kotlinx.android.synthetic.main.activity_pet_profile.*
import lt.getpet.getpet.R
import lt.getpet.getpet.data.PetResponse

class GetPetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_pet)


        val pet = intent.getParcelableExtra<PetResponse>("pet")

        Glide.with(this).load(pet.photo).into(get_pet_image)
        get_pet_name.text = pet.name
        get_pet_short_description.text = pet.short_description


        shelter_name.text = pet.shelter.name
        shelter_email.text = pet.shelter.email
        shelter_phone.text = pet.shelter.phone
    }
}
