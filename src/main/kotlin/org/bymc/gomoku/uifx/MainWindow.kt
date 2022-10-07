package org.bymc.gomoku.uifx

import org.bymc.gomoku.uifx.view.LabelView
import org.bymc.gomoku.uifx.view.common.FontConfig
import org.bymc.gomoku.uifx.window.RootViewWindowBase
import org.bymc.gomoku.uifx.window.WindowInitialConfig
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

    private val labelView: LabelView = LabelView(
        "Label A 你好呀", Color.BLACK, Rectangle(10, 10, 200, 20), true, Color.LIGHT_GRAY
    )

    private val labelView2: LabelView = LabelView(
        "Label B", Color.DARK_GRAY, Rectangle(10, 40, 200, 20), true, Color.LIGHT_GRAY
    )

    init {
        getRootView().appendSubView(labelView)
        getRootView().appendSubView(labelView2)
    }
}