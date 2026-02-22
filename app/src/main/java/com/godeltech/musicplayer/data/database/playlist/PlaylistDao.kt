package com.godeltech.musicplayer.data.database.playlist

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.godeltech.musicplayer.data.database.playlist.entities.PlaylistEntity
import com.godeltech.musicplayer.data.database.playlist.entities.PlaylistTrackEntity
import com.godeltech.musicplayer.data.database.playlist.entities.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(track: TrackEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPlaylist(playlistTrackEntity: PlaylistTrackEntity)

    @Query(
        "DELETE FROM playlist_tracks " +
                "where playlistId = :playlistId " +
                "AND trackId = :trackId"
    )
    suspend fun removeTrackFromPlaylist(playlistId: String, trackId: String)

    @Query(
        "SELECT EXISTS(" +
                "SELECT 1 FROM playlist_tracks where playlistId =:playlistId " +
                "and trackId = :trackId" +
                ")"
    )
    suspend fun isTrackInPlaylist(playlistId: String, trackId: String): Boolean

    @Query(
        "SELECT tracks.* FROM tracks" +
                " INNER JOIN playlist_tracks ON tracks.trackID = playlist_tracks.trackId" +
                " where playlist_tracks.playlistId = :playlistId ORDER BY playlist_tracks.addedAt DESC"
    )
    fun getTracksFromPlaylist(playlistId: String): Flow<List<TrackEntity>>

    @Query(
        "SELECT * from playlists where id =:playlistId limit 1"
    )
    fun getPlaylistById(playlistId: String): Flow<PlaylistEntity?>
}