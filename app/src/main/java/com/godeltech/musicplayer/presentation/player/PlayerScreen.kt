package com.godeltech.musicplayer.presentation.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import coil3.compose.AsyncImage
import com.godeltech.musicplayer.R
import com.godeltech.musicplayer.player.PlayerState
import com.godeltech.musicplayer.presentation.components.MusicPlayerSlider
import com.godeltech.musicplayer.presentation.components.ProgressIndicator
import com.godeltech.musicplayer.presentation.components.RoundButton
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    PlayerScreenContent(
        modifier = modifier,
        onAction = viewModel::onAction,
        playerState = playerState,
        state = state
    )
}

@Composable
fun PlayerScreenContent(
    playerState: PlayerState,
    state: com.godeltech.musicplayer.presentation.player.PlayerState,
    modifier: Modifier = Modifier,
    onAction: (PlayerUIAction) -> Unit,
) {

    Box(
        modifier.fillMaxSize()
    ) {
        if (state.isLoading) {
            ProgressIndicator(
                modifier = Modifier.zIndex(1f)
            )
        }

        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            model = playerState.currentlyPlayingTrack.imageUrl.takeIf {
                it.isNotEmpty()
            } ?: R.drawable.img_error,
            contentDescription = null,
            placeholder = painterResource(
                R.drawable.img_error
            ),
            error = painterResource(
                R.drawable.img_error
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent,
                            0.20f to MusicPlayerTheme.projectColors.colorNeutralBlack.copy(alpha = 0.70f),
                            0.40f to MusicPlayerTheme.projectColors.colorNeutralBlack.copy(alpha = 0.88f),
                            0.60f to MusicPlayerTheme.projectColors.colorNeutralBlack.copy(alpha = 0.96f),
                            0.80f to MusicPlayerTheme.projectColors.colorNeutralBlack.copy(alpha = 0.99f),
                            1.0f to MusicPlayerTheme.projectColors.colorNeutralBlack
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = MusicPlayerTheme.padding.paddingXL,
                    end = MusicPlayerTheme.padding.paddingXL,
                    bottom = MusicPlayerTheme.padding.paddingXXXXXL
                ),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = playerState.currentlyPlayingTrack.title,
                style = MusicPlayerTheme.typography.textHeading4,
                color = MusicPlayerTheme.projectColors.colorNeutralWhite,
                modifier = Modifier.padding(
                    bottom = MusicPlayerTheme.padding.paddingXXS
                )
            )
            Text(
                text = playerState.currentlyPlayingTrack.artistName,
                style = MusicPlayerTheme.typography.textBodyMd,
                color = MusicPlayerTheme.projectColors.colorNeutralWhite64
            )
            TrackOptionsRow(
                onShowQueue = {
                    onAction(PlayerUIAction.OnQueueClicked)
                }
            )

            MusicPlayerSlider(
                durationFormatted = state.data.playerDurationFormatted,
                sliderPosition = state.data.sliderPositionNormalized,
                onSliderValueChange = { value ->
                    onAction(PlayerUIAction.OnSliderValueChanged(value))
                },
                onSliderChangeFinished = {
                    onAction(PlayerUIAction.OnSliderValueChangeFinished)
                },
                positionFormatted = state.data.playerPositionFormatted

            )
            val repeatMode = playerState.repeatMode
            val icon = when (repeatMode) {
                Player.REPEAT_MODE_ONE -> {
                    R.drawable.ic_repeat_1
                }

                else -> R.drawable.ic_repeat
            }
            val tint = when (repeatMode) {
                Player.REPEAT_MODE_OFF -> MusicPlayerTheme.projectColors.colorNeutralWhite
                else -> MusicPlayerTheme.projectColors.colorPrimary.copy(0.9f)
            }
            PlaybackControlsRow(
                onPlayClicked = {
                    onAction(PlayerUIAction.PlayButtonClicked)
                },
                onNextClicked = {
                    onAction(PlayerUIAction.NextClicked)
                },
                onPrevClicked = {
                    onAction(PlayerUIAction.PrevClicked)
                },
                isPlaying = playerState.isPlaying,
                repeatModeIcon = icon,
                repeatModeTint = tint,
                onRepeatClicked = {
                    onAction(PlayerUIAction.OnRepeatClicked)
                },
                onShuffleClicked = {
                    onAction(PlayerUIAction.OnShuffleClicked)
                },
                shuffleEnabled = playerState.shuffleEnabled,
                isLoading = playerState.isLoading
            )
        }

    }
}

