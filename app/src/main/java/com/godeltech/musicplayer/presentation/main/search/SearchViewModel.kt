package com.godeltech.musicplayer.presentation.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godeltech.musicplayer.data.network.music.playlists.PlaylistRepository
import com.godeltech.musicplayer.data.network.music.tracks.TrackRepository
import com.godeltech.musicplayer.player.PlayerAction
import com.godeltech.musicplayer.player.PlayerManager
import com.godeltech.musicplayer.presentation.common.extensions.sendEvent
import com.godeltech.musicplayer.presentation.common.threading.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
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
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val trackRepository: TrackRepository,
    private val playlistRepository: PlaylistRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val searchMapper: SearchMapper,
    private val playerManager: PlayerManager
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState.Idle)
    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(10000L),
            SearchState.Idle
        )

    private val _event = Channel<SearchEvent>()
    val event = _event.receiveAsFlow()

    val playerState = playerManager.playerState

    init {
        viewModelScope.launch {
            _state
                .map { it.data }
                .map { Pair(it.searchInput, it.selectedSearchFilter.value) }
                .distinctUntilChanged()
                .debounce(500L)
                .collect { (input, filter) ->
                    search(input, filter)
                }
        }
    }

    fun onAction(searchAction: SearchAction) {
        when (searchAction) {
            is SearchAction.UserInputChanged -> {
                _state.update {
                    it.copy(
                        data = it.data.copy(
                            searchInput = searchAction.newInput
                        )
                    )
                }
            }

            is SearchAction.TrackClicked -> {
                val playlistId = "${state.value.data.searchInput}_${System.currentTimeMillis()}"
                val firstNavigation =
                    playerManager.playerState.value.currentlyPlayingTrack.id.isEmpty()
                playerManager.onAction(
                    PlayerAction.PlaySong(
                        state.value.data.searchResult.tracks,
                        playlistId,
                        index = searchAction.index,
                        playListName = state.value.data.searchInput,
                        reshuffle = false
                    )
                )
                if (firstNavigation) {
                    _event.sendEvent(viewModelScope) {
                        SearchEvent.NavigateToPlayer
                    }
                }
            }

            is SearchAction.FilterSelected -> {
                _state.update {
                    it.copy(
                        data = it.data.copy(
                            selectedSearchFilter = searchAction.filter
                        )
                    )
                }
            }

            is SearchAction.AlbumClicked -> {
                _event.sendEvent(viewModelScope) {
                    SearchEvent.NavigateToPlaylists(searchAction.id)
                }
            }
        }
    }

    private fun search(query: String, selectedFilter: String) {
        trackRepository.searchTracks(query, selectedFilter)
            .zip(
                playlistRepository.getSearchedPlaylists(
                    query,
                    selectedFilter
                )
            ) { tracks, playlists ->
                Pair(tracks, playlists)
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
            .map { data ->
                searchMapper.mapSearchData(
                    data.first, data.second
                )
            }
            .flowOn(dispatcherProvider.default())
            .onEach { result ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        isError = false,
                        data = it.data.copy(
                            searchResult = result
                        )
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
            .launchIn(viewModelScope)
    }
}