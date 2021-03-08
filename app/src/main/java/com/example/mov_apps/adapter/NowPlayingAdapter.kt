package com.example.mov_apps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.mov_apps.databinding.ItemNowPlayingBinding
import com.example.mov_apps.model.Result

class NowPlayingAdapter(val list: List<Result>): RecyclerView.Adapter<NowPlayingAdapter.NowPlayingViewHolder>() {

    inner class NowPlayingViewHolder(val binding: ItemNowPlayingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowPlayingViewHolder {
        return NowPlayingViewHolder(
            ItemNowPlayingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NowPlayingViewHolder, position: Int) {
        val movie = list[position]
        holder.itemView.apply {
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                .transform(CenterCrop(), RoundedCorners(50))
                .into(holder.binding.ivPoster)
            holder.binding.tvTitle.text = movie.original_title
            holder.binding.tvDate.text = movie.release_date
            holder.binding.tvRate.text = movie.vote_average.toString()
            setOnClickListener{
                onItemClickListener?.let { it(movie) }
            }
        }
    }

    override fun getItemCount(): Int {
       return list.size
    }

    private var onItemClickListener: ((Result) -> Unit)? = null

    fun setOnItemClickListener(listener: (Result) -> Unit){
        onItemClickListener = listener
    }

}