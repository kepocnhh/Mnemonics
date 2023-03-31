package org.kepocnhh.mnemonics.implementation.module.main

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import org.kepocnhh.mnemonics.foundation.provider.Injection
import org.kepocnhh.mnemonics.implementation.util.androidx.lifecycle.AbstractViewModel
import java.util.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

internal class MainViewModel(private val injection: Injection) : AbstractViewModel() {
    data class State(
        val isPaused: Boolean,
        val number: Int?,
        val progress: Float,
    )

    private val _state = MutableStateFlow(State(isPaused = true, number = null, progress = 0f))
    val state = _state.asStateFlow()

    private fun CoroutineScope.onNextNumber(start: Duration) {
        while (isActive) {
            val now = System.nanoTime().nanoseconds
            val duration = kotlin.math.min((now - start) / time, 1.0)
            if (duration < 1.0) {
                if (state.value.isPaused) {
                    _state.value = state.value.copy(progress = duration.toFloat())
                    break
                }
            } else {
                _state.value = state.value.copy(progress = 0f)
                break
            }
        }
    }

    private suspend fun onNextNumber() {
        if (state.value.isPaused) return
        val number = state.value.number
        val next = viewModelScope.async(injection.contexts.io) {
            nextNumber(random, 100, actual = number)
        }
        if (number != null) {
            withContext(injection.contexts.io) {
                onNextNumber(start = System.nanoTime().nanoseconds - time * state.value.progress.toDouble())
            }
        }
        if (!state.value.isPaused) {
            _state.value = state.value.copy(number = next.await())
        }
    }

    fun nextNumber() {
        injection.launch {
            onNextNumber()
        }
    }

    fun pause(value: Boolean) {
        _state.value = state.value.copy(isPaused = value)
    }

    companion object {
        private val random = Random()
        val time = 3.seconds

        private fun nextNumber(random: Random, max: Int, actual: Int?): Int {
            while (true) {
                val result = random.nextInt(max + 100) - 100
                if (result != actual) return result
            }
        }
    }
}
