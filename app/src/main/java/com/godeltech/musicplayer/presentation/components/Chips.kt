package com.godeltech.musicplayer.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@Composable
fun FilterChip(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    isSelected: Boolean
) {
    val backgroundColor = if (isSelected) {
        MusicPlayerTheme.projectColors.colorPrimary
    } else {
        MusicPlayerTheme.projectColors.colorNeutralWhite8
    }

    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    20.dp
                )
            )
            .background(backgroundColor)
            .widthIn(85.dp)
            .heightIn(
                MusicPlayerTheme.spacing.spacingXXXL
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = MusicPlayerTheme.padding.paddingL,
                vertical = MusicPlayerTheme.padding.paddingXS
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MusicPlayerTheme.typography.textButtonMd,
            color = MusicPlayerTheme.projectColors.colorNeutralWhite
        )
    }
}

@Preview
@Composable
private fun FilterChipPreview() {
    Column {
        FilterChip(
            text = "Albums",
            onClick = {},
            isSelected = true,
            modifier = Modifier.padding(
                bottom = 8.dp
            )
        )
        FilterChip(
            text = "Songs",
            onClick = {},
            isSelected = false
        )
    }
}