package com.godeltech.musicplayer.player.service

import android.content.Intent
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

private const val TAG = "PlayerService"

@AndroidEntryPoint
class PlayerService : MediaSessionService() {

    @Inject
    lateinit var mediaSession: MediaSession

    @Inject
    lateinit var playerServiceStateHandler: PlayerServiceStateHandler

    override fun onCreate() {
        super.onCreate()
        playerServiceStateHandler.onServiceStarted()
    }

    override fun onDestroy() {
        Log.d(TAG, "Service destroyed")
        mediaSession.run {
            player.release()
            release()
        }

        super.onDestroy()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession {
        return mediaSession
    }

    @OptIn(UnstableApi::class)
    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d(TAG, "Task removed")
        playerServiceStateHandler.onServiceStopped()
        pauseAllPlayersAndStopSelf()
    }
}