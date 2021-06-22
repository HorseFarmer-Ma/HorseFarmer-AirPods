package com.hf.airpods;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Notification.EXTRA_CHANNEL_ID;
import static android.provider.Settings.EXTRA_APP_PACKAGE;

public class IntroActivity extends BaseActivity {

    private Timer timer;
    private TextView msg;
    private Button btn;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        msg = findViewById(R.id.permsMsg);
        btn = findViewById(R.id.permsBtn);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run () {
                initScreen();

                if (PermissionUtils.checkAllPermissions(IntroActivity.this)) {
                    timer.cancel();
                    startActivity(new Intent(IntroActivity.this, MainActivity.class));
                    finish();
                }
            }
        }, 0, 100);
    }

    // Activity destroyed (or screen rotated). destroy the timer too
    @Override
    protected void onDestroy () {
        super.onDestroy();
        if (timer != null)
            timer.cancel();
    }

    private int getPermissionState () {
        if (!PermissionUtils.getBatteryOptimizationsPermission(this))
            return 1;
        else if (!PermissionUtils.getFineLocationPermission(this))
            return 2;
        else if (!PermissionUtils.getNotifyPermission(this))
            return 3;
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            if (!PermissionUtils.getBackgroundLocationPermission(this))
            return 4;

        return 0;
    }

    @SuppressLint("BatteryLife")
    private void initScreen () {
        int step = getPermissionState();
        int numOfSteps = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ? 4 : 3;

        switch (step) {
            case 1:
                msg.setText(String.format(Locale.getDefault(), "%s %d/%d: %s", getString(R.string.intro_step), step, numOfSteps, getString(R.string.intro_bat_perm)));
                btn.setOnClickListener(view -> {
                    Intent intent = new Intent();
                    getSystemService(Context.POWER_SERVICE);
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                });
                break;
            case 2:
                msg.setText(String.format(Locale.getDefault(), "%s %d/%d: %s", getString(R.string.intro_step), step, numOfSteps, getString(R.string.intro_loc1_perm)));
                btn.setOnClickListener(view -> requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 101)); // Location (for BLE)
                break;
            case 3:
                msg.setText(String.format(Locale.getDefault(), "%s %d/%d: %s", getString(R.string.intro_step), step, numOfSteps, getString(R.string.intro_loc2_perm)));
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                            intent.putExtra(EXTRA_APP_PACKAGE, getPackageName());
                            intent.putExtra(EXTRA_CHANNEL_ID, getApplicationInfo().uid);
                        }
                        intent.putExtra("app_package", getPackageName());
                        intent.putExtra("app_uid", getApplicationInfo().uid);
                        startActivity(intent);
                    }
                });
                break;
            case 4:
                msg.setText(String.format(Locale.getDefault(), "%s %d/%d: %s", getString(R.string.intro_step), step, numOfSteps, getString(R.string.intro_loc3_perm)));
                btn.setOnClickListener(view -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) requestPermissions(new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 102);
                });
                break;
        }

        runOnUiThread(() -> btn.setText(String.format(Locale.getDefault(), "%s (%d/%d)", getString(R.string.intro_allow), step, numOfSteps)));
    }
}
