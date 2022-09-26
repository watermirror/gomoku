package org.bymc.gomoku.model.common.util

import org.bymc.gomoku.model.abstraction.BoardView
import org.bymc.gomoku.model.abstraction.Cell
import org.bymc.gomoku.model.common.param.Drop
import org.bymc.gomoku.model.common.param.Polar
import org.bymc.gomoku.model.common.param.Stone

/**
 * 三三禁手判定器。
 *
 * @author: zheng.chez
 * @since: 2022/09/23
 */
class TtnhVerifier(

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
     * 判定是否三三禁手。
     */
    fun verify(): Boolean {

        // 禁手只针对黑棋。
        if (drop.stone == Stone.WHITE) {
            return false
        }

        // 如果满足五连珠，则不判定三三禁手。
        if (GomokuVerifier(boardView, drop.location).verifyBeforeDrop(drop.stone)) {
            return false
        }

        // 获取测试单元格，统计活三的数量。大于 1 则满足三三禁手判定。
        return countAliveTriple(boardView.getCell(drop.location)) > 1
    }

    /**
     * 根据测试单元格统计活三数量。
     */
    private fun countAliveTriple(testedCell: Cell): Int {

        // 统计连活三和跳活三的数量并求和。
        return countAliveLinkedTriple(testedCell) + countAliveUnlinkedTriple(testedCell)
    }

    /**
     * 统计连活三数量。
     */
    private fun countAliveLinkedTriple(testedCell: Cell): Int {

        return Polar.getNormalizedPolars().fold(0) { acc, polar ->
            acc + countAliveLinkedTriple(testedCell, polar)
        }
    }

    /**
     * 统计跳活三数量。
     */
    private fun countAliveUnlinkedTriple(testedCell: Cell): Int {

        return Polar.getNormalizedPolars().fold(0) { acc, polar ->
            acc + countAliveUnlinkedTriple(testedCell, polar)
        }
    }

    /**
     * 沿指定极轴统计连活三数量，结果非零即一。
     */
    private fun countAliveLinkedTriple(testedCell: Cell, polar: Polar): Int {

        // 近端、远端模式计数。
        val near = DirectedPatternCounter(testedCell, polar.nearEnd, listOf(drop.stone, null)).count()
        val far = DirectedPatternCounter(testedCell, polar.farEnd, listOf(drop.stone, null)).count()

        // 断言。
        require(near.size == 2 && far.size == 2)

        // 条件判定。
        return when {
            near[0] + far[0] != 2 -> 0
            near[1] == 0 || far[1] == 0 -> 0
            near[1] == 1 && far[1] == 1 -> 0
            else -> 1
        }
    }

    /**
     * 沿指定极轴统计跳活三数量，结果非零即一。
     */
    private fun countAliveUnlinkedTriple(testedCell: Cell, polar: Polar): Int {

        // 近端、远端模式计数。
        val near =
            DirectedPatternCounter(testedCell, polar.nearEnd, listOf(null, drop.stone, null, drop.stone, null)).count()
        val far =
            DirectedPatternCounter(testedCell, polar.farEnd, listOf(null, drop.stone, null, drop.stone, null)).count()

        // 断言。
        require(near.size == 5 && far.size == 5)

        // 条件判定。
        return when {
            near[0] > 0 && far[0] == 1 && far[1] == 2 && far[2] > 0 -> 1
            near[0] == 1 && near[1] == 1 && near[2] > 0 && far[0] == 0 && far[1] == 1 && far[2] > 0 -> 1
            near[0] == 0 && near[1] == 1 && near[2] == 1 && near[3] == 1 && near[4] > 0 && far[0] > 0 -> 1
            far[0] > 0 && near[0] == 1 && near[1] == 2 && near[2] > 0 -> 1
            far[0] == 1 && far[1] == 1 && far[2] > 0 && near[0] == 0 && near[1] == 1 && near[2] > 0 -> 1
            far[0] == 0 && far[1] == 1 && far[2] == 1 && far[3] == 1 && far[4] > 0 && near[0] > 0 -> 1
            else -> 0
        }
    }
}