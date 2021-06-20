package com.azizapp.test.binding

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.azizapp.test.R
import java.util.*

@BindingAdapter("android:setTextColorFromStatus")
fun TextView.setColorText(status: String) {
    when (status.toLowerCase(Locale.ROOT)) {
        "laporan proses" -> setTextColor(ContextCompat.getColor(context, R.color.orange))

        "sedang diproses" -> setTextColor(ContextCompat.getColor(context, R.color.orange))

        "laporan selesai" -> setTextColor(ContextCompat.getColor(context, R.color.green))

        "pengajuan ditolak" -> setTextColor(ContextCompat.getColor(context, R.color.danger))

        "sudah diverifikasi" -> setTextColor(ContextCompat.getColor(context, R.color.orange))

        "verified" -> setTextColor(ContextCompat.getColor(context, R.color.orange))

        "belum diverifikasi" -> setTextColor(ContextCompat.getColor(context, R.color.black))

        "menunggu konfirmasi" -> setTextColor(ContextCompat.getColor(context, R.color.black))
    }
}