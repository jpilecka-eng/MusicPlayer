package com.godeltech.musicplayer.presentation.main.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.godeltech.musicplayer.R
import com.godeltech.musicplayer.presentation.common.extensions.collectSiteEffectWithLifecycle
import com.godeltech.musicplayer.presentation.components.MediaCard
import com.godeltech.musicplayer.presentation.components.MusicPlayerTopBar
import com.godeltech.musicplayer.presentation.components.RoundButton
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel(),
    onNavigateToPlaylist: (id: String) -> Unit,

    ) {
    viewModel.event.collectSiteEffectWithLifecycle { event ->
        when (event) {
            is LibraryEvent.NavigateToPlaylist -> {
                onNavigateToPlaylist(event.id)
            }
        }
    }

    LibraryScreenContent(
        onAction = viewModel::onAction
    )
}

@Composable
fun LibraryScreenContent(
    modifier: Modifier = Modifier,
    onAction: (LibraryAction) -> Unit
) {
    Column(
        modifier
            .fillMaxSize()
            .background(
                MusicPlayerTheme.projectColors.colorNeutralBlack
            )
            .padding(horizontal = MusicPlayerTheme.padding.paddingXL)
    ) {
        MusicPlayerTopBar(
            title = stringResource(R.string.string_library_header),
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
                Row {
                    RoundButton(
                        iconSize = MusicPlayerTheme.spacing.spacingXL,
                        iconRes = R.drawable.ic_search,
                        description = R.string.string_library_search_description,
                        onClick = {},
                        isLoading = false,
                        backgroundColor = MusicPlayerTheme.projectColors.colorNeutralWhite8,
                        enabled = true,
                        modifier = Modifier
                            .size(
                                MusicPlayerTheme.spacing.spacingXXXXL
                            )
                            .padding(
                                end = MusicPlayerTheme.padding.paddingM
                            )
                    )
                    RoundButton(
                        iconSize = MusicPlayerTheme.spacing.spacingXL,
                        iconRes = R.drawable.ic_pluse,
                        description = R.string.string_library_add_description,
                        onClick = {},
                        isLoading = false,
                        backgroundColor = MusicPlayerTheme.projectColors.colorNeutralWhite8,
                        enabled = true,
                        modifier = Modifier.size(
                            MusicPlayerTheme.spacing.spacingXXXXL
                        )
                    )
                }
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = MusicPlayerTheme.padding.paddingXXL,
                    top = MusicPlayerTheme.padding.paddingXXXXXL
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.string_library_playlists_heading),
                color = MusicPlayerTheme.projectColors.colorNeutralWhite,
                style = MusicPlayerTheme.typography.textHeading5,
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        MusicPlayerTheme.padding.paddingXXS
                    )

            )
            Icon(
                painter = painterResource(R.drawable.ic_filter),
                contentDescription = null,
                tint = MusicPlayerTheme.projectColors.colorNeutralWhite
            )
            Icon(
                painter = painterResource(R.drawable.ic_grid),
                contentDescription = null,
                tint = MusicPlayerTheme.projectColors.colorNeutralWhite
            )
        }

        MediaCard(
            imageUrl = "",
            artistName = stringResource(R.string.string_library_auto_playlist),
            trackName = stringResource(R.string.string_library_liked_songs_header),
            imageSize = 155.dp,
            onClick = {
                onAction(LibraryAction.FavouritePlaylistClicked)
            },
            imagePlaceholder = R.drawable.img_favourites
        )
    }
}

@Preview
@Composable
private fun LibraryScreenPreview() {
    LibraryScreenContent(
        onAction = {}
    )
}