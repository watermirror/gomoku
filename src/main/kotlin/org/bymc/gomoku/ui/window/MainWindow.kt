package org.bymc.gomoku.ui.window

import org.bymc.gomoku.ui.view.BoardIntegrationView
import org.bymc.gomoku.uifx.window.RootViewWindowBase
import org.bymc.gomoku.uifx.window.WindowInitialConfig
import java.awt.Dimension
import java.awt.Rectangle

/**
 * 游戏主窗口。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
class MainWindow(

    /**
     * UI 配置接口。
     */
    private val uiConfig: UiConfig = DefaultUiConfig()

) : RootViewWindowBase(
    WindowInitialConfig(
        title = "Gomoku Paradise",
        resizable = false,
        central = true,
        explicitClientSize = true,
        size = uiConfig.getMainWindowClientSize()
    )
) {

    /**
     * 棋枰视图。
     */
    private val boardView: BoardIntegrationView = BoardIntegrationView(
        Rectangle(0, 0, uiConfig.getBoardSize().width, uiConfig.getBoardSize().height),
        uiConfig.getCellSize(),
        false
    )

    /**
     * 配置子视图。
     */
    init {

        getRootView().addSubView(boardView)
    }
}