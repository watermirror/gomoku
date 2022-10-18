package org.bymc.gomoku.model.impl

import org.bymc.gomoku.model.abstraction.Board
import org.bymc.gomoku.model.abstraction.Cell
import org.bymc.gomoku.model.common.param.Drop
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Size2D
import org.bymc.gomoku.model.common.param.Stone

/**
 * 棋枰接口实现类。
 *
 * @author: zheng.chez
 * @since: 2022/09/23
 */
class BoardImpl(

    /**
     * 棋枰尺寸。
     */
    private val boardSize: Size2D,

    /**
     * 初始化棋子，若棋子坐标超出棋枰范围，将抛出异常。
     */
    initialDrops: Set<Drop>

) : Board {

    /**
     * 棋子映射。该映射代表存在与棋枰特定坐标单元格的棋子。
     */
    private val stones = HashMap<Location2D, Stone>()

    /**
     * 初始化棋子映射。
     */
    init {

        initialDrops.forEach {

            // 检查越界。
            verifyLocation(it.location)

            // 记录棋子。
            stones[it.location] = it.stone
        }
    }

    /**
     * 在指定坐标的单元格放置棋子，若该单元格已经被占用，则用新的棋子替换。坐标始于 (0, 0)。
     */
    override fun dropStone(location: Location2D, stone: Stone) {

        // 检查越界。
        verifyLocation(location)

        // 记录棋子。
        stones[location] = stone
    }

    /**
     * 清除指定坐标单元格的棋子，若该单元格没有棋子，则保持现状。坐标始于 (0, 0)。
     */
    override fun cleanCell(location: Location2D) {

        // 检查越界。
        verifyLocation(location)

        // 清除。
        stones.remove(location)
    }

    /**
     * 获取棋枰尺寸。
     */
    override fun getSize(): Size2D = boardSize

    /**
     * 获取单元格，location 坐标始于 (0, 0)。若 location 超出棋枰范围，将导致异常。
     */
    override fun getCell(location: Location2D): Cell {

        // 检查越界。
        verifyLocation(location)

        // 组装单元格对象并返回。
        return CellImpl(this, location, stones[location])
    }

    /**
     * 获取棋子数量。
     */
    override fun getStoneCount(): Int = stones.size

    /**
     * 检查越界。
     */
    private fun verifyLocation(location: Location2D) {

        if (location.x !in 0 until boardSize.width || location.y !in 0 until boardSize.height) {
            throw RuntimeException("location is out of board")
        }
    }
}