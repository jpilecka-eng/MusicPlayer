package com.godeltech.musicplayer.data.network.music.playlists

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class PlaylistModule {
    @Provides
    @Singleton
    fun providePlaylistService(retrofit: Retrofit): PlaylistService {
        return retrofit.create(PlaylistService::class.java)
    }

    @Provides
    @Singleton
    fun providePlaylistRepository(playlistService: PlaylistService): PlaylistRepository {
        return PlaylistRepository(playlistService)
    }
}