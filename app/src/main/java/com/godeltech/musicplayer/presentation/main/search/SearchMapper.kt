package com.godeltech.musicplayer.presentation.main.search

import com.godeltech.musicplayer.data.network.music.playlists.responses.PlaylistsDataResponse
import com.godeltech.musicplayer.data.network.music.tracks.responses.TracksDataResponse
import com.godeltech.musicplayer.player.Track
import javax.inject.Inject

class SearchMapper @Inject constructor(
) {
    fun mapSearchData(
        tracksDataResponse: TracksDataResponse,
        playlistDataResponse: PlaylistsDataResponse
    ): SearchResult {
        return SearchResult(
            tracks = mapFromTrackDataResponse(tracksDataResponse),
            playlists = mapFromPlaylistDataResponse(playlistDataResponse)
        )
    }

    fun mapFromTrackDataResponse(trackDataResponse: TracksDataResponse): List<Track> {
        val tracks = trackDataResponse.tracksList.map { trackResponse ->
            Track(
                title = trackResponse.title ?: "",
                imageUrl = trackResponse.artworkResponse.imageUrl ?: "",
                artistName = trackResponse.artist.name ?: "",
                id = trackResponse.id ?: ""
            )
        }
        return tracks
    }

    fun mapFromPlaylistDataResponse(playlistDataResponse: PlaylistsDataResponse): List<SearchPlaylistModel> {
        return playlistDataResponse.playlists.map { playlist ->
            SearchPlaylistModel(
                title = playlist.name ?: "",
                imageUrl = playlist.coverPhoto?.photo ?: "",
                creatorName = playlist.user?.name ?: "",
                id = playlist.id ?: ""
            )
        }
    }
}