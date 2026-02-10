package com.godeltech.musicplayer.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godeltech.musicplayer.player.PlayerAction
import com.godeltech.musicplayer.player.PlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerControls: PlayerManager,
) : ViewModel() {

    val playerState = playerControls.playerState

    private val _state = MutableStateFlow(PlayerState.Idle)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            playerControls.playerState
                .map { it.positionMs }
                .filter { it > 0 }
                .distinctUntilChanged()
                .collect { positionMs ->
                    if (!state.value.data.isSeeking) {
                        _state.update {
                            it.copy(
                                data = it.data.copy(
                                    positionMs = positionMs,
                                    playerPositionFormatted = calculateFromMsFormatted(positionMs),
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
            playerControls.playerState
                .map { it.durationMs }
                .collect { durationMs ->
                    _state.update {
                        it.copy(
                            data = it.data.copy(
                                playerDurationFormatted = calculateFromMsFormatted(durationMs)
                            )
                        )
                    }
                }
        }
    }

    fun onAction(playerAction: PlayerUIAction) {
        when (playerAction) {
            is PlayerUIAction.PlayButtonClicked -> {
                playerControls.onAction(PlayerAction.PlayPause)
            }

            is PlayerUIAction.NextClicked -> {
                playerControls.onAction(PlayerAction.PlayNext)

            }

            is PlayerUIAction.PrevClicked -> {
                playerControls.onAction(PlayerAction.PlayPrevious)
            }

            is PlayerUIAction.OnSliderValueChanged -> {
                val normalized = playerAction.value
                val duration = playerState.value.durationMs
                val targetMs = (normalized * duration).toLong()

                _state.update {
                    it.copy(
                        data = it.data.copy(
                            sliderPositionNormalized = normalized,
                            positionMs = targetMs,
                            playerPositionFormatted = calculateFromMsFormatted(targetMs),
                            isSeeking = true
                        )
                    )
                }
            }

            is PlayerUIAction.OnSliderValueChangeFinished -> {
                playerControls.onAction(PlayerAction.SeekTo(state.value.data.positionMs))
                _state.update {
                    it.copy(
                        data = it.data.copy(
                            isSeeking = false
                        )
                    )
                }
            }

            is PlayerUIAction.OnRepeatClicked -> {
                playerControls.onAction(PlayerAction.Repeat)
            }

            is PlayerUIAction.OnShuffleClicked -> {
                playerControls.onAction(PlayerAction.Shuffle)
            }

            is PlayerUIAction.OnQueueClicked -> {
                _state.update {
                    it.copy(
                        data = it.data.copy(
                            showQueue = !state.value.data.showQueue
                        )
                    )
                }
            }
        }
    }

    private fun calculateFromMsFormatted(valueMs: Long): String {
        val minutes = valueMs / 60000
        val seconds = (valueMs % 60000) / 1000
        val positionFormatted = String.format(Locale.UK, "%d:%02d", minutes, seconds)
        return positionFormatted
    }

    private fun calculateSliderPosition(positionMs: Long, durationMs: Long): Float {
        if (positionMs > 0 && durationMs > 0) {
            return positionMs.toFloat() / durationMs
        }
        return 0f
    }
}