package com.godeltech.musicplayer.presentation

import android.content.ComponentName
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.godeltech.musicplayer.player.controller.ControllerProvider
import com.godeltech.musicplayer.player.service.PlayerService
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var controllerProvider: ControllerProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicPlayerTheme {
                App()
            }
        }
    }

    override fun onStart() {
        val sessionToken =
            SessionToken(
                this,
                ComponentName(this, PlayerService::class.java)
            )

        val controllerFuture =
            MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener({
            controllerProvider.setController(controllerFuture.get())
        }, MoreExecutors.directExecutor())

        super.onStart()
    }

    override fun onStop() {
        controllerProvider.releaseController()
        super.onStop()
    }
}
