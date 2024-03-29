package org.kepocnhh.mnemonics.foundation.provider.coroutine

import kotlin.coroutines.CoroutineContext

internal data class Contexts(
    val main: CoroutineContext,
    val io: CoroutineContext,
)
