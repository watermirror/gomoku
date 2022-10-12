package org.bymc.gomoku.uifx.view

import org.bymc.gomoku.uifx.view.common.BgConfig
import java.awt.Color

/**
 * 简单文本按钮风格配置。
 *
 * @author: zheng.chez
 * @since: 2022/10/12
 */
data class SimpleTextButtonStyleConfig(

    /**
     * 普通状态的背景配置。
     */
    private val normalBg: BgConfig,

    /**
     * 悬停状态的背景配置。若为 null，采用普通状态的。
     */
    private val hoveredBg: BgConfig? = null,

    /**
     * 按下状态的背景配置。若为 null，采用悬停状态的。
     */
    private val pressedBg: BgConfig? = null,

    /**
     * 禁用状态的背景配置。若为 null，采用普通状态的。
     */
    private val disabledBg: BgConfig? = null,

    /**
     * 普通状态的文本颜色。
     */
    private val normalTextColor: Color,

    /**
     * 悬停状态的文本颜色。若为 null，采用普通状态的。
     */
    private val hoveredTextColor: Color? = null,

    /**
     * 按下状态的文本颜色。若为 null，采用悬停状态的。
     */
    private val pressedTextColor: Color? = null,

    /**
     * 禁用状态的文本颜色。若为 null，采用普通状态的。
     */
    private val disabledTextColor: Color? = null

) {

    /**
     * 获取普通状态的背景配置。
     */
    fun getNormalBg(): BgConfig = normalBg

    /**
     * 获取悬停状态的背景配置。
     */
    fun getHoveredBg(): BgConfig = hoveredBg ?: normalBg

    /**
     * 获取按下状态的背景配置。
     */
    fun getPressedBg(): BgConfig = pressedBg ?: getHoveredBg()

    /**
     * 获取禁用状态的背景配置。
     */
    fun getDisabledBg(): BgConfig = disabledBg ?: normalBg

    /**
     * 获取普通状态的文本颜色。
     */
    fun getNormalTextColor(): Color = normalTextColor

    /**
     * 获取悬停状态的文本颜色。
     */
    fun getHoveredTextColor(): Color = hoveredTextColor ?: normalTextColor

    /**
     * 获取按下状态的文本颜色。
     */
    fun getPressedTextColor(): Color = pressedTextColor ?: getHoveredTextColor()

    /**
     * 获取禁用状态的文本颜色。
     */
    fun getDisabledTextColor(): Color = disabledTextColor ?: normalTextColor
}