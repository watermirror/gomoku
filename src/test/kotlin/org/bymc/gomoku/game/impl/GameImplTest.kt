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
        val sample = game.getBoardViewModel()

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

    @Test
    fun retractLast_duringPlaying() {

        val board: Board = mock()
        val record: DropRecord = mock()
        whenever(record.getDrop()).thenReturn(Drop(Location2D(7, 7), Stone.BLACK))
        val history: History = mock()
        whenever(history.retractLast()).thenReturn(record)
        whenever(history.getRecordCount()).thenReturn(1)
        whenever(history.getLastRecord()).thenReturn(record)
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.PLAYING)
        val game = GameImpl(board, history, rule)

        game.retractLast()

        verify(board).cleanCell(eq(Location2D(7, 7)))
        assertEquals(Stone.BLACK, game.getGameSituation().roundActor)
        assertEquals(GameState.PLAYING, game.getGameSituation().state)
        assertEquals(1, game.getGameSituation().dropCount)
        assertTrue(game.getGameSituation().retractionAvailable)
    }

    @Test
    fun retractLast_afterBlackWon() {

        val board: Board = mock()
        val record: DropRecord = mock()
        whenever(record.getDrop()).thenReturn(Drop(Location2D(7, 7), Stone.BLACK))
        val history: History = mock()
        whenever(history.retractLast()).thenReturn(record)
        whenever(history.getRecordCount()).thenReturn(4)
        whenever(history.getLastRecord()).thenReturn(record)
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.BLACK_WON).thenReturn(GameState.PLAYING)
        val game = GameImpl(board, history, rule)

        assertEquals(GameState.BLACK_WON, game.getGameSituation().state)
        game.retractLast()

        verify(board).cleanCell(eq(Location2D(7, 7)))
        assertEquals(Stone.BLACK, game.getGameSituation().roundActor)
        assertEquals(GameState.PLAYING, game.getGameSituation().state)
    }

    @Test
    fun retractLast_afterWhiteWon() {

        val board: Board = mock()
        val record: DropRecord = mock()
        whenever(record.getDrop()).thenReturn(Drop(Location2D(8, 8), Stone.WHITE))
        val history: History = mock()
        whenever(history.retractLast()).thenReturn(record)
        whenever(history.getRecordCount()).thenReturn(3)
        whenever(history.getLastRecord()).thenReturn(record)
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.WHITE_WON).thenReturn(GameState.PLAYING)
        val game = GameImpl(board, history, rule)

        assertEquals(GameState.WHITE_WON, game.getGameSituation().state)
        game.retractLast()

        verify(board).cleanCell(eq(Location2D(8, 8)))
        assertEquals(Stone.WHITE, game.getGameSituation().roundActor)
        assertEquals(GameState.PLAYING, game.getGameSituation().state)
    }

    @Test
    fun retractLast_afterDraw() {

        val board: Board = mock()
        val record: DropRecord = mock()
        whenever(record.getDrop()).thenReturn(Drop(Location2D(7, 7), Stone.WHITE))
        val history: History = mock()
        whenever(history.retractLast()).thenReturn(record)
        whenever(history.getRecordCount()).thenReturn(5)
        whenever(history.getLastRecord()).thenReturn(record)
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.DRAW).thenReturn(GameState.PLAYING)
        val game = GameImpl(board, history, rule)

        assertEquals(GameState.DRAW, game.getGameSituation().state)
        game.retractLast()

        verify(board).cleanCell(eq(Location2D(7, 7)))
        assertEquals(Stone.WHITE, game.getGameSituation().roundActor)
        assertEquals(GameState.PLAYING, game.getGameSituation().state)
    }

    @Test
    fun retractLast_withSingleStone() {

        val board: Board = mock()
        val record: DropRecord = mock()
        whenever(record.getDrop()).thenReturn(Drop(Location2D(7, 7), Stone.BLACK))
        val history: History = mock()
        whenever(history.retractLast()).thenReturn(record)
        whenever(history.getRecordCount()).thenReturn(0)
        whenever(history.getLastRecord()).thenReturn(null)
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.PLAYING)
        val game = GameImpl(board, history, rule)

        game.retractLast()

        verify(board).cleanCell(eq(Location2D(7, 7)))
        assertEquals(Stone.BLACK, game.getGameSituation().roundActor)
        assertEquals(0, game.getGameSituation().dropCount)
        assertFalse(game.getGameSituation().retractionAvailable)
    }

    @Test
    fun retractLast_withZeroStones_throws() {

        val board: Board = mock()
        val history: History = mock()
        whenever(history.retractLast()).thenReturn(null)
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.PLAYING)
        val game = GameImpl(board, history, rule)

        assertThrows(RuntimeException::class.java) {
            game.retractLast()
        }
    }

    @Test
    fun retractLast_boardStateReflectsRemoval() {

        val board: Board = mock()
        val record: DropRecord = mock()
        whenever(record.getDrop()).thenReturn(Drop(Location2D(3, 4), Stone.WHITE))
        val history: History = mock()
        whenever(history.retractLast()).thenReturn(record)
        whenever(history.getRecordCount()).thenReturn(2)
        whenever(history.getLastRecord()).thenReturn(record)
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.PLAYING)
        val game = GameImpl(board, history, rule)

        game.retractLast()

        verify(board).cleanCell(eq(Location2D(3, 4)))
        assertEquals(2, game.getGameSituation().dropCount)
        assertTrue(game.getGameSituation().retractionAvailable)
    }

    @Test
    fun retractLast_twice_consecutive_returnsTurnToBlackPlayer() {

        // Challenge-AI with player black: player turn at even dropCount. Retracting two
        // stones (AI white's + player black's) must return the turn to black (the player).
        val board: Board = mock()
        val recordWhite: DropRecord = mock()
        whenever(recordWhite.getDrop()).thenReturn(Drop(Location2D(8, 8), Stone.WHITE))
        val recordBlack: DropRecord = mock()
        whenever(recordBlack.getDrop()).thenReturn(Drop(Location2D(7, 7), Stone.BLACK))
        val history: History = mock()
        // retractLast returns the newest record first: white (AI's last), then black (player's).
        whenever(history.retractLast()).thenReturn(recordWhite, recordBlack)
        whenever(history.getRecordCount()).thenReturn(2)
        whenever(history.getLastRecord()).thenReturn(recordBlack)
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.PLAYING)
        val game = GameImpl(board, history, rule)

        game.retractLast()
        game.retractLast()

        verify(board).cleanCell(eq(Location2D(8, 8)))
        verify(board).cleanCell(eq(Location2D(7, 7)))
        assertEquals(Stone.BLACK, game.getGameSituation().roundActor)
        assertEquals(2, game.getGameSituation().dropCount)
    }

    @Test
    fun retractLast_twice_consecutive_returnsTurnToWhitePlayer() {

        // Challenge-AI with player white (AI black moves first): player turn at odd dropCount.
        // Retracting two stones (player white's + AI black's) must return the turn to white.
        val board: Board = mock()
        val recordWhite: DropRecord = mock()
        whenever(recordWhite.getDrop()).thenReturn(Drop(Location2D(9, 9), Stone.WHITE))
        val recordBlack: DropRecord = mock()
        whenever(recordBlack.getDrop()).thenReturn(Drop(Location2D(7, 7), Stone.BLACK))
        val history: History = mock()
        // Sequence is B(ai) W(player) B(ai): retractLast returns the newest record first,
        // i.e. AI's black, then the player's white.
        whenever(history.retractLast()).thenReturn(recordBlack, recordWhite)
        whenever(history.getRecordCount()).thenReturn(1)
        whenever(history.getLastRecord()).thenReturn(recordBlack)
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.PLAYING)
        val game = GameImpl(board, history, rule)

        game.retractLast()
        game.retractLast()

        verify(board).cleanCell(eq(Location2D(9, 9)))
        verify(board).cleanCell(eq(Location2D(7, 7)))
        assertEquals(Stone.WHITE, game.getGameSituation().roundActor)
        assertEquals(1, game.getGameSituation().dropCount)
    }

    @Test
    fun retractLast_twice_afterTerminalState_restoresPlaying() {

        // Terminal state (white won): retracting two stones restores PLAYING with the
        // turn back to the player.
        val board: Board = mock()
        val recordWhite: DropRecord = mock()
        whenever(recordWhite.getDrop()).thenReturn(Drop(Location2D(8, 8), Stone.WHITE))
        val recordBlack: DropRecord = mock()
        whenever(recordBlack.getDrop()).thenReturn(Drop(Location2D(7, 7), Stone.BLACK))
        val history: History = mock()
        whenever(history.retractLast()).thenReturn(recordWhite, recordBlack)
        whenever(history.getRecordCount()).thenReturn(2)
        whenever(history.getLastRecord()).thenReturn(recordBlack)
        val rule: Rule = mock()
        // Terminal before retracts, playing after the board shrinks.
        whenever(rule.judgeGameState(any()))
            .thenReturn(GameState.WHITE_WON, GameState.PLAYING, GameState.PLAYING, GameState.PLAYING)
        val game = GameImpl(board, history, rule)

        game.retractLast()
        game.retractLast()

        assertEquals(GameState.PLAYING, game.getGameSituation().state)
        assertEquals(Stone.BLACK, game.getGameSituation().roundActor)
    }

    @Test
    fun retractLast_once_afterPlayerWonTerminal_returnsTurnToPlayer() {

        // Player-won terminal (player black's winning move is the newest record):
        // retracting exactly one stone returns the turn to the player, while retracting
        // a second stone (the AI's white) would wrongly hand the turn to the AI.
        val board: Board = mock()
        val recordBlack: DropRecord = mock()
        whenever(recordBlack.getDrop()).thenReturn(Drop(Location2D(7, 7), Stone.BLACK))
        val history: History = mock()
        whenever(history.retractLast()).thenReturn(recordBlack)
        whenever(history.getRecordCount()).thenReturn(3)
        whenever(history.getLastRecord()).thenReturn(recordBlack)
        val rule: Rule = mock()
        whenever(rule.judgeGameState(any())).thenReturn(GameState.PLAYING)
        val game = GameImpl(board, history, rule)

        game.retractLast()

        assertEquals(Stone.BLACK, game.getGameSituation().roundActor)
        assertEquals(GameState.PLAYING, game.getGameSituation().state)
        assertEquals(3, game.getGameSituation().dropCount)
    }
}