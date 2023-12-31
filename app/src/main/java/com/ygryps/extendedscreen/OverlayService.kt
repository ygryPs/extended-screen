package com.ygryps.extendedscreen

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageButton
import kotlin.math.abs

class OverlayService : Service() {
    private lateinit var overlayView: View
    private var autoScrollRunning: Boolean = false

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.i("OverlayService", "OverlayService created")
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

        val touchListener = object : View.OnTouchListener {
            private val DRAG_THRESHOLD: Float = 10f
            private var offsetX: Float = 0f
            private var offsetY: Float = 0f
            private var initialTouchX: Float = 0f
            private var initialTouchY: Float = 0f
            private var isClick: Boolean = false

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.d("OverlayService", "pressed  - x: ${params.x}\t y: ${params.y}")
                        // Save the initial touch position
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        offsetX = params.x - event.rawX
                        offsetY = params.y - event.rawY
                        view.isPressed = true
                        isClick = true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        // Check if the finger has moved far enough to count as a drag
                        if (isClick && (abs(event.rawX - initialTouchX) > DRAG_THRESHOLD || abs(
                                event.rawY - initialTouchY
                            ) > DRAG_THRESHOLD)
                        ) {
                            view.isPressed = false
                            isClick = false
                        }

                        if (!isClick) {
                            // Calculate the new position of the overlay based on the touch movement
                            params.x = (event.rawX + offsetX).toInt()
                            params.y = (event.rawY + offsetY).toInt()

                            // Update the overlay position
                            windowManager.updateViewLayout(overlayView, params)
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        Log.d("OverlayService", "released - x: ${params.x}\t y: ${params.y}")
                        // Check if the touch event was a click
                        if (isClick) {
                            view.isPressed = false
                            // Perform click action here if needed
                            view.performClick()
                        }

                        val intent = Intent("com.ygryps.extendedscreen.ACTION_SWIPE")
                        intent.putExtra("displacementX", 100.0)
                        intent.putExtra("displacementY", 200.0)
                        sendBroadcast(intent)

                    }
                }
                return true
            }
        }

        // Set up touch listener to make the overlay draggable
        overlayView.setOnTouchListener(touchListener)

        // Set up button click listeners
        val btnToggleAutoScroll = overlayView.findViewById<ImageButton>(R.id.btnToggleAutoScroll)
        val btnCloseOverlay = overlayView.findViewById<ImageButton>(R.id.btnCloseOverlay)

        btnToggleAutoScroll.setOnTouchListener(touchListener)
        btnCloseOverlay.setOnTouchListener(touchListener)

        btnToggleAutoScroll.setOnClickListener {
            // Handle the action to toggle auto scrolling
            if (autoScrollRunning) {
                stopAutoScroll()
                autoScrollRunning = false
            } else {
                startAutoScroll()
                autoScrollRunning = true
            }
        }

        btnCloseOverlay.setOnClickListener {
            // Close the overlay and stop the service
            stopSelf()
        }
    }

    private fun startAutoScroll() {
        val btnToggleAutoScroll = overlayView.findViewById<ImageButton>(R.id.btnToggleAutoScroll)
        btnToggleAutoScroll.setImageResource(android.R.drawable.ic_media_pause)
        startService(Intent(applicationContext, AutoScrollService::class.java))
    }

    private fun stopAutoScroll() {
        val btnToggleAutoScroll = overlayView.findViewById<ImageButton>(R.id.btnToggleAutoScroll)
        btnToggleAutoScroll.setImageResource(android.R.drawable.ic_media_play)
        stopService(Intent(applicationContext, AutoScrollService::class.java))
    }


    override fun onDestroy() {
        Log.i("OverlayService", "OverlayService destroyed")
        super.onDestroy()

        if (autoScrollRunning) {
            stopAutoScroll()
        }
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.removeView(overlayView)
    }
}