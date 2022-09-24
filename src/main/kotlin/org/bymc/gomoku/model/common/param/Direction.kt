package org.bymc.gomoku.model.common.param

/**
 * 方位。
 *
 * @author: zheng.chez
 * @since: 2022/09/22
 */
enum class Direction(

    /**
     * X 轴单位偏移。
     */
    val offsetX: Int,

    /**
     * Y 轴单位偏移。
     */
    val offsetY: Int

) {

    /**
     * 上。
     */
    NORTH(0, 1),

    /**
     * 右上。
     */
    NORTHEAST(1, 1),

    /**
     * 右。
     */
    EAST(1, 0),

    /**
     * 右下。
     */
    SOUTHEAST(1, -1),

    /**
     * 下。
     */
    SOUTH(0, -1),

    /**
     * 左下。
     */
    SOUTHWEST(-1, -1),

    /**
     * 左。
     */
    WEST(-1, 0),

    /**
     * 左上。
     */
    NORTHWEST(-1, 1)
}