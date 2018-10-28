package lt.getpet.getpet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.person_account.*
import lt.getpet.getpet.R
import lt.getpet.getpet.data.UserAccount

class UserProfileFragment : Fragment() {

    private lateinit var userAccount: UserAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userAccount = arguments!!.getParcelable(EXTRA_PERSON_ACCOUNT)!!
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.person_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        user_name.text = userAccount.name
        if (userAccount.photo_url != null) {
            Glide.with(this).load(userAccount.photo_url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(user_photo)
        }
    }

    companion object {
        private const val EXTRA_PERSON_ACCOUNT = "EXTRA_PERSON_ACCOUNT"

        @JvmStatic
        fun newInstance(userAccount: UserAccount): UserProfileFragment {
            val bundle = Bundle().apply {
                putParcelable(EXTRA_PERSON_ACCOUNT, userAccount)
            }

            return UserProfileFragment().apply {
                arguments = bundle
            }
        }
    }


}