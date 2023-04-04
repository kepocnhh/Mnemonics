package org.kepocnhh.mnemonics.implementation.entity

import org.kepocnhh.mnemonics.foundation.entity.WrappedNumber
import kotlin.math.pow

private class WrappedNumberImpl(
    override val raw: Int,
    override val rank: Int
) : WrappedNumber {
    override fun toString(): String {
        val sum = getSumOfGeometricSeries(n = rank)
        for (i in 1 .. rank) {
            if (raw < getSumOfGeometricSeries(n = i) - sum) {
                val formatted = raw + sum - getSumOfGeometricSeries(n = i - 1)
                return "%${rank}s".format("%0${i}d".format(formatted.toInt()))
            }
        }
        error("Impossible!")
    }
}

/**
 * a*r.pow(1) + a*r.pow(2) + a*r.pow(3) + a*r.pow(4) + ... + a*r.pow(n)
 * 1*10       + 1*100      + 1*1_000    + 1*10_000   + ... + 1*10.pow(n)
 * https://en.wikipedia.org/wiki/Geometric_series
 */
private fun getSumOfGeometricSeries(a: Int = 1, r: Double = 10.0, n: Int): Double {
    return r * a * (1.0 - r.pow(n)) / (1.0 - r)
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
