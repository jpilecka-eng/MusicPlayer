package com.godeltech.musicplayer.player.service

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerServiceStateHandler @Inject constructor(
) {
    private val _isServiceRunning = MutableStateFlow(false)
    val serviceIsRunning = _isServiceRunning.asStateFlow()

    private val _shuffleOrder = MutableStateFlow(intArrayOf())
    val shuffleOrder = _shuffleOrder.asStateFlow()

    private val _shuffleOrderUpdated = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val shuffleOrderUpdated = _shuffleOrderUpdated.asSharedFlow()

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

    fun onShuffleOrderChanged(shuffleOrder: IntArray) {
        _shuffleOrder.update {
            shuffleOrder
        }
    }

    fun onShuffleOrderUpdated() {
        _shuffleOrderUpdated.tryEmit(Unit)
    }
}