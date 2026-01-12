package com.motoacademy.android.qiet



import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.Looper
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var keepSplashOnScreen = true
        val delay = 2000L

        installSplashScreen().setKeepOnScreenCondition { keepSplashOnScreen }
        Handler(
            Looper.getMainLooper()
        ).postDelayed(
            {
                keepSplashOnScreen = false
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            },
            delay
        )


//        Handler(Looper.getMainLooper()).postDelayed({
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
//        }, 2000) //  =  2 segundos
    }
}
