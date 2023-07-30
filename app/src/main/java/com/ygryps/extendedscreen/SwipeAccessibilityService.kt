package com.ygryps.extendedscreen

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class SwipeAccessibilityService : AccessibilityService() {
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        // Handle accessibility events if needed
    }

    override fun onInterrupt() {
        // This method is called when the service is interrupted.
    }

    fun performSwipe(displacementX: Float, displacementY: Float) {
        val swipeDuration = 100L // Set the desired duration for the swipe in milliseconds

        // Calculate the start and end points for the swipe based on displacement
        val startX = 200 // Set the X coordinate for the start point
        val startY = 200 // Set the Y coordinate for the start point
        val endX = startX + (displacementX * 100).toInt() // Adjust the multiplier to control the distance of the swipe
        val endY = startY + (displacementY * 100).toInt()

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
