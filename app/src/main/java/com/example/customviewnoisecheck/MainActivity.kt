package com.example.customviewnoisecheck

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ncView = findViewById<NoiseCheckView>(R.id.noiceView)
        findViewById<Button>(R.id.btnStart).setOnClickListener {
            ncView.circleInColor = Color.parseColor("#D7F1EC")
            ncView.circleOutColor = Color.parseColor("#36B99F")
            ncView.start()
        }

        findViewById<Button>(R.id.btnEnd).setOnClickListener {
            ncView.setDecibelResult(70, 60, Color.parseColor("#FDDEE1"), Color.parseColor("#F85C6A"))
        }

    }
}