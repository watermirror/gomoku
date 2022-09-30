package org.bymc.gomoku.uifx.component.viewbased

import org.bymc.gomoku.uifx.component.AwtEventListener
import org.bymc.gomoku.uifx.view.base.View
import java.awt.event.MouseEvent

/**
 * 视图 AWT 事件处理器。
 *
 * @author: zheng.chez
 * @since: 2022/09/30
 */
class ViewAwtEventProcessor(

    /**
     * 根视图。
     */
    private val rootView: View,

    /**
     * 当前捕获滑鼠事件的视图。
     */
    private var capturedView: View? = null,

    /**
     * 当前滑鼠悬停的视图。
     */
    private var hoveredView: View? = null

) : AwtEventListener {

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     * @param e the event to be processed
     */
    override fun mouseClicked(e: MouseEvent?) {}

    /**
     * Invoked when a mouse button has been pressed on a component.
     * @param e the event to be processed
     */
    override fun mousePressed(e: MouseEvent?) {

        // 非左右键，不处理；否则进行命中检测。
        if (e!!.button !in arrayOf(MouseEvent.BUTTON1, MouseEvent.BUTTON3)) {
            return
        }
        val hitTest = HitTester(rootView, e.point).test()

        // 变更捕获视图和悬停视图。
        handoverCapturedView(hitTest.getHitView())
        if (handoverHoveredView(hitTest.getHitView())) {
            // 如果悬停视图变更了，向新悬停视图发送滑鼠移动通知。
            hitTest.getHitView()?.onMouseMoved(hitTest.getRelativePosition()!!)
        }

        // 若没有命中，直接返回。
        if (hitTest.isEmpty()) {
            return
        }

        // 通知视图。
        when (e.button) {
            MouseEvent.BUTTON1 -> hitTest.getHitView()!!.onLButtonPressed(hitTest.getRelativePosition()!!)
            MouseEvent.BUTTON3 -> hitTest.getHitView()!!.onRButtonPressed(hitTest.getRelativePosition()!!)
        }
    }

    /**
     * Invoked when a mouse button has been released on a component.
     * @param e the event to be processed
     */
    override fun mouseReleased(e: MouseEvent?) {

        // 非左右键、捕获视图不存在，则不处理。
        if (capturedView == null || e!!.button !in arrayOf(MouseEvent.BUTTON1, MouseEvent.BUTTON3)) {
            return
        }

        // 变更捕获视图。
        val originalCapturedView = handoverCapturedView(null)
        require(originalCapturedView != null)

        // 计算滑鼠相对于原捕获视图的坐标，若原捕获视图离根、不可见或不可交互，则得到空坐标，直接返回。
        val relativePosition = PositionCalculator(originalCapturedView, rootView, e.point).calculate() ?: return

        // 通知视图。
        when (e.button) {
            MouseEvent.BUTTON1 -> originalCapturedView.onLButtonReleased(relativePosition)
            MouseEvent.BUTTON3 -> originalCapturedView.onRButtonReleased(relativePosition)
        }

        // 命中检测并变更悬停视图。
        val hitTest = HitTester(rootView, e.point).test()
        if (handoverHoveredView(hitTest.getHitView())) {
            // 如果悬停视图变更了，向新悬停视图发送滑鼠移动通知。
            hitTest.getHitView()?.onMouseMoved(hitTest.getRelativePosition()!!)
        }
    }

    /**
     * Invoked when the mouse enters a component.
     * @param e the event to be processed
     */
    override fun mouseEntered(e: MouseEvent?) {}

    /**
     * Invoked when the mouse exits a component.
     * @param e the event to be processed
     */
    override fun mouseExited(e: MouseEvent?) {

        // 若存在捕获视图，不做处理。与 mouseExited 同时发生的 mouseMoved 或 mouseDragged 事件中有合适的处理。
        if (capturedView != null) {
            return
        }

        // 否则置空悬停视图。
        handoverHoveredView(null)
    }

    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  `MOUSE_DRAGGED` events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     *
     *
     * Due to platform-dependent Drag&amp;Drop implementations,
     * `MOUSE_DRAGGED` events may not be delivered during a native
     * Drag&amp;Drop operation.
     * @param e the event to be processed
     */
    override fun mouseDragged(e: MouseEvent?) = genericMouseMoved(e!!)

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     * @param e the event to be processed
     */
    override fun mouseMoved(e: MouseEvent?) = genericMouseMoved(e!!)

    /**
     * 广义滑鼠移动事件，包含 mouseMoved 和 mouseDragged。
     */
    private fun genericMouseMoved(e: MouseEvent) {

        // 如果存在捕获视图，则将滑鼠移动事件通知捕获视图。
        if (capturedView != null) {
            handoverHoveredView(capturedView)
            return capturedView!!.onMouseMoved(
                PositionCalculator(capturedView!!, rootView, e.point).calculate() ?: return
            )
        }

        // 命中检测、可能变更悬停视图、通知滑鼠移动事件。
        val hitTest = HitTester(rootView, e.point).test()
        handoverHoveredView(hitTest.getHitView())
        hitTest.getHitView()?.onMouseMoved(hitTest.getRelativePosition()!!)
    }

    /**
     * 变更悬停视图，并进行必要的通知。返回是否发生变更。
     */
    private fun handoverHoveredView(hoveredView: View?): Boolean {

        // 若无变化，则不处理。
        if (this.hoveredView == hoveredView) {
            return false
        }

        // 替换悬停视图。
        val originalView = this.hoveredView
        this.hoveredView = hoveredView

        // 通知事件。
        originalView?.onMouseExited()
        hoveredView?.onMouseEntered()
        return true
    }

    /**
     * 变更捕获视图。
     */
    private fun handoverCapturedView(capturedView: View?): View? {

        // 替换捕获视图。
        val originalView = this.capturedView
        this.capturedView = capturedView

        // 返回原捕获视图。
        return originalView
    }
}