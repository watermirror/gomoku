package org.bymc.gomoku.model.common.util

import org.bymc.gomoku.model.common.param.Drop
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Size2D
import org.bymc.gomoku.model.common.param.Stone
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class FfnhVerifierTest {

    @Test
    fun verify() {

        val boardView = TestBoardView(
            Size2D(15, 15),
            """
                . . . . . . . o . . . . . . .
                . x x x . . . o . x x . x o .
                . . . . x . . . . x . . . . .
                . . . . x . . . . . x . . . .
                . . . . x . . . . . . x . . .
                . . . . o . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . . . . . . . . . . . . . .
                . . x x x . . . . . . . . . .
                . x x . . . . . . . . . . o .
                . x . x . . . . . . . . . o .
                . x . . x . . . . . . . . o .
                . x . . . . . . . . o o o . .
                . . . . . . . . . . . . . . .
            """.trimIndent()
        )
        assertEquals(true, FfnhVerifier(boardView, Drop(Location2D(4, 1), Stone.BLACK)).verify())
        assertEquals(true, FfnhVerifier(boardView, Drop(Location2D(8, 1), Stone.BLACK)).verify())
        assertEquals(false, FfnhVerifier(boardView, Drop(Location2D(4, 1), Stone.WHITE)).verify())
        assertEquals(false, FfnhVerifier(boardView, Drop(Location2D(12, 5), Stone.BLACK)).verify())
        assertEquals(false, FfnhVerifier(boardView, Drop(Location2D(13, 13), Stone.WHITE)).verify())
        assertEquals(false, FfnhVerifier(boardView, Drop(Location2D(1, 9), Stone.BLACK)).verify())
    }
}