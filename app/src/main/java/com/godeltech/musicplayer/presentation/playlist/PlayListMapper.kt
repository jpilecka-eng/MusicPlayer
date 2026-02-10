package com.godeltech.musicplayer.presentation.playlist

import com.godeltech.musicplayer.data.network.music.playlists.responses.PlayListDataResponse
import com.godeltech.musicplayer.data.network.music.playlists.responses.PlaylistTracksDataResponse
import com.godeltech.musicplayer.player.Track
import jakarta.inject.Inject

private const val MAX_DESCRIPTION_LENGTH = 80

class PlayListMapper @Inject constructor(
) {
    fun mapPlayListData(
        playListTracksDataResponse: PlaylistTracksDataResponse,
        playlistResponse: PlayListDataResponse
    ): PlaylistModel {
        val tracks = playListTracksDataResponse.tracksList.map { item ->
            Track(
                id = item.id ?: "",
                artistName = item.artist.name ?: "",
                title = item.title ?: "",
                imageUrl = item.artworkResponse.imageUrl ?: ""

            )
        }
        val description = playlistResponse.playlists[0].description ?: ""
        val hasReadMore = description.isNotEmpty() && description.length > MAX_DESCRIPTION_LENGTH
        val playlistInfo = PlaylistInfoModel(
            id = playlistResponse.playlists[0].id ?: "",
            title = playlistResponse.playlists[0].name ?: "",
            descriptionTruncated = getTruncatedDescription(
                playlistResponse.playlists[0].description ?: ""
            ),
            description = description,
            releaseDate = playlistResponse.playlists[0].releaseData ?: "",
            imageUrl = playlistResponse.playlists[0].coverPhoto?.photo ?: "",
            isAlbum = playlistResponse.playlists[0].isAlbum ?: false,
            hasReadMore = hasReadMore
        )
        return PlaylistModel(
            tracks = tracks,
            playlistInfo = playlistInfo
        )
    }

    private fun getTruncatedDescription(description: String): String {
        if (description.isEmpty()) return ""
        return description.take(MAX_DESCRIPTION_LENGTH) + "..."
    }
}