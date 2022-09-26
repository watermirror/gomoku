package org.bymc.gomoku.model.common.util

import org.bymc.gomoku.model.abstraction.BoardView
import org.bymc.gomoku.model.abstraction.Cell
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Size2D
import org.bymc.gomoku.model.common.param.Stone
import org.bymc.gomoku.model.impl.CellImpl

/**
 * 测试棋盘视图类。
 *
 * @author: zheng.chez
 * @since: 2022/09/27
 */
class TestBoardView(

    /**
     * 棋盘尺寸。
     */
    private val size: Size2D,

    /**
     * 棋盘内容，从 (0, 0), (1, 0) ... (0, 1), (1, 1) 逐行扫描。x 表示黑棋，o 表示白棋，. 表示空。空白字符忽略。
     */
    private val content: String

) : BoardView {

    init {
        val len = content.replace(" ", "").replace("\n", "").replace("\r", "").replace("\t", "").length
        require(len == size.width * size.height)
    }

    /**
     * 获取棋盘尺寸。
     */
    override fun getSize(): Size2D = size

    /**
     * 获取单元格，location 坐标始于 (0, 0)。若 location 超出棋盘范围，将导致异常。
     */
    override fun getCell(location: Location2D): Cell {

        require(location.x in 0 until size.width)
        require(location.y in 0 until size.height)

        val normalizedContent = content.replace(" ", "").replace("\n", "").replace("\r", "").replace("\t", "")
        return CellImpl(
            this,
            location,
            when (normalizedContent[location.y * size.width + location.x]) {
                'x' -> Stone.BLACK
                'o' -> Stone.WHITE
                else -> null
            }
        )
    }

    /**
     * 获取棋子数量。
     */
    override fun getStoneCount(): Int = content.count { it == 'x' || it == 'o' }
}