package com.godeltech.musicplayer.presentation.main.search

import com.godeltech.musicplayer.player.Track

data class SearchState(
    val isLoading: Boolean = false,
    val data: SearchModel = SearchModel(),
    val isError: Boolean = false
) {
    companion object {
        val Idle = SearchState()
    }
}

data class SearchModel(
    val searchResult: SearchResult = SearchResult(),
    val searchInput: String = "",
    val selectedSearchFilter: SearchFilter = SearchFilter.Relevant
)

data class SearchResult(
    val tracks: List<Track> = emptyList(),
    val playlists: List<SearchPlaylistModel> = emptyList()
)

data class SearchPlaylistModel(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val creatorName: String = ""
)

sealed class SearchAction {
    data class UserInputChanged(val newInput: String) : SearchAction()
    data class TrackClicked(val index: Int) : SearchAction()
    data class FilterSelected(val filter: SearchFilter) : SearchAction()
    data class AlbumClicked(
        val id: String
    ) : SearchAction()
}

sealed class SearchEvent {
    data object NavigateToPlayer : SearchEvent()
    data class NavigateToPlaylists(
        val id: String
    ) : SearchEvent()
}

enum class SearchFilter(val value: String) {
    Relevant("relevant"),
    Popular("popular"),
    Recent("recent")
}