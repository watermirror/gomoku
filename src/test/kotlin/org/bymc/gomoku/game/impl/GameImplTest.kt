package org.bymc.gomoku.game.impl

import com.nhaarman.mockitokotlin2.*
import org.bymc.gomoku.model.abstraction.Board
import org.bymc.gomoku.model.abstraction.DropRecord
import org.bymc.gomoku.model.abstraction.History
import org.bymc.gomoku.model.abstraction.Rule
import org.bymc.gomoku.model.common.param.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GameImplTest {

    @Test
    fun getGameSituation() {

        val board: Board = mock()
        val history: History = mock()
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.PLAYING)
        val game = GameImpl(board, history, rule)
        val sample = game.getGameSituation()

        assertEquals(GameState.PLAYING, sample.state)
        assertEquals(0, sample.dropCount)
        assertEquals(null, sample.endTime)
        assertEquals(null, sample.lastDropTime)
        assertEquals(false, sample.retractionAvailable)
        assertEquals(Stone.BLACK, sample.roundActor)
    }

    @Test
    fun getBoardView() {

        val board: Board = mock()
        val history: History = mock()
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.PLAYING)
        val game = GameImpl(board, history, rule)
        val sample = game.getBoardView()

        assertEquals(board, sample)
    }

    @Test
    fun dropStoneAt_case_1() {

        val board: Board = mock()
        val history: History = mock()
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.PLAYING)
        whenever(rule.judgeGameState(any(), any())).thenReturn(GameState.PLAYING)
        whenever(rule.checkDropLegality(any(), any())).thenReturn(DropLegality.LEGAL)
        val game = GameImpl(board, history, rule)
        val record: DropRecord = mock()
        whenever(record.getDrop()).thenReturn(Drop(Location2D(7, 7), Stone.BLACK))
        whenever(history.getRecordCount()).thenReturn(1)
        whenever(history.getLastRecord()).thenReturn(record)

        assertEquals(DropLegality.LEGAL, game.dropStoneAt(Location2D(7, 7)))
        assertEquals(Stone.WHITE, game.getGameSituation().roundActor)
        verify(board).dropStone(eq(Location2D(7, 7)), eq(Stone.BLACK))
        verify(history).appendRecord(eq(Drop(Location2D(7, 7), Stone.BLACK)), any())
    }

    @Test
    fun dropStoneAt_case_2() {

        val board: Board = mock()
        val history: History = mock()
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.PLAYING)
        whenever(rule.judgeGameState(any(), any())).thenReturn(GameState.PLAYING)
        whenever(rule.checkDropLegality(any(), any())).thenReturn(DropLegality.LEGAL)
        val game = GameImpl(board, history, rule)

        assertEquals(DropLegality.LEGAL, game.dropStoneAt(Location2D(7, 7)))
        assertEquals(Stone.WHITE, game.getGameSituation().roundActor)
        verify(board).dropStone(eq(Location2D(7, 7)), eq(Stone.BLACK))
        verify(history).appendRecord(eq(Drop(Location2D(7, 7), Stone.BLACK)), any())

        assertEquals(DropLegality.LEGAL, game.dropStoneAt(Location2D(8, 8)))
        assertEquals(Stone.BLACK, game.getGameSituation().roundActor)
        verify(board).dropStone(eq(Location2D(8, 8)), eq(Stone.WHITE))
        verify(history).appendRecord(eq(Drop(Location2D(8, 8), Stone.WHITE)), any())
    }

    @Test
    fun dropStoneAt_case_3() {

        val board: Board = mock()
        val history: History = mock()
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.PLAYING)
        whenever(rule.judgeGameState(any(), any())).thenReturn(GameState.PLAYING)
        whenever(rule.checkDropLegality(any(), any())).thenReturn(DropLegality.OCCUPIED_BY_SAME_STONE)
        val game = GameImpl(board, history, rule)

        assertEquals(DropLegality.OCCUPIED_BY_SAME_STONE, game.dropStoneAt(Location2D(7, 7)))
        assertEquals(Stone.BLACK, game.getGameSituation().roundActor)
        verify(board, times(0)).dropStone(any(), any())
        verify(history, times(0)).appendRecord(any(), any())
    }

    @Test
    fun dropStoneAt_case_4() {

        val board: Board = mock()
        val history: History = mock()
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.BLACK_WON)
        whenever(rule.judgeGameState(any(), any())).thenReturn(GameState.BLACK_WON)
        whenever(rule.checkDropLegality(any(), any())).thenReturn(DropLegality.LEGAL)
        val game = GameImpl(board, history, rule)

        assertThrows(RuntimeException::class.java) {
            game.dropStoneAt(Location2D(7, 7))
        }
    }

    @Test
    fun retract_case_1() {

        val board: Board = mock()
        val recordA: DropRecord = mock()
        whenever(recordA.getDrop()).thenReturn(Drop(Location2D(7, 7), Stone.BLACK))
        val recordB: DropRecord = mock()
        whenever(recordB.getDrop()).thenReturn(Drop(Location2D(8, 8), Stone.WHITE))
        val history: History = mock()
        whenever(history.retract(Stone.BLACK)).thenReturn(listOf(recordB, recordA))
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.PLAYING)
        whenever(rule.judgeGameState(any(), any())).thenReturn(GameState.PLAYING)
        val game = GameImpl(board, history, rule)
        game.retract()

        verify(board).cleanCell(eq(Location2D(7, 7)))
        verify(board).cleanCell(eq(Location2D(8, 8)))
        assertEquals(Stone.BLACK, game.getGameSituation().roundActor)
    }

    @Test
    fun retract_case_2() {

        val board: Board = mock()
        val record: DropRecord = mock()
        whenever(record.getDrop()).thenReturn(Drop(Location2D(7, 7), Stone.BLACK))
        val history: History = mock()
        whenever(history.retract(Stone.BLACK)).thenReturn(listOf(record))
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.PLAYING)
        whenever(rule.judgeGameState(any(), any())).thenReturn(GameState.PLAYING)
        val game = GameImpl(board, history, rule)
        game.retract()

        verify(board).cleanCell(eq(Location2D(7, 7)))
        assertEquals(Stone.BLACK, game.getGameSituation().roundActor)
    }

    @Test
    fun retract_case_3() {

        val board: Board = mock()
        val record: DropRecord = mock()
        whenever(record.getDrop()).thenReturn(Drop(Location2D(7, 7), Stone.BLACK))
        val history: History = mock()
        whenever(history.retract(Stone.BLACK)).thenReturn(listOf(record))
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.PLAYING)
        whenever(rule.judgeGameState(any(), any())).thenReturn(GameState.PLAYING)
        val game = GameImpl(board, history, rule)
        game.retract()

        verify(board).cleanCell(eq(Location2D(7, 7)))
        assertEquals(Stone.BLACK, game.getGameSituation().roundActor)
    }

    @Test
    fun retract_case_4() {

        val board: Board = mock()
        val record: DropRecord = mock()
        whenever(record.getDrop()).thenReturn(Drop(Location2D(7, 7), Stone.BLACK))
        val history: History = mock()
        whenever(history.retract(Stone.BLACK)).thenReturn(listOf(record))
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.DRAW)
        whenever(rule.judgeGameState(any(), any())).thenReturn(GameState.DRAW)
        val game = GameImpl(board, history, rule)

        assertThrows(RuntimeException::class.java) {
            game.retract()
        }
    }

    @Test
    fun retract_case_5() {

        val board: Board = mock()
        val history: History = mock()
        whenever(history.retract(Stone.BLACK)).thenReturn(emptyList())
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.PLAYING)
        whenever(rule.judgeGameState(any(), any())).thenReturn(GameState.PLAYING)
        val game = GameImpl(board, history, rule)

        assertThrows(RuntimeException::class.java) {
            game.retract()
        }
    }
}