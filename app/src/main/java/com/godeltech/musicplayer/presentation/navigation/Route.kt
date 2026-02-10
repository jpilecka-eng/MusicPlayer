package com.godeltech.musicplayer.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object Home : Route

    @Serializable
    data object Search : Route

    @Serializable
    data object Library : Route

    @Serializable
    data object Player : Route

    @Serializable
    data class Playlist(val id: String) : Route
}