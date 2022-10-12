package org.bymc.gomoku.uifx.component.viewbased

import org.bymc.gomoku.uifx.view.base.SubView
import org.bymc.gomoku.uifx.view.base.View
import org.bymc.gomoku.uifx.view.base.ViewBase
import java.awt.*

/**
 * 根视图 Canvas 类。
 *
 * @author: zheng.chez
 * @since: 2022/09/28
 */
class RootViewCanvas(

    /**
     * 视图坐标与尺寸。
     */
    area: Rectangle = Rectangle(0, 0, 0, 0),

    /**
     * 背景色。
     */
    bgColor: Color? = Color.WHITE,

    /**
     * 可交互性。
     */
    private var theInteractive: Boolean = true,

) : Canvas(), View {

    /**
     * 子视图。
     */
    private val subViews = ArrayList<View>()

    /**
     * 初始化对象。
     */
    init {

        // 设定位置、尺寸和背景色。
        bounds = area
        background = bgColor

        // 监听事件。
        val awtListener = ViewAwtEventProcessor(this)
        addMouseListener(awtListener)
        addMouseMotionListener(awtListener)
    }

    /**
     * 获取视图位置和尺寸。
     */
    override fun getArea(): Rectangle = bounds

    /**
     * 设置视图位置和尺寸。
     */
    override fun setArea(area: Rectangle) {

        bounds = area
    }

    /**
     * 获取视图可见性。
     */
    override fun getShowing(): Boolean = isVisible

    /**
     * 设置视图可见性。
     */
    override fun setShowing(showing: Boolean) {

        isVisible = showing
    }

    /**
     * 获取视图背景色，null 表示透明。
     */
    override fun getBgColor(): Color? = background

    /**
     * 设定视图背景颜色，null 表示透明。
     */
    override fun setBgColor(bgColor: Color?) {

        background = bgColor
    }

    /**
     * 获取可交互性。
     */
    override fun getInteractive(): Boolean = theInteractive

    /**
     * 设置可交互性。
     */
    override fun setInteractive(interactive: Boolean) {

        theInteractive = interactive
    }

    /**
     * 获取子视图。
     */
    override fun getSubViews(): List<View> = subViews

    /**
     * 是否包含指定子孙视图。
     */
    override fun containDescendantView(descendantView: View): Boolean {

        return subViews.find { it == descendantView || it.containDescendantView(descendantView) } != null
    }

    /**
     * 加入子视图，重复加入会抛异常。
     */
    override fun addSubView(subView: View) {

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

        val realAnchor = (anchor as? ViewBase)?.getShadowView() ?: anchor
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
    override fun removeSubView(subView: View) {

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
    override fun getParentView(): View? = null

    /**
     * 重绘区域，range 为 null 表示整个视图重绘。
     */
    override fun scheduleRender(range: Rectangle?) {

        if (range == null) {
            return repaint()
        }

        if (range.width == 0 || range.height == 0) {
            return
        }

        val bufferedImage = createVolatileImage(range.width, range.height)
        val bufferedGfx = bufferedImage.graphics
        bufferedGfx.translate(-range.x, -range.y)
        bufferedGfx.color = background
        bufferedGfx.fillRect(range.x, range.y, range.width, range.height)

        renderViewTree(bufferedGfx, range)

        graphics.drawImage(bufferedImage, range.x, range.y, this)
    }

    /**
     * 渲染。
     */
    override fun onRender(g: Graphics, range: Rectangle) {}

    /**
     * 移动事件。
     */
    override fun onMoved(originalPosition: Point, newPosition: Point) {}

    /**
     * 视图尺寸变化。
     */
    override fun onResized(originalSize: Dimension, newSize: Dimension) {}

    /**
     * 显示事件。
     */
    override fun onVisible() {}

    /**
     * 隐藏事件。
     */
    override fun onHidden() {}

    /**
     * 滑鼠左键按下。
     */
    override fun onLButtonPressed(sender: View, position: Point, pressedCount: Int) {}

    /**
     * 滑鼠左键释放。
     */
    override fun onLButtonReleased(sender: View, position: Point) {}

    /**
     * 滑鼠右键按下。
     */
    override fun onRButtonPressed(sender: View, position: Point, pressedCount: Int) {}

    /**
     * 滑鼠右键释放。
     */
    override fun onRButtonReleased(sender: View, position: Point) {}

    /**
     * 滑鼠进入视图。
     */
    override fun onMouseEntered(sender: View) {}

    /**
     * 滑鼠离开视图。
     */
    override fun onMouseExited(sender: View) {}

    /**
     * 滑鼠在视图上移动。
     */
    override fun onMouseMoved(sender: View, position: Point) {}

    /**
     * 成为捕获视图。
     */
    override fun onCaptureGot(sender: View) {}

    /**
     * 不再是捕获视图。
     */
    override fun onCaptureLost(sender: View) {}

    /**
     * Returns true if this component is painted to an offscreen image
     * ("buffer") that's copied to the screen later.  Component
     * subclasses that support double buffering should override this
     * method to return true if double buffering is enabled.
     *
     * @return false by default
     */
    override fun isDoubleBuffered(): Boolean = true

    /**
     * Paints this canvas.
     *
     *
     * Most applications that subclass `Canvas` should
     * override this method in order to perform some useful operation
     * (typically, custom painting of the canvas).
     * The default operation is simply to clear the canvas.
     * Applications that override this method need not call
     * super.paint(g).
     *
     * @param      g   the specified Graphics context
     * @see .update
     * @see Component.paint
     */
    override fun paint(g: Graphics?) {

        super.paint(g)
        require(g != null)

        renderViewTree(g, null)
    }

    /**
     * 渲染视图树。
     */
    private fun renderViewTree(gfx: Graphics, range: Rectangle?) {

        recursivelyRender(this, gfx, range ?: Rectangle(0, 0, getArea().width, getArea().height))
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

    companion object {

        /**
         * 递归渲染视图树。
         */
        private fun recursivelyRender(view: View, gfx: Graphics, range: Rectangle) {

            // 渲染当前视图。
            view.onRender(gfx, range)

            // 过滤可见视图并渲染。
            for (subView in view.getSubViews().filter { it.getShowing() }) {

                // 计算渲染区域。
                val targetRange = Rectangle()
                Rectangle.intersect(range, subView.getArea(), targetRange)
                targetRange.translate(-subView.getArea().x, -subView.getArea().y)

                // 变换图形对象的原点，设置裁剪区。
                gfx.translate(subView.getArea().x, subView.getArea().y)
                gfx.clipRect(targetRange.x, targetRange.y, targetRange.width, targetRange.height)

                // 渲染背景色。
                if (subView.getBgColor() != null) {
                    gfx.color = subView.getBgColor()
                    gfx.drawRect(targetRange.x, targetRange.y, targetRange.width, targetRange.height)
                    gfx.fillRect(targetRange.x, targetRange.y, targetRange.width, targetRange.height)
                }

                // 渲染子视图。
                recursivelyRender(subView, gfx, targetRange)

                // 清除裁剪区，恢复图形对象的原点。
                gfx.clip = null
                gfx.translate(-subView.getArea().x, -subView.getArea().y)
            }
        }
    }
}