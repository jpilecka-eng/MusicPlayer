package com.godeltech.musicplayer.presentation.common.threading

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DispatcherProvider @Inject constructor() {
    fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    fun ui(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    fun default(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    fun unconfined(): CoroutineDispatcher {
        return Dispatchers.Unconfined
    }
}