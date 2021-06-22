package com.hf.airpods

import android.app.Application
import android.content.Context

class AirPodsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        lateinit var context: Context
    }
}