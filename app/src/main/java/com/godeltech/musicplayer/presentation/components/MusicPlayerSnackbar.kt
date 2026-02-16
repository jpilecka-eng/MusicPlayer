package com.godeltech.musicplayer.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.godeltech.musicplayer.R
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@Composable
fun MusicPlayerSnackBar(
    modifier: Modifier = Modifier,
    message: String,
    onDismiss: () -> Unit
) {
    Snackbar(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                message,
                color = MusicPlayerTheme.projectColors.colorNeutralWhite,
                style = MusicPlayerTheme.typography.textCaptionMd,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = null,
                tint = MusicPlayerTheme.projectColors.colorPrimary,
                modifier = Modifier
                    .clickable {
                        onDismiss()
                    }
                    .size(
                        MusicPlayerTheme.spacing.spacingXL
                    )
            )
        }
    }
}

@Preview
@Composable
private fun MusicPlayerSnackBarPreview() {
    MusicPlayerSnackBar(
        message = "This song is currently unavailable",
        onDismiss = {}
    )
}