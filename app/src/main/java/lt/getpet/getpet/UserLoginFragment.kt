package lt.getpet.getpet

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class UserLoginFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        activity_home.setOnClickListener({
//            startActivity(Intent(this, MainActivity::class.java))
//        })
//
//        button_facebook.setOnClickListener({
//            startActivity(Intent(this, MainActivity::class.java))
//        })
//
//        button_google.setOnClickListener({
//            startActivity(Intent(this, MainActivity::class.java))
//        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_user_login, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = UserLoginFragment()
    }
}