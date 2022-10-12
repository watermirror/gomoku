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
    private var theInteractive: Boolean = true,

    /**
     * 是否开启阴影。
     */
    private var shadowEnabled: Boolean = false,

    /**
     * 阴影偏移。
     */
    private var theShadowOffset: Point = Point(6, 4),

    /**
     * 阴影视图。
     */
    private var shadowView: ViewBase? = null

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

        // 触发 Moved 事件。
        if (originalArea.location != area.location) {
            onMoved(originalArea.location, area.location)
        }

        // 触发 Resized 事件。
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

        // 事件通知。
        when (theShowing) {
            true -> onVisible()
            false -> onHidden()
        }

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
    final override fun addSubView(subView: View) {

        require(subView != this)
        require(subView as? SubView != null)
        if (containDescendantView(subView)) {
            throw RuntimeException("trying to add a duplicated descendant view")
        }

        subViews.add(subView)
        setParentInternally(subView)
    }

    /**
     * 在锚点视图之后插入视图。
     */
    override fun addSubViewAfter(subView: View, anchor: View) {

        require(subView != this)
        require(subView as? SubView != null)
        if (containDescendantView(subView)) {
            throw RuntimeException("trying to add a duplicated descendant view")
        }

        val realAnchor = (anchor as? ViewBase)?.shadowView ?: anchor
        addSubViewAfterInternally(subView, realAnchor)
    }

    /**
     * 在锚点视图之前插入视图。
     */
    override fun addSubViewBefore(subView: View, anchor: View) {

        require(subView != this)
        require(subView as? SubView != null)
        if (containDescendantView(subView)) {
            throw RuntimeException("trying to add a duplicated descendant view")
        }

        val index = subViews.indexOf(anchor)
        if (index < 0) {
            throw RuntimeException("try to add a sub view at a illegal position")
        }

        subViews.add(index, subView)
        setParentInternally(subView)
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
        (subView as? SubView)?.onDetached(this)

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
        if (getParentView() == null || !getParentView()!!.getShowing()) {
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
     * 移动事件。
     */
    override fun onMoved(originalPosition: Point, newPosition: Point) {

        adjustShadow()
    }

    /**
     * 视图尺寸变化。
     */
    override fun onResized(originalSize: Dimension, newSize: Dimension) {

        adjustShadow()
    }

    /**
     * 显示事件。
     */
    override fun onVisible() {

        adjustShadow()
    }

    /**
     * 隐藏事件。
     */
    override fun onHidden() {

        adjustShadow()
    }

    /**
     * 滑鼠左键按下。
     */
    override fun onLButtonPressed(sender: View, position: Point, pressedCount: Int) =
        mouseEventHandlers.forEach { it.onLButtonPressed(sender, position, pressedCount) }

    /**
     * 滑鼠左键释放。
     */
    override fun onLButtonReleased(sender: View, position: Point) =
        mouseEventHandlers.forEach { it.onLButtonReleased(sender, position) }

    /**
     * 滑鼠右键按下。
     */
    override fun onRButtonPressed(sender: View, position: Point, pressedCount: Int) =
        mouseEventHandlers.forEach { it.onRButtonPressed(sender, position, pressedCount) }

    /**
     * 滑鼠右键释放。
     */
    override fun onRButtonReleased(sender: View, position: Point) =
        mouseEventHandlers.forEach { it.onRButtonReleased(sender, position) }

    /**
     * 滑鼠进入视图。
     */
    override fun onMouseEntered(sender: View) = mouseEventHandlers.forEach { it.onMouseEntered(sender) }

    /**
     * 滑鼠离开视图。
     */
    override fun onMouseExited(sender: View) = mouseEventHandlers.forEach { it.onMouseExited(sender) }

    /**
     * 滑鼠在视图上移动。
     */
    override fun onMouseMoved(sender: View, position: Point) =
        mouseEventHandlers.forEach { it.onMouseMoved(sender, position) }

    /**
     * 成为捕获视图。
     */
    override fun onCaptureGot(sender: View) = mouseEventHandlers.forEach { it.onCaptureGot(sender) }

    /**
     * 不再是捕获视图。
     */
    override fun onCaptureLost(sender: View, ) = mouseEventHandlers.forEach { it.onCaptureLost(sender) }

    /**
     * 设置父视图。
     */
    final override fun setParent(parentView: View?) {

        this.parentView = parentView
    }

    /**
     * 启用阴影。
     */
    final override fun enableShadow() {

        if (shadowEnabled) { return }
        shadowEnabled = true

        addShadow(parentView)
        adjustShadow()
    }

    /**
     * 禁用阴影。
     */
    final override fun disableShadow() {

        if (!shadowEnabled) { return }
        shadowEnabled = false

        removeShadow(parentView)
    }

    /**
     * 设置阴影偏移。
     */
    override fun setShadowOffset(offset: Point) {

        if (theShadowOffset == offset) { return }
        theShadowOffset = offset
        adjustShadow()
    }

    /**
     * 获取阴影视图。
     */
    final override fun getShadowView(): View? = shadowView

    /**
     * 添加到父视图事件。
     */
    override fun onAttached(parentView: View) {

        if (!shadowEnabled) { return }
        addShadow(parentView)
        adjustShadow()
    }

    /**
     * 从父视图移除事件。
     */
    override fun onDetached(parentView: View) {

        if (!shadowEnabled) { return }
        removeShadow(parentView)
    }

    /**
     * 在锚点视图后插入视图。
     */
    private fun addSubViewAfterInternally(subView: View, anchor: View) {

        val index = subViews.indexOf(anchor)
        if (index < 0) {
            throw RuntimeException("try to add a sub view at a illegal position")
        }

        subViews.add(index + 1, subView)
        setParentInternally(subView)
    }

    /**
     * 设置子视图的父视图。
     */
    private fun setParentInternally(subView: View) {

        (subView as? SubView)?.setParent(this)
        (subView as? SubView)?.onAttached(this)

        if (subView.getShowing()) {
            scheduleRender(subView.getArea())
        }
    }

    /**
     * 初始化阴影视图。
     */
    private fun initializeShadowView() {

        if (!shadowEnabled) { return }
        if (shadowView != null) { return }
        shadowView = DefaultShadowView()
    }

    /**
     * 添加阴影视图。
     */
    private fun addShadow(parentView: View?) {

        if (parentView == null) { return }
        initializeShadowView()
        parentView.addSubViewBefore(shadowView!!, this)
    }

    /**
     * 移除阴影视图。
     */
    private fun removeShadow(parentView: View?) {

        if (parentView == null) { return }
        if (shadowView == null) { return }
        parentView.removeSubView(shadowView!!)
    }

    /**
     * 调整阴影视图位置或显示状态。。
     */
    private fun adjustShadow() {

        if (!shadowEnabled) { return }

        if (!theShowing) {
            return shadowView?.setShowing(false) ?: Unit
        }

        shadowView?.setShowing(true)
        shadowView?.setArea(
            Rectangle(getArea().x + theShadowOffset.x, getArea().y + theShadowOffset.y, getArea().width, getArea().height)
        )
    }
}