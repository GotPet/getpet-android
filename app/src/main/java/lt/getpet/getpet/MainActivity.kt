package lt.getpet.getpet

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import lt.getpet.getpet.data.PetResponse

class MainActivity : AppCompatActivity() {

    private lateinit var pets: List<PetResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pets = intent.extras.getParcelableArray(EXTRA_PETS).map { v -> v as PetResponse }.toList()


        replaceFragment(SwipeFragment.newInstance(), TAG_FRAGMENT_SWIPE)

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
                setTab(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {


            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                setTab(tab.position)
            }

        })
    }

    fun setTab(position: Int) {
        when (position) {
            0 -> {
                val fragment = supportFragmentManager.findFragmentByTag(TAG_USER_FRAGMENT)
                        ?: UserLoginFragment.newInstance()

                replaceFragment(fragment, TAG_USER_FRAGMENT)
            }
            1 -> {
                val fragment = supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_SWIPE)
                        ?: SwipeFragment.newInstance()

                replaceFragment(fragment, TAG_FRAGMENT_SWIPE)
            }
            2 -> {
                val fragment = supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_FAVORITE_PETS)
                        ?: PetFavoritesActivityFragment.newInstance()

                replaceFragment(fragment, TAG_FRAGMENT_FAVORITE_PETS)
            }
        }
    }

    fun replaceFragment(fragment: Fragment, tag: String) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.frame_container, fragment, tag)
        ft.commit()
    }

    companion object {
        const val TAG_FRAGMENT_SWIPE = "TAG_FRAGMENT_SWIPE"
        const val TAG_FRAGMENT_FAVORITE_PETS = "TAG_FRAGMENT_FAVORITE_PETS"
        const val TAG_USER_FRAGMENT = "TAG_USER_FRAGMENT"

        const val EXTRA_PETS = "EXTRA_PETS"
    }

}
