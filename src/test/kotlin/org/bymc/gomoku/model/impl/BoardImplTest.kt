package org.bymc.gomoku.model.impl

import org.bymc.gomoku.model.common.param.Drop
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Size2D
import org.bymc.gomoku.model.common.param.Stone
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class BoardImplTest {

    @Test
    fun dropStone() {

        val board = BoardImpl(Size2D(15, 15), setOf(Drop(Location2D(0, 0), Stone.WHITE)))
        assertEquals(CellImpl(board, Location2D(7, 7), null), board.getCell(Location2D(7, 7)))
        board.dropStone(Location2D(7, 7), Stone.BLACK)
        assertEquals(CellImpl(board, Location2D(7, 7), Stone.BLACK), board.getCell(Location2D(7, 7)))
    }

    @Test
    fun cleanCell() {

        val board = BoardImpl(Size2D(15, 15), setOf(Drop(Location2D(0, 0), Stone.WHITE)))
        board.dropStone(Location2D(7, 7), Stone.BLACK)
        assertEquals(CellImpl(board, Location2D(7, 7), Stone.BLACK), board.getCell(Location2D(7, 7)))
        assertEquals(CellImpl(board, Location2D(0, 0), Stone.WHITE), board.getCell(Location2D(0, 0)))
        board.cleanCell(Location2D(7, 7))
        board.cleanCell(Location2D(0, 0))
        assertEquals(CellImpl(board, Location2D(7, 7), null), board.getCell(Location2D(7, 7)))
        assertEquals(CellImpl(board, Location2D(0, 0), null), board.getCell(Location2D(0, 0)))
    }

    @Test
    fun getSize() {

        val board = BoardImpl(Size2D(15, 15), setOf(Drop(Location2D(0, 0), Stone.WHITE)))
        assertEquals(Size2D(15, 15), board.getSize())
    }

    @Test
    fun getSize_exceptional() {

        val board = BoardImpl(Size2D(15, 15), setOf(Drop(Location2D(0, 0), Stone.WHITE)))
        assertThrows(RuntimeException::class.java) {
            board.getCell(Location2D(0, 15))
        }
    }

    @Test
    fun getStoneCount() {

        assertEquals(0, BoardImpl(Size2D(15, 15), emptySet()).getStoneCount())
        assertEquals(1, BoardImpl(Size2D(15, 15), setOf(Drop(Location2D(0, 0), Stone.WHITE))).getStoneCount())

        val board = BoardImpl(Size2D(15, 15), setOf(Drop(Location2D(0, 0), Stone.WHITE)))
        board.dropStone(Location2D(7, 7), Stone.BLACK)
        board.dropStone(Location2D(8, 8), Stone.WHITE)
        assertEquals(3, board.getStoneCount())
    }
}