package org.bymc.gomoku.ui.window

import java.awt.Dimension
import java.awt.Point

/**
 * 窗口初始化配置。
 *
 * @author: zheng.chez
 * @since: 2022/09/27
 */
data class WindowInitialConfig(

    /**
     * 是否可关闭。
     */
    val closeable: Boolean = true,

    /**
     * 是否可拉伸尺寸。
     */
    val resizable: Boolean = true,

    /**
     * 初始远点坐标。
     */
    val origin: Point = Point(0, 0),

    /**
     * 精确定义客户区尺寸。若为 true，则 initWidth 和 initHeight 为客户区的精确尺寸。
     */
    val explicitClientSize: Boolean = true,

    /**
     * 窗口或客户区的尺寸。
     */
    val size: Dimension = Dimension(100, 100),

    /**
     * 是否可见。
     */
    val visible: Boolean = true,

    /**
     * 双缓冲渲染。
     */
    val doubleBuffered: Boolean = true
)