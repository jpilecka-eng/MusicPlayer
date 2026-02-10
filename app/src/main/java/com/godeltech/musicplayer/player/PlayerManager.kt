package com.godeltech.musicplayer.player

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.godeltech.musicplayer.player.controller.ControllerProvider
import com.godeltech.musicplayer.player.service.PlayerServiceStateHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerManager @Inject constructor(
    private val controllerProvider: ControllerProvider
) {
    @Inject
    lateinit var playerServiceStateHandler: PlayerServiceStateHandler

    private var controller: MediaController? = null

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private var progressJob: Job? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val listener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _playerState.update {
                it.copy(
                    isPlaying = isPlaying
                )
            }
            if (isPlaying) startProgressUpdates()
            else stopProgressUpdates()
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            _playerState.update {
                it.copy(
                    repeatMode = repeatMode
                )
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val metadata = mediaItem?.mediaMetadata
            _playerState.update {
                it.copy(
                    currentlyPlayingTrack = Track(
                        id = mediaItem?.mediaId ?: "",
                        title = metadata?.title?.toString() ?: "",
                        artistName = metadata?.artist?.toString() ?: "",
                        imageUrl = metadata?.artworkUri?.toString() ?: "",
                    ),
                    positionMs = 0L,
                )
            }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == Player.STATE_READY) {
                val duration = controller?.duration ?: 0L
                _playerState.update {
                    it.copy(
                        durationMs = duration
                    )
                }
            }
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            _playerState.update {
                it.copy(shuffleEnabled = shuffleModeEnabled)
            }
        }

        override fun onIsLoadingChanged(isLoading: Boolean) {
            _playerState.update {
                it.copy(
                    isLoading = isLoading
                )
            }
        }
    }

    init {
        scope.launch {
            controllerProvider.controller.collect { controller ->
                if (controller != null) {
                    this@PlayerManager.controller = controller
                    controller.addListener(listener)
                }
            }
        }
        scope.launch {
            playerServiceStateHandler.serviceIsRunning.collect { isRunning ->
                if (!isRunning) {
                    controller?.removeListener(listener)
                    clear()
                }
            }
        }
    }

    fun onAction(playerAction: PlayerAction) {
        when (playerAction) {
            is PlayerAction.PlaySong -> {
                playSong(
                    items = playerAction.items,
                    playlistId = playerAction.playlistId,
                    index = playerAction.index

                )
            }

            is PlayerAction.PlayPlaylist -> {
                playPlaylist(
                    items = playerAction.items,
                    playlistId = playerAction.playlistId,
                )
            }

            is PlayerAction.PlayPause -> {
                playPause()
            }

            is PlayerAction.Repeat -> {
                toggleRepeatMode()
            }

            is PlayerAction.Shuffle -> {
                toggleShuffle()
            }

            is PlayerAction.SeekTo -> {
                controller?.seekTo(playerAction.positionMs)
            }

            is PlayerAction.PlayNext -> {
                controller?.seekToNext()
                controller?.play()
            }

            is PlayerAction.PlayPrevious -> {
                controller?.seekToPrevious()
                controller?.play()
            }
        }
    }

    private fun setItems(items: List<Track>, index: Int) {
        val mediaItems: List<MediaItem> = items.map { item ->
            MediaItem.Builder()
                .setUri(buildStreamUrl(item.id))
                .setMediaId(item.id)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(item.title)
                        .setArtist(item.artistName)
                        .setArtworkUri(item.imageUrl.toUri())
                        .build()
                )
                .build()
        }
        controller?.setMediaItems(mediaItems, index, 0)
        controller?.prepare()
    }

    private fun toggleRepeatMode() {
        controller?.let { player ->
            player.repeatMode = when (player.repeatMode) {
                Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
                Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
                Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_OFF
                else -> Player.REPEAT_MODE_OFF
            }

        }
    }

    private fun toggleShuffle() {
        controller?.let { player ->
            player.shuffleModeEnabled = !player.shuffleModeEnabled
        }
    }

    private fun playPause() {
        controller?.let {
            if (it.isPlaying) it.pause() else it.play()
        }
    }

    private fun playPlaylist(items: List<Track>, playlistId: String) {
        val isSamePlaylist = playerState.value.currentlyPlayingPlaylistId == playlistId
        if (!isSamePlaylist) {
            _playerState.update {
                PlayerState(currentlyPlayingPlaylistId = playlistId)
            }
            setItems(items, 0)
            controller?.play() ?: return
        } else {
            playPause()
        }
    }

    private fun playSong(
        items: List<Track>,
        playlistId: String,
        index: Int
    ) {
        val isSamePlaylist = playerState.value.currentlyPlayingPlaylistId == playlistId
        if (!isSamePlaylist) {
            _playerState.update {
                PlayerState(currentlyPlayingPlaylistId = playlistId)
            }
            setItems(items, index)
        } else {
            controller?.seekTo(index, 0)
        }
        controller?.play() ?: return
    }

    private fun buildStreamUrl(id: String): String {
        return TRACK_STREAM_BASE_URL.format(id)
    }

    private fun startProgressUpdates() {
        if (progressJob != null) return

        progressJob = scope.launch {
            while (playerState.value.isPlaying) {
                val pos = controller?.currentPosition ?: 0L
                _playerState.update {
                    it.copy(
                        positionMs = pos,
                    )
                }
                delay(16L) //1000MS / 60X = 16 //most screens have 60 fps
            }
        }
    }

    private fun stopProgressUpdates() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun clear() {
        _playerState.update {
            PlayerState()
        }
        controller = null
        stopProgressUpdates()
    }
}