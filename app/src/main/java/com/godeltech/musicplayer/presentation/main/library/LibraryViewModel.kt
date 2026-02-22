package com.godeltech.musicplayer.presentation.main.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godeltech.musicplayer.presentation.common.extensions.sendEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
) : ViewModel() {

    private val _event = Channel<LibraryEvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: LibraryAction) {
        when (action) {
            is LibraryAction.FavouritePlaylistClicked -> {
                _event.sendEvent(viewModelScope) {
                    LibraryEvent.NavigateToPlaylist(FAVOURITE_PLAYLIST_ID)
                }
            }
        }
    }
}