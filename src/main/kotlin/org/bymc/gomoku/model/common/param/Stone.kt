package org.bymc.gomoku.model.common.param

/**
 * 棋子。
 *
 * @author: zheng.chez
 * @since: 2022/09/22
 */
enum class Stone {

    /**
     * 黑子。
     */
    BLACK,

    /**
     * 白子。
     */
    WHITE;

    companion object {

        /**
         * 获取对手棋子。
         */
        fun getOpponent(stone: Stone): Stone = when (stone) {
            BLACK -> WHITE
            else -> BLACK
        }
    }
}