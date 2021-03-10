package com.example.mov_apps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.mov_apps.databinding.ItemComingSoonBinding
import com.example.mov_apps.model.MovieCheckout
import com.example.mov_apps.model.Result

class HistoryAdapter(val list: List<MovieCheckout>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(val binding: ItemComingSoonBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            ItemComingSoonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val list = list[position]
        holder.itemView.apply {
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500${list.poster}")
                .transform(CenterCrop(), RoundedCorners(50))
                .into(holder.binding.ivPoster)
            holder.binding.tvTitle.text = list.title
            holder.binding.tvDate.text = list.dateAndTime
            holder.binding.tvRate.text = list.rating.toString()
            setOnClickListener {
                onItemClickListener?.let { it(list) }
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    private var onItemClickListener: ((MovieCheckout) -> Unit)? = null

    fun setOnItemClickListener(listener: (MovieCheckout) -> Unit) {
        onItemClickListener = listener
    }

}