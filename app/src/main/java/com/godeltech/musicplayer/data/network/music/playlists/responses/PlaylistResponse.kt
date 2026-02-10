package com.godeltech.musicplayer.data.network.music.playlists.responses

import com.google.gson.annotations.SerializedName

data class PlaylistResponse(
    val id: String?,
    @SerializedName("playlist_name")
    val name: String?,
    @SerializedName("user")
    val user: UserResponse?,
    @SerializedName("artwork")
    val coverPhoto: ArtworkResponse?,
    val description: String?,
    @SerializedName("release_date")
    val releaseData: String?,
    @SerializedName("is_album")
    val isAlbum: Boolean?
)

data class UserResponse(
    val id: String?,
    val name: String?
)

data class ArtworkResponse(
    @SerializedName("1000x1000")
    val photo: String?
)

data class PlaylistsDataResponse(
    @SerializedName("data")
    val playlists: List<PlaylistResponse>
)

data class PlayListDataResponse(
    @SerializedName("data")
    val playlists: List<PlaylistResponse>
)
