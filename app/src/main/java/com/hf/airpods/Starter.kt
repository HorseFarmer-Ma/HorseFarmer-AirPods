package com.hf.airpods

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class Starter : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_MY_PACKAGE_REPLACED, Intent.ACTION_BOOT_COMPLETED -> Starter.startPodsService(context)
        }
    }

    companion object {
        fun startPodsService(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) context.startForegroundService(
                Intent(
                    context,
                    PodsService::class.java
                )
            ) else context.startService(
                Intent(
                    context,
                    PodsService::class.java
                )
            )
        }

        fun restartPodsService(context: Context) {
            context.stopService(Intent(context, PodsService::class.java))
            try {
                Thread.sleep(500)
            } catch (ignored: Throwable) {
            }
            startPodsService(context)
        }
    }
}