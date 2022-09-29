package org.bymc.gomoku.ui

import org.bymc.gomoku.ui.view.LabelView
import org.bymc.gomoku.ui.window.RootViewWindowBase
import org.bymc.gomoku.ui.window.WindowInitialConfig
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import java.awt.Rectangle

/**
 * 主窗口。
 *
 * @author: zheng.chez
 * @since: 2022/09/27
 */
class MainWindow : RootViewWindowBase(
    WindowInitialConfig(
        resizable = true,
        origin = Point(100, 100),
        explicitClientSize = true,
        size = Dimension(500, 500)
    )
) {

    private val labelView: LabelView = LabelView("Hello god of world.", Rectangle(10, 10, 200, 20), true, Color.WHITE)

    private val labelView2: LabelView = LabelView("Hello god of world.", Rectangle(10, 40, 200, 20), true, null)

    init {
        getRootView().appendSubView(labelView)
        getRootView().appendSubView(labelView2)
    }
}