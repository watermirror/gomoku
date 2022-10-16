package org.bymc.gomoku.ui.view

import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.uifx.view.base.ViewBase
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.awt.Rectangle

/**
 * 棋枰视图。
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
    )

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
         * 星位半径。
         */
        private const val startRadius: Int = 3
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
}