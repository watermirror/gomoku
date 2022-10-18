package org.bymc.gomoku.ui.view

import org.bymc.gomoku.model.abstraction.BoardViewModel
import org.bymc.gomoku.model.abstraction.HistoryViewModel
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Stone
import org.bymc.gomoku.uifx.view.base.ViewBase
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.awt.Rectangle
import kotlin.math.max

/**
 * 棋枰格子视图。
 *
 * @author: zheng.chez
 * @since: 2022/10/16
 */
class BoardGridView(

    /**
     * 棋枰视图的区域。
     */
    area: Rectangle,

    /**
     * 棋枰绘制的原点。
     */
    private val boardOriginalPoint: Point,

    /**
     * 单元格尺寸。
     */
    private val cellSize: Int,

    /**
     * 棋枰每边容量，即横纵各含有的格子（交叉点）数量。
     */
    private val edgeCapacity: Int = 15,

    /**
     * 星位坐标。
     */
    private val starLocations: List<Location2D> = listOf(
        Location2D(3, 3), Location2D(7, 3), Location2D(11, 3),
        Location2D(3, 7), Location2D(7, 7), Location2D(11, 7),
        Location2D(3, 11), Location2D(7, 11), Location2D(11, 11)
    ),

    /**
     * 棋枰视图模型对象。
     */
    private var boardViewModel: BoardViewModel? = null,

    /**
     * 棋局历史视图模型对象。
     */
    private var historyViewModel: HistoryViewModel? = null

) : ViewBase(area, true, Color.WHITE) {

    companion object {

        /**
         * 边框宽度。
         */
        private const val borderWidth: Int = 2

        /**
         * 边框与格子的间距。
         */
        private const val borderMargin: Int = 3

        /**
         * 星位半径比例。
         */
        private const val startRadiusRatio: Double = 3.0 / 40.0

        /**
         * 棋子内嵌尺寸比例。
         */
        private const val stonePaddingRatio: Double = 1.0 / 15.0

        /**
         * 最后落点标记的尺寸比例。
         */
        private const val lastMarkSizeRatio: Double = 0.8 / 3.0
    }

    init {

        // 校验视图模型的尺寸。
        if (boardViewModel != null &&
            (boardViewModel!!.getSize().width != edgeCapacity || boardViewModel!!.getSize().height != edgeCapacity)) {
            throw RuntimeException("init: boardViewModel.getSize() does not match edgeCapacity")
        }
    }

    /**
     * 设置棋枰视图模型。
     */
    fun setBoardViewModel(model: BoardViewModel?) {

        if (model == boardViewModel) { return }

        // 校验视图模型的尺寸。
        if (model != null && (model.getSize().width != edgeCapacity || model.getSize().height != edgeCapacity)) {
            throw RuntimeException("setBoardViewModel: model.getSize() does not match edgeCapacity")
        }

        boardViewModel = model
        scheduleRender()
    }

    /**
     * 设置历史记录对象。
     */
    fun setHistoryViewModel(model: HistoryViewModel?) {

        if (model == historyViewModel) { return }
        historyViewModel = model
        scheduleRender()
    }

    /**
     * 渲染。
     */
    override fun onRender(g: Graphics, range: Rectangle) {

        // 绘制静态棋枰。
        drawStaticBoard(g)
    }

    /**
     * 绘制静态棋枰。
     */
    private fun drawStaticBoard(g: Graphics) {

        val originalColor = g.color
        g.color = Color.BLACK

        drawStaticGrid(g)
        drawStaticBorder(g)
        drawStaticStarts(g)
        drawStones(g)
        drawLastDrop(g)

        g.color = originalColor
    }

    /**
     * 绘制静态棋枰格。
     */
    private fun drawStaticGrid(g: Graphics) {

        val lineLength = cellSize * (edgeCapacity - 1)
        for (i in 0 until edgeCapacity) {
            g.drawLine(
                boardOriginalPoint.x + (cellSize / 2),
                boardOriginalPoint.y + i * cellSize + (cellSize / 2),
                boardOriginalPoint.x + (cellSize / 2) + lineLength,
                boardOriginalPoint.y + i * cellSize + (cellSize / 2)
            )
            g.drawLine(
                boardOriginalPoint.x + i * cellSize + (cellSize / 2),
                boardOriginalPoint.y + (cellSize / 2),
                boardOriginalPoint.x + i * cellSize + (cellSize / 2),
                boardOriginalPoint.y + (cellSize / 2) + lineLength
            )
        }
    }

    /**
     * 绘制静态棋枰边框。
     */
    private fun drawStaticBorder(g: Graphics) {

        val lineLength = cellSize * (edgeCapacity - 1)
        for (i in 0 until borderWidth) {
            g.drawLine(
                boardOriginalPoint.x + (cellSize / 2) - borderMargin - i,
                boardOriginalPoint.y + (cellSize / 2) - borderMargin - i,
                boardOriginalPoint.x + (cellSize / 2) + lineLength + borderMargin + i,
                boardOriginalPoint.y + (cellSize / 2) - borderMargin - i
            )
            g.drawLine(
                boardOriginalPoint.x + (cellSize / 2) - borderMargin - i,
                boardOriginalPoint.y + (cellSize / 2) + lineLength + borderMargin + i,
                boardOriginalPoint.x + (cellSize / 2) + lineLength + borderMargin + i,
                boardOriginalPoint.y + (cellSize / 2) + lineLength + borderMargin + i
            )
            g.drawLine(
                boardOriginalPoint.x + (cellSize / 2) - borderMargin - i,
                boardOriginalPoint.y + (cellSize / 2) - borderMargin - i,
                boardOriginalPoint.x + (cellSize / 2) - borderMargin - i,
                boardOriginalPoint.y + (cellSize / 2) + lineLength + borderMargin + i
            )
            g.drawLine(
                boardOriginalPoint.x + (cellSize / 2) + lineLength + borderMargin + i,
                boardOriginalPoint.y + (cellSize / 2) - borderMargin - i,
                boardOriginalPoint.x + (cellSize / 2) + lineLength + borderMargin + i,
                boardOriginalPoint.y + (cellSize / 2) + lineLength + borderMargin + i
            )
        }
    }

    /**
     * 绘制静态星位。
     */
    private fun drawStaticStarts(g: Graphics) {

        val startRadius = (cellSize * startRadiusRatio).toInt()

        for (location in starLocations) {

            g.drawOval(
                boardOriginalPoint.x + (cellSize / 2) + location.x * cellSize - startRadius,
                boardOriginalPoint.y + (cellSize / 2) + location.y * cellSize - startRadius,
                startRadius * 2,
                startRadius * 2
            )
            g.fillOval(
                boardOriginalPoint.x + (cellSize / 2) + location.x * cellSize - startRadius,
                boardOriginalPoint.y + (cellSize / 2) + location.y * cellSize - startRadius,
                startRadius * 2,
                startRadius * 2
            )
        }
    }

    /**
     * 绘制所有棋子。
     */
    private fun drawStones(g: Graphics) {

        val model = boardViewModel ?: return

        for (x in 0 until model.getSize().width) {
            for (y in 0 until model.getSize().height) {
                mayDrawStone(g, x, y)
            }
        }
    }

    /**
     * 若指定单元格存在棋子则绘制。
     */
    private fun mayDrawStone(g: Graphics, x: Int, y: Int) {

        val model = boardViewModel ?: return

        val cell = model.getCell(Location2D(x, y))
        if (!cell.isOccupied()) { return }
        val stonePadding = (cellSize * stonePaddingRatio).toInt()

        g.color = when (cell.getStone()) {
            Stone.BLACK -> Color.BLACK
            Stone.WHITE -> Color.WHITE
            else -> throw RuntimeException("mayDrawStone: illegal stone appears")
        }
        g.fillOval(
            boardOriginalPoint.x + cellSize * x + stonePadding,
            boardOriginalPoint.y + cellSize * y + stonePadding,
            cellSize - 2 * stonePadding,
            cellSize - 2 * stonePadding
        )

        g.color = Color.BLACK
        g.drawOval(
            boardOriginalPoint.x + cellSize * x + stonePadding,
            boardOriginalPoint.y + cellSize * y + stonePadding,
            cellSize - 2 * stonePadding,
            cellSize - 2 * stonePadding
        )
    }

    /**
     * 绘制最后落点。
     */
    private fun drawLastDrop(g: Graphics) {

        if (historyViewModel == null) { return }
        val record = historyViewModel!!.getLastRecord() ?: return

        g.color = when (record.getDrop().stone) {
            Stone.BLACK -> Color.WHITE
            Stone.WHITE -> Color.BLACK
        }

        val markSize = max(3.0, cellSize * lastMarkSizeRatio).toInt()
        val halfRemainingSize = (cellSize - markSize) / 2
        val realMarkSize = cellSize - halfRemainingSize * 2

        g.drawRect(
            boardOriginalPoint.x + cellSize * record.getDrop().location.x + halfRemainingSize,
            boardOriginalPoint.y + cellSize * record.getDrop().location.y + halfRemainingSize,
            realMarkSize,
            realMarkSize
        )
        g.fillRect(
            boardOriginalPoint.x + cellSize * record.getDrop().location.x + halfRemainingSize,
            boardOriginalPoint.y + cellSize * record.getDrop().location.y + halfRemainingSize,
            realMarkSize,
            realMarkSize
        )
    }
}