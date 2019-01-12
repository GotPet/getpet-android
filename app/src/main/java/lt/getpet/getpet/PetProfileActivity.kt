package lt.getpet.getpet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_pet_profile.*
import lt.getpet.getpet.adapters.PetPhotosAdapter
import lt.getpet.getpet.constants.ActivityConstants.Companion.EXTRA_PET
import lt.getpet.getpet.constants.ActivityConstants.Companion.EXTRA_SHOW_GET_PET_BUTTON
import lt.getpet.getpet.data.Pet
import lt.getpet.getpet.navigation.NavigationManager
import lt.getpet.getpet.services.PetsService
import timber.log.Timber
import javax.inject.Inject

class PetProfileActivity : BaseActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    @Inject
    lateinit var petsService: PetsService

    val pet: Pet by lazy {
        intent.getParcelableExtra<Pet>(EXTRA_PET)
    }

    val isGetPetButtonShown: Boolean by lazy {
        intent.getBooleanExtra(EXTRA_SHOW_GET_PET_BUTTON, false)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_profile)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val adapter = PetPhotosAdapter(this, pet.allPhotos())

        photos_viewpager.adapter = adapter

        photos_indicator.setViewPager(photos_viewpager)

        toolbar.title = pet.name
        toolbar_layout.title = pet.name

        pet_description.text = pet.description
        pet_short_description.text = pet.shortDescription


        button_get_pet.setOnClickListener {
            if (authenticationManager.isUserLoggedIn()) {
                navigationManager.navigateToGetPetActivity(this@PetProfileActivity, pet)
            } else {
                navigationManager.navigateToUserLoginActivity(this@PetProfileActivity)
            }
        }

        fab_favorite_pet.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        if (isGetPetButtonShown) {
            fab_favorite_pet.visibility = View.GONE
            button_get_pet.visibility = View.VISIBLE
        } else {
            fab_favorite_pet.visibility = View.VISIBLE
            button_get_pet.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_pet) {
            val disposable = petsService.savePetChoice(pet, false)
                    .subscribeOn(ioScheduler)
                    .observeOn(uiScheduler)
                    .subscribe({
                        finish()
                    }, {
                        Timber.w(it)
                    })

            subscriptions.add(disposable)

            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isGetPetButtonShown) {
            menuInflater.inflate(R.menu.profile_menu, menu)
        }

        return true
    }
}

