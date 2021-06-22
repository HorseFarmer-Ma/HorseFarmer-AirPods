package com.hf.airpods

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import java.util.*


object PermissionUtils {
    @JvmStatic
    fun getBatteryOptimizationsPermission(context: Context): Boolean {
        return try {
            Objects.requireNonNull(context.getSystemService(PowerManager::class.java)).isIgnoringBatteryOptimizations(context.packageName)
        } catch (ignored: Throwable) {
            true
        }
    }

    @JvmStatic
    fun getFineLocationPermission(context: Context?): Boolean {
        return ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @JvmStatic
    @RequiresApi(api = Build.VERSION_CODES.Q)
    fun getBackgroundLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    @JvmStatic
    fun getNotifyPermission(context: Context): Boolean {
        val manager = NotificationManagerCompat.from(context)
        return manager.areNotificationsEnabled()
    }

    @JvmStatic
    fun checkAllPermissions(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            getBatteryOptimizationsPermission(context)
                    && getFineLocationPermission(context)
                    && getBackgroundLocationPermission(context)
                    && getNotifyPermission(context)
        else getBatteryOptimizationsPermission(context)
                && getFineLocationPermission(context)
                && getNotifyPermission(context)
    }
}