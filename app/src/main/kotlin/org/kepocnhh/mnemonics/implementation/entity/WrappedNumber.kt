package org.kepocnhh.mnemonics.implementation.entity

import org.kepocnhh.mnemonics.foundation.entity.WrappedNumber
import kotlin.math.pow

private class WrappedNumberImpl(
    override val raw: Int,
    override val rank: Int
) : WrappedNumber {
    override fun toString(): String {
        for (i in 1 .. rank) {
            if (raw < -getSumOfGeometricSeries(n = rank, m = i)) {
                val formatted = raw + getSumOfGeometricSeries(n = rank, m = i - 1)
                return "%${rank}s".format("%0${i}d".format(formatted.toInt()))
            }
        }
        error("Impossible!")
    }
}

/**
 * a*r.pow(n) + a*r.pow(n - 1) + ... + a*r.pow(m - 1) + a*r.pow(m)
 * https://en.wikipedia.org/wiki/Geometric_series
 */
private fun getSumOfGeometricSeries(
    a: Int = 1,
    r: Double = 10.0,
    m: Int = 0,
    n: Int,
): Double {
    require(m >= 0)
    require(n >= m)
    return r * a * (r.pow(m) - r.pow(n)) / (1.0 - r)
}

private fun getRange(rank: Int): IntRange {
    val start = -getSumOfGeometricSeries(n = rank)
    return start.toInt() until 0
}

internal fun Int.wrapped(rank: Int): WrappedNumber {
    require(rank in 1..9)
    require(this in getRange(rank = rank))
    return WrappedNumberImpl(
        raw = this,
        rank = rank
    )
}
