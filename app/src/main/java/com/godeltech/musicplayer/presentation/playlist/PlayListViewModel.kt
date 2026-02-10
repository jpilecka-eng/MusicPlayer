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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip

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
    val state = _state.asStateFlow()

    val playerState = playerControls.playerState

    init {
        loadData(id)
    }

    fun onAction(playlistAction: PlaylistAction) {
        when (playlistAction) {
            is PlaylistAction.GlobalPlayClicked -> {
                playerControls.onAction(
                    PlayerAction.PlayPlaylist(
                        state.value.data.tracks,
                        state.value.data.playlistInfo.id
                    )
                )
                val isActive =
                    playerState.value.currentlyPlayingPlaylistId == state.value.data.playlistInfo.id
                if (!isActive) {
                    _event.sendEvent(viewModelScope) {
                        PlaylistEvent.NavigateToPlayer
                    }
                }
            }

            is PlaylistAction.TrackClicked -> {
                playerControls.onAction(
                    PlayerAction.PlaySong(
                        state.value.data.tracks,
                        state.value.data.playlistInfo.id,
                        playlistAction.index
                    )
                )
                _event.sendEvent(viewModelScope) {
                    PlaylistEvent.NavigateToPlayer
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
        }
    }

    private fun loadData(id: String) {
        playlistRepository.getPlaylistTracks(id)
            .zip(playlistRepository.getPlaylist(id)) { tracks, playlist ->
                Pair(tracks, playlist)
            }
            .flowOn(dispatcherProvider.io())
            .onStart {
                _state.update {
                    it.copy(
                        isLoading = true
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
                        isLoading = false
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
            }.launchIn(viewModelScope)
    }
}