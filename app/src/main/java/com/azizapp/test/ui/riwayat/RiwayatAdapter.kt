package com.azizapp.test.ui.riwayat

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azizapp.test.R
import com.azizapp.test.binding.setColorText
import com.azizapp.test.databinding.ItemListRiwayatBinding
import com.azizapp.test.model.Pengaduan

class RiwayatAdapter : RecyclerView.Adapter<RiwayatAdapter.ListViewHolder>() {

    private var mData = ArrayList<Pengaduan>()

    fun setData(items: ArrayList<Pengaduan>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListRiwayatBinding.bind(itemView)
        fun bind(items: Pengaduan) {
            binding.tvLokasi.text = items.namaJalan
            binding.tvJenisPengaduan.text = items.tipePengaduan
            binding.tvStatus.text = items.statusPengaduan
            binding.tvStatus.setColorText(items.statusPengaduan!!)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetilRiwayat::class.java)
                intent.putExtra(DetilRiwayat.DETAIL_EXTRA_PARCEL, items)

                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_riwayat, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}