package org.bymc.gomoku.ui.window

import org.bymc.gomoku.ui.component.RootViewCanvas
import org.bymc.gomoku.ui.view.base.View
import java.awt.Color
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.event.ComponentEvent
import java.awt.event.ComponentListener
import kotlin.math.max

/**
 * 根视图窗口基类。
 *
 * @author: zheng.chez
 * @since: 2022/09/29
 */
abstract class RootViewWindowBase(initialConfig: WindowInitialConfig) : WindowBase(initialConfig), ComponentListener {

    private val rootView = RootViewCanvas()

    init {

        // 添加根视图。
        add(rootView)
        val clientSize = when (initialConfig.explicitClientSize) {
            true -> initialConfig.size
            false -> calculateClientSize(initialConfig.size)
        }
        rootView.bounds = Rectangle(
            insets.left, insets.top, clientSize.width, clientSize.height
        )

        // 监听窗口尺寸变化。
        addComponentListener(this)
    }

    /**
     * 获取根视图。
     */
    fun getRootView(): View = rootView

    /**
     * Invoked when the component's size changes.
     * @param e the event to be processed
     */
    override fun componentResized(e: ComponentEvent?) {

        // 响应窗口尺寸变化，调整根视图的尺寸。
        val clientSize = calculateClientSize(Dimension(width, height))
        rootView.bounds = Rectangle(insets.left, insets.top, clientSize.width, clientSize.height)
    }

    /**
     * Invoked when the component's position changes.
     * @param e the event to be processed
     */
    override fun componentMoved(e: ComponentEvent?) {}

    /**
     * Invoked when the component has been made visible.
     * @param e the event to be processed
     */
    override fun componentShown(e: ComponentEvent?) {}

    /**
     * Invoked when the component has been made invisible.
     * @param e the event to be processed
     */
    override fun componentHidden(e: ComponentEvent?) {}

    /**
     * 根据外层尺寸计算客户区尺寸。
     */
    private fun calculateClientSize(outerSize: Dimension): Dimension {

        return Dimension(
            max(0, outerSize.width - insets.left - insets.right),
            max(0, outerSize.height - insets.top - insets.bottom)
        )
    }
}