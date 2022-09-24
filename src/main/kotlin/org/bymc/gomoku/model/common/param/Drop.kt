package org.bymc.gomoku.model.common.param

/**
 * 落子操作。
 *
 * @author: zheng.chez
 * @since: 2022/09/22
 */
data class Drop(

    /**
     * 落子的坐标，坐标始于 (0, 0)。
     */
    val location: Location2D,

    /**
     * 落子的棋子。
     */
    val stone: Stone
)