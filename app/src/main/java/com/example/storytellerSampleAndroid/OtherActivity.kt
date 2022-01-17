package com.example.storytellerSampleAndroid

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class OtherActivity : AppCompatActivity(R.layout.activity_other) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.content).setOnClickListener { finish() }
    }
}
