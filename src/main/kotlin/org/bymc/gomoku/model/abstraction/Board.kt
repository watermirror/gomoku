package org.bymc.gomoku.model.abstraction

import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Stone

/**
 * 棋盘接口。
 *
 * @author: zheng.chez
 * @since: 2022/09/22
 */
interface Board : BoardView {

    /**
     * 在指定坐标的单元格放置棋子，若该单元格已经被占用，则用新的棋子替换。坐标始于 (0, 0)。
     */
    fun dropStone(location: Location2D, stone: Stone)

    /**
     * 清除指定坐标单元格的棋子，若该单元格没有棋子，则保持现状。坐标始于 (0, 0)。
     */
    fun cleanCell(location: Location2D)
}