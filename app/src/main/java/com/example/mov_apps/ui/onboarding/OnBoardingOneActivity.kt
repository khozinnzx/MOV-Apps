package com.example.mov_apps.ui.onboarding

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mov_apps.R
import com.example.mov_apps.databinding.ActivityOnBoardingOneBinding
import com.example.mov_apps.ui.MainActivity
import com.example.mov_apps.ui.SignInActivity

class OnBoardingOneActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingOneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingOneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLanjutkan.setOnClickListener {
            Intent(this, OnBoardingTwoActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnLewati.setOnClickListener {
            finishAffinity()
            Intent(this, SignInActivity::class.java).also {
                startActivity(it)
            }
        }

    }
}