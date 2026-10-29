package org.bymc.gomoku.ui.window

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Exhaustive tests for RetractionPolicy across modes, turn ownership and drop counts.
 *
 * @author: zheng.chez
 * @since: 2026/10/29
 */
internal class RetractionPolicyTest {

    @Test
    fun `self-study mode always retracts one stone`() {

        assertEquals(0, RetractionPolicy.getStepCount(aiMode = false, dropCount = 0, roundActorIsAi = false))
        assertEquals(0, RetractionPolicy.getStepCount(aiMode = false, dropCount = 0, roundActorIsAi = true))
        assertEquals(1, RetractionPolicy.getStepCount(aiMode = false, dropCount = 1, roundActorIsAi = false))
        assertEquals(1, RetractionPolicy.getStepCount(aiMode = false, dropCount = 1, roundActorIsAi = true))
        assertEquals(1, RetractionPolicy.getStepCount(aiMode = false, dropCount = 2, roundActorIsAi = false))
        assertEquals(1, RetractionPolicy.getStepCount(aiMode = false, dropCount = 5, roundActorIsAi = false))
        assertEquals(1, RetractionPolicy.getStepCount(aiMode = false, dropCount = 225, roundActorIsAi = true))
    }

    @Test
    fun `challenge-ai mode with ai turn retracts exactly one stone`() {

        // Player-won terminal state: last record is the player's winning move.
        assertEquals(0, RetractionPolicy.getStepCount(aiMode = true, dropCount = 0, roundActorIsAi = true))
        assertEquals(1, RetractionPolicy.getStepCount(aiMode = true, dropCount = 1, roundActorIsAi = true))
        assertEquals(1, RetractionPolicy.getStepCount(aiMode = true, dropCount = 3, roundActorIsAi = true))
        assertEquals(1, RetractionPolicy.getStepCount(aiMode = true, dropCount = 11, roundActorIsAi = true))
        assertEquals(1, RetractionPolicy.getStepCount(aiMode = true, dropCount = 225, roundActorIsAi = true))
    }

    @Test
    fun `challenge-ai mode with player turn retracts two stones`() {

        // In-progress player turn or AI-won terminal state: last record is the AI's stone.
        assertEquals(0, RetractionPolicy.getStepCount(aiMode = true, dropCount = 0, roundActorIsAi = false))
        assertEquals(0, RetractionPolicy.getStepCount(aiMode = true, dropCount = 1, roundActorIsAi = false))
        assertEquals(2, RetractionPolicy.getStepCount(aiMode = true, dropCount = 2, roundActorIsAi = false))
        assertEquals(2, RetractionPolicy.getStepCount(aiMode = true, dropCount = 3, roundActorIsAi = false))
        assertEquals(2, RetractionPolicy.getStepCount(aiMode = true, dropCount = 10, roundActorIsAi = false))
        assertEquals(2, RetractionPolicy.getStepCount(aiMode = true, dropCount = 225, roundActorIsAi = false))
    }

    @Test
    fun `availability matches positive step count for all combinations`() {

        for (aiMode in listOf(false, true)) {
            for (roundActorIsAi in listOf(false, true)) {
                for (dropCount in 0..10) {
                    val steps = RetractionPolicy.getStepCount(aiMode, dropCount, roundActorIsAi)
                    assertEquals(
                        steps > 0,
                        RetractionPolicy.isEnabled(aiMode, dropCount, roundActorIsAi),
                        "aiMode=$aiMode, roundActorIsAi=$roundActorIsAi, dropCount=$dropCount"
                    )
                }
            }
        }
    }

    @Test
    fun `player-won terminal with single stone is retractable`() {

        // Player black wins with the very first move is impossible, but the policy must still
        // allow retracting a single player stone when the turn belongs to the AI.
        assertTrue(RetractionPolicy.isEnabled(aiMode = true, dropCount = 1, roundActorIsAi = true))
        assertFalse(RetractionPolicy.isEnabled(aiMode = true, dropCount = 0, roundActorIsAi = true))
    }
}
