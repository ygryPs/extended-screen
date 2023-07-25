package com.ygryps.extendedscreen

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AutoScrollService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}