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
     * raw = 0   , fmt = "  0", n=3, m=0, Sn=1110
     * raw = 10  , fmt = " 00", n=3, m=1, Sn=1110
     * raw = 110 , fmt = "000", n=3, m=2, Sn=1110
     * raw = 1109, fmt = "999", n=3, m=2, Sn=1110
     * raw = -1110, fmt = "  0", n=3, m=0, Sn=1110
     * raw = -1109, fmt = "  1", n=3, m=0, Sn=1110
     * raw = -1101, fmt = "  9", n=3, m=0, Sn=1110
     * raw = -1100, fmt = " 00", n=3, m=1, Sn=1110
     * raw = -1099, fmt = " 01", n=3, m=1, Sn=1110
     * raw = -1058, fmt = " 42", n=3, m=1, Sn=1110
     * raw = -1001, fmt = " 99", n=3, m=1, Sn=1110
     * raw = -1000, fmt = "000", n=3, m=2, Sn=1110
     * raw = -999 , fmt = "001", n=3, m=2, Sn=1110
     * raw = -942 , fmt = "058", n=3, m=2, Sn=1110
     * raw = -901 , fmt = "099", n=3, m=2, Sn=1110
     * raw = -900 , fmt = "100", n=3, m=2, Sn=1110
     * raw = -872 , fmt = "128", n=3, m=2, Sn=1110
     * raw = -744 , fmt = "256", n=3, m=2, Sn=1110
     * raw = -488 , fmt = "512", n=3, m=2, Sn=1110
     * raw = -99  , fmt = "901", n=3, m=2, Sn=1110
     * raw = -1   , fmt = "999", n=3, m=2, Sn=1110
     */
    override fun toString(): String {
        val i = getPowerOfGeometricSeries(sum = getSumOfGeometricSeries(n = rank) + raw).toInt()
        val formatted = getSumOfGeometricSeries(n = rank, m = i) + raw
//        val i = getPowerOfGeometricSeries(sum = raw.toDouble()).toInt()
//        val formatted = raw - getSumOfGeometricSeries(n = rank, m = i)
        return "%${rank}s".format("%0${i + 1}d".format(formatted.toInt()))
    }
}

/**
 * @param a is coefficient
 * @param r is the common ratio
 * @return sum of a*r.pow(n) + a*r.pow(n - 1) + ... + a*r.pow(m - 1) + a*r.pow(m)
 */
private fun getSumOfGeometricSeries(
    a: Int = 1,
    r: Double = 10.0,
    m: Int = 0,
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
