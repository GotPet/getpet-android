package lt.getpet.getpet

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.activity_pet_profile.*
import lt.getpet.getpet.data.PetResponse
import lt.getpet.getpet.network.GetPetActivity

class PetProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_profile)
        setSupportActionBar(toolbar)

        activity_profile.setOnClickListener({
            startActivity(Intent(this, UserLoginActivity::class.java))
        })

        activity_home.setOnClickListener({
            startActivity(Intent(this, MainActivity::class.java))
        })

        val pet = intent.getParcelableExtra<PetResponse>("pet")
        Log.d("PetProfileActivity",pet.toString())
        Glide.with(this).load(pet.photo).into(pet_image)
        pet_name.text = pet.name
        pet_description.text = pet.description
        pet_short_description.text =pet.short_description


        button_get_pet.setOnClickListener { view ->
            val i = Intent(this@PetProfileActivity, GetPetActivity::class.java)
            i.putExtra("pet", pet)
            startActivity(i)
        }
    }
}
