package com.godeltech.musicplayer.presentation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.godeltech.musicplayer.presentation.components.MiniPlayer
import com.godeltech.musicplayer.presentation.navigation.Navigator
import com.godeltech.musicplayer.presentation.navigation.Route
import com.godeltech.musicplayer.presentation.navigation.rememberNavigationState
import com.godeltech.musicplayer.presentation.navigation.toEntries
import com.godeltech.musicplayer.presentation.components.MusicPlayerNavigationBar
import com.godeltech.musicplayer.presentation.components.TOP_LEVEL_ROUTES
import com.godeltech.musicplayer.presentation.main.home.HomeScreen
import com.godeltech.musicplayer.presentation.main.library.LibraryScreen
import com.godeltech.musicplayer.presentation.main.search.SearchScreen
import com.godeltech.musicplayer.presentation.navigation.NavigationState
import com.godeltech.musicplayer.presentation.player.PlayerScreen
import com.godeltech.musicplayer.player.PlayerState
import com.godeltech.musicplayer.presentation.common.extensions.collectSiteEffectWithLifecycle
import com.godeltech.musicplayer.presentation.components.MusicPlayerSnackBar
import com.godeltech.musicplayer.presentation.playlist.PlayListScreen
import com.godeltech.musicplayer.presentation.theme.MusicPlayerTheme
import kotlinx.coroutines.launch

@Composable
fun App(
    rootViewModel: RootViewModel = hiltViewModel()
) {
    val navigationState = rememberNavigationState(
        startRoute = Route.Home,
        topLevelRoutes = TOP_LEVEL_ROUTES.keys
    )
    val navigator = remember {
        Navigator(navigationState)
    }

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val playerState by rootViewModel.playerState.collectAsStateWithLifecycle()
    val state by rootViewModel.state.collectAsStateWithLifecycle()

    rootViewModel.event.collectSiteEffectWithLifecycle { event ->
        when (event) {
            is RootEvent.NavigateToPlayer -> {
                navigator.navigate(Route.Player)
            }

            is RootEvent.ShowSnackBar -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        withDismissAction = true
                    )
                }
            }
        }
    }

    AppContent(
        playerState = playerState,
        navigator = navigator,
        navigationState = navigationState,
        state = state,
        onAction = rootViewModel::onAction,
        snackBarHostState = snackBarHostState
    )
}

@Composable
private fun AppContent(
    playerState: PlayerState,
    navigator: Navigator,
    navigationState: NavigationState,
    onAction: (RootAction) -> Unit,
    state: RootState,
    snackBarHostState: SnackbarHostState
) {
    val miniPlayerVisible =
        navigator.getCurrentScreenName() != Route.Player
                && playerState.currentlyPlayingTrack.id.isNotEmpty()

    val bottomPadding = if (miniPlayerVisible) {
        100.dp
    } else MusicPlayerTheme.padding.paddingXL

    Scaffold(
        bottomBar = {
            MusicPlayerNavigationBar(
                selectedKey = navigationState.topLevelRoute,
                onSelectKey = {
                    navigator.navigate(it)
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { data ->
                    MusicPlayerSnackBar(
                        message = data.visuals.message,
                        onDismiss = { data.dismiss() },
                        modifier = Modifier.padding(
                            top = MusicPlayerTheme.padding.paddingXL,
                            end = MusicPlayerTheme.padding.paddingL,
                            start = MusicPlayerTheme.padding.paddingL,
                            bottom = bottomPadding
                        )
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    MusicPlayerTheme.projectColors.colorNeutralBlack
                )
        ) {
            NavDisplay(
                modifier = Modifier.background(
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
                                },
                                isMiniPlayerVisible = miniPlayerVisible
                            )
                        }
                        entry<Route.Search> {
                            SearchScreen(
                                isMiniPlayerVisible = miniPlayerVisible,
                                onNavigateToPlayer = {
                                    navigator.navigate(Route.Player)
                                },
                                onNavigateToPlaylist = { id ->
                                    navigator.navigate(Route.Playlist(id))
                                }
                            )
                        }
                        entry<Route.Library> {
                            LibraryScreen()
                        }
                        entry<Route.Player>(
                            metadata = NavDisplay.transitionSpec {
                                slideInVertically(
                                    initialOffsetY = { it },
                                    animationSpec = tween(500)
                                ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                            }
                                    + NavDisplay.popTransitionSpec {
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
                        ) {
                            PlayerScreen(
                                onNavigateBack = {
                                    navigator.goBack()
                                }
                            )
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
                                },
                                isMiniPlayerVisible = miniPlayerVisible
                            )
                        }
                    }
                )
            )
            if (miniPlayerVisible) {
                MiniPlayer(
                    trackName = playerState.currentlyPlayingTrack.title,
                    artistName = playerState.currentlyPlayingTrack.artistName,
                    imageUrl = playerState.currentlyPlayingTrack.imageUrl,
                    isPlaying = playerState.isPlaying,
                    isLoading = playerState.isLoading,
                    onPlayClick = {
                        onAction(RootAction.PlayButtonClicked)
                    },
                    onPrevClick = {
                        onAction(RootAction.PrevButtonClicked)
                    },
                    onPlayerClick = {
                        onAction(RootAction.PlayerClicked)
                    },
                    sliderPosition = state.data.sliderPositionNormalized,
                    onSliderValueChange = {
                        onAction(RootAction.OnSliderValueChanged(it))
                    },
                    onSliderValueChangeFinished = {
                        onAction(RootAction.OnSliderValueChangeFinished)
                    },
                    modifier = Modifier
                        .padding(
                            start = MusicPlayerTheme.padding.paddingS,
                            end = MusicPlayerTheme.padding.paddingS
                        )
                        .align(
                            Alignment.BottomCenter
                        ),
                    onSwipeLeft = {
                        onAction(RootAction.OnSwipedLeft)
                    },
                    onSwipeRight = {
                        onAction(RootAction.OnSwipedRight)
                    }
                )
            }
        }
    }
}