package com.godeltech.musicplayer.data.database.playlist.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val id: String,
    val name: String,
    val author: String,
    val coverImageRes: Int,
    val createdAt: Long = System.currentTimeMillis()
)