package com.godeltech.musicplayer.presentation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.godeltech.musicplayer.presentation.navigation.Navigator
import com.godeltech.musicplayer.presentation.navigation.Route
import com.godeltech.musicplayer.presentation.navigation.rememberNavigationState
import com.godeltech.musicplayer.presentation.navigation.toEntries
import com.godeltech.musicplayer.presentation.components.MusicPlayerNavigationBar
import com.godeltech.musicplayer.presentation.components.TOP_LEVEL_ROUTES
import com.godeltech.musicplayer.presentation.main.home.HomeScreen
import com.godeltech.musicplayer.presentation.main.library.LibraryScreen
import com.godeltech.musicplayer.presentation.main.search.SearchScreen
import com.godeltech.musicplayer.presentation.player.PlayerScreen
import com.godeltech.musicplayer.presentation.playlist.PlayListScreen
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme

@Composable
fun App() {
    val navigationState = rememberNavigationState(
        startRoute = Route.Home,
        topLevelRoutes = TOP_LEVEL_ROUTES.keys
    )
    val navigator = remember {
        Navigator(navigationState)
    }

    Scaffold(
        bottomBar = {
            MusicPlayerNavigationBar(
                selectedKey = navigationState.topLevelRoute,
                onSelectKey = {
                    navigator.navigate(it)
                }
            )
        }
    ) { paddingValues ->
        NavDisplay(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    MusicPlayerTheme.projectColors.colorNeutralBlack
                ),
            onBack = { navigator.goBack() },
            entries = navigationState.toEntries(
                entryProvider {
                    entry<Route.Home> {
                        HomeScreen(
                            onNavigateToPlayer = {
                                navigator.navigate(Route.Player)
                            },
                            onNavigateToPlaylist = { id ->
                                navigator.navigate(Route.Playlist(id))
                            }
                        )
                    }
                    entry<Route.Search> {
                        SearchScreen()
                    }
                    entry<Route.Library> {
                        LibraryScreen()
                    }
                    entry<Route.Player> {
                        PlayerScreen()
                    }

                    entry<Route.Playlist>(
                        metadata = NavDisplay.transitionSpec {
                            slideInVertically(
                                initialOffsetY = { it },
                                animationSpec = tween(500)
                            ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                        } + NavDisplay.popTransitionSpec {
                            EnterTransition.None togetherWith
                                    slideOutVertically(
                                        targetOffsetY = { it },
                                        animationSpec = tween(500)
                                    )
                        } + NavDisplay.predictivePopTransitionSpec {
                            EnterTransition.None togetherWith
                                    slideOutVertically(
                                        targetOffsetY = { it },
                                        animationSpec = tween(500)
                                    )
                        }
                    ) { navEntry ->
                        PlayListScreen(
                            id = navEntry.id,
                            onNavigateToPlayer = {
                                navigator.navigate(Route.Player)
                            },
                            onNavigateBack = {
                                navigator.goBack()
                            }
                        )
                    }
                }
            )
        )
    }
}