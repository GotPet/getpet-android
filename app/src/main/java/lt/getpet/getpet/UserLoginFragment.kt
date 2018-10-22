package lt.getpet.getpet

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_user_login.*

class UserLoginFragment : Fragment() {

    private var callback: UserLoginCallback? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        callback = activity as UserLoginCallback


        button_facebook.setOnClickListener {
            callback?.onUserLoggedIn()
        }

        button_google.setOnClickListener {
            callback?.onUserLoggedIn()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_user_login, container, false)
    }


    interface UserLoginCallback {
        fun onUserLoggedIn()
    }

    companion object {
        @JvmStatic
        fun newInstance() = UserLoginFragment()
    }
}