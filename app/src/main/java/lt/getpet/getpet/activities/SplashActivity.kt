package lt.getpet.getpet.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import lt.getpet.getpet.R
import lt.getpet.getpet.MainActivity
import android.content.Intent
import android.os.Handler


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(Runnable {
            val loadMainActivity = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(loadMainActivity)
            finish()
        }, 1500)
    }
}
