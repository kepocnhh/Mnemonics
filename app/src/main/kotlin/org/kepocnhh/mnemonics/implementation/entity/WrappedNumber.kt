package org.kepocnhh.mnemonics.implementation.entity

import org.kepocnhh.mnemonics.foundation.entity.WrappedNumber
import kotlin.math.absoluteValue
import kotlin.math.pow

private class WrappedNumberImpl(
    override val raw: Int,
    override val rank: Int
) : WrappedNumber {
    /**
     * https://en.wikipedia.org/wiki/Geometric_series
     * length: 3, number: 0   , fmt: "  0", n=0, Sn=0
     * length: 3, number: 10  , fmt: " 00", n=1, Sn=10
     * length: 3, number: 110 , fmt: "000", n=2, Sn=110
     * length: 3, number: 1109, fmt: "999", n=2, Sn=110
     */
    override fun toString(): String {
        val n = getPowerOfGeometricSeries(sum = raw.toDouble()).toInt()
        val formatted = raw - getSumOfGeometricSeries(n = n)
        return "%${rank}s".format("%0${n + 1}d".format(formatted.toInt()))
    }
}

/**
 * @param a is coefficient
 * @param r is the common ratio
 * @return sum of a*r.pow(n) + a*r.pow(n - 1) + ... + a*r.pow(2) + a*r
 */
private fun getSumOfGeometricSeries(
    a: Int = 1,
    r: Double = 10.0,
    n: Int
): Double {
    require(n >= 0) { "n($n) is negative!" }
    return r * a * (1.0 - r.pow(n)) / (1.0 - r)
}

/**
 * @param a is coefficient
 * @param r is the common ratio
 * @return sum of a*r.pow(n) + a*r.pow(n - 1) + ... + a*r.pow(m + 1) + a*r.pow(m)
 */
private fun getSumOfGeometricSeries(
    a: Int = 1,
    r: Double = 10.0,
    m: Int,
    n: Int,
): Double {
    require(m >= 0) { "m($m) is negative!" }
    require(n >= m) { "n($n) < m($m)!" }
    return r * a * (r.pow(m) - r.pow(n)) / (1.0 - r)
}

private fun getSumOfGeometricSeries10(
    a: Int = 1,
    m: Int = 0,
    n: Int,
): Double {
    require(m >= 0) { "m($m) is negative!" }
    require(n >= m) { "n($n) < m($m)!" }
    return 10.0.pow(m + 1) * a * (1 - 10.0.pow(n - m)) / -9.0
}

/**
 * @param a is coefficient
 * @param r is the common ratio
 * @param sum - value of a*r.pow(n) + a*r.pow(n - 1) + ... + a*r.pow(1)
 * @return n
 */
private fun getPowerOfGeometricSeries(
    a: Int = 1,
    r: Double = 10.0,
    sum: Double
): Double {
    return kotlin.math.log(1 - sum * (1.0 - r) / (a * r), r)
}

private fun getPowerOfGeometricSeries10(
    a: Int = 1,
    sum: Double
): Double {
    return kotlin.math.log10(1 + (sum * 9.0) / (a * 10.0))
}

private fun getRange(length: Int): IntRange {
    val end = getSumOfGeometricSeries(n = length)
    return 0 until end.toInt()
}

internal fun Int.wrapped(rank: Int): WrappedNumber {
    require(rank in 1..9)
    require(this in getRange(length = rank))
    return WrappedNumberImpl(
        raw = this,
        rank = rank
    )
}

internal fun Int.formatted(length: Int): String {
    require(length in 1..9)
    require(this in getRange(length = length))
    val n = getPowerOfGeometricSeries(sum = toDouble()).toInt()
    val formatted = this - getSumOfGeometricSeries(n = n)
    return "%${length}s".format("%0${n + 1}d".format(formatted.toInt()))
}
