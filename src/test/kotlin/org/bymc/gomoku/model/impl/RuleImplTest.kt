package org.bymc.gomoku.model.impl

import org.bymc.gomoku.model.common.param.*
import org.bymc.gomoku.model.common.util.TestBoardViewModel
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class RuleImplTest {

    @Test
    fun checkDropLegality() {

        val boardView = TestBoardViewModel(
            Size2D(15, 15),
            """
                . . . . . . . . . . . . . . .
                . . . . . x . . . . . . . . .
                . . x x . . . . . x x . . . .
                . . . x . . . . . . x x . . .
                . . . . . . . . . x . x . . .
                . . x x . x . . . . . x . . .
                . . . . x . . . . . . x . . .
                . . . . x . . . . . . . . . .
                . . . . x . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . x x x . x x . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
            """.trimIndent()
        )
        val restrict = RuleImpl(ttnhEnabled = true, ffnhEnabled = true, olnhEnables = true)
        val loose = RuleImpl(ttnhEnabled = false, ffnhEnabled = false, olnhEnables = false)
        assertEquals(DropLegality.LEGAL, restrict.checkDropLegality(boardView, Drop(Location2D(11, 2), Stone.BLACK)))
        assertEquals(DropLegality.LEGAL, restrict.checkDropLegality(boardView, Drop(Location2D(13, 2), Stone.BLACK)))
        assertEquals(DropLegality.LEGAL, loose.checkDropLegality(boardView, Drop(Location2D(11, 2), Stone.BLACK)))
        assertEquals(DropLegality.LEGAL, loose.checkDropLegality(boardView, Drop(Location2D(13, 2), Stone.BLACK)))
        assertEquals(
            DropLegality.OCCUPIED_BY_SAME_STONE,
            restrict.checkDropLegality(boardView, Drop(Location2D(2, 2), Stone.BLACK))
        )
        assertEquals(
            DropLegality.OCCUPIED_BY_DIFFERENT_STONE,
            loose.checkDropLegality(boardView, Drop(Location2D(2, 2), Stone.WHITE))
        )
        assertEquals(
            DropLegality.FORBIDDEN_BY_TTNH_RULE,
            restrict.checkDropLegality(boardView, Drop(Location2D(4, 2), Stone.BLACK))
        )
        assertEquals(
            DropLegality.LEGAL,
            restrict.checkDropLegality(boardView, Drop(Location2D(4, 2), Stone.WHITE))
        )
        assertEquals(
            DropLegality.LEGAL,
            loose.checkDropLegality(boardView, Drop(Location2D(4, 2), Stone.BLACK))
        )
        assertEquals(
            DropLegality.FORBIDDEN_BY_FFNH_RULE,
            restrict.checkDropLegality(boardView, Drop(Location2D(4, 5), Stone.BLACK))
        )
        assertEquals(
            DropLegality.LEGAL,
            restrict.checkDropLegality(boardView, Drop(Location2D(4, 5), Stone.WHITE))
        )
        assertEquals(
            DropLegality.LEGAL,
            loose.checkDropLegality(boardView, Drop(Location2D(4, 5), Stone.BLACK))
        )
        assertEquals(
            DropLegality.FORBIDDEN_BY_OLNH_RULE,
            restrict.checkDropLegality(boardView, Drop(Location2D(4, 10), Stone.BLACK))
        )
        assertEquals(
            DropLegality.LEGAL,
            restrict.checkDropLegality(boardView, Drop(Location2D(4, 10), Stone.WHITE))
        )
        assertEquals(
            DropLegality.LEGAL,
            loose.checkDropLegality(boardView, Drop(Location2D(4, 10), Stone.BLACK))
        )
        assertEquals(
            DropLegality.OUT_OF_BOARD,
            loose.checkDropLegality(boardView, Drop(Location2D(15, 14), Stone.BLACK))
        )
    }

    @Test
    fun judgeGameState_case_1() {

        val boardView = TestBoardViewModel(
            Size2D(15, 15),
            """
                . . . . . . . . . . . . . . .
                . x x . x . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . x . o . . . . . . o . .
                . . . x . o . . . . . o . . .
                . . . x . o . . . . o . . . .
                . . . x . o . . . o . . . . .
                . . . x . o . . o . . . . . .
                . . . . . o . . . . . . . . .
                . . . . . o . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
            """.trimIndent()
        )
        val rule = RuleImpl(ttnhEnabled = true, ffnhEnabled = true, olnhEnables = true)
        assertEquals(GameState.PLAYING, rule.judgeGameState(boardView, Location2D(2, 1)))
        assertEquals(GameState.BLACK_WON, rule.judgeGameState(boardView, Location2D(3, 4)))
        assertEquals(GameState.WHITE_WON, rule.judgeGameState(boardView, Location2D(5, 4)))
        assertEquals(GameState.WHITE_WON, rule.judgeGameState(boardView, Location2D(8, 7)))
    }

    @Test
    fun judgeGameState_case_2() {

        val boardView = TestBoardViewModel(
            Size2D(15, 15),
            """
                . . . . . . . . . . . . . . .
                . x x . x . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . x . o . . . . . . o . .
                . . . x . o . . . . . o . . .
                . . . o . o . . . . o . . . .
                . . . x . o . . . . . . . . .
                . . . x . o . . o . . . . . .
                . . . . . o . . . . . . . . .
                . . . . . o . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
            """.trimIndent()
        )
        val rule = RuleImpl(ttnhEnabled = true, ffnhEnabled = true, olnhEnables = true)
        assertEquals(GameState.WHITE_WON, rule.judgeGameState(boardView))
    }

    @Test
    fun judgeGameState_case_3() {

        val boardView = TestBoardViewModel(
            Size2D(15, 15),
            """
                . . . . . . . . . . . . . . .
                . x x . x . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . x . o . . . . . . o . .
                . . . x . o . . . . . o . . .
                . . . o . o . . . . o . . . .
                . . . x . x . . . . . . . . .
                . . . x . o . . o . . . . . .
                . . . . . o . . . . . . . . .
                . . . . . o . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
            """.trimIndent()
        )
        val rule = RuleImpl(ttnhEnabled = true, ffnhEnabled = true, olnhEnables = true)
        assertEquals(GameState.PLAYING, rule.judgeGameState(boardView))
    }

    @Test
    fun judgeGameState_case_4() {

        val boardView = TestBoardViewModel(
            Size2D(15, 15),
            """
                x x x o o o x x x o o o x x x
                x x x o o o x x x o o o x x x
                x x x o o o x x x o o o x x x
                o o x x x o o o x x x o o o x
                o o o o o x x x o o o x x x o
                x x x x o o o x x x o o o o x
                o o o x x x x o o o x x x x o
                x x x o o o x x x o o o x x x
                x x x o o o x x x o o o x x x
                x x x o o o x x x o o o x x x
                o o x x x o o o x x x o o o x
                o o o o o x x x o o o x x x o
                x x x x o o o x x x o o o o x
                o o o x x x x o o o x x x x o
                x x x o o o x x o x o o o x x
            """.trimIndent()
        )
        val rule = RuleImpl(ttnhEnabled = true, ffnhEnabled = true, olnhEnables = true)
        assertEquals(GameState.DRAW, rule.judgeGameState(boardView))
    }
}