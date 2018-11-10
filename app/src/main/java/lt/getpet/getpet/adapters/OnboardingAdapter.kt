package lt.getpet.getpet.adapters

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import lt.getpet.getpet.constants.ActivityConstants.Companion.POSITION
import lt.getpet.getpet.fragments.OnBoardingFragment


/**
 * Created by Greta Grigutė on 2018-11-10.
 */
class OnboardingAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return newInstance(position)
    }

    override fun getCount(): Int {
        return 5
    }

    companion object {
        @JvmStatic
        fun newInstance(position: Int): OnBoardingFragment{
            val bundle = Bundle()
            bundle.putInt(POSITION, position)
            val fragmentOnBoarding = OnBoardingFragment()
            fragmentOnBoarding.setArguments(bundle)
            return fragmentOnBoarding
        }
    }
}