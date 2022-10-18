package org.bymc.gomoku.game.factory

import org.bymc.gomoku.game.abstraction.Game
import org.bymc.gomoku.game.impl.GameImpl
import org.bymc.gomoku.model.common.param.Size2D
import org.bymc.gomoku.model.impl.BoardImpl
import org.bymc.gomoku.model.impl.HistoryImpl
import org.bymc.gomoku.model.impl.RuleImpl
import java.util.*

/**
 * 游戏逻辑对象工厂类。
 *
 * @author: zheng.chez
 * @since: 2022/10/18
 */
object GameFactory {

    /**
     * 创建新游戏。
     */
    fun createGame(
        boardSize: Size2D,
        beginTime: Date,
        ttnhEnabled: Boolean = false,
        ffnhEnabled: Boolean = false,
        olnhEnabled: Boolean = false
    ): Game = GameImpl(
        BoardImpl(boardSize, emptySet()), HistoryImpl(beginTime), RuleImpl(ttnhEnabled, ffnhEnabled, olnhEnabled)
    )
}