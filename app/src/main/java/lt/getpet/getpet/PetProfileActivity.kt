package lt.getpet.getpet

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_pet_profile.*
import lt.getpet.getpet.data.Pet

class PetProfileActivity : AppCompatActivity() {

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_profile)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val pet = intent.getParcelableExtra<Pet>(EXTRA_PET)
        Glide.with(this).load(pet.photo).into(pet_image)
        toolbar.title = pet.name
        toolbar_layout.title = pet.name

        pet_description.text = pet.description
        pet_short_description.text = pet.short_description


        button_get_pet.setOnClickListener {
            val i = Intent(this@PetProfileActivity, GetPetActivity::class.java)
            i.putExtra(EXTRA_PET, pet)
            startActivity(i)
        }

        fab_favorite_pet.setOnClickListener {
            setResult(RESULT_OK, Intent())
            finish()
        }

        if (intent.getBooleanExtra(EXTRA_SHOW_FAVORITE_BUTTON, false)) {
            fab_favorite_pet.visibility = View.VISIBLE
        } else {
            fab_favorite_pet.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_PET = "pet"
        private const val EXTRA_SHOW_FAVORITE_BUTTON = "EXTRA_SHOW_FAVORITE_BUTTON"

        fun getStartActivityIntent(context: Context, pet: Pet, showFavoriteButton: Boolean): Intent {
            return Intent(context, PetProfileActivity::class.java).apply {
                putExtra(EXTRA_PET, pet)
                putExtra(EXTRA_SHOW_FAVORITE_BUTTON, showFavoriteButton)
            }
        }
    }
}

