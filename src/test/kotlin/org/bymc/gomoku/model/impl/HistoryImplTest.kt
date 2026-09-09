package org.bymc.gomoku.model.impl

import org.bymc.gomoku.model.abstraction.DropRecord
import org.bymc.gomoku.model.common.param.Drop
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Stone
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.Date

internal class HistoryImplTest {

    @Test
    fun getBeginTime() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)
        assertEquals(timestamp, history.getBeginTime())
    }

    @Test
    fun getRecordCount_case_1() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)
        assertEquals(0, history.getRecordCount())
    }

    @Test
    fun getRecordCount_case_2() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)
        history.appendRecord(Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        assertEquals(1, history.getRecordCount())
    }

    @Test
    fun getRecordCount_case_3() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)
        history.appendRecord(Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        history.appendRecord(Drop(Location2D(8, 8), Stone.WHITE), timestamp)
        assertEquals(2, history.getRecordCount())
    }

    @Test
    fun getRecord() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)
        history.appendRecord(Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        history.appendRecord(Drop(Location2D(8, 8), Stone.WHITE), timestamp)
        assertEquals(history.getRecord(0), DropRecordImpl(history, 0, Drop(Location2D(7, 7), Stone.BLACK), timestamp))
        assertThrows(RuntimeException::class.java) {
            history.getRecord(2)
        }
        assertThrows(RuntimeException::class.java) {
            history.getRecord(-1)
        }
    }

    @Test
    fun getFirstRecord_case_1() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)
        history.appendRecord(Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        history.appendRecord(Drop(Location2D(8, 8), Stone.WHITE), timestamp)
        assertEquals(
            DropRecordImpl(history, 0, Drop(Location2D(7, 7), Stone.BLACK), timestamp),
            history.getFirstRecord()
        )
    }

    @Test
    fun getFirstRecord_case_2() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)
        assertEquals(null, history.getFirstRecord())
    }

    @Test
    fun getLastRecord_case_1() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)
        history.appendRecord(Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        history.appendRecord(Drop(Location2D(8, 8), Stone.WHITE), timestamp)
        assertEquals(
            DropRecordImpl(history, 1, Drop(Location2D(8, 8), Stone.WHITE), timestamp),
            history.getLastRecord()
        )
    }

    @Test
    fun getLastRecord_case_2() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)
        assertEquals(null, history.getLastRecord())
    }

    @Test
    fun retract_case_1() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)
        assertEquals(
            emptyList<DropRecord>(),
            history.retract(Stone.BLACK)
        )
    }

    @Test
    fun retract_case_2() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)
        history.appendRecord(Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        assertEquals(
            listOf(
                DropRecordImpl(history, 0, Drop(Location2D(7, 7), Stone.BLACK), timestamp)
            ),
            history.retract(Stone.BLACK)
        )
        assertEquals(0, history.getRecordCount())
    }

    @Test
    fun retract_case_3() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)
        history.appendRecord(Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        history.appendRecord(Drop(Location2D(8, 8), Stone.WHITE), timestamp)
        assertEquals(
            listOf(
                DropRecordImpl(history, 1, Drop(Location2D(8, 8), Stone.WHITE), timestamp),
                DropRecordImpl(history, 0, Drop(Location2D(7, 7), Stone.BLACK), timestamp)
            ),
            history.retract(Stone.BLACK)
        )
        assertEquals(0, history.getRecordCount())
    }

    @Test
    fun retract_case_4() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)
        history.appendRecord(Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        history.appendRecord(Drop(Location2D(8, 8), Stone.WHITE), timestamp)
        history.appendRecord(Drop(Location2D(9, 9), Stone.BLACK), timestamp)
        assertEquals(
            listOf(
                DropRecordImpl(history, 2, Drop(Location2D(9, 9), Stone.BLACK), timestamp)
            ),
            history.retract(Stone.BLACK)
        )
        assertEquals(2, history.getRecordCount())
    }

    @Test
    fun retract_case_5() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)
        history.appendRecord(Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        history.appendRecord(Drop(Location2D(8, 8), Stone.WHITE), timestamp)
        history.appendRecord(Drop(Location2D(9, 9), Stone.BLACK), timestamp)
        history.appendRecord(Drop(Location2D(10, 10), Stone.WHITE), timestamp)
        assertEquals(
            listOf(
                DropRecordImpl(history, 3, Drop(Location2D(10, 10), Stone.WHITE), timestamp),
                DropRecordImpl(history, 2, Drop(Location2D(9, 9), Stone.BLACK), timestamp)
            ),
            history.retract(Stone.BLACK)
        )
        assertEquals(2, history.getRecordCount())
    }

    @Test
    fun retractLast_fromMultiRecordHistory() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)
        history.appendRecord(Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        history.appendRecord(Drop(Location2D(8, 8), Stone.WHITE), timestamp)
        history.appendRecord(Drop(Location2D(9, 9), Stone.BLACK), timestamp)

        val result = history.retractLast()
        assertNotNull(result)
        assertEquals(Drop(Location2D(9, 9), Stone.BLACK), result!!.getDrop())
        assertEquals(2, result.getOrder())
        assertEquals(2, history.getRecordCount())

        // Verify remaining records are untouched.
        assertEquals(Drop(Location2D(7, 7), Stone.BLACK), history.getRecord(0).getDrop())
        assertEquals(Drop(Location2D(8, 8), Stone.WHITE), history.getRecord(1).getDrop())
    }

    @Test
    fun retractLast_fromSingleRecordHistory() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)
        history.appendRecord(Drop(Location2D(7, 7), Stone.BLACK), timestamp)

        val result = history.retractLast()
        assertNotNull(result)
        assertEquals(Drop(Location2D(7, 7), Stone.BLACK), result!!.getDrop())
        assertEquals(0, result.getOrder())
        assertEquals(0, history.getRecordCount())
    }

    @Test
    fun retractLast_fromEmptyHistory() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)

        val result = history.retractLast()
        assertNull(result)
        assertEquals(0, history.getRecordCount())
    }

    @Test
    fun retractLast_remainingRecordsUntouched() {

        val timestamp = Date()
        val history = HistoryImpl(timestamp)
        history.appendRecord(Drop(Location2D(7, 7), Stone.BLACK), timestamp)
        history.appendRecord(Drop(Location2D(8, 8), Stone.WHITE), timestamp)
        history.appendRecord(Drop(Location2D(9, 9), Stone.BLACK), timestamp)
        history.appendRecord(Drop(Location2D(10, 10), Stone.WHITE), timestamp)

        history.retractLast()
        assertEquals(3, history.getRecordCount())
        assertEquals(Drop(Location2D(7, 7), Stone.BLACK), history.getRecord(0).getDrop())
        assertEquals(Drop(Location2D(8, 8), Stone.WHITE), history.getRecord(1).getDrop())
        assertEquals(Drop(Location2D(9, 9), Stone.BLACK), history.getRecord(2).getDrop())

        history.retractLast()
        assertEquals(2, history.getRecordCount())
        assertEquals(Drop(Location2D(7, 7), Stone.BLACK), history.getRecord(0).getDrop())
        assertEquals(Drop(Location2D(8, 8), Stone.WHITE), history.getRecord(1).getDrop())
    }
}