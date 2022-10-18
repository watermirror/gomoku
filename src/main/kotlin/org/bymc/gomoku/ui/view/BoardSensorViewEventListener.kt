package org.bymc.gomoku.ui.view

import org.bymc.gomoku.model.common.param.Location2D

/**
 * 棋枰传感器事件监听器。
 *
 * @author: zheng.chez
 * @since: 2022/10/18
 */
interface BoardSensorViewEventListener {

    /**
     * 单元格点击事件。
     */
    fun onCellClicked(cellLocation: Location2D)
}