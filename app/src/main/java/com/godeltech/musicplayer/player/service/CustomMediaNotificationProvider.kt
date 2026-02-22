package com.godeltech.musicplayer.player.service

import android.content.Context
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.CommandButton
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.MediaSession
import com.godeltech.musicplayer.R
import com.google.common.collect.ImmutableList

/**
 * Changes the play, pause, skip icons for media notification that is used on device with an OS below API 33 (Android 13)
 * On Android 13+ the system media notification no longer allows to change icon on notification
 * https://github.com/androidx/media/issues/2202
 * */

@UnstableApi
class CustomMediaNotificationProvider(
    context: Context
) : DefaultMediaNotificationProvider(context) {

    override fun getMediaButtons(
        session: MediaSession,
        playerCommands: Player.Commands,
        mediaButtonPreferences: ImmutableList<CommandButton>,
        showPauseButton: Boolean
    ): ImmutableList<CommandButton> {

        val buttons =
            super.getMediaButtons(session, playerCommands, mediaButtonPreferences, showPauseButton)
                .toMutableList()

        val playIcon = if (showPauseButton) {
            R.drawable.ic_pause
        } else R.drawable.ic_play
        val playPause = CommandButton.Builder()
            .setPlayerCommand(Player.COMMAND_PLAY_PAUSE)
            .setDisplayName("Play/Pause")
            .setCustomIconResId(playIcon)
            .build()

        val prev = CommandButton.Builder()
            .setPlayerCommand(Player.COMMAND_SEEK_TO_PREVIOUS)
            .setDisplayName("Previous")
            .setCustomIconResId(R.drawable.ic_previous)
            .build()

        val next = CommandButton.Builder()
            .setPlayerCommand(Player.COMMAND_SEEK_TO_NEXT)
            .setDisplayName("Next")
            .setCustomIconResId(R.drawable.ic_next)
            .build()

        if (buttons.size > 3) {
            buttons[0] = prev
            buttons[1] = playPause
            buttons[2] = next
        } else {
            buttons.add(playPause)
        }
        return ImmutableList.copyOf(buttons)
    }
}