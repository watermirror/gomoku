package org.bymc.gomoku.model.common.param

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class PolarTest {

    @Test
    fun getNearEnd() {

        assertEquals(Direction.NORTH, Polar.N2S.nearEnd)
        assertEquals(Direction.NORTHEAST, Polar.NE2SW.nearEnd)
        assertEquals(Direction.EAST, Polar.E2W.nearEnd)
        assertEquals(Direction.SOUTHWEST, Polar.SW2NE.nearEnd)
        assertEquals(Direction.SOUTH, Polar.S2N.nearEnd)
        assertEquals(Direction.SOUTHEAST, Polar.SE2NW.nearEnd)
        assertEquals(Direction.WEST, Polar.W2E.nearEnd)
        assertEquals(Direction.NORTHWEST, Polar.NW2SE.nearEnd)
    }

    @Test
    fun getFarEnd() {

        assertEquals(Direction.SOUTH, Polar.N2S.farEnd)
        assertEquals(Direction.SOUTHWEST, Polar.NE2SW.farEnd)
        assertEquals(Direction.WEST, Polar.E2W.farEnd)
        assertEquals(Direction.NORTHEAST, Polar.SW2NE.farEnd)
        assertEquals(Direction.NORTH, Polar.S2N.farEnd)
        assertEquals(Direction.NORTHWEST, Polar.SE2NW.farEnd)
        assertEquals(Direction.EAST, Polar.W2E.farEnd)
        assertEquals(Direction.SOUTHEAST, Polar.NW2SE.farEnd)
    }

    @Test
    fun getNormalizedPolars() {

        val polars = Polar.getNormalizedPolars()
        assertEquals(4, polars.size)
        assertTrue(polars.contains(Polar.N2S))
        assertTrue(polars.contains(Polar.NE2SW))
        assertTrue(polars.contains(Polar.E2W))
        assertTrue(polars.contains(Polar.SE2NW))
    }

    @Test
    fun reverse() {

        assertEquals(Polar.S2N, Polar.reverse(Polar.N2S))
        assertEquals(Polar.SW2NE, Polar.reverse(Polar.NE2SW))
        assertEquals(Polar.W2E, Polar.reverse(Polar.E2W))
        assertEquals(Polar.NW2SE, Polar.reverse(Polar.SE2NW))
        assertEquals(Polar.N2S, Polar.reverse(Polar.S2N))
        assertEquals(Polar.NE2SW, Polar.reverse(Polar.SW2NE))
        assertEquals(Polar.E2W, Polar.reverse(Polar.W2E))
        assertEquals(Polar.SE2NW, Polar.reverse(Polar.NW2SE))
    }
}