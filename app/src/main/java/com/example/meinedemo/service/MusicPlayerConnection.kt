package com.example.meinedemo.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

class MusicPlayerConnection : ServiceConnection {

    private var musicPlayerApi: MusicPlayerApi? = null
    private var isBound = false

    fun isConnected(): Boolean = isBound

    fun getMusicPlayerApi(): MusicPlayerApi? {
        return musicPlayerApi
    }

    override fun onServiceDisconnected(name: ComponentName) {
        musicPlayerApi = null
        isBound = false
    }

    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        musicPlayerApi = service as MusicPlayerApi
        isBound = true
    }

    fun clear() {
        musicPlayerApi = null
        isBound = false
    }
}
