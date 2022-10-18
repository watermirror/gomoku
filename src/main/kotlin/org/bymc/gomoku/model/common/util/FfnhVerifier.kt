package org.bymc.gomoku.model.common.util

import org.bymc.gomoku.model.abstraction.BoardViewModel
import org.bymc.gomoku.model.abstraction.Cell
import org.bymc.gomoku.model.common.param.Drop
import org.bymc.gomoku.model.common.param.Polar
import org.bymc.gomoku.model.common.param.Stone

/**
 * 四四禁手判定器。
 *
 * @author: zheng.chez
 * @since: 2022/09/25
 */
class FfnhVerifier(

    /**
     * 棋枰视图接口。
     */
    private val boardViewModel: BoardViewModel,

    /**
     * 待判定的落子。
     */
    private val drop: Drop

) {

    // 初始化校验，若落子位置有棋子则抛出异常。
    init { require(!boardViewModel.getCell(drop.location).isOccupied()) }

    /**
     * 判定是否四四禁手。
     */
    fun verify(): Boolean {

        // 禁手只针对黑棋。
        if (drop.stone == Stone.WHITE) {
            return false
        }

        // 如果满足五连珠，则不判定三三禁手。
        if (GomokuVerifier(boardViewModel, drop.location).verifyBeforeDrop(drop.stone)) {
            return false
        }

        // 获取测试单元格，统计活四和冲四的数量。大于 1 则满足四四禁手判定。
        val testedCell = boardViewModel.getCell(drop.location)
        return countAliveTetrad(testedCell) + countHalfAliveTetrad(testedCell) > 1
    }

    /**
     * 根据单元格坐标统计活四的数量。
     */
    private fun countAliveTetrad(testedCell: Cell): Int {

        return Polar.getNormalizedPolars().fold(0) { acc, polar ->
            acc + countAliveTetrad(testedCell, polar)
        }
    }

    /**
     * 根据单元格坐标统计冲四的数量。
     */
    private fun countHalfAliveTetrad(testedCell: Cell): Int {

        return Polar.getNormalizedPolars().fold(0) { acc, polar ->
            acc + countHalfAliveTetrad(testedCell, polar)
        }
    }

    /**
     * 根据单元格坐标和极轴判定活四数量，返回值非零即一。
     */
    private fun countAliveTetrad(testedCell: Cell, polar: Polar): Int {

        // 近端、远端模式计数。
        val near = DirectedPatternCounter(testedCell, polar.nearEnd, listOf(drop.stone, null)).count()
        val far = DirectedPatternCounter(testedCell, polar.farEnd, listOf(drop.stone, null)).count()

        // 断言。
        require(near.size == 2 && far.size == 2)

        // 条件判定。
        return when {
            near[0] + far[0] != 3 -> 0
            near[1] == 0 || far[1] == 0 -> 0
            else -> 1
        }
    }

    /**
     * 根据单元格坐标和极轴判定冲四数量，返回值非零即一。
     */
    private fun countHalfAliveTetrad(testedCell: Cell, polar: Polar): Int {

        // 若存在连冲四或跳冲四则认为存在一个冲四。
        return when {
            countLinkedHalfAliveTetrad(testedCell, polar) + countUnlinkedHalfAliveTetrad(testedCell, polar) > 0 -> 1
            else -> 0
        }
    }

    /**
     * 根据单元格坐标和极轴判定连冲四数量，返回值非零即一。
     */
    private fun countLinkedHalfAliveTetrad(testedCell: Cell, polar: Polar): Int {

        // 近端、远端模式计数。
        val near = DirectedPatternCounter(testedCell, polar.nearEnd, listOf(drop.stone, null)).count()
        val far = DirectedPatternCounter(testedCell, polar.farEnd, listOf(drop.stone, null)).count()

        // 断言。
        require(near.size == 2 && far.size == 2)

        // 条件判定。
        return when {
            near[0] + far[0] != 3 -> 0
            near[1] == 0 && far[1] == 0 -> 0
            near[1] != 0 && far[1] != 0 -> 0
            else -> 1
        }
    }

    /**
     * 根据单元格坐标和极轴判定跳冲四数量，返回值非零即一。
     */
    private fun countUnlinkedHalfAliveTetrad(testedCell: Cell, polar: Polar): Int {

        // 近端、远端模式计数。
        val near = DirectedPatternCounter(testedCell, polar.nearEnd, listOf(drop.stone, null, drop.stone)).count()
        val far = DirectedPatternCounter(testedCell, polar.farEnd, listOf(drop.stone, null, drop.stone)).count()

        // 断言。
        require(near.size == 3 && far.size == 3)

        // 条件判定。
        return when {
            near[0] == 0 && far[0] == 0 && far[1] == 1 && far[2] == 3 -> 1
            near[0] == 0 && near[1] == 1 && near[2] == 1 && far[0] == 2 -> 1
            near[0] == 1 && near[1] == 1 && near[2] == 1 && far[0] == 1 -> 1
            near[0] == 2 && near[1] == 1 && near[2] == 1 && far[0] == 0 -> 1
            far[0] == 0 && near[0] == 0 && near[1] == 1 && near[2] == 3 -> 1
            far[0] == 0 && far[1] == 1 && far[2] == 1 && near[0] == 2 -> 1
            far[0] == 1 && far[1] == 1 && far[2] == 1 && near[0] == 1 -> 1
            far[0] == 2 && far[1] == 1 && far[2] == 1 && near[0] == 0 -> 1
            else -> 0
        }
    }
}