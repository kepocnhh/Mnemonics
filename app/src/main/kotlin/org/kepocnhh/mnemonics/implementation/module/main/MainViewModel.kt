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
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

internal class MainViewModel(private val injection: Injection) : AbstractViewModel() {
    data class State(
        val isPaused: Boolean,
        val number: Int?,
        val progress: Float,
    )

    private val _state = MutableStateFlow(
        State(
            isPaused = true,
            number = null,
            progress = 0f
        )
    )
    val state = _state.asStateFlow()

    private val isStarted = AtomicBoolean(false)

    fun nextNumber() {
        if (isStarted.get()) return
        injection.launch {
            isStarted.set(true)
            val number = state.value.number
            val next = async(injection.contexts.io) {
                nextNumber(random, 100, actual = number)
            }
            if (number != null) {
                withContext(injection.contexts.io) {
                    val start = System.nanoTime().nanoseconds - time * state.value.progress.toDouble()
                    while (isActive) {
                        val now = System.nanoTime().nanoseconds
                        val duration = kotlin.math.min((now - start) / time, 1.0)
                        if (duration < 1.0) {
                            _state.value = state.value.copy(progress = duration.toFloat())
                            if (state.value.isPaused) break
                            delay(64.milliseconds)
                        } else {
                            _state.value = state.value.copy(progress = 0f)
                            break
                        }
                    }
                }
            }
            if (!state.value.isPaused) {
                _state.value = state.value.copy(number = next.await())
            }
            isStarted.set(false)
        }
    }

    fun pause(value: Boolean) {
        _state.value = state.value.copy(isPaused = value)
    }

    companion object {
        private val random = Random()
        val time = 6.seconds

        private fun nextNumber(random: Random, max: Int, actual: Int?): Int {
            while (true) {
                val result = random.nextInt(max + 100) - 100
                if (result != actual) return result
            }
        }
    }
}
