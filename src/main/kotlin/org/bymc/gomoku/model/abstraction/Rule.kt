package org.bymc.gomoku.model.abstraction

import org.bymc.gomoku.model.common.Drop
import org.bymc.gomoku.model.common.DropLegality
import org.bymc.gomoku.model.common.GameState
import org.bymc.gomoku.model.common.Location2D

/**
 * 规则接口。
 *
 * @author: zheng.chez
 * @since: 2022/09/22
 */
interface Rule {

    /**
     * 检查落子的合法性。
     */
    fun checkDropLegality(board: Board, drop: Drop): DropLegality

    /**
     * 根据最后落子的单元格坐标判定棋局状态。
     */
    fun judgeGameState(board: Board, lastDropLocation: Location2D): GameState

    /**
     * 判定棋局状态。该方法性能较差。
     */
    fun judgeGameState(board: Board): GameState
}