package com.example.mov_apps.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mov_apps.R
import com.example.mov_apps.databinding.FragmentDetailBinding
import com.example.mov_apps.model.Result

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private lateinit var binding: FragmentDetailBinding
    val args: DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)
        val movie = args.resultMovie

        setupView(movie)

        binding.btnPilihBangku.setOnClickListener {
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToPilihBangkuFragment(movie))
        }

    }

    private fun setupView(movie: Result) {
        Log.d("DetailFragment", "setupView: Success => ${movie.toString()}")
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
            .into(binding.ivImage)
        binding.tvTitle.text = movie.title
        binding.tvReleaseDate.text = movie.release_date
        binding.tvRating.text = movie.vote_average.toString()
        binding.tvDescription.text = movie.overview
    }
}