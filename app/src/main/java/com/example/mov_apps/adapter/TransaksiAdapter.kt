package com.example.mov_apps.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mov_apps.databinding.ItemTransaksiBinding
import com.example.mov_apps.model.MovieCheckout
import java.text.NumberFormat
import java.util.*

class TransaksiAdapter(val list: List<MovieCheckout>) :
    RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder>() {

    inner class TransaksiViewHolder(val binding: ItemTransaksiBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {
        return TransaksiViewHolder(
            ItemTransaksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        val transaksi = list[position]

        val localID = Locale("id", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localID)

        holder.itemView.apply {
            holder.binding.tvTransaksi.text = transaksi.title
            holder.binding.tvDate.text = transaksi.dateAndTime
            if (transaksi.status == "bayar"){
                holder.binding.tvHarga.text = "-" + formatRupiah.format(transaksi.harga)
                holder.binding.tvHarga.setTextColor(Color.RED)
            }else{
                holder.binding.tvHarga.text = "+" + formatRupiah.format(transaksi.harga)
                holder.binding.tvHarga.setTextColor(Color.GREEN)
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}