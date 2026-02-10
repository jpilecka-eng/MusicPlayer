package com.godeltech.musicplayer.presentation.playlist

import com.godeltech.musicplayer.player.Track

data class PlaylistState(
    val isLoading: Boolean = false,
    val data: PlaylistModel = PlaylistModel(),
    val isError: Boolean = false
) {
    companion object {
        val Idle = PlaylistState()
    }
}

data class PlaylistModel(
    val tracks: List<Track> = emptyList(),
    val playlistInfo: PlaylistInfoModel = PlaylistInfoModel()
)

data class PlaylistInfoModel(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val descriptionTruncated: String = "",
    val releaseDate: String = "",
    val isAlbum: Boolean = false,
    val hasReadMore: Boolean = false,
    val descriptionExpanded: Boolean = false
)

sealed class PlaylistAction {
    data object GlobalPlayClicked : PlaylistAction()
    data class TrackClicked(val index: Int) : PlaylistAction()
    data object OnReadMoreDescriptionClicked : PlaylistAction()
    data object OnNavigateBackClicked : PlaylistAction()
}

sealed class PlaylistEvent {
    data object NavigateToPlayer : PlaylistEvent()
    data object NavigateBack : PlaylistEvent()
}