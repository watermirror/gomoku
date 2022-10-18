package org.bymc.gomoku.model.common.util

import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Size2D
import org.bymc.gomoku.model.common.param.Stone
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GomokuVerifierTest {

    @Test
    fun verify() {

        val boardView = TestBoardViewModel(
            Size2D(15, 15),
            """
                . . . . . . . . . . . . . . .
                . x x x x x . . . . . . . . .
                . . . . . . . . . . . . . . .
                . o . x . . . . . . . . . . .
                . o . x . . o . . . . . . . .
                . o . x . . . o . . . . . . .
                . o . x . . . . o . . . . . .
                . o . . . . . . . o . . . . .
                . o . . . . . . . . o . . . .
                . . . . . . . . . . . . . . .
                . . . x x x . x . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
            """.trimIndent()
        )
        assertEquals(true, GomokuVerifier(boardView, Location2D(1, 1)).verify())
        assertEquals(true, GomokuVerifier(boardView, Location2D(1, 4)).verify())
        assertEquals(false, GomokuVerifier(boardView, Location2D(3, 4)).verify())
        assertThrows(RuntimeException::class.java) {
            GomokuVerifier(boardView, Location2D(6, 10)).verify()
        }
        assertEquals(true, GomokuVerifier(boardView, Location2D(6, 10)).verifyBeforeDrop(Stone.BLACK))
        assertEquals(false, GomokuVerifier(boardView, Location2D(6, 10)).verifyBeforeDrop(Stone.WHITE))
        assertEquals(true, GomokuVerifier(boardView, Location2D(8, 6)).verify())
    }
}