package com.example.mov_apps.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mov_apps.R
import com.example.mov_apps.databinding.ActivityOnBoardingTwoBinding
import com.example.mov_apps.ui.SignInActivity

class OnBoardingTwoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingTwoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingTwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLanjutkan.setOnClickListener {
            Intent(this, OnBoardingThreeActivity::class.java).also {
                startActivity(it)
            }
        }

    }
}