package com.hf.airpods

import android.content.Context
import android.media.AudioManager

object AudioController {
    private val audioManager = AirPodsApplication.context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    /**
     * 获取全局音乐焦点
     */
    @JvmStatic
    fun obtainMusicFocalPoint() {
        if(audioManager.isMusicActive()){
            audioManager.requestAudioFocus( null, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
        }
    }

    /**
     * 释放全局音乐焦点
     */
    @JvmStatic
    fun releaseMusicFocalPoint() {
        audioManager.abandonAudioFocus(null)
    }
}
