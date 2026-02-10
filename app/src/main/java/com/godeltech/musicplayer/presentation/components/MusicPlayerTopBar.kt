package com.godeltech.musicplayer.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.godeltech.musicplayer.R
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerTopBar(
    modifier: Modifier = Modifier,
    title: String,
    endAction: (@Composable () -> Unit)? = null,
    onNavigationIconClicked: () -> Unit
) {
    TopAppBar(
        windowInsets = WindowInsets(
            MusicPlayerTheme.padding.paddingS,
            MusicPlayerTheme.padding.paddingS,
            MusicPlayerTheme.padding.paddingS,
            MusicPlayerTheme.padding.paddingS
        ),
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MusicPlayerTheme.projectColors.colorNeutralBlack,
            titleContentColor = MusicPlayerTheme.projectColors.colorNeutralWhite,
        ),
        title = {
            Text(
                text = title,
                style = MusicPlayerTheme.typography.textHeading5,
                modifier = Modifier.padding(
                    horizontal = MusicPlayerTheme.padding.paddingXS
                )
            )
        },
        navigationIcon = {
            Icon(
                contentDescription = null,
                painter = painterResource(R.drawable.ic_arrow_left),
                tint = MusicPlayerTheme.projectColors.colorNeutralWhite,
                modifier = Modifier
                    .clickable {
                        onNavigationIconClicked()
                    }
                    .padding(
                        horizontal = MusicPlayerTheme.padding.paddingXS
                    )
            )
        },
        actions = { endAction?.invoke() }
    )
}

@Preview
@Composable
private fun MusicPlayerTopBarPreview() {
    MusicPlayerTopBar(
        title = "The weekend",
        endAction = {
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    tint = MusicPlayerTheme.projectColors.colorNeutralWhite,
                    contentDescription = "Mark as favorite"
                )
            }
        },
        onNavigationIconClicked = {}
    )
}