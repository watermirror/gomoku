package org.bymc.gomoku.ui.view

import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.uifx.view.base.ButtonViewBase
import org.bymc.gomoku.uifx.view.mouse.MouseInteractiveViewDisplayState
import java.awt.Graphics
import java.awt.Rectangle

/**
 * 单元格传感器按钮。
 *
 * @author: zheng.chez
 * @since: 2022/10/18
 */
class CellSensorButtonView(

    /**
     * 传感器按钮区域。
     */
    area: Rectangle,

    /**
     * 单元格坐标。
     */
    val location: Location2D

) : ButtonViewBase(area) {

    /**
     * 根据显示状态进行绘制。
     */
    override fun renderForState(g: Graphics, state: MouseInteractiveViewDisplayState) {}

    /**
     * 绘制 Disabled 状态。
     */
    override fun renderOnDisabled(g: Graphics) {}
}