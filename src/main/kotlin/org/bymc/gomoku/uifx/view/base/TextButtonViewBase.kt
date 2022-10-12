package org.bymc.gomoku.uifx.view.base

import org.bymc.gomoku.uifx.view.common.ButtonTextConfig
import org.bymc.gomoku.uifx.view.common.TextConfig
import org.bymc.gomoku.uifx.view.mouse.MouseInteractiveViewDisplayState
import org.bymc.gomoku.uifx.view.painter.TextPainter
import java.awt.Graphics
import java.awt.Insets
import java.awt.Rectangle

/**
 * 文本按钮视图基类。
 *
 * @author: zheng.chez
 * @since: 2022/10/12
 */
abstract class TextButtonViewBase(

    /**
     * 按钮文本配置。
     */
    private var buttonTextConfig: ButtonTextConfig = ButtonTextConfig(),

    /**
     * 文本按钮的区域。
     */
    area: Rectangle = Rectangle(0, 0, 0, 0),

    /**
     * 文本按钮是否可见。
     */
    showing: Boolean = true,

    /**
     * 文本内嵌尺寸。
     */
    private var insets: Insets = Insets(4, 4, 4, 4)

) : ButtonViewBase(area, showing) {

    /**
     * 根据显示状态进行背景绘制。
     */
    abstract fun renderBgForState(g: Graphics, state: MouseInteractiveViewDisplayState)

    /**
     * 绘制 Disabled 状态的背景。
     */
    abstract fun renderBgOnDisabled(g: Graphics)

    /**
     * 获取按钮文本配置。
     */
    fun getButtonTextConfig(): ButtonTextConfig = buttonTextConfig

    /**
     * 设置按钮文本配置。
     */
    fun setButtonTextConfig(config: ButtonTextConfig) {

        buttonTextConfig = config
        scheduleRender()
    }

    /**
     * 获取内嵌尺寸。
     */
    fun getInsets(): Insets = insets

    /**
     * 设置内嵌尺寸。
     */
    fun setInsets(insets: Insets) {

        this.insets = insets
        scheduleRender()
    }

    /**
     * 根据显示状态进行绘制。
     */
    override fun renderForState(g: Graphics, state: MouseInteractiveViewDisplayState) {

        renderBgForState(g, state)
        paintText(
            g,
            when (state) {
                MouseInteractiveViewDisplayState.HOVERED -> buttonTextConfig.getHoveredConfig()
                MouseInteractiveViewDisplayState.LEFT_PRESSED -> buttonTextConfig.getPressedConfig()
                else -> buttonTextConfig.getNormalConfig()
            }
        )
    }

    /**
     * 绘制 Disabled 状态。
     */
    override fun renderOnDisabled(g: Graphics) {

        renderBgOnDisabled(g)
        paintText(g, buttonTextConfig.getDisabledConfig())
    }

    /**
     * 绘制文本。
     */
    private fun paintText(g: Graphics, config: TextConfig) {

        // 计算内嵌后的绘制区域。
        val area = Rectangle(
            insets.left, insets.top,
            getArea().width - insets.left - insets.right, getArea().height - insets.top - insets.bottom
        )

        // 渲染文本。
        TextPainter(
            config.text, config.color, area, config.horizontalAlignment, config.verticalAlignment, config.fontConfig
        ).paint(g)
    }
}