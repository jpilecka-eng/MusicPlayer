package com.godeltech.musicplayer.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.godeltech.musicplayer.R
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@Composable
fun ErrorPage(
    modifier: Modifier = Modifier,
    onReload: () -> Unit
) {
    Box(
        modifier = modifier.background(
            MusicPlayerTheme.projectColors.colorNeutralBlack
        )
    ) {
        Image(
            painter = painterResource(R.drawable.img_error),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = stringResource(R.string.string_error_page_heading),
                color = MusicPlayerTheme.projectColors.colorNeutralWhite,
                style = MusicPlayerTheme.typography.textHeading4,
                modifier = Modifier.padding(
                    MusicPlayerTheme.padding.paddingXXXXL
                )
            )

            ButtonPrimary(
                modifier = Modifier.padding(
                    horizontal = MusicPlayerTheme.padding.paddingXL,
                    vertical = MusicPlayerTheme.padding.paddingXXL
                ),
                onClick = onReload,
                text = stringResource(R.string.string_error_page_reload_button)
            )
        }
    }
}

@Preview
@Composable
private fun ErrorPagePreview() {
    ErrorPage(
        onReload = {}
    )
}