package com.hf.airpods

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class HideNotificationService : Service() {

    companion object {
        private const val TAG = "HideNotificationService"
    }

    inner class LocalBinder : Binder() {
        fun getService() = this@HideNotificationService
    }

    override fun onBind(intent: Intent?): IBinder = LocalBinder()

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}