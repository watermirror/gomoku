package org.bymc.gomoku.model.impl

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.bymc.gomoku.model.abstraction.BoardView
import org.bymc.gomoku.model.common.param.Direction
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Size2D
import org.bymc.gomoku.model.common.param.Stone
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class CellImplTest {

    @Test
    fun getLocation() {

        val board: BoardView = mock()
        val cell = CellImpl(board, Location2D(7, 0), null)
        assertEquals(Location2D(7, 0), cell.getLocation())
    }

    @Test
    fun isOccupied_case_1() {

        val board: BoardView = mock()
        val cell = CellImpl(board, Location2D(7, 0), null)
        assertEquals(false, cell.isOccupied())
    }

    @Test
    fun isOccupied_case_2() {

        val board: BoardView = mock()
        val cell = CellImpl(board, Location2D(7, 0), Stone.WHITE)
        assertEquals(true, cell.isOccupied())
    }

    @Test
    fun getStone_case_1() {

        val board: BoardView = mock()
        val cell = CellImpl(board, Location2D(7, 0), null)
        assertEquals(null, cell.getStone())
    }

    @Test
    fun getStone_case_2() {

        val board: BoardView = mock()
        val cell = CellImpl(board, Location2D(7, 0), Stone.BLACK)
        assertEquals(Stone.BLACK, cell.getStone())
    }

    @Test
    fun getNeighbor_case_1() {

        val board: BoardView = mock()
        whenever(board.getSize()).thenReturn(Size2D(15, 15))
        whenever(board.getCell(Location2D(7, 1))).thenReturn(CellImpl(board, Location2D(7, 1), Stone.WHITE))
        val cell = CellImpl(board, Location2D(7, 0), Stone.BLACK)
        assertEquals(CellImpl(board, Location2D(7, 1), Stone.WHITE), cell.getNeighbor(Direction.NORTH))
    }

    @Test
    fun getNeighbor_case_2() {

        val board: BoardView = mock()
        whenever(board.getSize()).thenReturn(Size2D(15, 15))
        whenever(board.getCell(Location2D(8, 1))).thenReturn(CellImpl(board, Location2D(8, 1), Stone.WHITE))
        val cell = CellImpl(board, Location2D(7, 0), Stone.BLACK)
        assertEquals(CellImpl(board, Location2D(8, 1), Stone.WHITE), cell.getNeighbor(Direction.NORTHEAST))
    }

    @Test
    fun getNeighbor_case_3() {

        val board: BoardView = mock()
        whenever(board.getSize()).thenReturn(Size2D(15, 15))
        val cell = CellImpl(board, Location2D(7, 0), Stone.BLACK)
        assertEquals(null, cell.getNeighbor(Direction.SOUTH))
    }

    @Test
    fun getNeighbor_case_4() {

        val board: BoardView = mock()
        whenever(board.getSize()).thenReturn(Size2D(15, 15))
        val cell = CellImpl(board, Location2D(7, 14), Stone.BLACK)
        assertEquals(null, cell.getNeighbor(Direction.NORTHEAST))
    }
}