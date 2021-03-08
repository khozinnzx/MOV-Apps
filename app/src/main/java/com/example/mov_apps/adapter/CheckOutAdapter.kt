package com.example.mov_apps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mov_apps.databinding.ItemCheckoutBinding
import com.example.mov_apps.model.Checkout
import java.text.NumberFormat
import java.util.*

class CheckOutAdapter(val list: List<Checkout>) :
    RecyclerView.Adapter<CheckOutAdapter.CheckOutViewHolder>() {

    inner class CheckOutViewHolder(val binding: ItemCheckoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckOutViewHolder {
        return CheckOutViewHolder(
            ItemCheckoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CheckOutViewHolder, position: Int) {
        val seat = list[position]

        val localID = Locale("id", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localID)

        holder.itemView.apply {
            holder.binding.tvKursi.text = seat.kursi
            holder.binding.tvHarga.text = formatRupiah.format(seat.harga)

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}