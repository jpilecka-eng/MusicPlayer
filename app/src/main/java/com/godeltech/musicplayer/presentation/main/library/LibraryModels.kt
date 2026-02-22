package com.godeltech.musicplayer.presentation.main.library

data class LibraryState(
    val isLoading: Boolean = false,
    val isError: Boolean = false
) {
    companion object {
        val Idle = LibraryState()
    }
}

sealed class LibraryAction {
    data object FavouritePlaylistClicked : LibraryAction()
}

sealed class LibraryEvent {
    data class NavigateToPlaylist(val id: String) : LibraryEvent()
}

const val FAVOURITE_PLAYLIST_ID = "player_favourites"