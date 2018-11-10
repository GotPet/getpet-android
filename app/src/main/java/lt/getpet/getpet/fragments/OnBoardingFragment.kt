package lt.getpet.getpet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_onboarding.view.*
import lt.getpet.getpet.R
import lt.getpet.getpet.constants.ActivityConstants.Companion.POSITION
import java.lang.IndexOutOfBoundsException


/**
 * Created by Greta Grigutė on 2018-11-07.
 */
class OnBoardingFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, s: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_onboarding, container, false)

        val position = getArguments()!!.getInt(POSITION)
        when (position){
            0 -> {
                rootView.onboarding_image.setImageResource(R.drawable.onboarding_one)
                rootView.onboarding_text_heading.setText(R.string.onboarding_1_heading)
                rootView.onboarding_text_main.setText(R.string.onboarding_1_text)
            }

            1 -> {
                rootView.toolbar.visibility = VISIBLE
                rootView.onboarding_image.setImageResource(R.drawable.onboarding_two)
                rootView.onboarding_text_heading.setText(R.string.onboarding_2_heading)
                rootView.onboarding_text_main.setText(R.string.onboarding_2_text)
            }

            2 -> {
                rootView.onboarding_image.setImageResource(R.drawable.onboarding_tree)
                rootView.onboarding_text_heading.setText(R.string.onboarding_3_heading)
                rootView.onboarding_text_main.setText(R.string.onboarding_3_text)
            }

            3 -> {
                rootView.onboarding_image.setImageResource(R.drawable.onboarding_four)
                rootView.onboarding_text_heading.setText(R.string.onboarding_4_heading)
                rootView.onboarding_text_main.setText(R.string.onboarding_4_text)
            }

            4 -> {
                rootView.onboarding_image.setImageResource(R.drawable.onboarding_five)
                rootView.onboarding_text_heading.setText(R.string.onboarding_5_heading)
                rootView.onboarding_text_main.setText(R.string.onboarding_5_text)
            }

            else -> {val indexOutOfBoundsException = IndexOutOfBoundsException()
                throw indexOutOfBoundsException
            }
        }

        return rootView
    }
}