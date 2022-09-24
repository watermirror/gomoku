package org.bymc.gomoku.model.abstraction

import org.bymc.gomoku.model.common.param.Direction
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Stone

/**
 * 单元格接口。
 *
 * @author: zheng.chez
 * @since: 2022/09/22
 */
interface Cell {

    /**
     * 获取单元格坐标，坐标始于 (0, 0)。
     */
    fun getLocation(): Location2D

    /**
     * 单元格是否被棋子占用。
     */
    fun isOccupied(): Boolean

    /**
     * 占用单元格的棋子。如果单元格未被占用，返回 null。
     */
    fun getStone(): Stone?

    /**
     * 获取指定方位的邻近单元格。若该方位没有邻近的单元格则返回 null。
     */
    fun getNeighbor(direction: Direction): Cell?

    /**
     * 获取指定方位的邻近单元格列表。
     */
    fun getNeighbors(direction: Direction): List<Cell>
}