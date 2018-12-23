package lt.getpet.getpet

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import lt.getpet.getpet.fragments.FavoritePetsFragment
import lt.getpet.getpet.fragments.PetSwipeFragment
import lt.getpet.getpet.fragments.UserProfileFragment
import timber.log.Timber


class MainActivity : BaseActivity() {


    private val tabsPagerAdapter: TabsPagerAdapter by lazy { TabsPagerAdapter(supportFragmentManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_pager.adapter = tabsPagerAdapter

        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(view_pager))
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        view_pager.currentItem = 1
    }

    override fun onStart() {
        super.onStart()

        retrieveApiTokenIfNeeded()
    }

    private fun retrieveApiTokenIfNeeded() {
        if (authenticationManager.isUserLoggedIn() && !authenticationManager.isApiTokenSet()) {
            val disposable = authenticationManager.refreshAndGetApiToken()
                    .subscribeOn(ioScheduler)
                    .observeOn(uiScheduler)
                    .subscribe({
                        Timber.i("Api token retrieved")
                    }, {
                        Timber.w(it)
                        Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
                    })

            subscriptions.add(disposable)
        }
    }


    inner class TabsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {


        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> UserProfileFragment.newInstance()
                1 -> PetSwipeFragment.newInstance()
                2 -> FavoritePetsFragment.newInstance()
                else -> throw IllegalArgumentException("Tab for position $position doesn't exist")

            }
        }

        override fun getCount(): Int {
            return 3
        }

    }

}
