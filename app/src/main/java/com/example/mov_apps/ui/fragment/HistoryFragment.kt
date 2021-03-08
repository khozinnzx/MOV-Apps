package com.example.mov_apps.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mov_apps.R
import com.example.mov_apps.adapter.HistoryAdapter
import com.example.mov_apps.databinding.FragmentHistoryBinding
import com.example.mov_apps.model.MovieCheckout
import com.example.mov_apps.ui.MainActivity
import com.example.mov_apps.ui.MoviesViewModel
import com.example.mov_apps.utils.Resource
import com.google.firebase.auth.FirebaseAuth

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private lateinit var binding: FragmentHistoryBinding
    lateinit var viewModel: MoviesViewModel
    private lateinit var historyAdapter: HistoryAdapter
    private val auth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHistoryBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel

        viewModel.getMoviesCheckout(auth.currentUser?.uid)
        viewModel.listMoviesCheckout.observe(viewLifecycleOwner, Observer {
            if (it.data != null){
                when (it) {
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        it.data?.let { listCheckoutMovie ->
                            binding.tvJumlah.text = listCheckoutMovie.size.toString() + "tiket"
                            setupRvCheckout(listCheckoutMovie)

                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        it.message?.let { message ->
                            Log.d("HistoryFragment", "error rv checkout terjadi $message")

                        }
                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
            else{
                binding.progressBar.visibility = View.GONE
            }

        })


    }

    private fun setupRvCheckout(listCheckoutMovie: List<MovieCheckout>) {
        historyAdapter = HistoryAdapter(listCheckoutMovie)
        binding.rvHistory.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(activity)

        }
        historyAdapter.setOnItemClickListener { result ->
            findNavController().navigate(
                HistoryFragmentDirections.actionHistoryFragmentToDetailPurchasedFragment(
                    result
                )
            )
        }

    }
}