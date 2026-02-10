package com.godeltech.musicplayer.presentation.main.home

import com.godeltech.musicplayer.player.Track

data class HomeState(
    val isLoading: Boolean = false,
    val data: HomeModel = HomeModel(),
    val isError: Boolean = false
) {
    companion object {
        val Idle = HomeState()
    }
}

data class HomeModel(
    val trendingTracksA: TrackListModel = TrackListModel(),
    val trendingTracksB: TrackListModel = TrackListModel(),
    val recentTracks: TrackListModel = TrackListModel(),
    val recommendedPlaylists: List<PlaylistModel> = emptyList()
)

data class TrackListModel(
    val tracks: List<Track> = emptyList(),
    val id: HomeSectionId = HomeSectionId.UNKNOWN
)

enum class HomeSectionId {
    TRENDING_A,
    TRENDING_B,
    RECENT,
    UNKNOWN
}

data class TrackModel(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val artistName: String = ""
)

data class PlaylistModel(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val creatorName: String = ""
)

sealed class HomeEvent {
    data object NavigateToPlayer : HomeEvent()
    data class NavigateToPlaylist(val id: String) : HomeEvent()
}

sealed class HomeAction {
    data class TrackClicked(val index: Int, val id: HomeSectionId) : HomeAction()
    data class AlbumClicked(val id: String) : HomeAction()
}