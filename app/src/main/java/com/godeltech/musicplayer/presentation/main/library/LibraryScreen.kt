package com.godeltech.musicplayer.presentation.main.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

//todo - implement library screen
@Composable
fun LibraryScreen(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxSize()
            .background(
                MusicPlayerTheme.projectColors.colorNeutralBlack
            )
    ) {
        Text(
            "Library",
            color = MusicPlayerTheme.projectColors.colorNeutralWhite
        )
    }
}