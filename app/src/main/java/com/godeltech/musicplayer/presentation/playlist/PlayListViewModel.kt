package com.godeltech.musicplayer.presentation.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godeltech.musicplayer.presentation.common.threading.DispatcherProvider
import com.godeltech.musicplayer.data.network.music.playlists.PlaylistRepository
import com.godeltech.musicplayer.player.PlayerAction
import com.godeltech.musicplayer.player.PlayerManager
import com.godeltech.musicplayer.presentation.common.extensions.sendEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

@HiltViewModel(
    assistedFactory = PlayListViewModel.Factory::class
)
class PlayListViewModel @AssistedInject constructor(
    private val playlistRepository: PlaylistRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val playListMapper: PlayListMapper,
    @Assisted private val id: String,
    private val playerControls: PlayerManager
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: String): PlayListViewModel
    }

    private val _event = Channel<PlaylistEvent>()
    val event = _event.receiveAsFlow()

    private val _state = MutableStateFlow(PlaylistState.Idle)
    val state = _state
        .onStart {
            refresh()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(10000L),
            PlaylistState.Idle
        )

    val playerState = playerControls.playerState
    private val refreshTrigger = MutableSharedFlow<Unit>()

    init {
        refreshTrigger
            .flatMapLatest {
                loadData(id)
            }.launchIn(viewModelScope)
    }

    fun onAction(playlistAction: PlaylistAction) {
        when (playlistAction) {
            is PlaylistAction.GlobalPlayClicked -> {
                val firstNavigation =
                    playerControls.playerState.value.currentlyPlayingTrack.id.isEmpty()
                playerControls.onAction(
                    PlayerAction.PlayPlaylist(
                        state.value.data.tracks,
                        state.value.data.playlistInfo.id
                    )
                )
                val isActive =
                    playerState.value.currentlyPlayingPlaylistId == state.value.data.playlistInfo.id
                if (!isActive && firstNavigation) {
                    _event.sendEvent(viewModelScope) {
                        PlaylistEvent.NavigateToPlayer
                    }
                }
            }

            is PlaylistAction.TrackClicked -> {
                val firstNavigation =
                    playerControls.playerState.value.currentlyPlayingTrack.id.isEmpty()
                playerControls.onAction(
                    PlayerAction.PlaySong(
                        state.value.data.tracks,
                        state.value.data.playlistInfo.id,
                        index = playlistAction.index,
                        playListName = state.value.data.playlistInfo.title,
                        reshuffle = true
                    )
                )
                if (firstNavigation) {
                    _event.sendEvent(viewModelScope) {
                        PlaylistEvent.NavigateToPlayer
                    }
                }
            }

            is PlaylistAction.OnReadMoreDescriptionClicked -> {
                _state.update {
                    it.copy(
                        data = it.data.copy(
                            playlistInfo = it.data.playlistInfo.copy(
                                descriptionExpanded = !state.value.data.playlistInfo.descriptionExpanded
                            ),
                        )
                    )
                }
            }

            is PlaylistAction.OnNavigateBackClicked -> {
                _event.sendEvent(viewModelScope) {
                    PlaylistEvent.NavigateBack
                }
            }

            is PlaylistAction.OnReloadClicked -> {
                refresh()
            }
        }
    }

    private fun loadData(id: String): Flow<PlaylistModel> {
        return playlistRepository.getPlaylistTracks(id)
            .zip(playlistRepository.getPlaylist(id)) { tracks, playlist ->
                Pair(tracks, playlist)
            }
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
                playListMapper.mapPlayListData(
                    response.first, response.second
                )
            }
            .flowOn(dispatcherProvider.default())
            .onEach { data ->
                _state.update {
                    it.copy(
                        data = data,
                        isLoading = false,
                        isError = false
                    )
                }
            }
            .catch {
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