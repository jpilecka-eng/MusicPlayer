package com.godeltech.musicplayer.presentation.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.godeltech.musicplayer.R
import com.godeltech.musicplayer.presentation.common.extensions.collectSiteEffectWithLifecycle
import com.godeltech.musicplayer.presentation.components.ErrorPage
import com.godeltech.musicplayer.presentation.components.ProgressIndicator
import com.godeltech.musicplayer.presentation.components.MediaCard
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@Composable
fun HomeScreen(
    onNavigateToPlayer: () -> Unit,
    onNavigateToPlaylist: (id: String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.event.collectSiteEffectWithLifecycle { event ->
        when (event) {
            is HomeEvent.NavigateToPlayer -> {
                onNavigateToPlayer()
            }

            is HomeEvent.NavigateToPlaylist -> {
                onNavigateToPlaylist(event.id)
            }
        }
    }

    HomeScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MusicPlayerTheme.projectColors.colorNeutralBlack),
        contentAlignment = Alignment.Center
    ) {
        if (state.isLoading) {
            ProgressIndicator(
                modifier = Modifier.zIndex(1f)
            )
        } else if (state.isError) {
            ErrorPage() {
                //todo add reload
            }
        } else if (state != HomeState.Idle) {

            Column(
                modifier
                    .fillMaxSize()
                    .background(
                        MusicPlayerTheme.projectColors.colorNeutralBlack
                    )
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                //---new releases
                Text(
                    text = stringResource(R.string.string_home_new_releases_heading),
                    color = MusicPlayerTheme.projectColors.colorNeutralWhite,
                    style = MusicPlayerTheme.typography.textHeading5,
                    modifier = Modifier.padding(MusicPlayerTheme.spacing.spacingXXL)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(MusicPlayerTheme.padding.paddingL),
                    contentPadding = PaddingValues(horizontal = MusicPlayerTheme.padding.paddingXL)
                ) {
                    itemsIndexed(state.data.recentTracks.tracks) { index, item ->
                        MediaCard(
                            imageUrl = item.imageUrl,
                            artistName = item.artistName,
                            trackName = item.title,
                            imageSize = 155.dp,
                            onClick = {
                                onAction(HomeAction.TrackClicked(index, HomeSectionId.RECENT))
                            }
                        )
                    }
                }

                //---trending songs
                Text(
                    text = stringResource(R.string.string_home_trending_songs_heading),
                    color = MusicPlayerTheme.projectColors.colorNeutralWhite,
                    style = MusicPlayerTheme.typography.textHeading5,
                    modifier = Modifier.padding(MusicPlayerTheme.spacing.spacingXXL)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(MusicPlayerTheme.padding.paddingL),
                    contentPadding = PaddingValues(horizontal = MusicPlayerTheme.padding.paddingXL)
                ) {
                    itemsIndexed(state.data.trendingTracksA.tracks) { index, item ->
                        MediaCard(
                            imageUrl = item.imageUrl,
                            artistName = item.artistName,
                            trackName = item.title,
                            imageSize = 98.dp,
                            onClick = {
                                onAction(HomeAction.TrackClicked(index, HomeSectionId.TRENDING_A))
                            }
                        )
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(MusicPlayerTheme.padding.paddingL),
                    contentPadding = PaddingValues(
                        horizontal = MusicPlayerTheme.padding.paddingXL,
                        vertical = MusicPlayerTheme.padding.paddingXXL
                    )
                ) {
                    itemsIndexed(state.data.trendingTracksB.tracks) { index, item ->
                        MediaCard(
                            imageUrl = item.imageUrl,
                            artistName = item.artistName,
                            trackName = item.title,
                            imageSize = 98.dp,
                            onClick = {
                                onAction(HomeAction.TrackClicked(index, HomeSectionId.TRENDING_B))
                            }
                        )
                    }
                }

                //recommended playlists
                Text(
                    text = stringResource(R.string.string_home_recommended_albums_heading),
                    color = MusicPlayerTheme.projectColors.colorNeutralWhite,
                    style = MusicPlayerTheme.typography.textHeading5,
                    modifier = Modifier.padding(MusicPlayerTheme.spacing.spacingXXL)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(MusicPlayerTheme.padding.paddingL),
                    contentPadding = PaddingValues(horizontal = MusicPlayerTheme.padding.paddingXL)
                ) {
                    items(state.data.recommendedPlaylists) { item ->
                        MediaCard(
                            imageUrl = item.imageUrl,
                            artistName = item.creatorName,
                            trackName = item.title,
                            imageSize = 155.dp,
                            onClick = {
                                onAction(HomeAction.AlbumClicked(item.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        onNavigateToPlayer = {},
        onNavigateToPlaylist = {}
    )
}