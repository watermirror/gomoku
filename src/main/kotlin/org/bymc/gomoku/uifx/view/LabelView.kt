package org.bymc.gomoku.uifx.view

import org.bymc.gomoku.uifx.view.base.ViewBase
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.awt.Rectangle

/**
 * 标签视图。
 *
 * @author: zheng.chez
 * @since: 2022/09/29
 */
class LabelView(
    theText: String,
    area: Rectangle = Rectangle(0, 0, 0, 0),
    showing: Boolean = true,
    bgColor: Color? = Color.WHITE,
    interactive: Boolean = true
) : ViewBase(area, showing, bgColor, interactive) {

    /**
     * 标签内容。
     */
    private var text: String = theText

    /**
     * 获取标签内容。
     */
    fun getText(): String = text

    /**
     * 设置标签内容。
     */
    fun setTest(text: String) {

        this.text = text
        scheduleRender(null)
    }

    /**
     * 渲染。
     */
    override fun onRender(g: Graphics, range: Rectangle) {

        val bounds = g.fontMetrics.getStringBounds(text, g)
        g.color = Color.BLACK
        g.drawString(
            text,
            ((getArea().width - bounds.width) / 2).toInt(),
            ((getArea().height - bounds.height) / 2 + bounds.height - (bounds.height + bounds.y)).toInt()
        )
    }

    /**
     * 滑鼠左键按下。
     */
    override fun onLButtonPressed(position: Point) {
        println("$text Left Button Down.")
    }

    /**
     * 滑鼠左键释放。
     */
    override fun onLButtonReleased(position: Point) {
        println("$text Left Button Up.")
    }

    /**
     * 滑鼠右键按下。
     */
    override fun onRButtonPressed(position: Point) {
        println("$text Right Button Down.")
    }

    /**
     * 滑鼠右键释放。
     */
    override fun onRButtonReleased(position: Point) {
        println("$text Right Button Up.")
    }

    /**
     * 滑鼠进入视图。
     */
    override fun onMouseEntered() {
        println("$text Mouse Enter.")
    }

    /**
     * 滑鼠离开视图。
     */
    override fun onMouseExited() {
        println("$text Mouse Exit.")
    }

    /**
     * 滑鼠在视图上移动。
     */
    override fun onMouseMoved(position: Point) {
        println("$text Mouse Move.")
    }
}