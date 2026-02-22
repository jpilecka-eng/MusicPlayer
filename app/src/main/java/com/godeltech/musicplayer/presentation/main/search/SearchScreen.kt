package com.godeltech.musicplayer.presentation.main.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.godeltech.musicplayer.R
import com.godeltech.musicplayer.player.PlayerState
import com.godeltech.musicplayer.presentation.common.extensions.collectSiteEffectWithLifecycle
import com.godeltech.musicplayer.presentation.components.FilterChip
import com.godeltech.musicplayer.presentation.components.MediaCard
import com.godeltech.musicplayer.presentation.components.MusicPlayerSearchBar
import com.godeltech.musicplayer.presentation.components.MusicPlayerTopBar
import com.godeltech.musicplayer.presentation.components.PlaylistTrackRow
import com.godeltech.musicplayer.presentation.components.ProgressIndicator
import com.godeltech.musicplayer.presentation.components.RoundButton
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    isMiniPlayerVisible: Boolean,
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateToPlayer: () -> Unit,
    onNavigateToPlaylist: (id: String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()

    viewModel.event.collectSiteEffectWithLifecycle { event ->
        when (event) {
            is SearchEvent.NavigateToPlayer -> {
                onNavigateToPlayer()
            }

            is SearchEvent.NavigateToPlaylists -> {
                onNavigateToPlaylist(event.id)
            }
        }
    }

    SearchScreenContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
        playerState = playerState,
        isMiniPlayerVisible = isMiniPlayerVisible
    )
}

@Composable
fun SearchScreenContent(
    modifier: Modifier = Modifier,
    state: SearchState,
    onAction: (SearchAction) -> Unit,
    playerState: PlayerState,
    isMiniPlayerVisible: Boolean,
) {
    val bottomPadding = if (isMiniPlayerVisible) {
        100.dp
    } else 0.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                MusicPlayerTheme.projectColors.colorNeutralBlack
            )
            .padding(
                horizontal = MusicPlayerTheme.padding.paddingXL,
                vertical = MusicPlayerTheme.padding.paddingS
            )
    ) {
        MusicPlayerTopBar(
            title = stringResource(R.string.string_search_header),
            navigationIcon = {
                Image(
                    painter = painterResource(R.drawable.img_profile_placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(
                            end = MusicPlayerTheme.padding.paddingM
                        )
                        .size(
                            MusicPlayerTheme.spacing.spacingXXXXL
                        )
                )
            },
            endAction = {
                RoundButton(
                    iconSize = MusicPlayerTheme.spacing.spacingXL,
                    iconRes = R.drawable.ic_bell,
                    description = R.string.string_search_notifications_description,
                    onClick = {},
                    isLoading = false,
                    backgroundColor = MusicPlayerTheme.projectColors.colorNeutralWhite8,
                    enabled = true,
                    modifier = Modifier.size(
                        MusicPlayerTheme.spacing.spacingXXXXL
                    )
                )
            }
        )

        val searchTextState = rememberTextFieldState()
        LaunchedEffect(searchTextState) {
            snapshotFlow { searchTextState.text.toString() }.collectLatest {
                onAction(
                    SearchAction.UserInputChanged(it)
                )
            }
        }

        MusicPlayerSearchBar(
            placeholderTextRes = R.string.string_search_placeholder,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = MusicPlayerTheme.padding.paddingL,
                    top = MusicPlayerTheme.padding.paddingS
                ),
            textState = searchTextState
        )
        Row(
            modifier = Modifier.padding(
                bottom = MusicPlayerTheme.padding.paddingL
            )
        ) {
            FilterChip(
                modifier = Modifier.padding(
                    end = MusicPlayerTheme.padding.paddingS,
                ),
                onClick = {
                    onAction(
                        SearchAction.FilterSelected(SearchFilter.Relevant)
                    )
                },
                text = stringResource(R.string.string_search_chip_relevant),
                isSelected = state.data.selectedSearchFilter == SearchFilter.Relevant
            )

            FilterChip(
                onClick = {
                    onAction(
                        SearchAction.FilterSelected(
                            SearchFilter.Popular
                        )
                    )
                },
                text = stringResource(R.string.string_search_chip_popular),
                modifier = Modifier.padding(end = MusicPlayerTheme.padding.paddingS),
                isSelected = state.data.selectedSearchFilter == SearchFilter.Popular
            )

            FilterChip(
                onClick = {
                    onAction(
                        SearchAction.FilterSelected(
                            SearchFilter.Recent
                        )
                    )
                },
                text = stringResource(R.string.string_search_chip_recent),
                isSelected = state.data.selectedSearchFilter == SearchFilter.Recent
            )
        }
        if (state.isLoading) {
            ProgressIndicator(
                modifier = Modifier.zIndex(1f)
            )
        } else if (state != SearchState.Idle) {
            LazyColumn(
                contentPadding = PaddingValues(
                    bottom = bottomPadding
                )
            ) {
                item {
                    Text(
                        text = stringResource(R.string.string_search_albums_header),
                        color = MusicPlayerTheme.projectColors.colorNeutralWhite,
                        style = MusicPlayerTheme.typography.textHeading5,
                        modifier = Modifier.padding(
                            top = MusicPlayerTheme.spacing.spacingXL,
                            bottom = MusicPlayerTheme.spacing.spacingXS
                        )
                    )
                }
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(
                            MusicPlayerTheme.padding.paddingL
                        )
                    ) {
                        items(
                            items = state.data.searchResult.playlists,
                            key = { item ->
                                item.id
                            }
                        ) { item ->
                            MediaCard(
                                imageUrl = item.imageUrl,
                                artistName = item.creatorName,
                                trackName = item.title,
                                imageSize = 98.dp,
                                onClick = {
                                    onAction(
                                        SearchAction.AlbumClicked(item.id)
                                    )
                                }
                            )
                        }
                    }
                }
                item {
                    Text(
                        text = stringResource(R.string.string_search_singles_header),
                        color = MusicPlayerTheme.projectColors.colorNeutralWhite,
                        style = MusicPlayerTheme.typography.textHeading5,
                        modifier = Modifier.padding(
                            top = MusicPlayerTheme.spacing.spacingXL,
                            bottom = MusicPlayerTheme.spacing.spacingXS
                        )
                    )
                }

                itemsIndexed(state.data.searchResult.tracks) { index, item ->
                    PlaylistTrackRow(
                        imageUrl = item.imageUrl,
                        artistName = item.artistName,
                        trackName = item.title,
                        onClick = {
                            onAction(
                                SearchAction.TrackClicked(index)
                            )
                        },
                        isPlaying = playerState.currentlyPlayingTrack.id == item.id
                                && playerState.isPlaying
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    SearchScreenContent(
        state = SearchState.Idle,
        onAction = {},
        playerState = PlayerState(),
        isMiniPlayerVisible = false
    )
}