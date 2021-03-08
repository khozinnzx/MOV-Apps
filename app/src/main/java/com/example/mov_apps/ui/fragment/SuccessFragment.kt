package com.example.mov_apps.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mov_apps.R
import com.example.mov_apps.databinding.FragmentCheckoutBinding
import com.example.mov_apps.databinding.FragmentSuccessBinding

class SuccessFragment:Fragment(R.layout.fragment_success) {
    private lateinit var binding: FragmentSuccessBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSuccessBinding.bind(view)

        binding.btnHome.setOnClickListener {
            findNavController().navigate(R.id.action_successFragment_to_homeFragment)
        }

        binding.btnLihatTiket.setOnClickListener {
            findNavController().navigate(R.id.action_successFragment_to_historyFragment)
        }
    }
}