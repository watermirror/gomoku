package org.bymc.gomoku.model.common.util

import org.bymc.gomoku.model.common.param.Drop
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Size2D
import org.bymc.gomoku.model.common.param.Stone
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class TtnhVerifierTest {

    @Test
    fun verify() {

        val boardView = TestBoardViewModel(
            Size2D(15, 15),
            """
                . . . . . . . . . . . . . . .
                . . x x . . . . . . . . . . .
                . . . x . . . . . . . . . . .
                . . x . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . x . x . . . x . . .
                . . . . . . . . x . x . . . .
                . . x . x . . x x . . . . . .
                . . x . . . . . x . . . . . .
                . . . . . . . x . . . . . . .
                . . . . . . o . . . . . . . .
                . . . . . . o . o . . . . . .
                . . . . . . . . o . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
            """.trimIndent()
        )
        assertEquals(true, TtnhVerifier(boardView, Drop(Location2D(4, 1), Stone.BLACK)).verify())
        assertEquals(false, TtnhVerifier(boardView, Drop(Location2D(5, 1), Stone.BLACK)).verify())
        assertEquals(true, TtnhVerifier(boardView, Drop(Location2D(3, 7), Stone.BLACK)).verify())
        assertEquals(false, TtnhVerifier(boardView, Drop(Location2D(3, 7), Stone.WHITE)).verify())
        assertEquals(false, TtnhVerifier(boardView, Drop(Location2D(7, 11), Stone.WHITE)).verify())
        assertEquals(false, TtnhVerifier(boardView, Drop(Location2D(9, 7), Stone.BLACK)).verify())
    }
}