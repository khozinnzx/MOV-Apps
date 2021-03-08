package com.example.mov_apps.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.mov_apps.R
import com.example.mov_apps.ui.onboarding.OnBoardingOneActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

        val state = sharedPreferences.getBoolean("first_time", true)

        Handler(Looper.getMainLooper()).postDelayed({

            if (state) {
                Intent(this, OnBoardingOneActivity::class.java).also {
                    it.putExtra("extra_sharedPref", state)
                    startActivity(it)
                    finish()
                }
            } else {
                Intent(this, SignInActivity::class.java).also {
                    startActivity(it)
                    finish()
                }

            }
        }, 2000)
    }
}