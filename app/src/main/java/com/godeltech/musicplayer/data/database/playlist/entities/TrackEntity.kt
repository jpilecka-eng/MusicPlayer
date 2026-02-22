package com.godeltech.musicplayer.data.database.playlist.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey val trackId: String,
    val title: String?,
    val artistName: String?,
    val imageUrl: String?
)