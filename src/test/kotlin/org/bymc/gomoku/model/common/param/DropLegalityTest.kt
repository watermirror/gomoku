package org.bymc.gomoku.model.common.param

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class DropLegalityTest {

    @Test
    fun testNameAndValueOf() {

        assertEquals(DropLegality.LEGAL, DropLegality.valueOf(DropLegality.LEGAL.name))
        assertEquals(
            DropLegality.OCCUPIED_BY_SAME_STONE, DropLegality.valueOf(DropLegality.OCCUPIED_BY_SAME_STONE.name)
        )
        assertEquals(
            DropLegality.OCCUPIED_BY_DIFFERENT_STONE,
            DropLegality.valueOf(DropLegality.OCCUPIED_BY_DIFFERENT_STONE.name)
        )
        assertEquals(
            DropLegality.FORBIDDEN_BY_TTNH_RULE, DropLegality.valueOf(DropLegality.FORBIDDEN_BY_TTNH_RULE.name)
        )
        assertEquals(
            DropLegality.FORBIDDEN_BY_FFNH_RULE, DropLegality.valueOf(DropLegality.FORBIDDEN_BY_FFNH_RULE.name)
        )
        assertEquals(
            DropLegality.FORBIDDEN_BY_OLNH_RULE, DropLegality.valueOf(DropLegality.FORBIDDEN_BY_OLNH_RULE.name)
        )
        assertEquals(DropLegality.OUT_OF_BOARD, DropLegality.valueOf(DropLegality.OUT_OF_BOARD.name))
    }
}