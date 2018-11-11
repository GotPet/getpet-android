package lt.getpet.getpet

import android.os.Bundle
import android.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_onboarding.*
import lt.getpet.getpet.adapters.OnboardingAdapter
import lt.getpet.getpet.navigation.NavigationManager
import javax.inject.Inject
import androidx.viewpager.widget.ViewPager
import lt.getpet.getpet.constants.ActivityConstants
import lt.getpet.getpet.preferences.AppPreferences


class OnboardingActivity : BaseActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val adapter = OnboardingAdapter(this, supportFragmentManager)

        onboarding_viewpager.adapter = adapter

        indicator.setViewPager(onboarding_viewpager)

        onboarding_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (position == adapter.count - 1) button_next.setText(getString(R.string.button_end_onboarding))
                else button_next.setText(getString(R.string.button_next))
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        button_next.setOnClickListener {
            if (onboarding_viewpager.currentItem == adapter.count - 1) {
                // User has seen OnboardingFragment, so mark our SharedPreferences
                // flag as completed so that we don't show our OnboardingFragment
                // the next time the user launches the app.
                appPreferences.onboardingShown.set(true)
                // End on boarding, go to main activity
                showMainActivity()
            } else onboarding_viewpager.setCurrentItem(onboarding_viewpager.currentItem + 1, true)
        }
    }

    private fun showMainActivity() {
        navigationManager.navigateToMainActivity(this)
        finish()
    }
}
