package org.kepocnhh.mnemonics.foundation.entity

internal enum class ColorsType {
    DARK,
    LIGHT,
    AUTO
}

internal enum class Language {
    RU,
    EN,
    AUTO,
}

internal data class ThemeState(
    val colorsType: ColorsType,
    val language: Language,
)
