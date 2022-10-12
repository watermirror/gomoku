package org.bymc.gomoku.uifx.view.common

import java.awt.Color

/**
 * 文本配置。
 *
 * @author: zheng.chez
 * @since: 2022/10/12
 */
data class TextConfig(

    /**
     * 文本内容。
     */
    val text: String = "",

    /**
     * 文本颜色。
     */
    val color: Color = Color.BLACK,

    /**
     * 字体配置。
     */
    val fontConfig: FontConfig = FontConfig(),

    /**
     * 水平对齐。
     */
    val horizontalAlignment: HorizontalAlignment = HorizontalAlignment.CENTER,

    /**
     * 垂直对齐。
     */
    val verticalAlignment: VerticalAlignment = VerticalAlignment.CENTER
)