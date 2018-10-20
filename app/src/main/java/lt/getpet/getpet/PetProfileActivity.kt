package lt.getpet.getpet

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_pet_profile.*
import lt.getpet.getpet.data.PetResponse

class PetProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_profile)
        setSupportActionBar(toolbar)

        val pet = intent.getParcelableExtra<PetResponse>("pet")
        Log.d("PetProfileActivity",pet.toString())

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

}
