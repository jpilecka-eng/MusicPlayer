package com.godeltech.musicplayer.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Spacing(
    val spacingXS: Dp = 4.dp,
    val spacingS: Dp = 8.dp,
    val spacingM: Dp = 12.dp,
    val spacingL: Dp = 16.dp,
    val spacingXL: Dp = 20.dp,
    val spacingXXL: Dp = 24.dp,
    val spacingXXXL: Dp = 32.dp,
    val spacingXXXXL: Dp = 40.dp,
    val spacingXXXXXL: Dp = 48.dp,
    val spacingXXXXXXL: Dp = 56.dp,
    val spacingXXXXXXXL: Dp = 64.dp,
    val spacingXXXXXXXXL: Dp = 72.dp,
    val spacingXXXXXXXXXL: Dp = 80.dp,
    val spacingXXXXXXXXXXL: Dp = 88.dp,
    val spacingXXXXXXXXXXXL: Dp = 96.dp,
)

data class Padding(
    val paddingXXS: Dp = 2.dp,
    val paddingXS: Dp = 4.dp,
    val paddingS: Dp = 8.dp,
    val paddingM: Dp = 12.dp,
    val paddingL: Dp = 16.dp,
    val paddingXL: Dp = 20.dp,
    val paddingXXL: Dp = 24.dp,
    val paddingXXXL: Dp = 32.dp,
    val paddingXXXXL: Dp = 40.dp,
    val paddingXXXXXL: Dp = 48.dp,
    val paddingXXXXXXL: Dp = 56.dp,
)

data class Radius(
    val radiusXS: Dp = 8.dp,
    val radiusS: Dp = 12.dp,
    val radiusM: Dp = 16.dp,
    val radiusL: Dp = 24.dp,
    val radiusXL: Dp = 32.dp
)

val textSizeXXS = 10.sp
val textSizeXS = 12.sp
val textSizeS = 14.sp
val textSizeM = 16.sp
val textSizeL = 18.sp
val textSizeXL = 20.sp
val textSizeXXL = 24.sp
val textSizeXXXL = 32.sp
val textSizeXXXXL = 40.sp
val textSizeXXXXXL = 48.sp

val LocalSpacing = staticCompositionLocalOf { Spacing() }
val LocalPadding = staticCompositionLocalOf { Padding() }
val LocalRadius = staticCompositionLocalOf { Radius() }