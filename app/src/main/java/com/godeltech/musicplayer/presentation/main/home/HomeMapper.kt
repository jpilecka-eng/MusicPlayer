package com.godeltech.musicplayer.presentation.main.home

import com.godeltech.musicplayer.data.network.music.playlists.responses.PlaylistsDataResponse
import com.godeltech.musicplayer.data.network.music.tracks.responses.TracksDataResponse
import com.godeltech.musicplayer.player.Track
import javax.inject.Inject

class HomeMapper @Inject constructor(
) {
    fun mapHomeData(
        popularTracks: TracksDataResponse,
        recentTracks: TracksDataResponse,
        recommendedPlaylists: PlaylistsDataResponse
    ): HomeModel {

        val mappedTrendingTracks = mapFromTrackDataResponse(popularTracks)
        val half = mappedTrendingTracks.size / 2
        val trendingTracksAListModel = TrackListModel(
            mappedTrendingTracks.subList(0, half),
            HomeSectionId.TRENDING_A
        )
        val trendingTracksBListModel = TrackListModel(
            mappedTrendingTracks.subList(half, mappedTrendingTracks.size),
            HomeSectionId.TRENDING_B
        )
        val mappedRecentTracks = mapFromTrackDataResponse(recentTracks)
        val recentTracksListModel = TrackListModel(
            mappedRecentTracks,
            HomeSectionId.RECENT
        )
        val mappedRecommendedPlaylists = mapFromPlaylistDataResponse(recommendedPlaylists)

        return HomeModel(
            trendingTracksA = trendingTracksAListModel,
            trendingTracksB = trendingTracksBListModel,
            recentTracks = recentTracksListModel,
            recommendedPlaylists = mappedRecommendedPlaylists
        )
    }

    fun mapFromTrackDataResponse(trackListResponse: TracksDataResponse): List<Track> {
        val tracks = trackListResponse.tracksList.map { trackResponse ->
            val duration = (trackResponse.duration?.toLong()?.times(1000L)) ?: 0L
            Track(
                title = trackResponse.title ?: "",
                imageUrl = trackResponse.artworkResponse.imageUrl ?: "",
                artistName = trackResponse.artist.name ?: "",
                id = trackResponse.id ?: ""
            )
        }
        return tracks
    }

    fun mapFromPlaylistDataResponse(playlistDataResponse: PlaylistsDataResponse): List<PlaylistModel> {
        return playlistDataResponse.playlists.map { playlist ->
            PlaylistModel(
                title = playlist.name ?: "",
                imageUrl = playlist.coverPhoto?.photo ?: "",
                creatorName = playlist.user?.name ?: "",
                id = playlist.id ?: ""
            )
        }
    }
}