package com.azizapp.test.ui.riwayat

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.azizapp.test.R
import com.azizapp.test.databinding.ActivityDetilRiwayatBinding
import com.azizapp.test.databinding.FragmentEditProfileBinding
import com.azizapp.test.databinding.FragmentRiwayatBinding
import com.azizapp.test.model.Pengaduan
import com.azizapp.test.ui.profile.EditProfileViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_detil_riwayat.*
import kotlinx.android.synthetic.main.layout_persistent_bottom_sheet.*
import kotlinx.android.synthetic.main.layout_persistent_bottom_sheet.gambar

@AndroidEntryPoint
class DetilRiwayat : AppCompatActivity() {

    lateinit var binding: ActivityDetilRiwayatBinding
    private val detilRiwayatViewModel: DetilRiwayatViewModel by viewModels()

    companion object{
        const val DETAIL_EXTRA_PARCEL = "DETAIL_EXTRA_PARCEL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detil_riwayat)

        val item: Pengaduan? = intent.getParcelableExtra(DETAIL_EXTRA_PARCEL)

        binding.apply {
            lifecycleOwner = this@DetilRiwayat
            data = item
        }
    }
}