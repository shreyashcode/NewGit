package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {

    lateinit var sm: SensorManager
    lateinit var accelSenor: Sensor
    lateinit var sensorEventListener: SensorEventListener
    lateinit var discoBack: FrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        discoBack = findViewById(R.id.color)

        sm = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorList = sm.getSensorList(Sensor.TYPE_ALL)
        for(sensor: Sensor in sensorList)
        {
            Log.d("MainActivity", "${sensor.name} ${sensor.vendor}")
        }

        accelSenor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)



        sensorEventListener = object: SensorEventListener
        {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onSensorChanged(event: SensorEvent?) {
                event?.values?.let{ axis->
                    val bgColor = getBackGroundColor(axis[0], axis[1], axis[2])
                    discoBack.setBackgroundColor(bgColor)
                }
            }
        }

    }

    fun getBackGroundColor(aX: Float, aY: Float, aZ: Float): Int
    {
        var R = ((aX.div(12F))/24)*255
        var G = ((aY.div(12F))/24)*255
        var B = ((aZ.div(12F))/24)*255
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Color.rgb(R, G, B)
        } else {
            TODO("VERSION.SDK_INT < O")
            return -1
        }
    }

    override fun onResume() {
        super.onResume()
        sm.registerListener(sensorEventListener, accelSenor, 3000)
    }

    override fun onPause() {
        super.onPause()
        sm.unregisterListener(sensorEventListener)
    }
}
