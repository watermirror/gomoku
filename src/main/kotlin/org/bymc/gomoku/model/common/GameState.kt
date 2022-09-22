package org.bymc.gomoku.model.common

/**
 * 棋局状态。
 *
 * @author: zheng.chez
 * @since: 2022/09/22
 */
enum class GameState {

    /**
     * 进行中。
     */
    PLAYING,

    /**
     * 黑棋胜。
     */
    BLACK_WON,

    /**
     * 白棋胜。
     */
    WHITE_WON,

    /**
     * 和局。
     */
    DRAW
}