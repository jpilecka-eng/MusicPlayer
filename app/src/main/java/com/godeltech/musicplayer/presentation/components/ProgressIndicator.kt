package com.godeltech.musicplayer.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@Composable
fun ProgressIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                MusicPlayerTheme.projectColors.colorNeutralBlack
            )
            .pointerInput(Unit) {},
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MusicPlayerTheme.projectColors.colorPrimary,
            modifier = modifier
        )
    }
}