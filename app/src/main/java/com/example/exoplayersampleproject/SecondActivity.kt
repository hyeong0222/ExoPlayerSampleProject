package com.example.exoplayersampleproject

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource

class SecondActivity : AppCompatActivity() {

    private lateinit var pv: StyledPlayerView
    private lateinit var player: ExoPlayer

    private lateinit var videoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        pv = findViewById(R.id.pv)

        val uri = intent.getStringExtra("uri")
        videoUri = Uri.parse(uri)
    }

    override fun onStart() {
        super.onStart()

        player = ExoPlayer.Builder(this).build()
        pv.player = player

        val factory = DefaultDataSource.Factory(this)
        val mediaSource = ProgressiveMediaSource.Factory(factory).createMediaSource(MediaItem.fromUri(videoUri))

        player.setMediaSource(mediaSource)
        player.prepare()

        player.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()

        pv.player = null
        player.release()
    }
}