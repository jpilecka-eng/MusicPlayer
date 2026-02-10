package com.godeltech.musicplayer.presentation.playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.godeltech.musicplayer.R
import com.godeltech.musicplayer.player.PlayerState
import com.godeltech.musicplayer.presentation.common.extensions.collectSiteEffectWithLifecycle
import com.godeltech.musicplayer.presentation.components.AlbumCard
import com.godeltech.musicplayer.presentation.components.ErrorPage
import com.godeltech.musicplayer.presentation.components.MusicPlayerTopBar
import com.godeltech.musicplayer.presentation.components.PlaylistTrackRow
import com.godeltech.musicplayer.presentation.components.ProgressIndicator
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@Composable
fun PlayListScreen(
    id: String,
    viewModel: PlayListViewModel = hiltViewModel<PlayListViewModel, PlayListViewModel.Factory> { factory ->
        factory.create(id = id)
    },
    onNavigateToPlayer: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val playerState: PlayerState by viewModel.playerState.collectAsStateWithLifecycle()

    viewModel.event.collectSiteEffectWithLifecycle { event ->
        when (event) {
            is PlaylistEvent.NavigateToPlayer -> {
                onNavigateToPlayer()
            }

            is PlaylistEvent.NavigateBack -> {
                onNavigateBack()
            }
        }
    }

    PlayListScreenContent(
        state = state,
        onAction = viewModel::onAction,
        playerState = playerState
    )

}

@Composable
fun PlayListScreenContent(
    modifier: Modifier = Modifier,
    state: PlaylistState,
    playerState: PlayerState,
    onAction: (PlaylistAction) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MusicPlayerTheme.projectColors.colorNeutralBlack)
            .padding(
                bottom = MusicPlayerTheme.padding.paddingXL
            )
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(
                        MusicPlayerTheme.radius.radiusXS
                    )
                )
                .blur(MusicPlayerTheme.radius.radiusXS),
            contentScale = ContentScale.Crop,
            model = state.data.playlistInfo.imageUrl.takeIf {
                !it.isNullOrEmpty()
            } ?: R.drawable.ic_notes,
            contentDescription = null,
            placeholder = painterResource(
                R.drawable.ic_notes
            ),
            error = painterResource(
                R.drawable.ic_notes
            )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
        )

        if (state.isLoading) {
            ProgressIndicator(
                modifier = Modifier.zIndex(1f)
            )
        } else if (state.isError) {
            ErrorPage() {
                //todo add reload
            }
        }
        val playListInfo = state.data.playlistInfo
        Column {
            MusicPlayerTopBar(
                modifier = Modifier.consumeWindowInsets(WindowInsets.navigationBars),
                title = state.data.playlistInfo.title,
                endAction = {
                    IconButton(onClick = {
                        //todo implement search
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            tint = MusicPlayerTheme.projectColors.colorNeutralWhite,
                            contentDescription = null
                        )
                    }
                },
                onNavigationIconClicked = {
                    onAction(PlaylistAction.OnNavigateBackClicked)
                }
            )
            LazyColumn(
            ) {
                //todo - move
                val currentPlayListIsPlaying =
                    (playerState.currentlyPlayingPlaylistId == playListInfo.id && playerState.isPlaying)
                item {
                    AlbumCard(
                        playlistName = playListInfo.title,
                        releaseDate = playListInfo.releaseDate,
                        description = if (playListInfo.descriptionExpanded) {
                            playListInfo.description
                        } else playListInfo.descriptionTruncated,
                        imageUrl = playListInfo.imageUrl,
                        isAlbum = playListInfo.isAlbum,
                        modifier = Modifier.padding(
                            top = MusicPlayerTheme.padding.paddingL,
                            start = MusicPlayerTheme.padding.paddingXL,
                            end = MusicPlayerTheme.padding.paddingXL
                        ),
                        onPlayClicked = {
                            onAction(PlaylistAction.GlobalPlayClicked)
                        },
                        onReadMore = {
                            onAction(PlaylistAction.OnReadMoreDescriptionClicked)
                        },
                        hasReadMore = playListInfo.hasReadMore,
                        isPlaying = currentPlayListIsPlaying,
                        isLoading = playerState.isLoading,
                        isExpanded = state.data.playlistInfo.descriptionExpanded
                    )
                }
                itemsIndexed(state.data.tracks) { index, item ->
                    PlaylistTrackRow(
                        imageUrl = item.imageUrl,
                        artistName = item.artistName,
                        trackName = item.title,
                        onClick = {
                            onAction(PlaylistAction.TrackClicked(index))
                        },
                        isPlaying = (playerState.currentlyPlayingTrack.id == item.id) && playerState.isPlaying
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PlayListScreenContentPreview() {
    PlayListScreenContent(
        state = PlaylistState.Idle,
        onAction = {},
        playerState = PlayerState()
    )
}