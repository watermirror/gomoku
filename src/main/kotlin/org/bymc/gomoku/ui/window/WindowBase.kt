package org.bymc.gomoku.ui.window

import java.awt.Frame

/**
 * 窗口基类。
 *
 * @author: zheng.chez
 * @since: 2022/09/27
 */
abstract class WindowBase(

    /**
     * 初始化配置。
     */
    private val initialConfig: WindowInitialConfig

) : Frame() {

    /**
     * 初始化窗口。
     */
    init {
        WindowInitializer(this, initialConfig).initialize()
    }

    /**
     * Returns true if this component is painted to an offscreen image
     * ("buffer") that's copied to the screen later.  Component
     * subclasses that support double buffering should override this
     * method to return true if double buffering is enabled.
     *
     * @return false by default
     */
    override fun isDoubleBuffered(): Boolean = initialConfig.doubleBuffered
}