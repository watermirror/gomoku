package org.bymc.gomoku.uifx.view.util.painter

import org.bymc.gomoku.uifx.view.common.FontConfig
import org.bymc.gomoku.uifx.view.common.HorizontalAlignment
import org.bymc.gomoku.uifx.view.common.VerticalAlignment
import java.awt.*

/**
 * 文本绘制器。
 *
 * @author: zheng.chez
 * @since: 2022/10/01
 */
class TextPainter(

    /**
     * 文本内容。
     */
    private val text: String,

    /**
     * 文本颜色。
     */
    private val textColor: Color,

    /**
     * 绘制区域。
     */
    private val area: Rectangle,

    /**
     * 水平对齐方式。
     */
    private val horizontalAlignment: HorizontalAlignment = HorizontalAlignment.LEFT,

    /**
     * 垂直对齐方式。
     */
    private val verticalAlignment: VerticalAlignment = VerticalAlignment.CENTER,

    /**
     * 字体配置。
     */
    private val fontConfig: FontConfig = FontConfig()

) {

    /**
     * 在指定的绘制对象的指定区域内渲染文本。
     */
    fun paint(g: Graphics) {

        // 保存原字体，设置字体。
        val originalFont = g.font
        g.font = Font(fontConfig.fontName, fontConfig.fontStyle.rawStyle, fontConfig.fontSize)

        // 计算文本区域、保存原色、设定文本色。
        val bounds = g.fontMetrics.getStringBounds(text, g)
        val originalColor = g.color
        g.color = textColor

        // 渲染文本。
        g.drawString(
            text,
            (area.x - bounds.x + when (horizontalAlignment) {
                HorizontalAlignment.LEFT -> 0.0
                HorizontalAlignment.CENTER -> (area.width - bounds.width) / 2
                HorizontalAlignment.RIGHT -> area.width - bounds.width
            }).toInt(),
            (area.y - bounds.y + when (verticalAlignment) {
                VerticalAlignment.TOP -> 0.0
                VerticalAlignment.CENTER -> (area.height - bounds.height) / 2
                VerticalAlignment.BOTTOM -> area.height - bounds.height
            }).toInt()
        )

        // 恢复原色、字体。
        g.color = originalColor
        g.font = originalFont
    }
}