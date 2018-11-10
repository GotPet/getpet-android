package lt.getpet.getpet

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_onboarding.*
import lt.getpet.getpet.adapters.OnboardingAdapter
import lt.getpet.getpet.navigation.NavigationManager
import javax.inject.Inject
import androidx.viewpager.widget.ViewPager



class OnboardingActivity : BaseActivity() {

    @Inject
    lateinit var navigationManager: NavigationManager

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
               if(position == 4) button_next.setText(getString(R.string.button_end_onboarding))
                else button_next.setText(getString(R.string.button_next))
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        button_next.setOnClickListener {
            if (onboarding_viewpager.currentItem == 4) showMainActivity()
            else onboarding_viewpager.setCurrentItem(onboarding_viewpager.currentItem + 1, true)
        }
    }

    private fun showMainActivity() {
        navigationManager.navigateToMainActivity(this)
        finish()
    }
}