package com.godeltech.musicplayer.presentation.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godeltech.musicplayer.presentation.common.threading.DispatcherProvider
import com.godeltech.musicplayer.data.network.music.playlists.PlaylistRepository
import com.godeltech.musicplayer.data.network.music.tracks.TrackRepository
import com.godeltech.musicplayer.player.PlayerAction
import com.godeltech.musicplayer.player.PlayerManager
import com.godeltech.musicplayer.player.Track
import com.godeltech.musicplayer.presentation.common.extensions.sendEvent
import com.godeltech.musicplayer.presentation.common.extensions.zip3
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val trackRepository: TrackRepository,
    private val playlistRepository: PlaylistRepository,
    private val mapper: HomeMapper,
    private val dispatcherProvider: DispatcherProvider,
    private val playerControls: PlayerManager
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState.Idle)
    val state = _state
        .onStart {
            refresh()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(10000L),
            HomeState.Idle
        )

    private val _event = Channel<HomeEvent>()
    val event = _event.receiveAsFlow()

    private val refreshTrigger = MutableSharedFlow<Unit>()

    init {
        refreshTrigger
            .flatMapLatest {
                loadData()
            }.launchIn(viewModelScope)
    }

    fun onAction(homeAction: HomeAction) {
        when (homeAction) {
            is HomeAction.TrackClicked -> {
                val trackList: List<Track> = when (homeAction.id) {
                    HomeSectionId.TRENDING1 -> {
                        state.value.data.trendingTracksA.tracks
                    }

                    HomeSectionId.TRENDING2 -> {
                        state.value.data.trendingTracksB.tracks
                    }

                    HomeSectionId.NEW_RELEASES -> {
                        state.value.data.recentTracks.tracks
                    }

                    HomeSectionId.UNKNOWN -> {
                        emptyList()
                    }
                }
                val firstNavigation =
                    playerControls.playerState.value.currentlyPlayingTrack.id.isEmpty()
                playerControls.onAction(
                    PlayerAction.PlaySong(
                        trackList,
                        homeAction.id.name,
                        index = homeAction.index,
                        playListName = homeAction.id.name,
                        reshuffle = true
                    )
                )
                if (firstNavigation) {
                    _event.sendEvent(viewModelScope) {
                        HomeEvent.NavigateToPlayer
                    }
                }
            }

            is HomeAction.AlbumClicked -> {
                _event.sendEvent(viewModelScope) {
                    HomeEvent.NavigateToPlaylist(homeAction.id)
                }
            }

            is HomeAction.ReloadClicked -> {
                refresh()
            }
        }
    }

    private fun loadData(): Flow<HomeModel> {
        val trendingTracksFlow = trackRepository.getTrendingTracks()
        val recentTracksFlow = trackRepository.getRecentTracks()
        val recommendedPlaylistsFlow = playlistRepository.getRecommendedPlaylists()
        return trendingTracksFlow.zip3(recentTracksFlow, recommendedPlaylistsFlow)
            .flowOn(dispatcherProvider.io())
            .onStart {
                _state.update {
                    it.copy(
                        isLoading = true,
                        isError = false
                    )
                }
            }
            .map { response ->
                mapper.mapHomeData(
                    response.first,
                    response.second,
                    response.third
                )
            }
            .flowOn(dispatcherProvider.default())
            .onEach { data ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        data = data,
                        isError = false
                    )
                }
            }
            .catch { e ->
                Log.d(TAG, e.toString())
                _state.update {
                    it.copy(
                        isLoading = false,
                        isError = true
                    )
                }
            }
    }

    private fun refresh() {
        viewModelScope.launch {
            refreshTrigger.emit(Unit)
        }
    }
}