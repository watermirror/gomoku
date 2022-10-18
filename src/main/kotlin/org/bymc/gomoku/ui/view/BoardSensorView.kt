package org.bymc.gomoku.ui.view

import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.uifx.view.base.ButtonViewBase
import org.bymc.gomoku.uifx.view.base.ButtonViewEventListener
import org.bymc.gomoku.uifx.view.base.ViewBase
import java.awt.Point
import java.awt.Rectangle

/**
 * 棋枰传感器视图。
 *
 * @author: zheng.chez
 * @since: 2022/10/18
 */
class BoardSensorView(

    /**
     * 棋枰视图的区域。
     */
    area: Rectangle,

    /**
     * 感应器视图可见性。
     */
    showing: Boolean,

    /**
     * 棋枰绘制的原点。
     */
    boardOriginalPoint: Point,

    /**
     * 单元格尺寸。
     */
    cellSize: Int,

    /**
     * 棋枰每边容量，即横纵各含有的格子（交叉点）数量。
     */
    edgeCapacity: Int = 15

) : ViewBase(area, showing), ButtonViewEventListener {

    /**
     * 事件监听器列表。
     */
    private val listeners: MutableList<BoardSensorViewEventListener> = ArrayList()

    /**
     * 初始化单元格传感器按钮。
     */
    init {

        for (x in 0 until edgeCapacity) {
            for (y in 0 until edgeCapacity) {
                val button = CellSensorButtonView(
                    Rectangle(
                        boardOriginalPoint.x + cellSize * x, boardOriginalPoint.y + cellSize * y, cellSize, cellSize
                    ),
                    Location2D(x, y)
                )
                addSubView(button)
                button.addEventListener(this)
            }
        }
    }

    /**
     * 添加监听器。
     */
    fun addListener(listener: BoardSensorViewEventListener) {

        if (listeners.contains(listener)) { return }
        listeners.add(listener)
    }

    /**
     * 移除监听器。
     */
    fun removeListener(listener: BoardSensorViewEventListener) {

        val index = listeners.indexOf(listener)
        if (index < 0) { return }
        listeners.removeAt(index)
    }

    /**
     * 按钮点击事件。
     */
    override fun onClicked(sender: ButtonViewBase) {

        val cellButton = sender as? CellSensorButtonView ?: return
        listeners.forEach { it.onCellClicked(cellButton.location) }
    }
}