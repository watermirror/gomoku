package org.bymc.gomoku.uifx

import org.bymc.gomoku.uifx.view.LabelView
import org.bymc.gomoku.uifx.view.LabelViewEventListener
import org.bymc.gomoku.uifx.view.SimpleTextButtonView
import org.bymc.gomoku.uifx.view.base.ButtonViewBase
import org.bymc.gomoku.uifx.view.base.ButtonViewEventListener
import org.bymc.gomoku.uifx.view.base.MouseEventHandler
import org.bymc.gomoku.uifx.view.base.View
import org.bymc.gomoku.uifx.view.common.BorderConfig
import org.bymc.gomoku.uifx.window.RootViewWindowBase
import org.bymc.gomoku.uifx.window.WindowInitialConfig
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
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
), LabelViewEventListener, ButtonViewEventListener, MouseEventHandler {

    private val labelView: LabelView = LabelView(
        "Drag Me!", Color.BLACK, Rectangle(10, 10, 200, 20), true, Color.LIGHT_GRAY,
        borderConfig = BorderConfig(2)
    )

    private val labelView2: LabelView = LabelView(
        "Label B, Hello", Color.DARK_GRAY, Rectangle(10, 40, 200, 20), true, Color.LIGHT_GRAY
    )

    private val simpleTextButtonView: SimpleTextButtonView = SimpleTextButtonView(
        "Toggle \"Drag Me!\"", Rectangle(10, 70, 150, 30)
    )

    private val simpleTextButtonView2: SimpleTextButtonView = SimpleTextButtonView(
        "Click Me 2", Rectangle(10, 110, 150, 30)
    )

    private var draggedView: View? = null

    private var pressedPosition: Point = Point()

    /**
     * 添加子视图。
     */
    init {
        getRootView().addSubView(labelView)
        getRootView().addSubView(labelView2)
        getRootView().addSubView(simpleTextButtonView)
        getRootView().addSubView(simpleTextButtonView2)
    }

    init {
        labelView.addEventListener(this)
        labelView.addMouseEventHandler(this)
        labelView.enableShadow()
        labelView2.addEventListener(this)
        simpleTextButtonView.addEventListener(this)
        simpleTextButtonView.enableShadow()
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

        if (sender == simpleTextButtonView) {
            labelView.setShowing(!labelView.getShowing())
        }
    }

    /**
     * 滑鼠左键按下。
     */
    override fun onLButtonPressed(sender: View, position: Point, pressedCount: Int) {

        if (sender == labelView) {
            draggedView = labelView
            pressedPosition = position
        }
    }

    /**
     * 滑鼠左键释放。
     */
    override fun onLButtonReleased(sender: View, position: Point) {

        draggedView = null
    }

    /**
     * 滑鼠右键按下。
     */
    override fun onRButtonPressed(sender: View, position: Point, pressedCount: Int) {}

    /**
     * 滑鼠右键释放。
     */
    override fun onRButtonReleased(sender: View, position: Point) {}

    /**
     * 滑鼠进入视图。
     */
    override fun onMouseEntered(sender: View) {}

    /**
     * 滑鼠离开视图。
     */
    override fun onMouseExited(sender: View) {}

    /**
     * 滑鼠在视图上移动。
     */
    override fun onMouseMoved(sender: View, position: Point) {

        if (draggedView == null) { return }
        val offset = Point(position.x - pressedPosition.x, position.y - pressedPosition.y)
        val area = draggedView!!.getArea()
        draggedView!!.setArea(Rectangle(area.x + offset.x, area.y + offset.y, area.width, area.height))
    }

    /**
     * 成为捕获视图。
     */
    override fun onCaptureGot(sender: View) {}

    /**
     * 不再是捕获视图。
     */
    override fun onCaptureLost(sender: View) {

        draggedView = null
    }
}