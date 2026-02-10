package com.godeltech.musicplayer.player.service

import android.app.Service
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
class PlayerServiceModule {

    @OptIn(UnstableApi::class)
    @Provides
    @ServiceScoped
    fun provideExoPlayer(
        @ApplicationContext context: Context
    ): ExoPlayer {
        return ExoPlayer.Builder(context).build().apply {
            preloadConfiguration = ExoPlayer.PreloadConfiguration(5_000_000L)
        }
    }

    @Provides
    @ServiceScoped
    fun provideMediaSession(
        service: Service,
        exoPlayer: ExoPlayer
    ): MediaSession {
        return MediaSession.Builder(service, exoPlayer).build()
    }
}