package com.azizapp.test.ui.riwayat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azizapp.test.databinding.ItemListRiwayatBinding
import com.azizapp.test.model.Pengaduan

class RiwayatRecyclerAdapter(
    val riwayatViewModel: RiwayatViewModel
) : RecyclerView.Adapter<RiwayatRecyclerAdapter.RiwayatViewHolder>() {

    var items: ArrayList<Pengaduan>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RiwayatViewHolder {
        return RiwayatViewHolder(
            ItemListRiwayatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, itemPosition: Int) {
        val currentItem = items?.get(itemPosition)

        holder.binding.apply {
            viewModelRiwayat = riwayatViewModel
            position = itemPosition
            data = currentItem
        }
    }

    override fun getItemCount(): Int = items?.size ?: 0

    inner class RiwayatViewHolder(val binding: ItemListRiwayatBinding) :
        RecyclerView.ViewHolder(binding.root)
}