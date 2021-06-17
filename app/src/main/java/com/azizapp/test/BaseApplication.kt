package com.azizapp.test

import android.app.Application
import com.azizapp.test.utill.Session
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Session.init(this)
    }
}