package org.kepocnhh.mnemonics.presentation.util.androidx.compose.strings

import org.kepocnhh.mnemonics.implementation.entity.Range
import org.kepocnhh.mnemonics.implementation.entity.formatted
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Strings
import kotlin.time.Duration

internal object En : Strings {
    override val auto = "auto"
    override val back = "back"
    override val colors = "colors"
    override val dark = "dark"
    override val en = "english"
    override val language = "language"
    override val light = "light"
    override val pause = "pause"
    override val play = "play"
    override val ru = "russian"
    override val settings = "settings"
    override val range = "range"
    override val ok = "ok"
    override val rangeFrom = "from:"
    override val rangeTo = "to:"
    override val delay = "delay"
    override val seconds = "seconds"

    override fun range(range: Range, length: Int): String {
        return "from ${range.start.formatted(length = length).trim()} to ${range.endInclusive.formatted(length = length).trim()}"
    }

    override fun delay(delay: Duration): String {
        return "${delay.inWholeSeconds} seconds"
    }
}
