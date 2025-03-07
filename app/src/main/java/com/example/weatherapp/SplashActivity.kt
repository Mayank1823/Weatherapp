package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.os.postDelayed

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
             val intent = Intent(this,MainActivity::class.java)  // passed from splash screen activity to main activity
            startActivity(intent)
            finish()  // application direct band kardega

        },3000)  // splash screen is seen for 3 seconds
    }
}