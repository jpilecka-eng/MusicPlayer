package com.godeltech.musicplayer.presentation.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.godeltech.musicplayer.R
import com.godeltech.musicplayer.player.Track
import com.godeltech.musicplayer.presentation.components.PlaylistTrackRow
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerQueueBottomSheet(
    onDismissRequest: () -> Unit,
    queue: List<Track>,
    playingTrack: Track,
    playlistName: String,
    onItemClicked: (index: Int) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        containerColor = MusicPlayerTheme.projectColors.colorNeutralBlack,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MusicPlayerTheme.projectColors.colorNeutralWhite
            )
        }
    ) {
        PlayerQueueBottomSheetContent(
            queue = queue,
            playingTrack = playingTrack,
            playlistName = playlistName,
            onItemClicked = onItemClicked,
        )
    }
}

@Composable
fun PlayerQueueBottomSheetContent(
    queue: List<Track>,
    playingTrack: Track,
    playlistName: String,
    onItemClicked: (index: Int) -> Unit
) {
    val lazyListState = rememberLazyListState()
    Column {
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
            state = lazyListState,
            modifier = Modifier
                .weight(1f, fill = false)
                .nestedScroll(
                    object : NestedScrollConnection {
                        override fun onPostScroll(
                            consumed: Offset,
                            available: Offset,
                            source: NestedScrollSource
                        ) = available
                    }
                ),
            contentPadding = PaddingValues(
                bottom = MusicPlayerTheme.padding.paddingXL
            )
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