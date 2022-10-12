package org.bymc.gomoku.uifx.view.base

import org.bymc.gomoku.uifx.view.util.mouse.MouseInteractiveEventListener
import org.bymc.gomoku.uifx.view.util.mouse.MouseInteractiveStateTracker
import org.bymc.gomoku.uifx.view.util.mouse.MouseInteractiveViewArea
import org.bymc.gomoku.uifx.view.util.mouse.MouseInteractiveViewDisplayState
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

/**
 * 按钮视图基类。
 *
 * @author: zheng.chez
 * @since: 2022/10/11
 */
abstract class ButtonViewBase(

    /**
     * 视图坐标与尺寸。
     */
    area: Rectangle = Rectangle(0, 0, 0, 0),

    /**
     * 可见性。
     */
    showing: Boolean = true,

    /**
     * 背景色。
     */
    bgColor: Color? = null

) : ViewBase(area, showing, bgColor), MouseInteractiveViewArea, MouseInteractiveEventListener {

    /**
     * 事件监听器列表。
     */
    private val listeners: MutableList<ButtonViewEventListener> = ArrayList()

    /**
     * 滑鼠状态跟踪器。
     */
    private val mouseTracker = MouseInteractiveStateTracker(this, this)

    /**
     * 按钮的显示状态。
     */
    private var displayState: MouseInteractiveViewDisplayState = MouseInteractiveViewDisplayState.NONE

    /**
     * 按钮的可用状态。
     */
    private var enabled: Boolean = true

    init {

        /**
         * 注册滑鼠事件处理器。
         */
        this.addMouseEventHandler(mouseTracker)
    }

    /**
     * 添加事件监听器。
     */
    fun addEventListener(listener: ButtonViewEventListener) {

        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    /**
     * 移除事件监听器。
     */
    fun removeEventListener(listener: ButtonViewEventListener) {

        val index = listeners.indexOf(listener)
        if (index < 0) {
            return
        }
        listeners.removeAt(index)
    }

    /**
     * 获取按钮是否可用。
     */
    fun isEnabled(): Boolean = enabled

    /**
     * 使按钮可用。
     */
    fun enable() {

        if (enabled) { return }
        enabled = true
        scheduleRender()
    }

    /**
     * 禁用按钮。
     */
    fun disable() {

        if (!enabled) { return }
        enabled = false
        scheduleRender()
    }

    /**
     * 根据显示状态进行绘制。
     */
    abstract fun renderForState(g: Graphics, state: MouseInteractiveViewDisplayState)

    /**
     * 绘制 Disabled 状态。
     */
    abstract fun renderOnDisabled(g: Graphics)

    /**
     * 获取滑鼠交互的视图区域，应与滑鼠事件处于同一个坐标系内。
     */
    override fun getViewArea(): Rectangle {

        return when (enabled) {
            true -> Rectangle(0, 0, getArea().width, getArea().height)
            false -> Rectangle(0, 0, 0, 0)
        }
    }

    /**
     * 左键点击事件。
     */
    override fun onLeftClicked() {

        if (!enabled) { return }
        listeners.forEach { it.onClicked(this) }
    }

    /**
     * 右键点击事件。
     */
    override fun onRightClicked() {}

    /**
     * 左键双击事件。
     */
    override fun onLeftDoubleClicked() {}

    /**
     * 右键双击事件。
     */
    override fun onRightDoubleClicked() {}

    /**
     * 滑鼠交互显示状态变更事件。
     */
    override fun onDisplayStateChanged(
        original: MouseInteractiveViewDisplayState,
        current: MouseInteractiveViewDisplayState
    ) {

        displayState = current
        scheduleRender()
    }

    /**
     * 渲染。
     */
    override fun onRender(g: Graphics, range: Rectangle) {

        when (enabled) {
            true -> renderForState(g, displayState)
            false -> renderOnDisabled(g)
        }
    }
}