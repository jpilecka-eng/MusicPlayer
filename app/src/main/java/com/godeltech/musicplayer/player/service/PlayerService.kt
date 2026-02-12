package com.godeltech.musicplayer.player.service

import android.content.Intent
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ShuffleOrder
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TAG = "PlayerService"

@OptIn(UnstableApi::class)
@AndroidEntryPoint
class PlayerService : MediaSessionService() {

    @Inject
    lateinit var mediaSession: MediaSession

    @Inject
    lateinit var playerServiceStateHandler: PlayerServiceStateHandler

    @Inject
    lateinit var exoPlayer: ExoPlayer

    private var shuffleJob: Job? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        playerServiceStateHandler.onServiceStarted()

        shuffleJob = serviceScope.launch {
            playerServiceStateHandler.shuffleOrder.collect { order ->
                if (order.isNotEmpty()) {
                    val seed = System.currentTimeMillis()
                    exoPlayer.shuffleOrder = ShuffleOrder.DefaultShuffleOrder(order, seed)
                }
            }
        }
    }

    override fun onDestroy() {
        Log.d(TAG, "Service destroyed")
        mediaSession.run {
            player.release()
            release()
        }
        shuffleJob?.cancel()
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