package org.bymc.gomoku.uifx.view.common

import java.awt.Font

/**
 * 字体风格。
 *
 * @author: zheng.chez
 * @since: 2022/10/07
 */
enum class FontStyle(val rawStyle: Int) {

    /**
     * 常规体。
     */
    NORMAL(Font.PLAIN),

    /**
     * 粗体。
     */
    BOLD(Font.BOLD),

    /**
     * 斜体。
     */
    ITALIC(Font.ITALIC),

    /**
     * 粗斜体。
     */
    BOLD_ITALIC(Font.BOLD + Font.ITALIC)
}