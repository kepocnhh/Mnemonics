package org.kepocnhh.mnemonics.implementation.entity

import org.junit.Assert.assertEquals
import org.junit.Test

internal class WrappedNumberTest {
    @Test
    fun toStringRank1Test() {
        val rank = 1
        mapOf(
            0 to "0",
            1 to "1",
            8 to "8",
            9 to "9",
        ).forEach { (raw, expected) ->
            val number = raw.wrapped(rank = rank)
            assertEquals(expected, number.toString())
        }
    }

    @Test
    fun toStringRank2Test() {
        val rank = 2
        mapOf(
            0   to " 0",
            1   to " 1",
            9   to " 9",
            10  to "00",
            18  to "08",
            26  to "16",
            42  to "32",
            74  to "64",
            109 to "99",
        ).forEach { (raw, expected) ->
            val number = raw.wrapped(rank = rank)
            assertEquals(expected, number.toString())
        }
    }

    @Test
    fun toStringRank3Test() {
        val rank = 3
        mapOf(
            0    to "  0",
            1    to "  1",
            9    to "  9",
            10   to " 00",
            11   to " 01",
            52   to " 42",
            109  to " 99",
            110  to "000",
            111  to "001",
            238  to "128",
            622  to "512",
            1109 to "999",
        ).forEach { (raw, expected) ->
            val number = raw.wrapped(rank = rank)
            assertEquals(expected, number.toString())
        }
    }

    @Test
    fun toStringRank9Test() {
        val rank = 9
        mapOf(
            0             to "        0",
            1             to "        1",
            10            to "       00",
            52            to "       42",
            109           to "       99",
            622           to "      512",
            2134          to "     1024",
            3158          to "     2048",
            5206          to "     4096",
            9302          to "     8192",
            27494         to "    16384",
            1_111_111_109 to "999999999",
        ).forEach { (raw, expected) ->
            val number = raw.wrapped(rank = rank)
            assertEquals(expected, number.toString())
        }
    }
}
