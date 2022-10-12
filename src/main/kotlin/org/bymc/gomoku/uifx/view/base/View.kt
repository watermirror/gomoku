package org.bymc.gomoku.uifx.view.base

import java.awt.*

/**
 * 视图接口。
 *
 * @author: zheng.chez
 * @since: 2022/09/27
 */
interface View : MouseEventHandler {

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
    fun addSubView(subView: View)

    /**
     * 在锚点视图之后插入视图。
     */
    fun addSubViewAfter(subView: View, anchor: View)

    /**
     * 在锚点视图之前插入视图。
     */
    fun addSubViewBefore(subView: View, anchor: View)

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
     * 移动事件。
     */
    fun onMoved(originalPosition: Point, newPosition: Point)

    /**
     * 视图尺寸变化。
     */
    fun onResized(originalSize: Dimension, newSize: Dimension)

    /**
     * 显示事件。
     */
    fun onVisible()

    /**
     * 隐藏事件。
     */
    fun onHidden()
}