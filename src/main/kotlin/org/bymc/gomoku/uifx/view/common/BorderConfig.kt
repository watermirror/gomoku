package org.bymc.gomoku.uifx.view.common

import java.awt.Color

/**
 * 边框配置。
 *
 * @author: zheng.chez
 * @since: 2022/10/07
 */
data class BorderConfig(

    /**
     * 边框宽度。
     */
    val borderWidth: Int = 0,

    /**
     * 边框颜色。
     */
    val borderColor: Color = Color.BLACK
)