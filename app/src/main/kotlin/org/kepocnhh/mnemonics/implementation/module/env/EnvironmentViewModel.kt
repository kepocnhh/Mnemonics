package org.kepocnhh.mnemonics.implementation.module.env

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import org.kepocnhh.mnemonics.foundation.provider.Injection
import org.kepocnhh.mnemonics.implementation.entity.Environment
import org.kepocnhh.mnemonics.implementation.entity.Range
import org.kepocnhh.mnemonics.implementation.util.androidx.lifecycle.AbstractViewModel
import kotlin.time.Duration

internal class EnvironmentViewModel(private val injection: Injection) : AbstractViewModel() {
    private val _state = MutableSharedFlow<Environment?>()
    val state = _state.asSharedFlow()

    fun requestState() {
        injection.launch {
            val value = withContext(injection.contexts.io) {
                injection.local.env
            }
            _state.emit(value)
        }
    }

    fun setRange(range: Range) {
        injection.launch {
            val value = withContext(injection.contexts.io) {
                injection.local.env = injection.local.env.copy(range = range)
                injection.local.env
            }
            _state.emit(value)
        }
    }

    fun setDelay(delay: Duration) {
        injection.launch {
            val value = withContext(injection.contexts.io) {
                injection.local.env = injection.local.env.copy(delay = delay)
                injection.local.env
            }
            _state.emit(value)
        }
    }
}
