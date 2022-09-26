package org.bymc.gomoku.model.common.util

import org.bymc.gomoku.model.abstraction.Cell
import org.bymc.gomoku.model.common.param.Direction
import org.bymc.gomoku.model.common.param.Stone

/**
 * 指向性模式计数器。
 *
 * @author: zheng.chez
 * @since: 2022/09/23
 */
class DirectedPatternCounter(

    /**
     * 启动坐标。启动坐标不计数。
     */
    private val originalCell: Cell,

    /**
     * 匹配计数方向。
     */
    private val direction: Direction,

    /**
     * 计数模式，即单元格内容类型的匹配顺序。null 表示匹配空单元格。
     */
    private val countPattern: List<Stone?>

) {

    /**
     * 校验模式合法性。
     */
    init {

        // 不允许模式为空。
        if (countPattern.isEmpty()) {
            throw RuntimeException("illegal count pattern")
        }

        // 不允许相邻匹配相同。
        if (countPattern.size > 1) {
            var previous = countPattern[0]
            for (i in 1 until countPattern.size) {
                if (countPattern[i] == previous) {
                    throw RuntimeException("illegal count pattern")
                }
                previous = countPattern[i]
            }
        }
    }

    /**
     * 计数。
     */
    fun count(): List<Int> {

        require(countPattern.isNotEmpty())

        val countList = ArrayList<Int>()
        var patternIndex = 0
        var tempCount = 0

        val cells = listOf(*originalCell.getNeighbors(direction).toTypedArray(), null)
        var cellIndex = 0
        while (cellIndex < cells.size) {
            val cell = cells[cellIndex]
            if (cell == null) {
                countList.add(tempCount)
                break
            }
            if (patternIndex >= countPattern.size) {
                break
            }
            if (cell.getStone() != countPattern[patternIndex]) {
                countList.add(tempCount)
                patternIndex++
                tempCount = 0
                continue
            }
            tempCount++
            cellIndex++
        }

        for (i in countList.size until countPattern.size) {
            countList.add(0)
        }

        return countList
    }
}