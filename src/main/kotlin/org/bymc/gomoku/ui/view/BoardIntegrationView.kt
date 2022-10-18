package org.bymc.gomoku.ui.view

import org.bymc.gomoku.model.abstraction.BoardViewModel
import org.bymc.gomoku.model.abstraction.HistoryViewModel
import org.bymc.gomoku.uifx.view.base.ViewBase
import java.awt.Point
import java.awt.Rectangle

/**
 * 棋枰集成视图。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
class BoardIntegrationView(

    /**
     * 视图区域。
     */
    area: Rectangle,

    /**
     * 单元格尺寸。
     */
    cellSize: Int,

    /**
     * 操作是否开启。
     */
    private var enabled: Boolean

) : ViewBase(area) {

    /**
     * 棋枰格子视图。
     */
    private val gridView: BoardGridView = BoardGridView(
        Rectangle(0, 0, area.width, area.height), Point(0, 0), cellSize
    )

    /**
     * 棋枰传感器视图。
     */
    private val sensorView: BoardSensorView = BoardSensorView(
        Rectangle(0, 0, area.width, area.height), enabled, Point(0, 0), cellSize
    )

    /**
     * 配置子视图。
     */
    init {

        addSubView(gridView)
        addSubView(sensorView)
    }

    /**
     * 获取视图是否启用了操作能力。
     */
    fun isEnabled(): Boolean = enabled

    /**
     * 启用操作。
     */
    fun enable() {

        if (enabled) { return }
        enabled = true
        sensorView.setShowing(true)
    }

    /**
     * 禁用操作。
     */
    fun disable() {

        if (!enabled) { return }
        enabled = false
        sensorView.setShowing(false)
    }

    /**
     * 设置视图模型。
     */
    fun setViewModels(boardViewModel: BoardViewModel?, historyViewModel: HistoryViewModel?) {

        gridView.setBoardViewModel(boardViewModel)
        gridView.setHistoryViewModel(historyViewModel)
    }

    /**
     * 添加传感器事件监听器。
     */
    fun addSensorEventListener(listener: BoardSensorViewEventListener) = sensorView.addListener(listener)

    /**
     * 移除传感器事件监听器。
     */
    fun removeSensorEventListener(listener: BoardSensorViewEventListener) = sensorView.removeListener(listener)
}