package org.bymc.gomoku.model.common.param

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GameStateTest {

    @Test
    fun testNameAndOfValue() {

        assertEquals(GameState.PLAYING, GameState.valueOf(GameState.PLAYING.name))
        assertEquals(GameState.BLACK_WON, GameState.valueOf(GameState.BLACK_WON.name))
        assertEquals(GameState.WHITE_WON, GameState.valueOf(GameState.WHITE_WON.name))
        assertEquals(GameState.DRAW, GameState.valueOf(GameState.DRAW.name))
    }
}