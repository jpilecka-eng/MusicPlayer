package com.godeltech.musicplayer.data.network.music.playlists.responses

import com.google.gson.annotations.SerializedName

data class PlayListTrackResponse(
    @SerializedName("id")
    val id: String? = "",
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("artwork")
    val artworkResponse: PlaylistArtworkResponse,
    @SerializedName("user")
    val artist: PlaylistArtistResponse,
    @SerializedName("duration")
    val duration: Int? = 0
)

data class PlaylistArtworkResponse(
    @SerializedName("1000x1000")
    val imageUrl: String? = ""
)

data class PlaylistArtistResponse(
    @SerializedName("name")
    val name: String?
)

data class PlaylistTracksDataResponse(
    @SerializedName("data")
    val tracksList: List<PlayListTrackResponse>
)