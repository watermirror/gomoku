package org.bymc.gomoku.uifx.window

import java.awt.Frame
import java.awt.Toolkit
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

/**
 * 窗口初始化器。
 *
 * @author: zheng.chez
 * @since: 2022/09/27
 */
class WindowInitializer(

    /**
     * 待初始化的窗口。
     */
    private val window: Frame,

    /**
     * 初始化配置。
     */
    private val config: WindowInitialConfig

) {

    /**
     * 执行窗口初始化。
     */
    fun initialize() {

        window.layout = null
        // 需要先设置 visible 后 configArea() 才能有效。
        window.isVisible = true
        configTitle()
        configCloseable()
        configResizable()
        configArea()
        configVisible()
    }

    /**
     * 配置窗口标题。
     */
    private fun configTitle() {

        window.title = config.title
    }

    /**
     * 配置窗口的可关闭性。
     */
    private fun configCloseable() {

        if (!config.closeable) {
            return
        }

        window.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                super.windowClosing(e)
                window.dispose()
            }
        })
    }

    /**
     * 配置窗口的可拉伸性。
     */
    private fun configResizable() {

        window.isResizable = config.resizable
    }

    /**
     * 配置窗口区域。
     */
    private fun configArea() {

        when (config.explicitClientSize) {
            true -> window.setBounds(
                config.origin.x,
                config.origin.y,
                config.size.width + window.insets.left + window.insets.right,
                config.size.height + window.insets.top + window.insets.bottom
            )
            false -> window.setBounds(config.origin.x, config.origin.y, config.size.width, config.size.height)
        }

        if (config.central) {
            moveToCenter()
        }
    }

    /**
     * 配置窗口可见性。
     */
    private fun configVisible() {

        window.isVisible = config.visible
    }

    /**
     * 将窗口移动到屏幕中心。
     */
    private fun moveToCenter() {

        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val bounds = window.bounds
        window.setBounds(
            (screenSize.width - bounds.width) / 2,
            (screenSize.height - bounds.height) / 2,
            bounds.width,
            bounds.height
        )
    }
}