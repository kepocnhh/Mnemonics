package org.kepocnhh.mnemonics.foundation.provider

import org.kepocnhh.mnemonics.foundation.provider.coroutine.Contexts
import org.kepocnhh.mnemonics.foundation.provider.data.local.LocalDataProvider

internal data class Injection(
    val contexts: Contexts,
    val local: LocalDataProvider,
)
