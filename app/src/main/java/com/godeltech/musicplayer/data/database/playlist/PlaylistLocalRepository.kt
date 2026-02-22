package com.godeltech.musicplayer.data.database.playlist

import com.godeltech.musicplayer.data.database.playlist.entities.PlaylistEntity
import com.godeltech.musicplayer.data.database.playlist.entities.PlaylistTrackEntity
import com.godeltech.musicplayer.data.database.playlist.entities.TrackEntity
import com.godeltech.musicplayer.player.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlaylistLocalRepository @Inject constructor(
    private val playlistDao: PlaylistDao
) {
    fun getPlaylistTracks(playlistId: String): Flow<List<TrackEntity>> {
        return playlistDao.getTracksFromPlaylist(playlistId)
    }

    fun getPlaylist(playlistId: String): Flow<PlaylistEntity?> {
        return playlistDao.getPlaylistById(playlistId)
    }

    fun isTrackInPlaylist(playlistId: String, trackId: String) = flow {
        emit(playlistDao.isTrackInPlaylist(playlistId, trackId))
    }

    suspend fun addPlaylist(
        playlistId: String,
        name: String,
        author: String,
        coverImageRes: Int
    ) {
        val existingPlaylist = playlistDao.getPlaylistById(playlistId)

        if (existingPlaylist == null) {
            playlistDao.insertPlaylist(
                PlaylistEntity(
                    id = playlistId,
                    name = name,
                    author = author,
                    coverImageRes = coverImageRes
                )
            )
        }
    }

    suspend fun addTrackToPlaylist(playlistId: String, track: Track) {
        playlistDao.addTrack(
            TrackEntity(
                trackId = track.id,
                title = track.title,
                artistName = track.artistName,
                imageUrl = track.imageUrl
            )
        )
        playlistDao.addTrackToPlaylist(
            PlaylistTrackEntity(
                trackId = track.id,
                playlistId = playlistId
            )
        )
    }

    suspend fun removeTrackFromPlaylist(playlistId: String, trackId: String) {
        playlistDao.removeTrackFromPlaylist(playlistId = playlistId, trackId = trackId)
    }
}