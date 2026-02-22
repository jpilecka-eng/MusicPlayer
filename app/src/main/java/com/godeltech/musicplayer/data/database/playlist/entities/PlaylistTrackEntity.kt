package com.godeltech.musicplayer.data.database.playlist.entities

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "playlist_tracks",
    primaryKeys = ["playlistId", "trackId"],
    indices = [Index("playlistId"), Index("trackId")]
)
data class PlaylistTrackEntity(
    val playlistId: String,
    val trackId: String,
    val addedAt: Long = System.currentTimeMillis()
)