package org.bymc.gomoku.model.common.util

import org.bymc.gomoku.model.abstraction.BoardView
import org.bymc.gomoku.model.abstraction.Cell
import org.bymc.gomoku.model.common.param.Drop
import org.bymc.gomoku.model.common.param.Polar
import org.bymc.gomoku.model.common.param.Stone

/**
 * 长连禁手判定器。
 *
 * @author: zheng.chez
 * @since: 2022/09/25
 */
class OlnhVerifier(

    /**
     * 棋盘视图接口。
     */
    private val boardView: BoardView,

    /**
     * 待判定的落子。
     */
    private val drop: Drop

) {

    // 初始化校验，若落子位置有棋子则抛出异常。
    init { require(!boardView.getCell(drop.location).isOccupied()) }

    /**
     * 判定是否长连禁手。
     */
    fun verify(): Boolean {

        // 禁手只针对黑棋。
        if (drop.stone == Stone.WHITE) {
            return false
        }

        // 统计四个归一化极轴的连棋数量。
        val linkedCountList =
            Polar.getNormalizedPolars().map { countLinkedStones(boardView.getCell(drop.location), it) }

        // 如果存在五连珠的情况，则不禁手。
        linkedCountList.find { it == 4 }?.also { return false }

        // 如果存在五连珠以上的连珠则禁手。
        return linkedCountList.find { it > 4 } != null
    }

    /**
     * 根据单元格坐标和极轴统计连棋子数量。
     */
    private fun countLinkedStones(testedCell: Cell, polar: Polar): Int {

        // 近端、远端模式计数。
        val near = DirectedPatternCounter(testedCell, polar.nearEnd, listOf(drop.stone)).count()
        val far = DirectedPatternCounter(testedCell, polar.farEnd, listOf(drop.stone)).count()

        // 断言。
        require(near.size == 1 && far.size == 1)

        // 两端求和。
        return near[0] + far[0]
    }
}