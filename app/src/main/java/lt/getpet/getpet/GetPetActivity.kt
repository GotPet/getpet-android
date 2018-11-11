package lt.getpet.getpet

import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_get_pet.*
import lt.getpet.getpet.constants.ActivityConstants.Companion.EXTRA_PET
import lt.getpet.getpet.data.Pet

class GetPetActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_pet)


        val pet = intent.getParcelableExtra<Pet>(EXTRA_PET)

        Glide.with(this).load(pet.photo)
                .apply(RequestOptions.circleCropTransform())
                .into(get_pet_image)

        get_pet_name.text = pet.name
        get_pet_short_description.text = pet.shortDescription


        shelter_name.text = pet.shelter.name
        shelter_email.text = pet.shelter.email
        shelter_phone.text = pet.shelter.phone
    }
}
