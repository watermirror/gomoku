package org.bymc.gomoku.uifx.view

import org.bymc.gomoku.uifx.view.base.TextButtonViewBase
import org.bymc.gomoku.uifx.view.common.*
import org.bymc.gomoku.uifx.view.mouse.MouseInteractiveViewDisplayState
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

/**
 * 简单文本按钮。
 *
 * @author: zheng.chez
 * @since: 2022/10/12
 */
class SimpleTextButtonView(

    /**
     * 按钮文本。
     */
    text: String,

    /**
     * 文本按钮的区域。
     */
    area: Rectangle = Rectangle(0, 0, 0, 0),

    /**
     * 文本按钮是否可见。
     */
    showing: Boolean = true,

    /**
     * 字体配置。
     */
    fontConfig: FontConfig = FontConfig(),

    /**
     * 文本水平对齐。
     */
    horizontalAlignment: HorizontalAlignment = HorizontalAlignment.CENTER,

    /**
     * 文本垂直对齐。
     */
    verticalAlignment: VerticalAlignment = VerticalAlignment.CENTER,

    /**
     * 按钮背景配置。
     */
    private val buttonStyleConfig: SimpleTextButtonStyleConfig = SimpleTextButtonStyleConfig(
        normalBg = BgConfig(
            Color.WHITE,
            BorderConfig(3, Color.BLACK)
        ),
        hoveredBg = BgConfig(
            Color.LIGHT_GRAY,
            BorderConfig(3, Color.BLACK)
        ),
        pressedBg = BgConfig(
            Color.DARK_GRAY,
            BorderConfig(3, Color.BLACK)
        ),
        disabledBg = BgConfig(
            Color.LIGHT_GRAY,
            BorderConfig(3, Color.GRAY)
        ),
        normalTextColor = Color.BLACK,
        pressedTextColor = Color.WHITE,
        disabledTextColor = Color.GRAY
    )

) : TextButtonViewBase(
    ButtonTextConfig(
        normalConfig = TextConfig(
            text = text,
            color = buttonStyleConfig.getNormalTextColor(),
            fontConfig = fontConfig,
            horizontalAlignment = horizontalAlignment,
            verticalAlignment = verticalAlignment
        ),
        hoveredConfig = TextConfig(
            text = text,
            color = buttonStyleConfig.getHoveredTextColor(),
            fontConfig = fontConfig,
            horizontalAlignment = horizontalAlignment,
            verticalAlignment = verticalAlignment
        ),
        pressedConfig = TextConfig(
            text = text,
            color = buttonStyleConfig.getPressedTextColor(),
            fontConfig = fontConfig,
            horizontalAlignment = horizontalAlignment,
            verticalAlignment = verticalAlignment
        ),
        disabledConfig = TextConfig(
            text = text,
            color = buttonStyleConfig.getDisabledTextColor(),
            fontConfig = fontConfig,
            horizontalAlignment = horizontalAlignment,
            verticalAlignment = verticalAlignment
        )
    ),
    area,
    showing
) {

    /**
     * 根据显示状态进行背景绘制。
     */
    override fun renderBgForState(g: Graphics, state: MouseInteractiveViewDisplayState) {

        paintBg(
            g,
            when (state) {
                MouseInteractiveViewDisplayState.HOVERED -> buttonStyleConfig.getHoveredBg()
                MouseInteractiveViewDisplayState.LEFT_PRESSED -> buttonStyleConfig.getPressedBg()
                else -> buttonStyleConfig.getNormalBg()
            }
        )
    }

    /**
     * 绘制 Disabled 状态的背景。
     */
    override fun renderBgOnDisabled(g: Graphics) {

        paintBg(g, buttonStyleConfig.getDisabledBg())
    }

    /**
     * 绘制背景。
     */
    private fun paintBg(g: Graphics, bgConfig: BgConfig) {

        // 保存原色。
        val originalColor = g.color

        // 渲染背景。
        g.color = bgConfig.bgColor
        g.fillRect(0, 0, getArea().width, getArea().height)
        if (bgConfig.borderConfig.borderWidth <= 0) {
            g.drawRect(0, 0, getArea().width, getArea().height)
        } else {
            paintBorder(g, bgConfig.borderConfig)
        }

        // 恢复原色。
        g.color = originalColor
    }

    /**
     * 绘制边框。
     */
    private fun paintBorder(g: Graphics, config: BorderConfig) {

        g.color = config.borderColor
        for (i in 0 until config.borderWidth) {
            g.drawRect(0 + i, 0 + i, getArea().width - 2 * i, getArea().height - 2 * i)
        }
    }
}