package com.godeltech.musicplayer.presentation.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.godeltech.musicplayer.R
import com.godeltech.musicplayer.player.Track
import com.godeltech.musicplayer.presentation.components.PlaylistTrackRow
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerQueueBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    queue: List<Track>,
    playingTrack: Track,
    playlistName: String,
    onItemClicked: (index: Int) -> Unit
) {
    val lazyListState = rememberLazyListState()

    val isAtTop =
        remember {
            derivedStateOf {
                lazyListState.firstVisibleItemIndex == 0
                        && lazyListState.firstVisibleItemScrollOffset == 0
            }
        }

    ModalBottomSheet(
        modifier = modifier
            .systemBarsPadding(),
        onDismissRequest = {
            onDismissRequest()
        },
        containerColor = MusicPlayerTheme.projectColors.colorNeutralBlack,
        sheetState = sheetState,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MusicPlayerTheme.projectColors.colorNeutralWhite
            )
        },
        sheetGesturesEnabled = isAtTop.value
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = MusicPlayerTheme.padding.paddingXL
                )
        ) {
            Text(
                text = stringResource(R.string.string_player_queue_bottom_sheet_heading),
                style = MusicPlayerTheme.typography.textHeading6,
                color = MusicPlayerTheme.projectColors.colorNeutralWhite,
                modifier = Modifier.padding(
                    start = MusicPlayerTheme.padding.paddingXL,
                    end = MusicPlayerTheme.padding.paddingXL,
                    bottom = MusicPlayerTheme.padding.paddingXS
                )
            )
            Row {
                Text(
                    text = stringResource(R.string.string_player_queue_bottom_sheet_playing),
                    style = MusicPlayerTheme.typography.textButtonSm,
                    color = MusicPlayerTheme.projectColors.colorNeutralWhite64,
                    modifier = Modifier.padding(
                        start = MusicPlayerTheme.padding.paddingXL
                    )
                )
                Text(
                    text = playlistName,
                    style = MusicPlayerTheme.typography.textButtonSm,
                    color = MusicPlayerTheme.projectColors.colorNeutralWhite,
                    modifier = Modifier.padding(
                        start = MusicPlayerTheme.padding.paddingXS,
                        end = MusicPlayerTheme.padding.paddingXS
                    )
                )
            }
            PlaylistTrackRow(
                imageUrl = playingTrack.imageUrl,
                trackName = playingTrack.title,
                artistName = playingTrack.artistName,
                onClick = {},
                isPlaying = true
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_shuffle),
                    contentDescription = null,
                    Modifier
                        .padding(
                            start = MusicPlayerTheme.padding.paddingXL,
                            top = MusicPlayerTheme.padding.paddingXL,
                            bottom = MusicPlayerTheme.padding.paddingXS
                        )
                        .size(MusicPlayerTheme.spacing.spacingL),
                    tint = MusicPlayerTheme.projectColors.colorNeutralWhite64
                )
                Text(
                    text = stringResource(R.string.string_player_queue_bottom_sheet_shuffling),
                    style = MusicPlayerTheme.typography.textButtonSm,
                    color = MusicPlayerTheme.projectColors.colorNeutralWhite64,
                    modifier = Modifier.padding(
                        start = MusicPlayerTheme.padding.paddingXS,
                        end = MusicPlayerTheme.padding.paddingXS,
                        top = MusicPlayerTheme.padding.paddingXL,
                        bottom = MusicPlayerTheme.padding.paddingXS
                    )
                )
            }
            LazyColumn(
                state = lazyListState
            ) {
                items(
                    items = queue,
                    key = { it.id }
                ) { item ->
                    PlaylistTrackRow(
                        imageUrl = item.imageUrl,
                        trackName = item.title,
                        artistName = item.artistName,
                        onClick = {
                            onItemClicked(item.playlistIndex)
                        },
                        isPlaying = false
                    )
                }
            }
        }
    }
}