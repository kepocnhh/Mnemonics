package org.kepocnhh.mnemonics.implementation.entity

import kotlin.time.Duration

internal class Environment private constructor(
    val delay: Duration,
    val range: Range,
    val length: Int
) {
    companion object {
        internal fun new(
            delay: Duration,
            range: Range,
            length: Int,
        ): Environment {
            require(delay > Duration.ZERO)
            require(length in 1..9)
            return Environment(
                delay = delay,
                range = range,
                length = length,
            )
        }
    }

    fun copy(range: Range): Environment {
        return Environment(
            delay = delay,
            length = length,
            range = range
        )
    }

    fun copy(delay: Duration): Environment {
        return Environment(
            delay = delay,
            length = length,
            range = range
        )
    }
}
