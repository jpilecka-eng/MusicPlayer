package com.godeltech.musicplayer.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerSlider(
    modifier: Modifier = Modifier,
    durationFormatted: String,
    sliderPosition: Float,
    positionFormatted: String,
    onSliderValueChange: (duration: Float) -> Unit,
    onSliderChangeFinished: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Slider(
            modifier = Modifier
                .padding(top = MusicPlayerTheme.spacing.spacingXXXL),
            value = sliderPosition,
            onValueChange = { onSliderValueChange(it) },
            onValueChangeFinished = {
                onSliderChangeFinished()
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    modifier = Modifier.height(MusicPlayerTheme.spacing.spacingXS),
                    thumbTrackGapSize = 0.dp,
                    drawStopIndicator = null,
                    colors = SliderDefaults.colors(
                        thumbColor = MusicPlayerTheme.projectColors.colorNeutralWhite,
                        activeTrackColor = MusicPlayerTheme.projectColors.colorNeutralWhite48,
                        activeTickColor = MusicPlayerTheme.projectColors.colorNeutralWhite48,
                        inactiveTrackColor = MusicPlayerTheme.projectColors.colorNeutralWhite16,
                        inactiveTickColor = MusicPlayerTheme.projectColors.colorNeutralWhite16,
                        disabledThumbColor = MusicPlayerTheme.projectColors.colorNeutralWhite16,
                        disabledActiveTrackColor = MusicPlayerTheme.projectColors.colorNeutralWhite48,
                        disabledActiveTickColor = MusicPlayerTheme.projectColors.colorNeutralWhite48,
                        disabledInactiveTrackColor = MusicPlayerTheme.projectColors.colorNeutralWhite16,
                        disabledInactiveTickColor = MusicPlayerTheme.projectColors.colorNeutralWhite16,
                    )
                )
            },
            thumb = {}
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = positionFormatted,
                color = MusicPlayerTheme.projectColors.colorNeutralWhite64,
                style = MusicPlayerTheme.typography.textButtonSm
            )
            Text(
                text = durationFormatted,
                color = MusicPlayerTheme.projectColors.colorNeutralWhite64,
                style = MusicPlayerTheme.typography.textButtonSm
            )
        }
    }
}

@Preview
@Composable
private fun MusicPlayerSliderPreview() {
    MusicPlayerSlider(
        durationFormatted = "0",
        sliderPosition = 0f,
        onSliderValueChange = {},
        onSliderChangeFinished = {},
        positionFormatted = "0"
    )
}