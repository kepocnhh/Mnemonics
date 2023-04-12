package org.kepocnhh.mnemonics.implementation.module.main

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import org.kepocnhh.mnemonics.foundation.provider.Injection
import org.kepocnhh.mnemonics.implementation.entity.Environment
import org.kepocnhh.mnemonics.implementation.entity.Range
import org.kepocnhh.mnemonics.implementation.util.androidx.lifecycle.AbstractViewModel
import java.util.Random
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds

internal class MainViewModel(private val injection: Injection) : AbstractViewModel() {
    data class State(
        val isPaused: Boolean,
        val number: Int?,
        val progress: Float,
    )

    private val _state = MutableStateFlow(empty)
    val state = _state.asStateFlow()

    private val _env = MutableStateFlow<Environment?>(null)
    val env = _env.asStateFlow()

    private var job: Job? = null

    fun nextNumber() {
        if (job?.isActive == true) return
        job = injection.launch {
            val actual = state.value.number
            val delay = env.value?.delay
            if (actual == null || delay == null) {
                _env.value = withContext(injection.contexts.io) {
                    injection.local.env
                }
                _state.value = withContext(injection.contexts.io) {
                    State(
                        number = nextNumber(random, range = injection.local.env.range),
                        isPaused = false,
                        progress = 0f,
                    )
                }
            } else {
                val next = async(injection.contexts.io) {
                    nextNumber(random, range = injection.local.env.range, actual = actual)
                }
                withContext(injection.contexts.io) {
                    val start = System.nanoTime().nanoseconds - delay * state.value.progress.toDouble()
                    while (isActive) {
                        if (_state.value.isPaused) break
                        val now = System.nanoTime().nanoseconds
                        val duration = ((now - start) / delay).takeIf { it < 1 } ?: break
                        _state.value = state.value.copy(progress = duration.toFloat())
                        delay(64.milliseconds)
                    }
                }
                if (!state.value.isPaused) {
                    _state.value = state.value.copy(number = next.await(), progress = 0f)
                }
            }
        }
    }

    fun pause(value: Boolean) {
        _state.value = state.value.copy(isPaused = value)
    }

    fun reset() {
        injection.launch {
            _state.value = empty
        }
    }

    companion object {
        private val random = Random()
        private val empty = State(
            isPaused = true,
            number = null,
            progress = 0f,
        )

        private fun nextNumber(
            random: Random,
            range: Range,
            actual: Int?
        ): Int {
            while (true) {
                val result = nextNumber(random, range)
                if (result != actual) return result
            }
        }

        private fun nextNumber(
            random: Random,
            range: Range,
        ): Int {
            return random.nextInt(range.endInclusive - range.start + 1) + range.start
        }
    }
}
