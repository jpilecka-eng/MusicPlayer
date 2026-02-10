package com.godeltech.musicplayer.data.network.music.tracks

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class TrackModule {
    @Provides
    @Singleton
    fun provideMarketService(retrofit: Retrofit): TrackService {
        return retrofit.create(TrackService::class.java)
    }

    @Provides
    @Singleton
    fun provideTrackRepository(trackService: TrackService): TrackRepository {
        return TrackRepository(trackService)
    }
}