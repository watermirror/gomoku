package org.bymc.gomoku.uifx.view.common

import java.awt.Color

/**
 * 背景配置对象。
 *
 * @author: zheng.chez
 * @since: 2022/10/12
 */
data class BgConfig(

    /**
     * 背景色。
     */
    val bgColor: Color? = Color.WHITE,

    /**
     * 边框配置。
     */
    val borderConfig: BorderConfig = BorderConfig()
)