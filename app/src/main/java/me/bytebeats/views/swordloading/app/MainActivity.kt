package me.bytebeats.views.swordloading.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import me.bytebeats.views.swordloading.SwordLoadingView
import me.bytebeats.views.swordloading.app.R

class MainActivity : AppCompatActivity() {

    private val swordLoadingView by lazy { findViewById<SwordLoadingView>(R.id.sword_loading_view) }
    private val btnStart by lazy { findViewById<Button>(R.id.bt_start) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnStart.setOnClickListener { swordLoadingView.start() }
    }
}