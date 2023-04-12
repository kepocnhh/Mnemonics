package org.kepocnhh.mnemonics.implementation.entity

import org.kepocnhh.mnemonics.implementation.util.math.getSumOfSeries
import java.util.Objects

internal class Range private constructor(
    val start: Int,
    val endInclusive: Int,
) {
    companion object {
        fun new(start: Int, endInclusive: Int, length: Int) : Range {
            require(start >= 0)
            require(endInclusive > start)
            require(endInclusive <= getSumOfSeries(n = length))
            return Range(
                start = start,
                endInclusive = endInclusive,
            )
        }
    }

    fun copy(start: Int): Range {
        require(start >= 0)
        require(endInclusive > start)
        return Range(
            start = start,
            endInclusive = this.endInclusive,
        )
    }

    fun copy(endInclusive: Int, length: Int): Range {
        require(endInclusive > start)
        require(endInclusive <= getSumOfSeries(n = length))
        return Range(
            start = this.start,
            endInclusive = endInclusive,
        )
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Range -> start == other.start && endInclusive == other.endInclusive
            else -> false
        }
    }

    override fun hashCode(): Int {
        return Objects.hash(start, endInclusive)
    }
}
