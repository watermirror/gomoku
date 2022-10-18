package org.bymc.gomoku.model.common.util

import org.bymc.gomoku.model.common.param.Drop
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Size2D
import org.bymc.gomoku.model.common.param.Stone
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class OlnhVerifierTest {

    @Test
    fun verify() {

        val boardView = TestBoardViewModel(
            Size2D(15, 15),
            """
                . . . . . . . . . . . . . . .
                . x x x x . x . . . . . . . .
                . . . . . . . . . . . . . . .
                . x x x x . x . . . . . . . .
                . . . . x . . . . . . . . . .
                . . . x . . . . . . . . . . .
                . . x . . . . . . . . . . . .
                . x . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . o o o o . o . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
            """.trimIndent()
        )
        assertEquals(true, OlnhVerifier(boardView, Drop(Location2D(5, 1), Stone.BLACK)).verify())
        assertEquals(false, OlnhVerifier(boardView, Drop(Location2D(5, 3), Stone.BLACK)).verify())
        assertEquals(false, OlnhVerifier(boardView, Drop(Location2D(5, 9), Stone.WHITE)).verify())
    }
}