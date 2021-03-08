package com.example.mov_apps.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.mov_apps.R
import com.example.mov_apps.databinding.FragmentMyWalletBinding
import com.example.mov_apps.ui.MainActivity
import com.example.mov_apps.ui.MoviesViewModel
import com.example.mov_apps.utils.Resource

class MyWalletFragment: Fragment(R.layout.fragment_my_wallet) {
    private lateinit var binding: FragmentMyWalletBinding
    lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyWalletBinding.bind(view)

        viewModel.user.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { dataUser ->
                        binding.tvSaldo.text = dataUser.saldo.toString()
                    }
                }
                is Resource.Error -> {
                    it.message?.let {
                        Log.d("ProfileFragment", "eror akses data user: ")
                    }
                }
            }
        })


    }
}