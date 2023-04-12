package org.kepocnhh.mnemonics.implementation.entity

import kotlin.time.Duration

internal class Environment private constructor(
    val time: Duration,
    val range: Range,
    val length: Int
) {
    companion object {
        internal fun new(
            time: Duration,
            range: Range,
            length: Int,
        ): Environment {
            require(time > Duration.ZERO)
            require(length in 1..9)
            return Environment(
                time = time,
                range = range,
                length = length,
            )
        }
    }

    fun copy(range: Range): Environment {
        return Environment(
            time = time,
            length = length,
            range = range
        )
    }
}
