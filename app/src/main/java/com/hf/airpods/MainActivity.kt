package com.hf.airpods

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatTextView
import java.util.*


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if Bluetooth LE is available on this device. If not, show an error
        val btAdapter: BluetoothAdapter = (getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter
        if (btAdapter.isEnabled && btAdapter.bluetoothLeScanner == null || !packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            startActivity(Intent(this@MainActivity, NoBTActivity::class.java))
            finish()
            return
        }

        if (PermissionUtils.checkAllPermissions(this)) {
            Starter.startPodsService(applicationContext)
        } else {
            startActivity(Intent(this@MainActivity, IntroActivity::class.java))
            finish()
        }

        if (RomUtil.isOppo()) {
            val bgPermissionBtn = findViewById<Button>(R.id.btn_get_full_background_permission)
            findViewById<AppCompatTextView>(R.id.btn_get_full_background_permission_tip).visibility = View.VISIBLE
            bgPermissionBtn.visibility = View.VISIBLE
            bgPermissionBtn.setOnClickListener {
                startActivity(Intent("android.settings.APPLICATION_DETAILS_SETTINGS").apply {
                    data = Uri.parse("package:$packageName")
                })
            }
        }
    }
}