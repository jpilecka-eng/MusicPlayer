package com.godeltech.musicplayer.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.godeltech.musicplayer.R
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@Composable
fun MusicPlayerSearchBar(
    textState: TextFieldState,
    modifier: Modifier = Modifier,
    @StringRes placeholderTextRes: Int
) {
    OutlinedTextField(
        state = textState,
        contentPadding = OutlinedTextFieldDefaults.contentPadding(
            top = MusicPlayerTheme.padding.paddingM,
            bottom = MusicPlayerTheme.padding.paddingM
        ),
        modifier = modifier.heightIn(
            max = MusicPlayerTheme.spacing.spacingXXXXXL
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedTextColor = MusicPlayerTheme.projectColors.colorNeutralWhite,
            focusedTextColor = MusicPlayerTheme.projectColors.colorNeutralWhite,
            focusedContainerColor = MusicPlayerTheme.projectColors.colorNeutralWhite8,
            unfocusedContainerColor = MusicPlayerTheme.projectColors.colorNeutralWhite8,
            cursorColor = MusicPlayerTheme.projectColors.colorNeutralWhite,
            focusedBorderColor = MusicPlayerTheme.projectColors.colorNeutralWhite8,
            unfocusedBorderColor = MusicPlayerTheme.projectColors.colorNeutralWhite8,
            selectionColors = TextSelectionColors(
                handleColor = MusicPlayerTheme.projectColors.colorNeutralWhite,
                backgroundColor = LocalTextSelectionColors.current.backgroundColor
            )
        ),
        shape = RoundedCornerShape(MusicPlayerTheme.radius.radiusXXL),
        textStyle = MusicPlayerTheme.typography.textBodyMd,
        placeholder = {
            Text(
                text = stringResource(placeholderTextRes),
                style = MusicPlayerTheme.typography.textBodyMd,
                color = MusicPlayerTheme.projectColors.colorNeutralWhite56
            )
        },
        lineLimits = TextFieldLineLimits.SingleLine
    )
}

@Preview
@Composable
private fun MusicPlayerSearchBarPreview() {
    MusicPlayerSearchBar(
        placeholderTextRes = R.string.string_search_placeholder,
        textState = TextFieldState()
    )
}