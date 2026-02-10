package com.godeltech.musicplayer.presentation.common.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <T> Channel<T>.sendEvent(scope: CoroutineScope, onGetEventListener: () -> T) {
    scope.launch {
        this@sendEvent.send(onGetEventListener.invoke())
    }
}

@Composable
fun <T> Flow<T>.collectSiteEffectWithLifecycle(onEvent: (T) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(this, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                this@collectSiteEffectWithLifecycle.collect(onEvent)
            }
        }
    }
}

data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

fun <A, B, C, D> Flow<A>.zip4(
    flow2: Flow<B>,
    flow3: Flow<C>,
    flow4: Flow<D>
): Flow<Quadruple<A, B, C, D>> {
    return this
        .zip(flow2) { a, b -> Pair(a, b) }
        .zip(flow3) { pair, c -> Triple(pair.first, pair.second, c) }
        .zip(flow4) { triple, d -> Quadruple(triple.first, triple.second, triple.third, d) }
}

fun <A, B, C> Flow<A>.zip3(
    flow2: Flow<B>,
    flow3: Flow<C>,
): Flow<Triple<A, B, C>> {
    return this
        .zip(flow2) { a, b -> Pair(a, b) }
        .zip(flow3) { pair, c -> Triple(pair.first, pair.second, c) }
}