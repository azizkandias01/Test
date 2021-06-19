package com.azizapp.test.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide

@BindingAdapter("loadImgFromUrl")
fun ImageView.loadImgFromUrl(url: String) {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    val fullUrl = "https://gis-drainase.pocari.id/storage/app/public/images/$url"

    Glide.with(this.context)
        .load(fullUrl)
        .placeholder(circularProgressDrawable)
        .into(this)
}