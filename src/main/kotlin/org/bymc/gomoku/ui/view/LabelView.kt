package org.bymc.gomoku.ui.view

import org.bymc.gomoku.ui.view.base.ViewBase
import java.awt.Color
import java.awt.Graphics
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

        g.drawLine(-5, -5, getArea().width + 5, getArea().height + 5)
    }
}