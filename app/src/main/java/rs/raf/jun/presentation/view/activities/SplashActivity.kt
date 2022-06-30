package rs.raf.jun.presentation.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import rs.raf.jun.R

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val pref = applicationContext.getSharedPreferences(packageName, MODE_PRIVATE)
        val isLoggedIn = pref.getString(LoginActivity.SharedPrefKeys.CREDENTIALS_KEY, null)

        if(isLoggedIn == "loggedIn"){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}