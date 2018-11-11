package lt.getpet.getpet.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import lt.getpet.getpet.fragments.OnBoardingFragment


/**
 * Created by Greta Grigutė on 2018-11-10.
 */
class OnboardingAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return OnBoardingFragment.newInstance(position)
    }

    override fun getCount(): Int {
        return 5
    }

}