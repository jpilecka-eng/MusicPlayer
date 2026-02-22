package com.godeltech.musicplayer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.godeltech.musicplayer.data.database.playlist.PlaylistDao
import com.godeltech.musicplayer.data.database.playlist.entities.PlaylistEntity
import com.godeltech.musicplayer.data.database.playlist.entities.PlaylistTrackEntity
import com.godeltech.musicplayer.data.database.playlist.entities.TrackEntity

@Database(
    entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        PlaylistTrackEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}