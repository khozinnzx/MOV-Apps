package com.example.mov_apps.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mov_apps.R
import com.example.mov_apps.databinding.FragmentProfileBinding
import com.example.mov_apps.ui.MainActivity
import com.example.mov_apps.ui.MoviesViewModel
import com.example.mov_apps.utils.Resource
import java.text.NumberFormat
import java.util.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    lateinit var viewModel: MoviesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel

        viewModel.user.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.progressBarImage.visibility = View.INVISIBLE
                    it.data?.let { dataUser ->
                        binding.tvName.text = dataUser.nama
                        binding.tvEmail.text = dataUser.email
                        Glide.with(this).load(dataUser.url).placeholder(R.drawable.ic_profile)
                            .centerCrop()
                            .into(binding.ivPhoto)
                    }
                }
                is Resource.Error -> {
                    binding.progressBarImage.visibility = View.INVISIBLE
                    it.message?.let {
                        Log.d("ProfileFragment", "eror akses data user: ")
                    }
                }
                is Resource.Loading -> {
                    binding.progressBarImage.visibility = View.VISIBLE
                }
            }
        })

        binding.tvEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        binding.tvMyWallet.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_myWalletFragment)
        }


    }
}