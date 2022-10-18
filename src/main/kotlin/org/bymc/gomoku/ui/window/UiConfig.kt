package org.bymc.gomoku.ui.window

import java.awt.Dimension

/**
 * UI 配置接口。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
interface UiConfig {

    /**
     * 获取单元格的尺寸。
     */
    fun getCellSize(): Int

    /**
     * 获取棋枰尺寸。
     */
    fun getBoardSize(): Dimension

    /**
     * 获取控制面板尺寸。
     */
    fun getControlPanelWidth(): Int

    /**
     * 获取主窗口客户区尺寸。
     */
    fun getMainWindowClientSize(): Dimension
}