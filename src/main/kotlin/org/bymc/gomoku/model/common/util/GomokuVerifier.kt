package org.bymc.gomoku.model.common.util

import org.bymc.gomoku.model.abstraction.BoardViewModel
import org.bymc.gomoku.model.abstraction.Cell
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Polar
import org.bymc.gomoku.model.common.param.Stone

/**
 * 五连珠判定器。
 *
 * @author: zheng.chez
 * @since: 2022/09/25
 */
class GomokuVerifier(

    /**
     * 棋枰视图接口。
     */
    private val boardViewModel: BoardViewModel,

    /**
     * 最后落子坐标或即将落子坐标。
     */
    private val dropLocation: Location2D

) {

    /**
     * 判定当下是否五连珠。
     */
    fun verify(): Boolean {

        // 获取单元格、断言
        val cell = boardViewModel.getCell(dropLocation)
        require(cell.getStone() != null)

        // 统计四个归一化极轴上的连棋数量。
        val linkedCountList = Polar.getNormalizedPolars().map { countLinkedStones(cell, cell.getStone()!!, it) }

        // 任意一个极轴上的连棋数量达到 5 即判定为五连珠。
        return linkedCountList.find { it >= 5 } != null
    }

    /**
     * 判定落子后是否五连珠。
     */
    fun verifyBeforeDrop(dropStone: Stone): Boolean {

        // 获取单元格、断言
        val cell = boardViewModel.getCell(dropLocation)
        require(cell.getStone() == null)

        // 统计四个归一化极轴上的连棋数量。
        val linkedCountList = Polar.getNormalizedPolars().map { countLinkedStones(cell, dropStone, it) }

        // 任意一个极轴上的连棋数量达到 5 即判定为五连珠。
        return linkedCountList.find { it >= 5 } != null
    }

    /**
     * 根据单元格和极轴统计连棋数量（含测试坐标的棋子）。
     */
    private fun countLinkedStones(cell: Cell, stone: Stone, polar: Polar): Int {

        // 近端、远端模式计数。
        val near = DirectedPatternCounter(cell, polar.nearEnd, listOf(stone)).count()
        val far = DirectedPatternCounter(cell, polar.farEnd, listOf(stone)).count()

        // 断言。
        require(near.size == 1 && far.size == 1)

        // 求和。
        return near[0] + far[0] + 1
    }
}