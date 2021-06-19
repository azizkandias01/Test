package com.azizapp.test.binding

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.layout_persistent_bottom_sheet.view.*

@BindingAdapter("app:loadImgFromUrl")
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