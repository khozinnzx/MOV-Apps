package com.example.mov_apps.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mov_apps.R
import com.example.mov_apps.adapter.TransaksiAdapter
import com.example.mov_apps.databinding.FragmentMyWalletBinding
import com.example.mov_apps.model.MovieCheckout
import com.example.mov_apps.ui.MainActivity
import com.example.mov_apps.ui.MoviesViewModel
import com.example.mov_apps.utils.Resource
import com.google.firebase.auth.FirebaseAuth

class MyWalletFragment : Fragment(R.layout.fragment_my_wallet) {
    private lateinit var binding: FragmentMyWalletBinding
    lateinit var viewModel: MoviesViewModel
    private lateinit var transaksiAdapter: TransaksiAdapter
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        viewModel.getMoviesCheckout(auth.currentUser?.uid)

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


        viewModel.listMoviesCheckout.observe(viewLifecycleOwner, Observer {
            setupRvTransaksi(it.data)
        })

        binding.btnTOpUp.setOnClickListener {
            findNavController().navigate(R.id.action_myWalletFragment_to_topUpFragment)
        }


    }

    private fun setupRvTransaksi(data: List<MovieCheckout>?) {
        transaksiAdapter = TransaksiAdapter(data!!)
        binding.rvTransaksi.apply {
            adapter = transaksiAdapter
            layoutManager = LinearLayoutManager(activity)

        }

    }
}