package org.kepocnhh.mnemonics.implementation.module.theme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.kepocnhh.mnemonics.foundation.entity.ColorsType
import org.kepocnhh.mnemonics.foundation.entity.ThemeState
import org.kepocnhh.mnemonics.foundation.provider.Injection
import org.kepocnhh.mnemonics.implementation.util.androidx.lifecycle.AbstractViewModel

internal class ThemeViewModel(private val injection: Injection) : AbstractViewModel() {
    private val _state = MutableStateFlow<ThemeState?>(null)
    val state = _state.asStateFlow()

    fun requestThemeState() {
        injection.launch {
            _state.value = ThemeState(colorsType = ColorsType.DARK)
            // todo
        }
    }

    fun setColorsType(type: ColorsType) {
        injection.launch {
            _state.value = ThemeState(colorsType = type)
            // todo
        }
    }
}
