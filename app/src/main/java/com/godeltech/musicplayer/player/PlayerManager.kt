package com.godeltech.musicplayer.player

import android.util.Log
import androidx.core.net.toUri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.session.MediaController
import com.godeltech.musicplayer.player.controller.ControllerProvider
import com.godeltech.musicplayer.player.service.PlayerServiceStateHandler
import com.godeltech.musicplayer.presentation.common.extensions.sendEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PlayerManager"

@Singleton
class PlayerManager @Inject constructor(
    private val controllerProvider: ControllerProvider
) {
    @Inject
    lateinit var playerServiceStateHandler: PlayerServiceStateHandler

    private var controller: MediaController? = null

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private val _event = Channel<PlayerEvent>()
    val event = _event.receiveAsFlow()

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
            val queue = buildQueue()
            _playerState.update {
                it.copy(
                    queue = queue,
                    repeatMode = repeatMode
                )
            }
            updateSkipPositions()
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            val queue = buildQueue()
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
                    queue = queue,
                    currentIndex = controller?.currentPosition ?: 0
                )
            }
            updateSkipPositions()
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
            val queue = buildQueue()
            _playerState.update {
                it.copy(
                    shuffleEnabled = shuffleModeEnabled,
                    queue = queue
                )
            }
            updateSkipPositions()
        }

        override fun onIsLoadingChanged(isLoading: Boolean) {
            _playerState.update {
                it.copy(
                    isLoading = isLoading
                )
            }
        }

        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            if (reason == Player.TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED) {
                val queue = buildQueue()
                _playerState.update {
                    it.copy(
                        queue = queue
                    )
                }
            }
            updateSkipPositions()
        }

        override fun onPlayerError(error: PlaybackException) {
            Log.d(TAG, "Error: $error")
            _event.sendEvent(scope) {
                PlayerEvent.Error
            }
            super.onPlayerError(error)
        }
    }

    init {
        scope.launch {
            controllerProvider.controller.collect { controller ->
                if (controller != null) {
                    this@PlayerManager.controller = controller
                    controller.addListener(listener)
                    updateSkipPositions()
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
        scope.launch {
            playerServiceStateHandler.shuffleOrderUpdated.collect {
                val queue = buildQueue()
                _playerState.update {
                    it.copy(
                        queue = queue
                    )
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
                    index = playerAction.index,
                    playListName = playerAction.playListName,
                    reshuffle = playerAction.reshuffle
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
        val order = getCustomShuffleOrder(playerState.value.currentIndex.toInt())
        playerServiceStateHandler.onShuffleOrderChanged(order)

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
        playListName: String,
        index: Int,
        reshuffle: Boolean,
    ) {
        val isSamePlaylist = playerState.value.currentlyPlayingPlaylistId == playlistId
        if (!isSamePlaylist) {
            _playerState.update {
                it.copy(
                    currentlyPlayingPlaylistId = playlistId,
                    currentlyPlayingPlaylistName = playListName
                )
            }
            setItems(items, index)
        } else {
            controller?.seekTo(index, 0)
        }
        if (controller?.shuffleModeEnabled == true && reshuffle) {
            val order = getCustomShuffleOrder(index)
            playerServiceStateHandler.onShuffleOrderChanged(order)
        }
        playPause()
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

    private fun getCustomShuffleOrder(index: Int): IntArray {
        val count = controller?.mediaItemCount
        if (count == null || count <= 0) {
            return intArrayOf()
        }
        val indexes = (0 until count).toMutableList()
        indexes.remove(index)
        indexes.shuffle()
        val finalOrder = listOf(index) + indexes
        _playerState.update {
            it.copy(
                shuffleOrder = finalOrder
            )
        }
        return finalOrder.toIntArray()
    }

    private fun buildQueue(): List<Track> {
        val timeline = controller?.currentTimeline
        if (timeline == null
            || timeline.isEmpty
            || controller == null
            || controller?.currentMediaItem == null
        ) return emptyList()

        val window = Timeline.Window()
        val result = mutableListOf<Track>()

        var index = controller?.currentMediaItemIndex
        if (index == C.INDEX_UNSET || index == null) return emptyList()
        if (controller!!.repeatMode == Player.REPEAT_MODE_ONE) {
            val item = controller!!.currentMediaItem!!
            return listOf(
                Track(
                    id = item.mediaId,
                    title = item.mediaMetadata.title?.toString() ?: "",
                    artistName = item.mediaMetadata.artist?.toString() ?: "",
                    imageUrl = item.mediaMetadata.artworkUri.toString(),
                    playlistIndex = index
                )
            )
        }

        repeat(timeline.windowCount) {
            timeline.getWindow(index!!, window)
            val item = window.mediaItem
            result.add(
                Track(
                    id = item.mediaId,
                    title = item.mediaMetadata.title?.toString() ?: "",
                    artistName = item.mediaMetadata.artist?.toString() ?: "",
                    imageUrl = item.mediaMetadata.artworkUri.toString(),
                    playlistIndex = index
                )
            )
            index = timeline.getNextWindowIndex(
                index,
                controller!!.repeatMode,
                controller!!.shuffleModeEnabled
            )
            if (index == C.INDEX_UNSET) {
                return result
            }
        }
        return result
    }

    private fun updateSkipPositions() {
        controller?.let {
            _playerState.update {
                it.copy(
                    hasNext = controller?.hasNextMediaItem() == true,
                    hasPrev = controller?.hasPreviousMediaItem() == true
                )
            }
        }
    }
}