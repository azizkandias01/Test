package com.azizapp.test.ui.laporan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.azizapp.test.R
import com.azizapp.test.ui.navigationbar.MainActivityNav
import com.azizapp.test.ui.navigationbar.MainActivityNavGuest
import kotlinx.android.synthetic.main.success_page.*

class SuccessPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.success_page)
        val type = intent.getStringExtra("type")
        btnKembali.setOnClickListener {
            when (type) {
                "login" -> {
                    val intent = Intent(this, MainActivityNav::class.java)
                    finishAffinity()
                    startActivity(intent)
                }
                "anonim" -> {
                    val intent = Intent(this, MainActivityNavGuest::class.java)
                    finishAffinity()
                    startActivity(intent)
                }
            }

        }
    }
}