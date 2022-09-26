package org.bymc.gomoku.model.common.param

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Size2DTest {

    @Test
    fun testEquals() {

        assertTrue(Size2D(15, 10) == Size2D(15, 10))
    }
}