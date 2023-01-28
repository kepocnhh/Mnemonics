package org.kepocnhh.mnemonics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.cancel
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Main {
    private val isPaused = mutableStateOf(true)
    private val isResumed = mutableStateOf(false)
    private val numbers = flow {
        while (!isPaused.value && isResumed.value) {
            val numbers = System.currentTimeMillis().toString()
            emit(numbers)
            println("numbers: $numbers")
            delay(1_000)
        }
        println("finish flow")
    }

    @Composable
    fun Screen(toSettings: () -> Unit) {
        var isPaused by remember { isPaused }
        println("is paused: $isPaused")
        var isResumed by remember { isResumed }
        println("is resumed: $isResumed")
        var text by remember { mutableStateOf("...") }
        if (!isPaused && isResumed) {
            println("running...")
            OneShotEffect {
                App.scope.launch {
                    println("collect...")
                    numbers.flowOn(Dispatchers.IO).onEach { numbers ->
                        text = "num: $numbers"
                    }.collect()
                }
            }
        }
        OnLifecycleEvent { event ->
            println("Lifecycle.Event: $event")
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    isPaused = true
                    isResumed = false
                }
                Lifecycle.Event.ON_RESUME -> {
                    isResumed = true
                }
                Lifecycle.Event.ON_PAUSE -> {
                    isResumed = false
                }
                else -> {
                    // ignored
                }
            }
        }
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {
                Button(
                    text = "settings",
                    onClick = {
                        toSettings()
                    }
                )
                Text(text = text)
                Button(
                    text = when (isPaused) {
                        true -> "play"
                        false -> "pause"
                    },
                    onClick = {
                        isPaused = !isPaused
                    }
                )
            }
        }
    }
}
