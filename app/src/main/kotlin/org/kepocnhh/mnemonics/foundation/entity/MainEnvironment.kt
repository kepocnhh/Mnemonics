package org.kepocnhh.mnemonics.foundation.entity

import kotlin.time.Duration

internal data class MainEnvironment(
    val time: Duration,
    val range: Range,
    val length: Int,
) {
    class Range(start: Int, endInclusive: Int) {
        val start: Int
        val endInclusive: Int

        init {
            require(start >= 0)
            require(endInclusive > start)
            this.start = start
            this.endInclusive = endInclusive
        }

        override fun toString(): String {
            return "{$start..$endInclusive}"
        }
    }
}
