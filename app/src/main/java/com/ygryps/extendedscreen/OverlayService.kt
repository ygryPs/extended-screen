package com.ygryps.extendedscreen

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageButton

class OverlayService : Service() {
    private lateinit var overlayView: View

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        // Create and set up the floating overlay layout
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay, FrameLayout(this), false)
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // Allow the overlay to be shown on top of other apps
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // The overlay should not receive touch events
            PixelFormat.TRANSLUCENT
        )

        // Add the overlay view to the window manager
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.addView(overlayView, params)

        // Set up button click listeners
        val btnToggleAutoScroll = overlayView.findViewById<ImageButton>(R.id.btnToggleAutoScroll)
        val btnCloseOverlay = overlayView.findViewById<ImageButton>(R.id.btnCloseOverlay)

        btnToggleAutoScroll.setOnClickListener { _ ->
            // Handle the action to toggle auto scrolling
            // Implement this functionality later
        }

        btnCloseOverlay.setOnClickListener { _ ->
            // Close the overlay and stop the service
            stopSelf()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.removeView(overlayView)
    }
}