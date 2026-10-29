package org.bymc.gomoku.game.impl

import org.bymc.gomoku.model.common.param.Drop
import org.bymc.gomoku.model.common.param.DropLegality
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Size2D
import org.bymc.gomoku.model.common.param.Stone
import org.bymc.gomoku.model.common.util.TestBoardViewModel
import org.bymc.gomoku.model.impl.RuleImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Tests for RandomAiPlayer.
 *
 * @author: zheng.chez
 * @since: 2026/10/28
 */
internal class RandomAiPlayerTest {

    private val rule = RuleImpl(ttnhEnabled = true, ffnhEnabled = true, olnhEnables = true)
    private val looseRule = RuleImpl(ttnhEnabled = false, ffnhEnabled = false, olnhEnables = false)

    @Test
    fun `returns in-board location on empty board`() {

        val board = TestBoardViewModel(Size2D(15, 15), ".".repeat(15 * 15))
        val player = RandomAiPlayer(seed = 42L)

        val location = player.pickDropLocation(board, rule, Stone.BLACK)

        assertNotNull(location)
        assertTrue(location!!.x in 0 until 15)
        assertTrue(location.y in 0 until 15)
    }

    @Test
    fun `returns empty cell only`() {

        val board = TestBoardViewModel(
            Size2D(15, 15),
            """
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . x x . x . . . . . . . . .
                . . . x . . . . . . . x x . .
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
        val player = RandomAiPlayer(seed = 7L)

        repeat(100) {

            val location = player.pickDropLocation(board, rule, Stone.WHITE)!!

            assertFalse(board.getCell(location).isOccupied())
        }
    }

    @Test
    fun `black never drops on forbidden location`() {

        // Location (4, 2) is a 3-3 forbidden point for black under the strict rule.
        val board = TestBoardViewModel(
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
        val player = RandomAiPlayer(seed = 99L)

        repeat(200) {

            val location = player.pickDropLocation(board, rule, Stone.BLACK)!!

            assertEquals(DropLegality.LEGAL, rule.checkDropLegality(board, Drop(location, Stone.BLACK)))
            assertFalse(board.getCell(location).isOccupied())
        }
    }

    @Test
    fun `white is not restricted by forbidden rules`() {

        // Dropping black at (3, 0) forms a six-stone overline (forbidden), but the same
        // location is legal for white since white has no forbidden rules.
        val board = TestBoardViewModel(Size2D(6, 1), "x x x . x x")
        val player = RandomAiPlayer(seed = 1L)

        assertEquals(Location2D(3, 0), player.pickDropLocation(board, rule, Stone.WHITE))
    }

    @Test
    fun `returns null when board is full`() {

        val board = TestBoardViewModel(Size2D(2, 1), "x o")
        val player = RandomAiPlayer(seed = 1L)

        assertNull(player.pickDropLocation(board, looseRule, Stone.BLACK))
        assertNull(player.pickDropLocation(board, looseRule, Stone.WHITE))
    }

    @Test
    fun `returns null when all empty cells are forbidden`() {

        // Single free cell (3, 0): dropping black there forms a six-stone overline (forbidden),
        // so black has no legal location while white can still pick it.
        val board = TestBoardViewModel(Size2D(6, 1), "x x x . x x")
        val player = RandomAiPlayer(seed = 1L)

        assertNull(player.pickDropLocation(board, rule, Stone.BLACK))
    }

    @Test
    fun `fixed seed produces reproducible result`() {

        val board = TestBoardViewModel(Size2D(15, 15), ".".repeat(15 * 15))

        val first = RandomAiPlayer(seed = 2026L).pickDropLocation(board, rule, Stone.BLACK)
        val second = RandomAiPlayer(seed = 2026L).pickDropLocation(board, rule, Stone.BLACK)

        assertEquals(first, second)
    }
}
