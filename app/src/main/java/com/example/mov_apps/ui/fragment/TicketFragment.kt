package com.example.mov_apps.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mov_apps.R
import com.example.mov_apps.adapter.ComingSoonAdapter
import com.example.mov_apps.adapter.NowPlayingAdapter
import com.example.mov_apps.databinding.FragmentHomeBinding
import com.example.mov_apps.databinding.FragmentTicketBinding
import com.example.mov_apps.model.Result
import com.example.mov_apps.ui.MainActivity
import com.example.mov_apps.ui.MoviesViewModel
import com.example.mov_apps.utils.Constant
import com.example.mov_apps.utils.Resource

class TicketFragment: Fragment(R.layout.fragment_ticket) {

    private lateinit var binding: FragmentTicketBinding
    lateinit var viewModel: MoviesViewModel
    lateinit var comingSoonAdapter: ComingSoonAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTicketBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel

        viewModel.moviesComingSoon.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    it.data?.let { listMovie ->
                        setupRvComingSoon(listMovie.results)
                        binding.tvTotalMovies.text = "${listMovie.results.size.toString()} Movies"
                    }

                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    it.message?.let { message ->
                        Log.d("HomeFragment", "error rv coming soon terjadi $message")

                    }
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }

        })

        binding.ivHistory.setOnClickListener {
            findNavController().navigate(R.id.action_ticketFragment_to_historyFragment)
        }
    }

    private fun setupRvComingSoon(item: List<Result>) {
        comingSoonAdapter = ComingSoonAdapter(item)
        binding.rvTicket.apply {
            adapter = comingSoonAdapter
            layoutManager = LinearLayoutManager(activity)

        }
        comingSoonAdapter.setOnItemClickListener { result ->
            val bundle = Bundle().apply {
                putSerializable("resultMovie", result)
            }
            findNavController().navigate(
                R.id.action_ticketFragment_to_detailFragment,
                bundle
            )
        }
    }
}