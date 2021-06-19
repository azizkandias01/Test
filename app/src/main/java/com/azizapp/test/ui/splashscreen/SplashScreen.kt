package com.azizapp.test.ui.splashscreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.azizapp.test.ui.welcome.MainActivity
import com.azizapp.test.R
import com.azizapp.test.ui.navigationbar.MainActivityNav
import com.azizapp.test.utill.Session
import kotlinx.android.synthetic.main.splashscreen.*

class SplashScreen : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)

        drainit.alpha = 0f
        drainit.animate().setDuration(3000).alpha(1f).withEndAction {
            if (Session.bearer != null) {
                val Intent = Intent(this, MainActivityNav::class.java)
                startActivity(Intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }else{
                val Intent = Intent(this, MainActivity::class.java)
                startActivity(Intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
    }
}