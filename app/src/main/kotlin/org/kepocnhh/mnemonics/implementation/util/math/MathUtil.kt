package org.kepocnhh.mnemonics.implementation.util.math

import kotlin.math.pow

/**
 * @return sum of a*r^n + a*r^(n - 1) + ... + a*r^2 + a*r
 */
internal fun getSumOfSeries(
    a: Int = 1,
    r: Double = 10.0,
    n: Int
): Double {
    require(n >= 0) { "n($n) is negative!" }
    return r * a * (1.0 - r.pow(n)) / (1.0 - r)
}
