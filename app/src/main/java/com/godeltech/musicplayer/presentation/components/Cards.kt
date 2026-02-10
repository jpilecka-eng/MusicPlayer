package com.godeltech.musicplayer.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.godeltech.musicplayer.R
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@Composable
fun MediaCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    trackName: String,
    artistName: String,
    imageSize: Dp,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clickable {
                onClick()
            }
    ) {
        AsyncImage(
            modifier = Modifier
                .size(imageSize)
                .clip(
                    RoundedCornerShape(
                        MusicPlayerTheme.radius.radiusXS
                    )
                ),
            contentScale = ContentScale.Crop,
            model = imageUrl.takeIf {
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
        Text(
            text = trackName,
            style = MusicPlayerTheme.typography.textSubtitleSm,
            color = MusicPlayerTheme.projectColors.colorNeutralWhite,
            modifier = Modifier
                .padding(
                    top = MusicPlayerTheme.padding.paddingXS
                )
                .width(96.dp),
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
                    top = MusicPlayerTheme.padding.paddingXS
                )
                .width(96.dp),
        )
    }
}

@Composable
fun AlbumCard(
    modifier: Modifier = Modifier,
    playlistName: String,
    releaseDate: String,
    imageUrl: String,
    description: String,
    isAlbum: Boolean,
    onPlayClicked: () -> Unit,
    isPlaying: Boolean,
    hasReadMore: Boolean,
    onReadMore: () -> Unit,
    isLoading: Boolean,
    isExpanded: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            modifier = Modifier
                .size(200.dp)
                .clip(
                    RoundedCornerShape(
                        MusicPlayerTheme.radius.radiusXS
                    )
                ),
            contentScale = ContentScale.Crop,
            model = imageUrl.takeIf {
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MusicPlayerTheme.padding.paddingM)
                .clip(
                    RoundedCornerShape(
                        MusicPlayerTheme.radius.radiusM
                    )
                )
                .background(
                    MusicPlayerTheme.projectColors.colorNeutralWhite8
                )
                .padding(
                    horizontal = MusicPlayerTheme.padding.paddingXL,
                    vertical = MusicPlayerTheme.padding.paddingM
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = playlistName,
                    style = MusicPlayerTheme.typography.textSubtitleMd,
                    color = MusicPlayerTheme.projectColors.colorNeutralWhite,
                    modifier = Modifier.padding(
                        vertical = MusicPlayerTheme.padding.paddingXS
                    )
                )
                if (description.isNotEmpty()) {
                    Column(
                        modifier = Modifier.animateContentSize()
                    ) {
                        Text(
                            text = description,
                            style = MusicPlayerTheme.typography.textCaptionMd,
                            color = MusicPlayerTheme.projectColors.colorNeutralWhite56,
                            modifier = Modifier.padding(
                                vertical = MusicPlayerTheme.padding.paddingXS
                            )
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    val heading = if (isAlbum) R.string.string_album_heading
                    else R.string.string_playlist_heading
                    Text(
                        text = stringResource(
                            heading
                        ),
                        style = MusicPlayerTheme.typography.textCaptionMd,
                        color = MusicPlayerTheme.projectColors.colorNeutralWhite56
                    )
                    if (isAlbum) {
                        Icon(
                            painter = painterResource(R.drawable.ic_dot),
                            contentDescription = null,
                            modifier = Modifier.padding(
                                MusicPlayerTheme.padding.paddingXS
                            ),
                            tint = MusicPlayerTheme.projectColors.colorNeutralWhite56
                        )

                        Text(
                            text = releaseDate,
                            style = MusicPlayerTheme.typography.textCaptionMd,
                            color = MusicPlayerTheme.projectColors.colorNeutralWhite56
                        )
                    }
                }
                if (hasReadMore) {
                    val text = if (!isExpanded) {
                        R.string.string_playlist_description_read_more
                    } else R.string.string_playlist_description_read_less
                    Text(
                        text = stringResource(text),
                        style = MusicPlayerTheme.typography.textCaptionMd,
                        fontWeight = Bold,
                        color = MusicPlayerTheme.projectColors.colorNeutralWhite56,
                        modifier = Modifier
                            .clickable {
                                onReadMore()
                            }
                            .padding(
                                vertical = MusicPlayerTheme.padding.paddingXS
                            ),
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
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
        }
    }

}

@Preview
@Composable
private fun AlbumCardPreview() {
    AlbumCard(
        playlistName = "The Highlights (Deluxe)",
        releaseDate = "2024",
        description = "Very good album",
        imageUrl = "",
        isAlbum = false,
        onPlayClicked = {},
        hasReadMore = true,
        onReadMore = {},
        isPlaying = false,
        isLoading = true,
        isExpanded = true
    )
}

@Preview
@Composable
private fun MediaCardPreview() {
    MediaCard(
        imageUrl = "",
        trackName = "After hours After hours",
        artistName = "The weekend After hours",
        onClick = {},
        imageSize = 98.dp
    )
}