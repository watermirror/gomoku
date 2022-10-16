package org.bymc.gomoku.ui.view

import org.bymc.gomoku.uifx.window.RootViewWindowBase
import org.bymc.gomoku.uifx.window.WindowInitialConfig
import java.awt.Dimension
import java.awt.Point
import java.awt.Rectangle

/**
 * 测试窗口。
 *
 * @author: zheng.chez
 * @since: 2022/10/17
 */
class GameTestingWindow : RootViewWindowBase(
    WindowInitialConfig(
        title = "Game Testing Window",
        resizable = false,
        central = true,
        explicitClientSize = true,
        size = Dimension(615, 615)
    )
) {

    private val boardGridView: BoardGridView = BoardGridView(Rectangle(0, 0, 615, 615), Point(0, 0), 41)

    init {

        getRootView().addSubView(boardGridView)
    }
}