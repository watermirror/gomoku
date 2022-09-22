package org.bymc.gomoku.game.common

import org.bymc.gomoku.model.common.GameState
import org.bymc.gomoku.model.common.Stone
import java.util.Date

/**
 * 游戏情况。
 *
 * @author: zheng.chez
 * @since: 2022/09/22
 */
data class GameSituation(

    /**
     * 棋局状态。
     */
    val state: GameState,

    /**
     * 当前回合执行方。
     */
    val roundActor: Stone,

    /**
     * 已经存在的落子次数。
     */
    val dropCount: Int,

    /**
     * 当前局面能否执行悔棋操作。当前回合执行方如果尚未落子记录，则不能悔棋。
     */
    val retractionAvailable: Boolean,

    /**
     * 开始时间。
     */
    val beginTime: Date,

    /**
     * 结束时间。当 state 为 PLAYING 时恒为 null。
     */
    val endTime: Date?,

    /**
     * 最近落子时间。当 dropCount 为 0 时恒为 null。
     */
    val lastDropTime: Date?
)