package org.bymc.gomoku.uifx.view.common

/**
 * 字体配置。
 *
 * @author: zheng.chez
 * @since: 2022/10/07
 */
data class FontConfig(

    /**
     * 字体名称。
     */
    val fontName: String = "LucidaGrande",

    /**
     * 字体尺寸。
     */
    val fontSize: Int = 13,

    /**
     * 字体风格。
     */
    val fontStyle: FontStyle = FontStyle.NORMAL
)