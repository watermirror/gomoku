package org.bymc.gomoku.uifx.view.base

import java.awt.*

/**
 * 视图基类。
 *
 * @author: zheng.chez
 * @since: 2022/09/27
 */
abstract class ViewBase(

    /**
     * 视图坐标与尺寸。
     */
    private var theArea: Rectangle = Rectangle(0, 0, 0, 0),

    /**
     * 可见性。
     */
    private var theShowing: Boolean = true,

    /**
     * 背景色。
     */
    private var theBgColor: Color? = null,

    /**
     * 可交互性。
     */
    private var theInteractive: Boolean = true

) : View, SubView {

    /**
     * 子视图。
     */
    private val subViews = ArrayList<View>()

    /**
     * 父视图。
     */
    private var parentView: View? = null

    /**
     * 滑鼠事件处理器列表。
     */
    private val mouseEventHandlers: MutableList<MouseEventHandler> = ArrayList()

    /**
     * 添加滑鼠事件处理器。
     */
    fun addMouseEventHandler(handler: MouseEventHandler) {

        if (mouseEventHandlers.contains(handler)) {
            return
        }
        mouseEventHandlers.add(handler)
    }

    /**
     * 移除滑鼠事件处理器。
     */
    fun removeMouseEventHandler(handler: MouseEventHandler) {

        val index = mouseEventHandlers.indexOf(handler)
        if (index < 0) {
            return
        }
        mouseEventHandlers.removeAt(index)
    }

    /**
     * 获取视图位置和尺寸。
     */
    final override fun getArea(): Rectangle = theArea

    /**
     * 设置视图位置和尺寸。
     */
    final override fun setArea(area: Rectangle) {

        // 没有改变视图则不用处理。
        if (theArea == area) {
            return
        }

        // 更新范围。
        val originalArea = theArea
        theArea = area

        // 触发 Resize 事件。
        if (originalArea.size != area.size) {
            onResized(originalArea.size, area.size)
        }

        // 调度渲染。
        if (getParentView() == null || !getParentView()!!.getShowing()) {
            return
        }
        val targetArea = Rectangle()
        Rectangle.union(originalArea, area, targetArea)
        getParentView()!!.scheduleRender(targetArea)
    }

    /**
     * 获取视图可见性。
     */
    final override fun getShowing(): Boolean = theShowing

    /**
     * 设置视图可见性。
     */
    final override fun setShowing(showing: Boolean) {

        // 没有变化，不做处理。
        if (theShowing == showing) {
            return
        }

        // 更新可见性。
        theShowing = showing

        // 调度渲染。
        scheduleRender()
    }

    /**
     * 获取视图背景色，null 表示透明。
     */
    final override fun getBgColor(): Color? = theBgColor

    /**
     * 设定视图背景颜色，null 表示透明。
     */
    final override fun setBgColor(bgColor: Color?) {

        // 没有变化，不做处理。
        if (theBgColor == bgColor) {
            return
        }

        // 更新背景色。
        theBgColor = bgColor

        // 调度渲染。
        scheduleRender()
    }

    /**
     * 获取可交互性。
     */
    final override fun getInteractive(): Boolean = theInteractive

    /**
     * 设置可交互性。
     */
    final override fun setInteractive(interactive: Boolean) {

        theInteractive = interactive
    }

    /**
     * 获取子视图。
     */
    final override fun getSubViews(): List<View> = subViews

    /**
     * 是否包含指定子孙视图。
     */
    final override fun containDescendantView(descendantView: View): Boolean {

        return subViews.find { it == descendantView || it.containDescendantView(descendantView) } != null
    }

    /**
     * 加入子视图，重复加入会抛异常。
     */
    final override fun appendSubView(subView: View) {

        require(subView != this)
        require(subView as? SubView != null)
        if (containDescendantView(subView)) {
            throw RuntimeException("trying to append a duplicated descendant view")
        }

        subViews.add(subView)
        (subView as? SubView)?.setParent(this)

        if (subView.getShowing()) {
            scheduleRender(subView.getArea())
        }
    }

    /**
     * 移除子视图，不存在会抛异常。
     */
    final override fun removeSubView(subView: View) {

        val index = subViews.indexOf(subView)
        if (index < 0) {
            throw RuntimeException("trying to remove a nonexistent sub view")
        }

        (subView as? SubView)?.setParent(null)
        subViews.removeAt(index)

        if (subView.getShowing()) {
            scheduleRender(subView.getArea())
        }
    }

    /**
     * 获取父视图。
     */
    final override fun getParentView(): View? = parentView

    /**
     * 重绘区域，range 为 null 表示整个视图重绘。
     */
    final override fun scheduleRender(range: Rectangle?) {

        // 无父视图或父视图不可见，则不做处理。
        if (!getShowing() || getParentView() == null || !getParentView()!!.getShowing()) {
            return
        }

        // 调度重会父视图中本视图占据的区域。
        if (range == null) {
            return getParentView()!!.scheduleRender(getArea())
        }

        // 求交集重绘制。
        val translated = Rectangle(range).also { it.location = Point(getArea().location) }
        val targetRange = Rectangle()
        Rectangle.intersect(getArea(), translated, targetRange)
        Rectangle.intersect(
            targetRange,
            Rectangle(0, 0, getParentView()!!.getArea().width, getParentView()!!.getArea().height),
            targetRange
        )
        if (targetRange.width != 0 && targetRange.height != 0) {
            getParentView()!!.scheduleRender(targetRange)
        }
    }

    /**
     * 渲染。
     */
    override fun onRender(g: Graphics, range: Rectangle) {}

    /**
     * 视图尺寸变化。
     */
    override fun onResized(originalSize: Dimension, newSize: Dimension) {}

    /**
     * 滑鼠左键按下。
     */
    override fun onLButtonPressed(position: Point, pressedCount: Int) =
        mouseEventHandlers.forEach { it.onLButtonPressed(position, pressedCount) }

    /**
     * 滑鼠左键释放。
     */
    override fun onLButtonReleased(position: Point) = mouseEventHandlers.forEach { it.onLButtonReleased(position) }

    /**
     * 滑鼠右键按下。
     */
    override fun onRButtonPressed(position: Point, pressedCount: Int) =
        mouseEventHandlers.forEach { it.onRButtonPressed(position, pressedCount) }

    /**
     * 滑鼠右键释放。
     */
    override fun onRButtonReleased(position: Point) = mouseEventHandlers.forEach { it.onRButtonReleased(position) }

    /**
     * 滑鼠进入视图。
     */
    override fun onMouseEntered() = mouseEventHandlers.forEach { it.onMouseEntered() }

    /**
     * 滑鼠离开视图。
     */
    override fun onMouseExited() = mouseEventHandlers.forEach { it.onMouseExited() }

    /**
     * 滑鼠在视图上移动。
     */
    override fun onMouseMoved(position: Point) = mouseEventHandlers.forEach { it.onMouseMoved(position) }

    /**
     * 成为捕获视图。
     */
    override fun onCaptureGot() = mouseEventHandlers.forEach { it.onCaptureGot() }

    /**
     * 不再是捕获视图。
     */
    override fun onCaptureLost() = mouseEventHandlers.forEach { it.onCaptureLost() }

    /**
     * 设置父视图。
     */
    final override fun setParent(parentView: View?) {

        this.parentView = parentView
    }
}