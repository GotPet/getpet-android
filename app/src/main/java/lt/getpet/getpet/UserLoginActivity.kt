package lt.getpet.getpet

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_user_login.*

class UserLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        activity_home.setOnClickListener({
            startActivity(Intent(this, MainActivity::class.java))
        })

        button_facebook.setOnClickListener({
            startActivity(Intent(this, MainActivity::class.java))
        })

        button_google.setOnClickListener({
            startActivity(Intent(this, MainActivity::class.java))
        })
    }
}
