package com.azizapp.test.utill

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar


fun ContentResolver.getFileName(uri: Uri): String {
    var name = ""
    var cursor = query(uri, null, null, null, null)
    cursor?.use {
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
    return name
}

fun View.snackbar(message: String) {
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_LONG
    ).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}

@BindingAdapter("app:loadImgFromUrl")
fun ImageView.loadImgFromUrl(url: String) {
    val fullUrl = "https://gis-drainase.pocari.id/storage/app/public/images/$url"
    Glide.with(this.context).load(fullUrl).into(this)
}