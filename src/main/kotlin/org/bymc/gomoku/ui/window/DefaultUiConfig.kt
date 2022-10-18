package org.bymc.gomoku.ui.window

import java.awt.Dimension

/**
 * 缺省 UI 配置接口实现。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
class DefaultUiConfig : UiConfig {

    /**
     * 获取单元格的尺寸。
     */
    override fun getCellSize(): Int = 41

    /**
     * 获取棋枰尺寸。
     */
    override fun getBoardSize(): Dimension = Dimension(
        Constants.edgeCapacity * getCellSize(), Constants.edgeCapacity * getCellSize()
    )

    /**
     * 获取控制面板尺寸。
     */
    override fun getControlPanelWidth(): Int = 300

    /**
     * 获取主窗口客户区尺寸。
     */
    override fun getMainWindowClientSize(): Dimension {

        return Dimension(getBoardSize().width + getControlPanelWidth(), getBoardSize().height)
    }
}