package com.azizapp.test.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.azizapp.test.R
import com.azizapp.test.ui.daftar.DaftarActivity
import com.azizapp.test.ui.login.LoginActivity
import com.azizapp.test.ui.navigationbar.MainActivityNavGuest
import kotlinx.android.synthetic.main.splash.*
import me.relex.circleindicator.CircleIndicator3

class MainActivity : AppCompatActivity() {

    private var descList = mutableListOf<String>()
    private var imagesList = mutableListOf<Int>()

    var desc = arrayOf(
        "Laporkan kerusakan jaringan drainase manapun dengan mudah",
        "Ditangani langsung oleh Pemerintah Kota Pekanbaru"
    )
    var image = intArrayOf(R.raw.smartphone, R.raw.laborer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        postToList()

        view_pager2.adapter = ViewPagerAdapter(descList, imagesList)
        view_pager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicator = findViewById<CircleIndicator3>(R.id.indicator)
        indicator.setViewPager(view_pager2)

        btnBuatAkun.setOnClickListener {
            val intent = Intent(this, DaftarActivity::class.java)
            startActivity(intent)
        }

        linkMasuk.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        linkGuest.setOnClickListener {
            val intent = Intent(this, MainActivityNavGuest::class.java)
            startActivity(intent)
        }
    }

    private fun addToList(description: String, image: Int) {
        descList.add(description)
        imagesList.add(image)
    }

    private fun postToList() {
        for (i in 0..1) {
            addToList(desc[i], image[i])
        }
    }
}