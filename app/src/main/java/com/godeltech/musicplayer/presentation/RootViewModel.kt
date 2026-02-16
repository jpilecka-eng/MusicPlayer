package com.godeltech.musicplayer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godeltech.musicplayer.player.PlayerAction
import com.godeltech.musicplayer.player.PlayerEvent
import com.godeltech.musicplayer.player.PlayerManager
import com.godeltech.musicplayer.presentation.common.extensions.sendEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val playerManager: PlayerManager
) : ViewModel() {

    val playerState = playerManager.playerState
    val playerEvent = playerManager.event

    private val _state = MutableStateFlow(RootState.Idle)
    val state = _state.asStateFlow()

    private val _event = Channel<RootEvent>()
    val event = _event.receiveAsFlow()

    private var autoSkipJob: Job? = null

    init {
        viewModelScope.launch {
            playerState
                .map { it.positionMs }
                .filter { it > 0 }
                .distinctUntilChanged()
                .collect { positionMs ->
                    if (!state.value.data.isSeeking) {
                        _state.update {
                            it.copy(
                                data = it.data.copy(
                                    positionMs = positionMs,
                                    sliderPositionNormalized = calculateSliderPosition(
                                        positionMs,
                                        playerState.value.durationMs
                                    )
                                )
                            )
                        }
                    }
                }
        }

        viewModelScope.launch {
            playerEvent.collect { event ->
                when (event) {
                    is PlayerEvent.Error -> {
                        _event.sendEvent(viewModelScope) {
                            RootEvent.ShowSnackBar("This song is currently unavailable") //todo - move into resources
                        }
                        autoSkipJob?.cancel()
                        if (playerState.value.hasNext) {
                            autoSkipJob = launch {
                                delay(500)
                                playerManager.onAction(PlayerAction.PlayNext)
                            }
                        }
                    }
                }
            }
        }
    }

    fun onAction(rootAction: RootAction) {
        when (rootAction) {
            is RootAction.PlayerClicked -> {
                _event.sendEvent(viewModelScope) {
                    RootEvent.NavigateToPlayer
                }
            }

            is RootAction.PlayButtonClicked -> {
                playerManager.onAction(PlayerAction.PlayPause)
            }

            is RootAction.PrevButtonClicked -> {
                playerManager.onAction(PlayerAction.PlayPrevious)
            }

            is RootAction.OnSliderValueChanged -> {
                val normalized = rootAction.value
                val duration = playerState.value.durationMs
                val targetMs = (normalized * duration).toLong()
                _state.update {
                    it.copy(
                        data = it.data.copy(
                            positionMs = targetMs,
                            sliderPositionNormalized = normalized,
                            isSeeking = true
                        )
                    )
                }
            }

            is RootAction.OnSliderValueChangeFinished -> {
                playerManager.onAction(PlayerAction.SeekTo(state.value.data.positionMs))
                _state.update {
                    it.copy(
                        data = it.data.copy(
                            isSeeking = false
                        )
                    )
                }
            }

            is RootAction.OnSwipedLeft -> {
                playerManager.onAction(PlayerAction.PlayNext)
            }

            is RootAction.OnSwipedRight -> {
                playerManager.onAction(PlayerAction.PlayPrevious)
            }
        }
    }

    private fun calculateSliderPosition(positionMs: Long, durationMs: Long): Float {
        if (positionMs > 0 && durationMs > 0) {
            return positionMs.toFloat() / durationMs
        }
        return 0f
    }
}