package org.bymc.gomoku.model.impl

import org.bymc.gomoku.model.abstraction.BoardViewModel
import org.bymc.gomoku.model.abstraction.Cell
import org.bymc.gomoku.model.common.param.Direction
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Stone

/**
 * 单元格接口默认实现类。
 *
 * @author: zheng.chez
 * @since: 2022/09/23
 */
data class CellImpl(

    /**
     * 单元格所属的棋枰视图。
     */
    private val boardViewModel: BoardViewModel,

    /**
     * 单元的坐标，始于 (0, 0)。
     */
    private val cellLocation: Location2D,

    /**
     * 占据单元格的棋子，若没有棋子则为 null。
     */
    private val occupiedStone: Stone?

) : Cell {

    /**
     * 获取单元格坐标，坐标始于 (0, 0)。
     */
    override fun getLocation(): Location2D = cellLocation

    /**
     * 单元格是否被棋子占用。
     */
    override fun isOccupied(): Boolean = occupiedStone != null

    /**
     * 占用单元格的棋子。如果单元格未被占用，返回 null。
     */
    override fun getStone(): Stone? = occupiedStone

    /**
     * 获取指定方位的邻近单元格。若该方位没有邻近的单元格则饭后 null。
     */
    override fun getNeighbor(direction: Direction): Cell? {

        // 计算邻近单元格坐标。
        val targetLocation = cellLocation.copy(
            x = cellLocation.x + direction.offsetX, y = cellLocation.y + direction.offsetY
        )

        // 检测越界。
        if (targetLocation.x !in 0 until boardViewModel.getSize().width) return null
        if (targetLocation.y !in 0 until boardViewModel.getSize().height) return null

        // 查询目标单元格并返回。
        return boardViewModel.getCell(targetLocation)
    }

    /**
     * 获取指定方位的邻近单元格列表。
     */
    override fun getNeighbors(direction: Direction): List<Cell> {

        val neighbors = ArrayList<Cell>()
        var cell = getNeighbor(direction)
        while (cell != null) {
            neighbors.add(cell)
            cell = cell.getNeighbor(direction)
        }
        return neighbors
    }
}