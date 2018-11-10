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
class OnboardingAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var mBundle: Bundle? = null
    private var mFragment: Fragment? = null
    private var mPosition: Int? = null

    override fun getItem(position: Int): Fragment? {
        mPosition = position
        when (position) {
            0 -> {
                mBundle = Bundle()
                mBundle!!.putInt(POSITION, 0)
                mFragment = OnBoardingFragment()
                mFragment!!.setArguments(mBundle)
                return mFragment
            }
            1 -> {
                mBundle = Bundle()
                mBundle!!.putInt(POSITION, 1)
                mFragment = OnBoardingFragment()
                mFragment!!.setArguments(mBundle)
                return mFragment
            }
            2 -> {
                mBundle = Bundle()
                mBundle!!.putInt(POSITION, 2)
                mFragment = OnBoardingFragment()
                mFragment!!.setArguments(mBundle)
                return mFragment
            }
            3 -> {
                mBundle = Bundle()
                mBundle!!.putInt(POSITION, 3)
                mFragment = OnBoardingFragment()
                mFragment!!.setArguments(mBundle)
                return mFragment
            }
            4 -> {
                mBundle = Bundle()
                mBundle!!.putInt(POSITION, 4)
                mFragment = OnBoardingFragment()
                mFragment!!.setArguments(mBundle)
                return mFragment
            }
            else -> return null
        }
    }

    override fun getCount(): Int {
        return 5
    }

}