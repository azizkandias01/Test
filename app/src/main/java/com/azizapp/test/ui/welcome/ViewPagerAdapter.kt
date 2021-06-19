package com.azizapp.test.ui.welcome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.azizapp.test.R

class ViewPagerAdapter(
    private var details: List<String>,
    private var images: List<Int>
) : RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemDetails: TextView = itemView.findViewById(R.id.tvAbout)
        val itemImage: LottieAnimationView = itemView.findViewById(R.id.ivImage)

        init {
            itemImage.setOnClickListener {
                val position = absoluteAdapterPosition
                Toast.makeText(
                    itemView.context,
                    "You clicked on item #${position + 1}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Pager2ViewHolder {
        return Pager2ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_splash, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return details.size
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        holder.itemDetails.text = details[position]
        holder.itemImage.setAnimation(images[position])
    }
}