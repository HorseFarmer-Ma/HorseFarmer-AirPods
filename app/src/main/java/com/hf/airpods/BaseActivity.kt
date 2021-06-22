package com.hf.airpods

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        ColorUtils.autoStatusBarTextColor(this)
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        ColorUtils.autoStatusBarTextColor(this)
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        ColorUtils.autoStatusBarTextColor(this)
    }
}