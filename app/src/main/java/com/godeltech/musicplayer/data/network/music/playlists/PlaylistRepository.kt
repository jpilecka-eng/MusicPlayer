package com.godeltech.musicplayer.data.network.music.playlists

import kotlinx.coroutines.flow.flow

class PlaylistRepository(
    private val playlistService: PlaylistService
) {
    fun getRecommendedPlaylists() = flow {
        val playlists = playlistService.getRecommendedPlaylists()
        emit(playlists)
    }

    fun getPlaylistTracks(id: String) = flow {
        val playListTracks = playlistService.getPlaylistTracks(id)
        emit(playListTracks)
    }

    fun getPlaylist(id: String) = flow {
        val playListInfo = playlistService.getPlaylists(id)
        emit(playListInfo)
    }
}