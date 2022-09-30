package org.bymc.gomoku.uifx.view.base

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Point
import java.awt.Rectangle

/**
 * 视图接口。
 *
 * @author: zheng.chez
 * @since: 2022/09/27
 */
interface View {

    /**
     * 获取视图位置和尺寸。
     */
    fun getArea(): Rectangle

    /**
     * 设置视图位置和尺寸。
     */
    fun setArea(area: Rectangle)

    /**
     * 获取视图可见性。
     */
    fun getShowing(): Boolean

    /**
     * 设置视图可见性。
     */
    fun setShowing(showing: Boolean)

    /**
     * 获取视图背景色，null 表示透明。
     */
    fun getBgColor(): Color?

    /**
     * 设定视图背景颜色，null 表示透明。
     */
    fun setBgColor(bgColor: Color?)

    /**
     * 获取可交互性。
     */
    fun getInteractive(): Boolean

    /**
     * 设置可交互性。
     */
    fun setInteractive(interactive: Boolean)

    /**
     * 获取子视图。
     */
    fun getSubViews(): List<View>

    /**
     * 是否包含指定子孙视图。
     */
    fun containDescendantView(descendantView: View): Boolean

    /**
     * 加入子视图，重复加入会抛异常。
     */
    fun appendSubView(subView: View)

    /**
     * 移除子视图，不存在会抛异常。
     */
    fun removeSubView(subView: View)

    /**
     * 获取父视图。
     */
    fun getParentView(): View?

    /**
     * 重绘区域，range 为 null 表示整个视图重绘。
     */
    fun scheduleRender(range: Rectangle? = null)

    /**
     * 渲染。
     */
    fun onRender(g: Graphics, range: Rectangle)

    /**
     * 滑鼠左键按下。
     */
    fun onLButtonPressed(position: Point)

    /**
     * 滑鼠左键释放。
     */
    fun onLButtonReleased(position: Point)

    /**
     * 滑鼠右键按下。
     */
    fun onRButtonPressed(position: Point)

    /**
     * 滑鼠右键释放。
     */
    fun onRButtonReleased(position: Point)

    /**
     * 滑鼠进入视图。
     */
    fun onMouseEntered()

    /**
     * 滑鼠离开视图。
     */
    fun onMouseExited()

    /**
     * 滑鼠在视图上移动。
     */
    fun onMouseMoved(position: Point)

    /**
     * 视图尺寸变化。
     */
    fun onResized(originalSize: Dimension, newSize: Dimension)
}