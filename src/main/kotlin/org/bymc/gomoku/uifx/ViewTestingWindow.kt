package org.bymc.gomoku.uifx

import org.bymc.gomoku.uifx.view.LabelView
import org.bymc.gomoku.uifx.view.LabelViewEventListener
import org.bymc.gomoku.uifx.view.SimpleTextButtonView
import org.bymc.gomoku.uifx.view.base.ButtonViewBase
import org.bymc.gomoku.uifx.view.base.ButtonViewEventListener
import org.bymc.gomoku.uifx.view.common.BorderConfig
import org.bymc.gomoku.uifx.window.RootViewWindowBase
import org.bymc.gomoku.uifx.window.WindowInitialConfig
import java.awt.Color
import java.awt.Dimension
import java.awt.Rectangle

/**
 * 集成测试窗口。
 *
 * @author: zheng.chez
 * @since: 2022/09/27
 */
class ViewTestingWindow : RootViewWindowBase(
    WindowInitialConfig(
        resizable = true,
        central = true,
        explicitClientSize = true,
        size = Dimension(500, 500)
    )
), LabelViewEventListener, ButtonViewEventListener {

    private val labelView: LabelView = LabelView(
        "Label A, Hello", Color.BLACK, Rectangle(10, 10, 200, 20), true, Color.LIGHT_GRAY,
        borderConfig = BorderConfig(2)
    )

    private val labelView2: LabelView = LabelView(
        "Label B", Color.DARK_GRAY, Rectangle(10, 40, 200, 20), true, Color.LIGHT_GRAY
    )

    private val simpleTextButtonView: SimpleTextButtonView = SimpleTextButtonView(
        "Click Me", Rectangle(10, 70, 100, 30)
    )

    private val simpleTextButtonView2: SimpleTextButtonView = SimpleTextButtonView(
        "Click Me 2", Rectangle(10, 110, 100, 30)
    )

    /**
     * 添加子视图。
     */
    init {
        getRootView().appendSubView(labelView)
        getRootView().appendSubView(labelView2)
        getRootView().appendSubView(simpleTextButtonView)
        getRootView().appendSubView(simpleTextButtonView2)
    }

    init {
        labelView.addEventListener(this)
        labelView2.addEventListener(this)
        simpleTextButtonView.addEventListener(this)
        simpleTextButtonView2.addEventListener(this)
        simpleTextButtonView2.disable()
    }

    /**
     * 滑鼠左键点击。
     */
    override fun onLeftButtonClicked(sender: LabelView) {

        println("onLeftButtonClicked: ${sender.getText()}")
    }

    /**
     * 滑鼠右键点击。
     */
    override fun onRightButtonClicked(sender: LabelView) {

        println("onRightButtonClicked: ${sender.getText()}")
    }

    /**
     * 滑鼠左键双击。
     */
    override fun onLeftButtonDoubleClicked(sender: LabelView) {

        println("onLeftButtonDoubleClicked: ${sender.getText()}")
    }

    /**
     * 滑鼠右键双击。
     */
    override fun onRightButtonDoubleClicked(sender: LabelView) {

        println("onRightButtonDoubleClicked: ${sender.getText()}")
    }

    /**
     * 按钮点击事件。
     */
    override fun onClicked(sender: ButtonViewBase) {

        println("onClicked: ${
            (sender as? SimpleTextButtonView)?.getButtonTextConfig()?.getNormalConfig()?.text ?: "a button"
        }")
    }
}