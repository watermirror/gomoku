package org.bymc.gomoku.model.common.util

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.bymc.gomoku.model.abstraction.Cell
import org.bymc.gomoku.model.common.param.Direction
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Stone
import org.bymc.gomoku.model.impl.CellImpl
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class DirectedPatternCounterTest {

    @Test
    fun count_case_1() {

        val cell: Cell = mock()
        val emptyCell = CellImpl(mock(), Location2D(0, 0), null)
        val blackCell = CellImpl(mock(), Location2D(0, 0), Stone.BLACK)
        val whiteCell = CellImpl(mock(), Location2D(0, 0), Stone.WHITE)
        whenever(cell.getNeighbors(Direction.EAST))
            .thenReturn(listOf(emptyCell, blackCell, blackCell, whiteCell, blackCell))
        val countList = DirectedPatternCounter(cell, Direction.EAST, listOf(null, Stone.BLACK, Stone.WHITE)).count()
        assertEquals(listOf(1, 2, 1), countList)
    }

    @Test
    fun count_case_2() {

        val cell: Cell = mock()
        val emptyCell = CellImpl(mock(), Location2D(0, 0), null)
        val blackCell = CellImpl(mock(), Location2D(0, 0), Stone.BLACK)
        val whiteCell = CellImpl(mock(), Location2D(0, 0), Stone.WHITE)
        whenever(cell.getNeighbors(Direction.EAST))
            .thenReturn(listOf(emptyCell, blackCell, blackCell, whiteCell, blackCell))
        val countList = DirectedPatternCounter(
            cell, Direction.EAST, listOf(null, Stone.BLACK, null, Stone.WHITE, Stone.BLACK, Stone.WHITE, null)
        ).count()
        assertEquals(listOf(1, 2, 0, 1, 1, 0, 0), countList)
    }

    @Test
    fun count_exceptional_1() {

        assertThrows(RuntimeException::class.java) {
            DirectedPatternCounter(mock(), Direction.EAST, emptyList())
        }
    }

    @Test
    fun count_exceptional_2() {

        assertThrows(RuntimeException::class.java) {
            DirectedPatternCounter(mock(), Direction.EAST, listOf(null, Stone.BLACK, null, null, Stone.WHITE))
        }
    }
}