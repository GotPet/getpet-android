package lt.getpet.getpet

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import lt.getpet.getpet.managers.ManageFavourites
import com.google.android.material.tabs.TabLayout.*
import io.reactivex.disposables.Disposable
import lt.getpet.getpet.fragments.PetFavoritesActivityFragment
import lt.getpet.getpet.fragments.ProfileFragment
import lt.getpet.getpet.fragments.SwipeFragment
import lt.getpet.getpet.fragments.UserLoginFragment

class MainActivity : AppCompatActivity(), UserLoginFragment.UserLoginCallback {

    private var isLoggedIn: Boolean = false
    private var subscription: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ManageFavourites(context = applicationContext).clear()

        replaceFragment(SwipeFragment.newInstance(), TAG_FRAGMENT_SWIPE)

        tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabReselected(tab: Tab) {
                setTab(tab.position)
            }

            override fun onTabUnselected(tab: Tab) {
                supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_SWIPE)

            }

            override fun onTabSelected(tab: Tab) {
                setTab(tab.position)
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription?.dispose()
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
        ft.addToBackStack(null)
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
    }

}
