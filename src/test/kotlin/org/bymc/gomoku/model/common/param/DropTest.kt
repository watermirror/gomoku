package org.bymc.gomoku.model.common.param

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class DropTest {

    @Test
    fun testEquals() {

        assertTrue(Drop(Location2D(0, 0), Stone.WHITE) == Drop(Location2D(0, 0), Stone.WHITE))
    }
}