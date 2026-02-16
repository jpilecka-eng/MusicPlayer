package com.godeltech.musicplayer.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.godeltech.musicplayer.R
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@Composable
fun MiniPlayer(
    modifier: Modifier = Modifier,
    imageUrl: String,
    trackName: String,
    artistName: String,
    isPlaying: Boolean,
    onPrevClick: () -> Unit,
    onPlayClick: () -> Unit,
    onPlayerClick: () -> Unit,
    isLoading: Boolean,
    sliderPosition: Float,
    onSliderValueChange: (value: Float) -> Unit,
    onSliderValueChangeFinished: () -> Unit,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit
) {
    var offset by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current
    val thresholdPx = remember(density) {
        with(density) {
            25.dp.toPx()
        }
    }

    Box(
        modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    MusicPlayerTheme.radius.radiusL
                )
            )
            .background(
                MusicPlayerTheme.projectColors.colorNeutralBlack
            )
            .border(
                width = 1.dp,
                color = MusicPlayerTheme.projectColors.colorNeutralWhite8,
                shape = RoundedCornerShape(
                    MusicPlayerTheme.radius.radiusL
                )
            )
            .clickable {
                onPlayerClick()
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { offset = 0f },
                    onDragEnd = {
                        if (offset <= -thresholdPx) {
                            onSwipeLeft()
                        }
                        if (offset >= thresholdPx) {
                            onSwipeRight()
                        }
                    }
                ) { _, dragAmount ->
                    offset += dragAmount
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .clip(
                    RoundedCornerShape(
                        MusicPlayerTheme.radius.radiusL
                    )
                )
                .background(
                    MusicPlayerTheme.projectColors.colorNeutralWhite8
                )
                .padding(
                    MusicPlayerTheme.padding.paddingL
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(MusicPlayerTheme.spacing.spacingXXXXXXL)
                    .clip(
                        RoundedCornerShape(
                            MusicPlayerTheme.radius.radiusXS
                        )
                    ),
                contentScale = ContentScale.Crop,
                model = imageUrl.takeIf {
                    it.isNotEmpty()
                } ?: R.drawable.ic_notes,
                contentDescription = null,
                placeholder = painterResource(
                    R.drawable.ic_notes
                ),
                error = painterResource(
                    R.drawable.ic_notes
                )
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = MusicPlayerTheme.padding.paddingL
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                end = MusicPlayerTheme.padding.paddingS
                            )
                    ) {
                        Text(
                            text = trackName,
                            style = MusicPlayerTheme.typography.textSubtitleMd,
                            color = MusicPlayerTheme.projectColors.colorNeutralWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = artistName,
                            style = MusicPlayerTheme.typography.textCaptionMd,
                            color = MusicPlayerTheme.projectColors.colorNeutralWhite64,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(
                                    top = MusicPlayerTheme.padding.paddingXXS
                                )
                        )
                    }

                    Icon(
                        contentDescription = stringResource(R.string.string_player_ic_previous_description),
                        painter = painterResource(
                            R.drawable.ic_previous
                        ),
                        tint = MusicPlayerTheme.projectColors.colorNeutralWhite,
                        modifier = Modifier
                            .size(
                                MusicPlayerTheme.spacing.spacingXL
                            )
                            .clickable {
                                onPrevClick()
                            }
                    )
                    val icon = if (isPlaying) {
                        R.drawable.ic_pause
                    } else R.drawable.ic_play

                    RoundButton(
                        iconRes = icon,
                        modifier = Modifier
                            .padding(start = MusicPlayerTheme.padding.paddingS)
                            .size(MusicPlayerTheme.spacing.spacingXXXXL),
                        iconSize = MusicPlayerTheme.spacing.spacingXL,
                        description = R.string.string_player_ic_play_description,
                        onClick = {
                            onPlayClick()
                        },
                        isLoading = isLoading,
                        progressIndicatorSize = MusicPlayerTheme.spacing.spacingXXXXL
                    )
                }

                MusicPlayerSlider(
                    modifier = Modifier.padding(
                        top = 10.dp
                    ),
                    durationFormatted = "",
                    sliderPosition = sliderPosition,
                    onSliderValueChange = {
                        onSliderValueChange(it)
                    },
                    onSliderChangeFinished = {
                        onSliderValueChangeFinished()
                    },
                    positionFormatted = ""
                )
            }
        }
    }
}

@Preview
@Composable
private fun MiniPlayerPreview() {
    MiniPlayer(
        trackName = "Until I Bleed Out",
        artistName = "The Weekend",
        imageUrl = "",
        isPlaying = true,
        isLoading = false,
        onPlayClick = {},
        onPrevClick = {},
        onPlayerClick = {},
        sliderPosition = 0f,
        onSliderValueChange = {},
        onSliderValueChangeFinished = {},
        onSwipeLeft = {},
        onSwipeRight = {}
    )
}