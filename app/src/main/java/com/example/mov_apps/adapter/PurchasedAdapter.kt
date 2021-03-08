package com.example.mov_apps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mov_apps.databinding.ItemSeatBinding
import com.example.mov_apps.model.Checkout
import java.text.NumberFormat
import java.util.*

class PurchasedAdapter(val list: List<Checkout>) :
    RecyclerView.Adapter<PurchasedAdapter.CheckOutViewHolder>() {

    inner class CheckOutViewHolder(val binding: ItemSeatBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckOutViewHolder {
        return CheckOutViewHolder(
            ItemSeatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CheckOutViewHolder, position: Int) {
        val seat = list[position]

        holder.itemView.apply {
            holder.binding.tvKursi.text = seat.kursi

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}