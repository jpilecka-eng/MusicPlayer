package com.godeltech.musicplayer.data.network.music.tracks

import com.godeltech.musicplayer.data.network.music.tracks.responses.TrackDataResponse
import com.godeltech.musicplayer.data.network.music.tracks.responses.TracksDataResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface TrackService {

    @GET("tracks/recent-premium")
    suspend fun getRecentTracks(
    ): TracksDataResponse

    @GET("tracks/trending")
    suspend fun getTrendingTracks(
    ): TracksDataResponse

    @GET("tracks/{id}")
    suspend fun getTrack(@Path("id") id: String): TrackDataResponse
}