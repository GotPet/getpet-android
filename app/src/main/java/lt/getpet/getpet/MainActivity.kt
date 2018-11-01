package lt.getpet.getpet

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.disposables.Disposable
import lt.getpet.getpet.fragments.FavoritePetsFragment
import lt.getpet.getpet.fragments.UserProfileFragment
import lt.getpet.getpet.fragments.PetSwipeFragment
import lt.getpet.getpet.fragments.UserLoginFragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import lt.getpet.getpet.data.UserAccount


class MainActivity : BaseActivity(), UserLoginFragment.UserLoginCallback {


    private var userAccount: UserAccount? = null
    private var subscription: Disposable? = null
    private val tabsPagerAdapter: TabsPagerAdapter by lazy { TabsPagerAdapter(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_pager.adapter = tabsPagerAdapter

        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        view_pager.currentItem = 1
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription?.dispose()
    }

    override fun onUserLoggedIn(userAccount: UserAccount) {
        this.userAccount = userAccount
        Handler().post { tabsPagerAdapter.notifyDataSetChanged() }
    }

    private fun isLoggedIn(): Boolean {
        return userAccount != null
    }

    inner class TabsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    if (isLoggedIn()) {
                        UserProfileFragment.newInstance(userAccount!!)
                    } else {
                        UserLoginFragment.newInstance()
                    }
                }
                1 -> PetSwipeFragment.newInstance()
                2 -> FavoritePetsFragment.newInstance()
                else -> throw IllegalArgumentException("Tab for position $position doesn't exist")

            }
        }

        override fun getCount(): Int {
            return 3
        }

        override fun getItemPosition(item: Any): Int {
            if (item is UserProfileFragment && !isLoggedIn()) {
                return PagerAdapter.POSITION_NONE
            }

            if (item is UserLoginFragment && isLoggedIn()) {
                return PagerAdapter.POSITION_NONE
            }

            return super.getItemPosition(item)
        }

    }

}
