package com.azizapp.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView

class ViewPagerAdapter(private var details: List<String>,
                       private var images: List<Int>) : RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>(){

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemDetails: TextView = itemView.findViewById(R.id.tvAbout)
        val itemImage: LottieAnimationView = itemView.findViewById(R.id.ivImage)
    }
