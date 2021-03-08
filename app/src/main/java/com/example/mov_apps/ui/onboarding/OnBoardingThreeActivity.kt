package com.example.mov_apps.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mov_apps.R
import com.example.mov_apps.databinding.ActivityOnBoardingThreeBinding
import com.example.mov_apps.ui.SignInActivity

class OnBoardingThreeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingThreeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingThreeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMulai.setOnClickListener{
            finishAffinity()
            Intent(this, SignInActivity::class.java).also {
                startActivity(it)
            }
        }

    }
}