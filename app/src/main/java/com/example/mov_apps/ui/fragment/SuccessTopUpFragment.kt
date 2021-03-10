package com.example.mov_apps.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mov_apps.R
import com.example.mov_apps.adapter.TransaksiAdapter
import com.example.mov_apps.databinding.FragmentMyWalletBinding
import com.example.mov_apps.databinding.FragmentSuccessTopUpBinding
import com.example.mov_apps.ui.MoviesViewModel
import com.google.firebase.auth.FirebaseAuth

class SuccessTopUpFragment : Fragment(R.layout.fragment_success_top_up) {
    private lateinit var binding: FragmentSuccessTopUpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSuccessTopUpBinding.bind(view)

        binding.btnHome.setOnClickListener {
            findNavController().navigate(R.id.action_successTopUpFragment_to_homeFragment)
        }

    }
}