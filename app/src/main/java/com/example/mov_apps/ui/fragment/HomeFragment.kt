package com.example.mov_apps.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mov_apps.R
import com.example.mov_apps.adapter.ComingSoonAdapter
import com.example.mov_apps.adapter.NowPlayingAdapter
import com.example.mov_apps.databinding.FragmentHomeBinding
import com.example.mov_apps.model.Result
import com.example.mov_apps.ui.MainActivity
import com.example.mov_apps.ui.MoviesViewModel
import com.example.mov_apps.utils.Constant.Companion.QUERY_PAGE_SIZE
import com.example.mov_apps.utils.Constant.Companion.QUERY_PAGE_SIZE2
import com.example.mov_apps.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import java.text.NumberFormat
import java.util.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: MoviesViewModel
    lateinit var nowPlayingAdapter: NowPlayingAdapter
    lateinit var comingSoonAdapter: ComingSoonAdapter
    private val auth = FirebaseAuth.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel
        viewModel.retrieveUser(auth.currentUser?.uid)

        viewModel.user.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.progressBarImage.visibility = View.INVISIBLE
                    it.data?.let { dataUser ->
                        binding.tvName.text = dataUser.nama
                        val localID = Locale("id", "ID")
                        val formatRupiah = NumberFormat.getCurrencyInstance(localID)
                        binding.tvSaldo.text = formatRupiah.format(dataUser.saldo)
                        Glide.with(this).load(dataUser.url).placeholder(R.drawable.ic_profile)
                            .centerCrop()
                            .into(binding.ivProfile)
                    }
                }
                is Resource.Error -> {
                    binding.progressBarImage.visibility = View.INVISIBLE
                    it.message?.let {
                        Log.d("HomeFragment", "eror akses data user: ")
                    }
                }
                is Resource.Loading -> {
                    binding.progressBarImage.visibility = View.VISIBLE
                }
            }
        })


        viewModel.moviesNowPlaying.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    isLoading = false
                    it.data?.let { movie ->
                        setupRvNowPlaying(movie.results)
                        val totalPages = movie.total_results / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.moviesNowPlayingPage == totalPages
                    }

                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    isLoading = false
                    it.message?.let { message ->
                        Log.d("HomeFragment", "error rv now playing terjadi $message")
                    }
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    isLoading = true
                }
            }

        })

        viewModel.moviesComingSoon.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar2.visibility = View.INVISIBLE
                    isLoading2 = false
                    it.data?.let { movie ->
                        setupRvComingSoon(movie.results)
                        val totalPages = movie.total_results / QUERY_PAGE_SIZE + 2
                        isLastPage2 = viewModel.moviesComingSoonPage == totalPages
                    }

                }
                is Resource.Error -> {
                    binding.progressBar2.visibility = View.INVISIBLE
                    isLoading2 = false
                    it.message?.let { message ->
                        Log.d("HomeFragment", "error rv coming soon terjadi $message")

                    }
                }
                is Resource.Loading -> {
                    binding.progressBar2.visibility = View.VISIBLE
                    isLoading2 = true
                }
            }

        })

    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListenerNowPlaying = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getMoviesNowPlaying()
                isScrolling = false
            }

        }
    }

    var isLoading2 = false
    var isLastPage2 = false
    var isScrolling2 = false

    val scrollListenerComingSoon = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling2 = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading2 && !isLastPage2
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE2
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling2
            if (shouldPaginate) {
                viewModel.getMoviesComingSoon()
                isScrolling2 = false
            }

        }
    }


    private fun setupRvNowPlaying(item: List<Result>) {
        nowPlayingAdapter = NowPlayingAdapter(item)
        binding.rvNowPlaying.apply {
            adapter = nowPlayingAdapter
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            addOnScrollListener(this@HomeFragment.scrollListenerNowPlaying)

        }
        nowPlayingAdapter.setOnItemClickListener { result ->
            val bundle = Bundle().apply {
                putSerializable("resultMovie", result)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_detailFragment,
                bundle
            )
        }

    }


    private fun setupRvComingSoon(item: List<Result>) {
        comingSoonAdapter = ComingSoonAdapter(item)
        binding.rvComingSoon.apply {
            adapter = comingSoonAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HomeFragment.scrollListenerComingSoon)

        }
        comingSoonAdapter.setOnItemClickListener { result ->
            val bundle = Bundle().apply {
                putSerializable("resultMovie", result)
            }
            findNavController().navigate(
                R.id.action_homeFragment_to_detailFragment,
                bundle
            )
        }
    }


}