package com.godeltech.musicplayer.data.network.music.tracks.responses

import com.google.gson.annotations.SerializedName

data class TracksDataResponse(
    @SerializedName("data")
    val tracksList: List<TrackResponse>
)

data class TrackDataResponse(
    @SerializedName("data")
    val tracksList: TrackResponse
)