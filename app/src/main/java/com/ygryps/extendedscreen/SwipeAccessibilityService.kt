package com.ygryps.extendedscreen

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class SwipeAccessibilityService : AccessibilityService() {
    private val mainHandler = Handler(Looper.getMainLooper())

    private val swipeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "com.ygryps.extendedscreen.ACTION_SWIPE") {
                val displacementX = intent.getFloatExtra("displacementX", 0f)
                val displacementY = intent.getFloatExtra("displacementY", 0f)
                performSwipe(displacementX, displacementY)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("SwipeService", "SwipeAccessibilityService created")

        // Register the broadcast receiver to listen for swipe requests
        val intentFilter = IntentFilter("com.ygryps.extendedscreen.ACTION_SWIPE")
        registerReceiver(swipeReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("SwipeService", "SwipeAccessibilityService destroyed")

        // Unregister the broadcast receiver when the service is destroyed
        unregisterReceiver(swipeReceiver)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        // Handle accessibility events if needed
    }

    override fun onInterrupt() {
        // This method is called when the service is interrupted.
    }

    fun performSwipe(displacementX: Float, displacementY: Float) {
        Log.d("SwipeService", "Sending swipe")
        val swipeDuration = 100L // Set the desired duration for the swipe in milliseconds

        // Calculate the start and end points for the swipe based on displacement
        val startX = 500 // Set the X coordinate for the start point
        val startY = 1000 // Set the Y coordinate for the start point
        val endX = startX + 200 + displacementX.toInt() // Adjust the multiplier to control the distance of the swipe
        val endY = startY + 400 + displacementY.toInt()

        // Create a Path representing the swipe gesture
        val path = Path()
        path.moveTo(startX.toFloat(), startY.toFloat())
        path.lineTo(endX.toFloat(), endY.toFloat())

        // Build the GestureDescription representing the swipe gesture
        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, 0, swipeDuration))

        // Dispatch the gesture on the main thread
        mainHandler.post {
            dispatchGesture(gestureBuilder.build(), null, null)
        }
    }
}
