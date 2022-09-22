package org.bymc.gomoku.game.abstraction

import org.bymc.gomoku.game.common.GameSituation
import org.bymc.gomoku.model.abstraction.BoardView
import org.bymc.gomoku.model.common.Drop
import org.bymc.gomoku.model.common.DropLegality

/**
 * 游戏逻辑接口。
 *
 * @author: zheng.chez
 * @since: 2022/09/22
 */
interface Game {

    /**
     * 获取游戏情况。
     */
    fun getGameSituation(): GameSituation

    /**
     * 获取棋盘视图。
     */
    fun getBoardView(): BoardView

    /**
     * 落子。返回 LEGAL 表示落子成功，否则不合法。请保障在棋局进行的状态下调用该方法，若 getGameSituation().state 不为 PLAYING，该方法
     * 将抛出异常。
     */
    fun dropStone(drop: Drop): DropLegality

    /**
     * 执行悔棋。
     */
    fun retract()
}