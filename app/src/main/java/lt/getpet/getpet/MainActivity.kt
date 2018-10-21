package lt.getpet.getpet

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), UserLoginFragment.UserLoginCallback {


//    private lateinit var pets: List<PetResponse>

    private var isLoggedIn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        pets = intent.extras.getParcelableArray(EXTRA_PETS).map { v -> v as PetResponse }.toList()


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
                if (!isLoggedIn) {
                    val fragment = supportFragmentManager.findFragmentByTag(TAG_USER_LOGIN_FRAGMENT)
                            ?: UserLoginFragment.newInstance()

                    replaceFragment(fragment, TAG_USER_LOGIN_FRAGMENT)
                } else {
                    val fragment = supportFragmentManager.findFragmentByTag(TAG_PROFILE_FRAGMENT)
                            ?: ProfileFragment.newInstance()

                    replaceFragment(fragment, TAG_PROFILE_FRAGMENT)
                }
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

    override fun onUserLoggedIn() {
        isLoggedIn = true
        setTab(0)
    }

    companion object {
        const val TAG_FRAGMENT_SWIPE = "TAG_FRAGMENT_SWIPE"
        const val TAG_FRAGMENT_FAVORITE_PETS = "TAG_FRAGMENT_FAVORITE_PETS"
        const val TAG_USER_LOGIN_FRAGMENT = "TAG_USER_LOGIN_FRAGMENT"
        const val TAG_PROFILE_FRAGMENT = "TAG_PROFILE_FRAGMENT"

        const val EXTRA_PETS = "EXTRA_PETS"
    }

}
