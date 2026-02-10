package com.godeltech.musicplayer.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.godeltech.musicplayer.R
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@Composable
fun PlaylistTrackRow(
    modifier: Modifier = Modifier,
    imageUrl: String,
    trackName: String,
    artistName: String,
    onClick: () -> Unit,
    isPlaying: Boolean
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                start = MusicPlayerTheme.padding.paddingXL,
                end = MusicPlayerTheme.padding.paddingXL,
                top = MusicPlayerTheme.padding.paddingXL
            ),
        verticalAlignment = Alignment.CenterVertically,
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
                    horizontal = MusicPlayerTheme.padding.paddingL,
                    vertical = MusicPlayerTheme.padding.paddingXS
                )
        ) {
            val titleColor = if (isPlaying) MusicPlayerTheme.projectColors.colorPrimary.copy(
                alpha = 0.95f
            )
            else MusicPlayerTheme.projectColors.colorNeutralWhite
            Text(
                text = trackName,
                style = MusicPlayerTheme.typography.textSubtitleMd,
                color = titleColor,
                modifier = Modifier
                    .padding(
                        top = MusicPlayerTheme.padding.paddingXS
                    ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = artistName,
                style = MusicPlayerTheme.typography.textButtonSm,
                color = MusicPlayerTheme.projectColors.colorNeutralWhite64,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(
                        top = MusicPlayerTheme.padding.paddingXS
                    )
            )
        }

        Icon(
            painter = painterResource(R.drawable.ic_more),
            contentDescription = null,
            tint = MusicPlayerTheme.projectColors.colorNeutralWhite
        )
    }
}

@Preview
@Composable
private fun PlayListTrackRowPreview() {
    PlaylistTrackRow(
        imageUrl = "",
        trackName = "Until I Bleed Out",
        artistName = "The Weeknd",
        onClick = {},
        isPlaying = true
    )
}