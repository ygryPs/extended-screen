package com.ygryps.extendedscreen

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class AutoScrollService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.d("AutoScrollService", "$this created")
        super.onCreate()
    }

    override fun onDestroy() {
        Log.d("AutoScrollService", "$this destroyed")
        super.onDestroy()
    }
}