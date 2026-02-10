package com.godeltech.musicplayer.presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.godeltech.musicplayer.R
import com.godeltech.musicplayer.presentation.navigation.Route
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@Composable
fun MusicPlayerNavigationBar(
    selectedKey: NavKey,
    onSelectKey: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MusicPlayerTheme.projectColors.colorNeutralBlack
    ) {
        TOP_LEVEL_ROUTES.forEach { (topLevelDestination, data) ->
            addNavigationBarItem(
                isSelected = topLevelDestination == selectedKey,
                onItemClicked = {
                    onSelectKey(topLevelDestination)
                },
                item = data
            )
        }
    }
}

val TOP_LEVEL_ROUTES = mapOf(
    Route.Home to BottomBarItem(
        titleRes = R.string.string_bottom_menu_home_title,
        selectedIconRes = R.drawable.ic_home_selected,
        unselectedIconRes = R.drawable.ic_home,
    ),
    Route.Search to BottomBarItem(
        titleRes = R.string.string_bottom_menu_search_title,
        selectedIconRes = R.drawable.ic_search_selected,
        unselectedIconRes = R.drawable.ic_search,
    ),
    Route.Library to BottomBarItem(
        titleRes = R.string.string_bottom_menu_library_title,
        selectedIconRes = R.drawable.ic_library_selected,
        unselectedIconRes = R.drawable.ic_library,
    )
)

data class BottomBarItem(
    @StringRes val titleRes: Int,
    @DrawableRes val selectedIconRes: Int,
    @DrawableRes val unselectedIconRes: Int,
)

@Composable
private fun RowScope.addNavigationBarItem(
    isSelected: Boolean,
    item: BottomBarItem,
    onItemClicked: () -> Unit
) {
    val color = if (isSelected) {
        MusicPlayerTheme.projectColors.colorNeutralWhite
    } else MusicPlayerTheme.projectColors.colorNeutralWhite40
    NavigationBarItem(
        selected = isSelected,
        onClick = {
            onItemClicked()
        },
        label = {
            Text(
                text = stringResource(item.titleRes),
                color = color,
                style = MusicPlayerTheme.typography.textButtonMd
            )
        },
        alwaysShowLabel = true,
        icon = {
            Icon(
                modifier = Modifier
                    .heightIn(min = 24.dp)
                    .widthIn(min = 24.dp),
                painter = if (isSelected) {
                    painterResource(item.selectedIconRes)
                } else painterResource(item.unselectedIconRes),
                contentDescription = null
            )
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MusicPlayerTheme.projectColors.colorNeutralWhite,
            unselectedIconColor = MusicPlayerTheme.projectColors.colorNeutralWhite40,
            indicatorColor = Color.Transparent
        )
    )
}