package com.example.a14vfilm.order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.MediaController
import android.widget.VideoView
import com.example.a14vfilm.R

class PlayVideoActivity : AppCompatActivity() {
    var videoMC: MediaController? = null
    var VVPlay: VideoView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_video)

        val video = intent.getStringExtra("video")
        VVPlay = findViewById(R.id.VVPlay)

        videoMC = MediaController(this)
        videoMC!!.setAnchorView(VVPlay)
        VVPlay!!.setMediaController(videoMC)
        VVPlay!!.setVideoPath(video)
        VVPlay!!.requestFocus()
        VVPlay!!.start()

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar!!.hide()
    }
}