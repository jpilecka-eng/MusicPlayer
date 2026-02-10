package com.godeltech.musicplayer.presentation.player


data class PlayerState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val data: PlayerModel = PlayerModel()
) {
    companion object {
        val Idle = PlayerState()
    }
}

data class PlayerModel(
    val positionMs: Long = 0L,
    val sliderPositionNormalized: Float = 0f,
    val playerPositionFormatted: String = "",
    val playerDurationFormatted: String = "",
    val isSeeking: Boolean = false,
    val showQueue: Boolean = false
)

sealed class PlayerUIAction {
    data object PlayButtonClicked : PlayerUIAction()
    data object NextClicked : PlayerUIAction()
    data object PrevClicked : PlayerUIAction()
    data class OnSliderValueChanged(val value: Float) : PlayerUIAction()
    data object OnSliderValueChangeFinished : PlayerUIAction()
    data object OnRepeatClicked : PlayerUIAction()
    data object OnShuffleClicked : PlayerUIAction()
    data object OnQueueClicked : PlayerUIAction()
}