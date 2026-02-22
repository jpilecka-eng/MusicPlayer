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
    modifier: Modifier = Modifier,
    onNavigateToPlayer: () -> Unit,
    onNavigateToPlaylist: (id: String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    isMiniPlayerVisible: Boolean
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
        onAction = viewModel::onAction,
        isMiniPlayerVisible = isMiniPlayerVisible,
        modifier = modifier
    )
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    isMiniPlayerVisible: Boolean
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
            ErrorPage {
                onAction(HomeAction.ReloadClicked)
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
                    itemsIndexed(
                        items = state.data.recentTracks.tracks,
                        key = { _, item ->
                            item.id
                        }
                    ) { index, item ->
                        MediaCard(
                            imageUrl = item.imageUrl,
                            artistName = item.artistName,
                            trackName = item.title,
                            imageSize = 155.dp,
                            onClick = {
                                onAction(
                                    HomeAction.TrackClicked(index, HomeSectionId.NEW_RELEASES)
                                )
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
                    itemsIndexed(
                        items = state.data.trendingTracksA.tracks,
                        key = { _, item ->
                            item.id
                        }
                    ) { index, item ->
                        MediaCard(
                            imageUrl = item.imageUrl,
                            artistName = item.artistName,
                            trackName = item.title,
                            imageSize = 98.dp,
                            onClick = {
                                onAction(
                                    HomeAction.TrackClicked(index, HomeSectionId.TRENDING1)
                                )
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
                    itemsIndexed(
                        items = state.data.trendingTracksB.tracks,
                        key = { _, item ->
                            item.id
                        }
                    ) { index, item ->
                        MediaCard(
                            imageUrl = item.imageUrl,
                            artistName = item.artistName,
                            trackName = item.title,
                            imageSize = 98.dp,
                            onClick = {
                                onAction(
                                    HomeAction.TrackClicked(index, HomeSectionId.TRENDING2)
                                )
                            }
                        )
                    }
                }

                val bottomPadding = if (isMiniPlayerVisible) {
                    120.dp
                } else MusicPlayerTheme.padding.paddingXXXXXL

                //recommended playlists
                Text(
                    text = stringResource(R.string.string_home_recommended_albums_heading),
                    color = MusicPlayerTheme.projectColors.colorNeutralWhite,
                    style = MusicPlayerTheme.typography.textHeading5,
                    modifier = Modifier.padding(MusicPlayerTheme.spacing.spacingXXL)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(MusicPlayerTheme.padding.paddingL),
                    contentPadding = PaddingValues(
                        start = MusicPlayerTheme.padding.paddingXL,
                        end = MusicPlayerTheme.padding.paddingXL,
                        bottom = bottomPadding
                    )
                ) {
                    items(
                        items = state.data.recommendedPlaylists,
                        key = { item ->
                            item.id
                        }
                    ) { item ->
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
        onNavigateToPlaylist = {},
        isMiniPlayerVisible = false
    )
}