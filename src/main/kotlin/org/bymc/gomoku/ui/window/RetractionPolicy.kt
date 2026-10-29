package org.bymc.gomoku.ui.window

/**
 * 悔棋策略：根据对局模式与局面决定悔棋可用性与单次悔棋撤子数。
 *
 * 目标不变式: 挑战 AI 模式下，一次悔棋完成后必然轮到玩家落子。
 *
 * - 研学模式: 撤一步，可用条件为已落子数不少于 1；
 * - 挑战 AI 模式、轮到 AI（玩家获胜终局，最后记录为玩家的获胜手）: 撤一步（玩家的获胜手，
 *   AI 的应手保留），可用条件为已落子数不少于 1；
 * - 挑战 AI 模式、轮到玩家（进行中或 AI 获胜终局，最后记录为 AI 的子）: 撤两步
 *   （AI 一子与玩家一子），可用条件为已落子数不少于 2。
 *
 * 依据 GameImpl.retractLast 语义: 撤子后 roundActor 为被撤那一手的执子方。
 * 玩家获胜终局最后记录为玩家的子，撤一步即回到玩家回合；若继续撤第二步
 * （AI 的子）反而会轮到 AI，导致玩家落子被记为 AI 执子——因此该局面只撤一步。
 *
 * AI 待落子等待期（500ms 延迟内）不由此策略判断可用性，由调度方直接禁用。
 *
 * @author: zheng.chez
 * @since: 2026/10/29
 */
object RetractionPolicy {

    /**
     * 悔棋按钮是否可用。roundActorIsAi 表示当前回合归属 AI（玩家获胜终局即为此形态）。
     */
    fun isEnabled(aiMode: Boolean, dropCount: Int, roundActorIsAi: Boolean): Boolean =

        getStepCount(aiMode, dropCount, roundActorIsAi) > 0

    /**
     * 单次悔棋撤子数。返回 0 表示当前不可悔棋。
     */
    fun getStepCount(aiMode: Boolean, dropCount: Int, roundActorIsAi: Boolean): Int = when {

        !aiMode && dropCount >= 1 -> 1
        aiMode && roundActorIsAi && dropCount >= 1 -> 1
        aiMode && !roundActorIsAi && dropCount >= 2 -> 2
        else -> 0
    }
}
