package lt.getpet.getpet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.activity_pet_profile.*
import lt.getpet.getpet.data.Pet

class PetProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_profile)
        setSupportActionBar(toolbar)

//        activity_profile.setOnClickListener({
//            startActivity(Intent(this, UserLoginActivity::class.java))
//        })

        activity_home.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val pet = intent.getParcelableExtra<Pet>("pet")
        Glide.with(this).load(pet.photo).into(pet_image)
        pet_name.text = pet.name
        pet_description.text = pet.description
        pet_short_description.text = pet.short_description


        button_get_pet.setOnClickListener {
            val i = Intent(this@PetProfileActivity, GetPetActivity::class.java)
            i.putExtra("pet", pet)
            startActivity(i)
        }
    }
}
