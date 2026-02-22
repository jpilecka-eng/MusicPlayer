package com.godeltech.musicplayer.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.godeltech.musicplayer.R
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@Composable
fun RoundButton(
    modifier: Modifier = Modifier,
    iconSize: Dp,
    iconRes: Int,
    description: Int,
    onClick: () -> Unit,
    isLoading: Boolean = false,
    progressIndicatorSize: Dp = 10.dp,
    backgroundColor: Color = MusicPlayerTheme.projectColors.colorNeutralWhite8,
    enabled: Boolean = true,
    tint: Color? = null
) {
    val defaultTint = if (enabled) {
        MusicPlayerTheme.projectColors.colorNeutralWhite
    } else MusicPlayerTheme.projectColors.colorNeutralWhite.copy(0.3f)

    val iconTint = tint ?: defaultTint

    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(MusicPlayerTheme.radius.radiusXL)
            )
            .background(backgroundColor)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center

    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MusicPlayerTheme.projectColors.colorPrimary,
                modifier = Modifier.size(progressIndicatorSize)
            )
        } else {
            Icon(
                modifier = Modifier.size(iconSize),
                painter = painterResource(iconRes),
                contentDescription = stringResource(description),
                tint = iconTint
            )
        }
    }
}

@Composable
fun ButtonPrimary(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .heightIn(56.dp)
            .widthIn(353.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MusicPlayerTheme.projectColors.colorPrimary
        ),
        shape = RoundedCornerShape(MusicPlayerTheme.radius.radiusXL)
    ) {
        Text(
            text = text,
            color = MusicPlayerTheme.projectColors.colorNeutralWhite,
            style = MusicPlayerTheme.typography.textButtonXL
        )
    }
}

@Preview
@Composable
private fun ButtonPrimaryPreview() {
    ButtonPrimary(
        onClick = {},
        text = "Button"
    )
}

@Preview
@Composable
private fun RoundButtonPreview() {
    RoundButton(
        iconRes = R.drawable.ic_pause,
        iconSize = 18.dp,
        description = R.string.string_player_ic_play_description,
        onClick = {},
        isLoading = false,
        progressIndicatorSize = 18.dp
    )
}