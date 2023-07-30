package com.ygryps.extendedscreen

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log

class AutoScrollService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private val sensorEventListener = object : SensorEventListener {
        private var velocityX = 0f
        private var velocityY = 0f
        private var displacementX = 0f
        private var displacementY = 0f
        private val SECS_PER_NS = 1.0f / 1_000_000_000.0f
        var lastTimestamp: Long = 0 // remember to initialize when registering listener

        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
                // Get the acceleration values along the X and Y axes
                val accelerationX = event.values[0]
                val accelerationY = event.values[1]

                // Get the time elapsed since the last sensor update (in seconds)
                val deltaTime = (event.timestamp - lastTimestamp) * SECS_PER_NS
                lastTimestamp = event.timestamp

                // Update velocity based on the acceleration
                velocityX += accelerationX * deltaTime
                velocityY += accelerationY * deltaTime

                // Update displacement based on the velocity
                displacementX += velocityX * deltaTime
                displacementY += velocityY * deltaTime

                // Use the displacement values for your application logic
                // For example, update the position of a view, etc.
                Log.d("AutoScrollService", "DisplacementX: %.2f, DisplacementY: %.2f".format(displacementX, displacementY))
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // This method is called when the accuracy of the sensor changes
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("AutoScrollService", "AutoScrollService created")

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        sensorEventListener.lastTimestamp = System.nanoTime()
        sensorManager.registerListener(
            sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL
        ) // TODO: choose delay
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("AutoScrollService", "AutoScrollService destroyed")

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(sensorEventListener)
    }
}