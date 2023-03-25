package org.kepocnhh.mnemonics.foundation.entity

internal enum class ColorsType {
    DARK,
    LIGHT,
    AUTO
}

internal data class ThemeState(
    val colorsType: ColorsType
)
