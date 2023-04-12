package org.kepocnhh.mnemonics.presentation.util.androidx.compose

import androidx.compose.runtime.Immutable
import org.kepocnhh.mnemonics.implementation.entity.Range
import kotlin.time.Duration

@Immutable
internal interface Strings {
    val auto: String
    val back: String
    val colors: String
    val dark: String
    val en: String
    val language: String
    val light: String
    val pause: String
    val play: String
    val ru: String
    val settings: String
    val range: String
    val ok: String
    val rangeFrom: String
    val rangeTo: String
    val delay: String
    val seconds: String

    fun range(range: Range, length: Int): String
    fun delay(delay: Duration): String
}
