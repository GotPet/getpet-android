package lt.getpet.getpet

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_pet_profile.*
import lt.getpet.getpet.constants.ActivityConstants.Companion.EXTRA_PET
import lt.getpet.getpet.constants.ActivityConstants.Companion.EXTRA_SHOW_FAVORITE_BUTTON
import lt.getpet.getpet.data.Pet
import lt.getpet.getpet.navigation.NavigationManager
import javax.inject.Inject

class PetProfileActivity : BaseActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

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
            if (authenticationManager.isUserLoggedIn()) {
                navigationManager.navigateToGetPetActivity(this@PetProfileActivity, pet)
            } else {
                navigationManager.navigateToUserLoginActivity(this@PetProfileActivity)
            }
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
}

