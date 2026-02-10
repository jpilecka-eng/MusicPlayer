package com.godeltech.musicplayer.player.controller

import androidx.media3.session.MediaController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ControllerProvider @Inject constructor(
) {
    private val _controller = MutableStateFlow<MediaController?>(null)
    val controller = _controller.asStateFlow()

    fun setController(controller: MediaController) {
        _controller.update {
            controller
        }
    }

    fun releaseController() {
        _controller.value?.release()
        _controller.update {
            null
        }
    }
}