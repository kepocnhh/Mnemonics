package org.kepocnhh.mnemonics.implementation.entity

import org.junit.Assert.assertEquals
import org.junit.Test

internal class WrappedNumberTest {
    @Test
    fun toStringRank1Test() {
        val rank = 1
        mapOf(
            -10 to "0",
            -9  to "1",
            -2  to "8",
            -1  to "9",
        ).forEach { (raw, expected) ->
            val number = raw.wrapped(rank = rank)
            assertEquals(expected, number.toString())
        }
    }

    @Test
    fun toStringRank2Test() {
        val rank = 2
        mapOf(
            -110 to " 0",
            -102 to " 8",
            -92  to "08",
            -84  to "16",
            -68  to "32",
            -58  to "42",
            -1   to "99",
        ).forEach { (raw, expected) ->
            val number = raw.wrapped(rank = rank)
            assertEquals(expected, number.toString())
        }
    }

    @Test
    fun toStringRank3Test() {
        val rank = 3
        mapOf(
//            0    to "  0",
//            10   to " 00",
//            100  to "000",
//            1099 to "999",
            -1110 to "  0",
            -1109 to "  1",
            -1101 to "  9",
            -1100 to " 00",
            -1099 to " 01",
            -1058 to " 42",
            -1001 to " 99",
            -1000 to "000",
            -999  to "001",
            -942  to "058",
            -901  to "099",
            -900  to "100",
            -872  to "128",
            -744  to "256",
            -488  to "512",
            -99   to "901",
            -1    to "999",
        ).forEach { (raw, expected) ->
            val number = raw.wrapped(rank = rank)
            assertEquals(expected, number.toString())
        }
    }

    @Test
    fun toStringRank9Test() {
        val rank = 9
        mapOf(
            -1_111_111_110 to "        0",
            -1             to "999999999",
        ).forEach { (raw, expected) ->
            val number = raw.wrapped(rank = rank)
            assertEquals(expected, number.toString())
        }
    }
}
