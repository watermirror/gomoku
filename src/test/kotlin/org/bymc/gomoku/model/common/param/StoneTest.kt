package org.bymc.gomoku.model.common.param

import org.bymc.gomoku.model.common.param.Stone
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class StoneTest {

    @Test
    fun getOpponent() {

        assertEquals(Stone.WHITE, Stone.getOpponent(Stone.BLACK))
        assertEquals(Stone.BLACK, Stone.getOpponent(Stone.WHITE))
    }
}