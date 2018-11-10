package lt.getpet.getpet.fragments

import android.preference.PreferenceManager
import lt.getpet.getpet.constants.ActivityConstants.Companion.COMPLETED_ONBOARDING_PREF_NAME
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import kotlinx.android.synthetic.main.fragment_onboarding.view.*
import lt.getpet.getpet.R
import lt.getpet.getpet.constants.ActivityConstants.Companion.POSITION


/**
 * Created by Greta Grigutė on 2018-11-07.
 */
class OnBoardingFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, s: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_onboarding, container, false)

        val mPosition = getArguments()!!.getInt(POSITION);
        if (mPosition == 0) {
            rootView.onboarding_image.setImageResource(R.drawable.onboarding_one)
            rootView.onboarding_text_heading.setText(R.string.onboarding_1_heading)
            rootView.onboarding_text_main.setText(R.string.onboarding_1_text)

        } else if (mPosition == 1) {
            rootView.toolbar.visibility = VISIBLE
            rootView.onboarding_image.setImageResource(R.drawable.onboarding_two)
            rootView.onboarding_text_heading.setText(R.string.onboarding_2_heading)
            rootView.onboarding_text_main.setText(R.string.onboarding_2_text)

        } else if (mPosition == 2) {
            rootView.onboarding_image.setImageResource(R.drawable.onboarding_tree)
            rootView.onboarding_text_heading.setText(R.string.onboarding_3_heading)
            rootView.onboarding_text_main.setText(R.string.onboarding_3_text)

        } else if (mPosition == 3) {
            rootView.onboarding_image.setImageResource(R.drawable.onboarding_four)
            rootView.onboarding_text_heading.setText(R.string.onboarding_4_heading)
            rootView.onboarding_text_main.setText(R.string.onboarding_4_text)

        } else if (mPosition == 4) {
            rootView.onboarding_image.setImageResource(R.drawable.onboarding_five)
            rootView.onboarding_text_heading.setText(R.string.onboarding_5_heading)
            rootView.onboarding_text_main.setText(R.string.onboarding_5_text)

        }

        return rootView
    }

    override fun onDetach() {
        super.onDetach()
        // User has seen OnboardingFragment, so mark our SharedPreferences
        // flag as completed so that we don't show our OnboardingFragment
        // the next time the user launches the app.
        PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
            putBoolean(COMPLETED_ONBOARDING_PREF_NAME, true)
            apply()
        }
    }
}