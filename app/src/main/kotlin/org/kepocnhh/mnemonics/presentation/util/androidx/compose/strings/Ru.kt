package org.kepocnhh.mnemonics.presentation.util.androidx.compose.strings

import org.kepocnhh.mnemonics.implementation.entity.Range
import org.kepocnhh.mnemonics.implementation.entity.formatted
import org.kepocnhh.mnemonics.presentation.util.androidx.compose.Strings
import kotlin.time.Duration

internal object Ru : Strings {
    override val auto = "автоматически"
    override val back = "назад"
    override val colors = "цвета"
    override val dark = "тёмные"
    override val en = "английский"
    override val language = "язык"
    override val light = "светлые"
    override val pause = "пауза"
    override val play = "продолжить"
    override val ru = "русский"
    override val settings = "настройки"
    override val range = "интервал"
    override val ok = "ок"
    override val rangeFrom = "от:"
    override val rangeTo = "до:"
    override val delay = "задержка"
    override val seconds = "секунд"

    override fun range(range: Range, length: Int): String {
        return "от ${range.start.formatted(length = length).trim()} до ${range.endInclusive.formatted(length = length).trim()}"
    }

    override fun delay(delay: Duration): String {
        return "${delay.inWholeSeconds} секунд"
    }
}
