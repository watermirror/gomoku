package org.bymc.gomoku.model.common.param

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Location2DTest {

    @Test
    fun testEquals() {

        assertTrue(Location2D(1, 2) == Location2D(1, 2))
    }
}