package com.godeltech.musicplayer.data.network.music.tracks

import kotlinx.coroutines.flow.flow

class TrackRepository(
    private val trackService: TrackService
) {
    fun getTrendingTracks() = flow {
        val data = trackService.getTrendingTracks()
        emit(data)
    }

    fun getRecentTracks() = flow {
        val data = trackService.getRecentTracks()
        emit(data)
    }

    fun getTrack(id: String) = flow {
        val track = trackService.getTrack(id)
        emit(track)
    }
}