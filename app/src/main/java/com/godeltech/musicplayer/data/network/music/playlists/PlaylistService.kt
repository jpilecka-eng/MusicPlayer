package com.godeltech.musicplayer.data.network.music.playlists

import com.godeltech.musicplayer.data.network.music.playlists.responses.PlayListDataResponse
import com.godeltech.musicplayer.data.network.music.playlists.responses.PlaylistTracksDataResponse
import com.godeltech.musicplayer.data.network.music.playlists.responses.PlaylistsDataResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PlaylistService {

    @GET("playlists/trending")
    suspend fun getRecommendedPlaylists(
    ): PlaylistsDataResponse

    @GET("playlists/{id}/tracks")
    suspend fun getPlaylistTracks(
        @Path("id") id: String
    ): PlaylistTracksDataResponse

    @GET("playlists/{id}")
    suspend fun getPlaylists(
        @Path("id") id: String
    ): PlayListDataResponse
}