@Composable
fun TrackOptionsRow(
    modifier: Modifier = Modifier,
    onShowQueue: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(MusicPlayerTheme.spacing.spacingXXXXL)
            .padding(top = MusicPlayerTheme.padding.paddingXXXL),
        horizontalArrangement = Arrangement.spacedBy(
            space = MusicPlayerTheme.padding.paddingM,
            alignment = Alignment.Start
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(
                space = MusicPlayerTheme.padding.paddingM,
                alignment = Alignment.Start
            )
        ) {
            RoundButton(
                iconRes = R.drawable.ic_heart,
                modifier = Modifier.size(MusicPlayerTheme.spacing.spacingXXXXL),
                iconSize = MusicPlayerTheme.spacing.spacingXL,
                description = R.string.string_player_ic_save_description,
                onClick = {}
            )

            RoundButton(
                iconRes = R.drawable.ic_download,
                modifier = Modifier.size(MusicPlayerTheme.spacing.spacingXXXXL),
                iconSize = MusicPlayerTheme.spacing.spacingXL,
                description = R.string.string_player_ic_download_description,
                onClick = {}
            )
        }
        RoundButton(
            iconRes = R.drawable.ic_queue,
            modifier = Modifier.size(MusicPlayerTheme.spacing.spacingXXXXL),
            iconSize = MusicPlayerTheme.spacing.spacingXL,
            description = R.string.string_player_ic_download_description,
            onClick = {
                onShowQueue()
            }
        )
    }
}

@Composable
fun PlaybackControlsRow(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    isPlaying: Boolean,
    repeatModeIcon: Int,
    repeatModeTint: Color,
    shuffleEnabled: Boolean,
    onPlayClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onPrevClicked: () -> Unit,
    onRepeatClicked: () -> Unit,
    onShuffleClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(MusicPlayerTheme.spacing.spacingXXXXXXL)
            .padding(top = MusicPlayerTheme.padding.paddingXXL),
        horizontalArrangement = Arrangement.spacedBy(
            space = MusicPlayerTheme.spacing.spacingXXL,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val shuffleTint = if (shuffleEnabled) {
            MusicPlayerTheme.projectColors.colorPrimary
        } else MusicPlayerTheme.projectColors.colorNeutralWhite
        Icon(
            painter = painterResource(R.drawable.ic_shuffle),
            contentDescription = stringResource(R.string.string_player_ic_shuffle_description),
            tint = shuffleTint,
            modifier = Modifier
                .size(MusicPlayerTheme.spacing.spacingXL)
                .clickable {
                    onShuffleClicked()
                }
        )
        RoundButton(
            iconRes = R.drawable.ic_previous,
            modifier = Modifier.size(MusicPlayerTheme.spacing.spacingXXXXXL),
            iconSize = MusicPlayerTheme.spacing.spacingXXL,
            description = R.string.string_player_ic_previous_description,
            onClick = {
                onPrevClicked()
            }
        )
        val icon = if (isPlaying) {
            R.drawable.ic_pause
        } else R.drawable.ic_play
        RoundButton(
            iconRes = icon,
            modifier = Modifier
                .size(MusicPlayerTheme.spacing.spacingXXXXXXL),
            iconSize = MusicPlayerTheme.spacing.spacingXXXL,
            description = R.string.string_player_ic_play_description,
            onClick = onPlayClicked,
            isLoading = isLoading

        )
        RoundButton(
            iconRes = R.drawable.ic_next,
            modifier = Modifier.size(MusicPlayerTheme.spacing.spacingXXXXXL),
            iconSize = MusicPlayerTheme.spacing.spacingXXL,
            description = R.string.string_player_ic_next_description,
            onClick = onNextClicked
        )

        Icon(
            painter = painterResource(repeatModeIcon),
            contentDescription = stringResource(R.string.string_player_ic_repeat_description),
            tint = repeatModeTint,
            modifier = Modifier
                .size(MusicPlayerTheme.spacing.spacingXL)
                .clickable {
                    onRepeatClicked()
                }
        )
    }
}

@Preview
@Composable
private fun PlayerScreenContentPreview() {
    PlayerScreenContent(
        onAction = {},
        playerState = PlayerState(),
        state = com.godeltech.musicplayer.presentation.player.PlayerState.Idle
    )
}