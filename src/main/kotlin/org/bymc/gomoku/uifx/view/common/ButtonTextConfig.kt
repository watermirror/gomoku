package org.bymc.gomoku.uifx.view.common

/**
 * 按钮文本配置对象。
 *
 * @author: zheng.chez
 * @since: 2022/10/12
 */
data class ButtonTextConfig(

    /**
     * 普通状态的文本配置。
     */
    private val normalConfig: TextConfig = TextConfig(),

    /**
     * 悬停状态的文本配置。若为 null，采用普通状态的。
     */
    private val hoveredConfig: TextConfig? = null,

    /**
     * 按下状态的文本配置。若为 null，采用悬停状态的。
     */
    private val pressedConfig: TextConfig? = null,

    /**
     * 禁用状态的文本配置。若为 null，采用普通状态的。
     */
    private val disabledConfig: TextConfig? = null

) {

    /**
     * 获取普通状态的文本配置。
     */
    fun getNormalConfig(): TextConfig = normalConfig

    /**
     * 获取悬停状态的文本配置。
     */
    fun getHoveredConfig(): TextConfig = hoveredConfig ?: normalConfig

    /**
     * 获取按下状态的文本配置。
     */
    fun getPressedConfig(): TextConfig = pressedConfig ?: getHoveredConfig()

    /**
     * 获取禁用状态的文本配置。
     */
    fun getDisabledConfig(): TextConfig = disabledConfig ?: normalConfig
}