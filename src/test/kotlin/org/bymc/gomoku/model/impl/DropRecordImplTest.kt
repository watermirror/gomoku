package org.bymc.gomoku.model.impl

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.bymc.gomoku.model.abstraction.History
import org.bymc.gomoku.model.common.param.Drop
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Stone
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.Date

internal class DropRecordImplTest {

    @Test
    fun getOrder() {

        val timestamp = Date()
        val history: History = mock()
        val record = DropRecordImpl(history, 5, Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        assertEquals(5, record.getOrder())
    }

    @Test
    fun getDrop() {

        val timestamp = Date()
        val history: History = mock()
        val record = DropRecordImpl(history, 5, Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        assertEquals(Drop(Location2D(7, 7), Stone.BLACK), record.getDrop())
    }

    @Test
    fun getTime() {

        val timestamp = Date()
        val history: History = mock()
        val record = DropRecordImpl(history, 5, Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        assertEquals(timestamp, record.getTime())
    }

    @Test
    fun getPrevious() {

        val timestamp = Date()
        val history: History = mock()
        whenever(history.getRecord(4)).thenReturn(
            DropRecordImpl(history, 4, Drop(Location2D(8, 8), Stone.WHITE), timestamp)
        )
        val record = DropRecordImpl(history, 5, Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        assertEquals(DropRecordImpl(history, 4, Drop(Location2D(8, 8), Stone.WHITE), timestamp), record.getPrevious())
    }

    @Test
    fun getNext_case_1() {

        val timestamp = Date()
        val history: History = mock()
        whenever(history.getRecordCount()).thenReturn(10)
        whenever(history.getRecord(6)).thenReturn(
            DropRecordImpl(history, 6, Drop(Location2D(8, 8), Stone.WHITE), timestamp)
        )
        val record = DropRecordImpl(history, 5, Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        assertEquals(DropRecordImpl(history, 6, Drop(Location2D(8, 8), Stone.WHITE), timestamp), record.getNext())
    }

    @Test
    fun getNext_case_2() {

        val timestamp = Date()
        val history: History = mock()
        whenever(history.getRecordCount()).thenReturn(6)
        val record = DropRecordImpl(history, 5, Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        assertEquals(null, record.getNext())
    }

    @Test
    fun getNext_case_3() {

        val timestamp = Date()
        val history: History = mock()
        val record = DropRecordImpl(history, 0, Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        assertEquals(null, record.getPrevious())
    }
}