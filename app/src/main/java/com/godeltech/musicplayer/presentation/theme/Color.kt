package com.godeltech.musicplayer.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class Colors(

    //primary-------------------------------------------------
    val colorPrimary: Color = Color(0xffDF3131),
    val colorPrimaryTint1: Color = Color(0xFFFBEBE8),
    val colorPrimaryTint3: Color = Color(0xFFF4C5BA),
    val colorPrimaryTint5: Color = Color(0xFFED9E8C),
    val colorPrimaryTint7: Color = Color(0xFFE6785E),
    val colorPrimaryShade2: Color = Color(0xFFAD3F26),
    val colorPrimaryShade4: Color = Color(0xFF7B2D1B),
    val colorPrimaryShade6: Color = Color(0xFF4A1B10),
    val colorPrimaryShade8: Color = Color(0xFF180905),

    //secondary-------------------------------------------------
    val colorSecondary: Color = Color(0xFFF2BF08),
    val colorSecondaryTint1: Color = Color(0xFFF2BF08),
    val colorSecondaryTint3: Color = Color(0xFFFAF4C3),
    val colorSecondaryTint5: Color = Color(0xFFF7EC9B),
    val colorSecondaryTint7: Color = Color(0xFFF4E573),
    val colorSecondaryShade2: Color = Color(0xFFBBAC3B),
    val colorSecondaryShade4: Color = Color(0xFF857B2A),
    val colorSecondaryShade6: Color = Color(0xFF504A19),
    val colorSecondaryShade8: Color = Color(0xFF1A1808),

    //neutral-------------------------------------------------
    val colorNeutralWhite: Color = Color(0xFFFDFBEB),
    val colorNeutralBlack: Color = Color(0xFF0C0C0C),

    val colorNeutralWhite8: Color = Color(0x14ffffff),
    val colorNeutralWhite16: Color = Color(0x29ffffff),
    val colorNeutralWhite24: Color = Color(0x3DFFFFFF),
    val colorNeutralWhite32: Color = Color(0x52FFFFFF),
    val colorNeutralWhite40: Color = Color(0x66FFFFFF),
    val colorNeutralWhite48: Color = Color(0x7AFFFFFF),
    val colorNeutralWhite56: Color = Color(0x8FFFFFFF),
    val colorNeutralWhite64: Color = Color(0xA3FFFFFF),
    val colorNeutralWhite72: Color = Color(0xB8FFFFFF),
    val colorNeutralWhite80: Color = Color(0xCCD9D9D9),

    //state-------------------------------------------------
    val colorStateErrorTint: Color = Color(0xffF6CBCB),
    val colorStateErrorShade: Color = Color(0xffDF2121),

    val colorStateSuccessTint: Color = Color(0xffD6F6CB),
    val colorStateSuccessShade: Color = Color(0xff28BD37),

    val colorStateWarningTint: Color = Color(0xffF6F2CB),
    val colorStateWarningShade: Color = Color(0xffDABC1D)
)

val LocalColors = staticCompositionLocalOf { Colors() }

