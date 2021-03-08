package com.example.mov_apps.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.mov_apps.R
import com.example.mov_apps.adapter.PurchasedAdapter
import com.example.mov_apps.databinding.FragmentDetailPurchasedBinding
import com.example.mov_apps.model.Checkout

class DetailPurchasedFragment : Fragment(R.layout.fragment_detail_purchased) {
    private lateinit var binding: FragmentDetailPurchasedBinding
    private lateinit var purchasedAdapter: PurchasedAdapter
    val args: DetailPurchasedFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailPurchasedBinding.bind(view)
        val checkout = args.checkoutMovie

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500${checkout.poster}")
            .transform(CenterCrop(), RoundedCorners(50))
            .into(binding.ivPoster)
        binding.tvTitle.text = checkout.title
        binding.tvRating.text = checkout.rating.toString()
        binding.tvDate.text = checkout.dateAndTime
        binding.tvCinema.text = checkout.place +", Cinema 3"
        setupRv(checkout.seat as ArrayList<Checkout>)


    }

    private fun setupRv(seatList: ArrayList<Checkout>) {
        purchasedAdapter = PurchasedAdapter(seatList)
        binding.rvSeat.apply {
            adapter = purchasedAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


}