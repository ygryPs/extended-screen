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
        override fun onSensorChanged(event: SensorEvent) {
            // This method is called whenever the sensor values change
            // Process the sensor data here
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // This method is called when the accuracy of the sensor changes
        }
    }

    override fun onCreate() {
        Log.d("AutoScrollService", "$this created")
        super.onCreate()

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(
            sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL
        ) // TODO: choose delay
    }

    override fun onDestroy() {
        Log.d("AutoScrollService", "$this destroyed")
        super.onDestroy()

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(sensorEventListener)
    }
}