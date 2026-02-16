package com.godeltech.musicplayer.presentation

data class RootState(
    val data: RootMiniPlayerModel = RootMiniPlayerModel(),
) {
    companion object {
        val Idle = RootState()
    }
}

sealed class RootAction {
    data object PlayButtonClicked : RootAction()
    data object PrevButtonClicked : RootAction()
    data object PlayerClicked : RootAction()
    data class OnSliderValueChanged(val value: Float) : RootAction()
    data object OnSliderValueChangeFinished : RootAction()
    data object OnSwipedLeft : RootAction()
    data object OnSwipedRight : RootAction()
}

sealed class RootEvent {
    data object NavigateToPlayer : RootEvent()
    data class ShowSnackBar(val message: String) : RootEvent()
}

data class RootMiniPlayerModel(
    val positionMs: Long = 0L,
    val sliderPositionNormalized: Float = 0f,
    val isSeeking: Boolean = false,
)