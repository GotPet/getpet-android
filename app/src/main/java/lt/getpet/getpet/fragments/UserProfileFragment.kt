package lt.getpet.getpet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_user_profile.*
import lt.getpet.getpet.R
import lt.getpet.getpet.authentication.AuthStateChangedListener
import lt.getpet.getpet.authentication.AuthenticationManager
import lt.getpet.getpet.data.RegularUser
import lt.getpet.getpet.navigation.NavigationManager
import javax.inject.Inject

class UserProfileFragment : BaseFragment(), AuthStateChangedListener {

    @Inject
    lateinit var authenticationManager: AuthenticationManager

    @Inject
    lateinit var navigationManager: NavigationManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onStart() {
        super.onStart()

        authenticationManager.addAuthStateChangeListener(this)

        val user = authenticationManager.getCurrentUser()
        when (user) {
            is RegularUser -> showUserProfile(user)
            else -> showAnonymousProfile()
        }

        button_login.setOnClickListener {
            navigationManager.navigateToUserLoginActivity(activity!!)
        }
    }

    override fun onStop() {
        authenticationManager.removeAuthStateChangeListener(this)
        super.onStop()
    }

    private fun showAnonymousProfile() {
        user_name.setText(R.string.anonymous_user_name)
        button_login.visibility = View.VISIBLE
        Glide.with(this).load(R.drawable.anonymous_avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(user_photo)
    }

    private fun showUserProfile(user: RegularUser) {
        user_name.text = user.name
        button_login.visibility = View.GONE

        if (user.photo_url != null) {
            Glide.with(this).load(user.photo_url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(user_photo)
        }
    }

    override fun onRegularUserLoggedIn(regularUser: RegularUser) {
        showUserProfile(regularUser)
    }

    override fun onAuthError(throwable: Throwable) {
        Toast.makeText(context, throwable.localizedMessage, Toast.LENGTH_LONG).show()
    }

    companion object {

        @JvmStatic
        fun newInstance(): UserProfileFragment {
            return UserProfileFragment()
        }
    }


}