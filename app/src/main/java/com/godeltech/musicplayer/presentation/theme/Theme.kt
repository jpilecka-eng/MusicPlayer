package com.godeltech.musicplayer.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme.projectColors

@Composable
fun MusicPlayerTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalSpacing provides MusicPlayerTheme.spacing,
        LocalColors provides projectColors,
        LocalTypography provides MusicPlayerTheme.typography,
        LocalPadding provides MusicPlayerTheme.padding,
        LocalRadius provides MusicPlayerTheme.radius
    ) {
        content()
    }
}

object MusicPlayerTheme {
    val projectColors: Colors
        @Composable
        get() = LocalColors.current

    val spacing: Spacing
        @Composable
        get() = LocalSpacing.current

    val padding: Padding
        @Composable
        get() = LocalPadding.current

    val radius: Radius
        @Composable
        get() = LocalRadius.current

    val typography: Typography
        @Composable
        get() = LocalTypography.current
}