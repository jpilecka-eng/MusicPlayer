package com.godeltech.musicplayer.player.service

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerServiceStateHandler @Inject constructor(
) {
    private val _isServiceRunning = MutableStateFlow(false)
    val serviceIsRunning = _isServiceRunning.asStateFlow()

    fun onServiceStopped() {
        _isServiceRunning.update {
            false
        }
    }

    fun onServiceStarted() {
        _isServiceRunning.update {
            true
        }
    }
}