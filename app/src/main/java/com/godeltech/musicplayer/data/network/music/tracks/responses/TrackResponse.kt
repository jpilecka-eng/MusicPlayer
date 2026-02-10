package com.godeltech.musicplayer.data.network.music.tracks.responses

import com.google.gson.annotations.SerializedName

data class TrackResponse(
    @SerializedName("id")
    val id: String? = "",
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("artwork")
    val artworkResponse: ArtworkResponse,
    @SerializedName("user")
    val artist: ArtistResponse,
    @SerializedName("duration")
    val duration: Int? = 0
)

data class ArtworkResponse(
    @SerializedName("1000x1000")
    val imageUrl: String? = ""
)

data class ArtistResponse(
    @SerializedName("name")
    val name: String?,
)