package org.bymc.gomoku.model.abstraction

import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Size2D

/**
 * 棋枰视图模型接口。
 *
 * @author: zheng.chez
 * @since: 2022/09/22
 */
interface BoardViewModel {

    /**
     * 获取棋枰尺寸。
     */
    fun getSize(): Size2D

    /**
     * 获取单元格，location 坐标始于 (0, 0)。若 location 超出棋枰范围，将导致异常。
     */
    fun getCell(location: Location2D): Cell

    /**
     * 获取棋子数量。
     */
    fun getStoneCount(): Int
}