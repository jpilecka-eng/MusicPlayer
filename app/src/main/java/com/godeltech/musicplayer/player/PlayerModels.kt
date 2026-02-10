package com.godeltech.musicplayer.player

import androidx.media3.common.Player

const val TRACK_STREAM_BASE_URL = "https://api.audius.co/v1/tracks/%s/stream"

data class PlayerState(
    val currentlyPlayingTrack: Track = Track(),
    val currentlyPlayingPlaylistId: String = "",
    val isLoading: Boolean = false,
    val isPlaying: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val repeatMode: Int = Player.REPEAT_MODE_OFF,
    val shuffleEnabled: Boolean = false
)

data class Track(
    val id: String = "",
    val title: String = "",
    val artistName: String = "",
    val imageUrl: String = ""
)

sealed class PlayerAction {
    data class PlayPlaylist(
        val items: List<Track>,
        val playlistId: String
    ) : PlayerAction()

    data class PlaySong(
        val items: List<Track>,
        val playlistId: String,
        val index: Int
    ) : PlayerAction()

    data object PlayNext : PlayerAction()
    data object PlayPrevious : PlayerAction()
    data object PlayPause : PlayerAction()
    data class SeekTo(val positionMs: Long) : PlayerAction()
    data object Repeat : PlayerAction()
    data object Shuffle : PlayerAction()
}