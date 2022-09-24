package org.bymc.gomoku.model.common.param

/**
 * 极轴。
 *
 * @author: zheng.chez
 * @since: 2022/09/23
 */
enum class Polar(

    /**
     * 近端方向。
     */
    val nearEnd: Direction,

    /**
     * 远端方向。
     */
    val farEnd: Direction

) {

    /**
     * 北-南。
     */
    N2S(Direction.NORTH, Direction.SOUTH),

    /**
     * 东北-西南。
     */
    NE2SW(Direction.NORTHEAST, Direction.SOUTHWEST),

    /**
     * 东-西。
     */
    E2W(Direction.EAST, Direction.WEST),

    /**
     * 东南-西北。
     */
    SE2NW(Direction.SOUTHEAST, Direction.NORTHWEST),

    /**
     * 南-北。
     */
    S2N(Direction.SOUTH, Direction.NORTH),

    /**
     * 西南-东北。
     */
    SW2NE(Direction.SOUTHWEST, Direction.NORTHEAST),

    /**
     * 西-东。
     */
    W2E(Direction.WEST, Direction.EAST),

    /**
     * 西北-东南。
     */
    NW2SE(Direction.NORTHWEST, Direction.SOUTHEAST);

    companion object {

        /**
         * 获取归一化的极轴。
         */
        fun getNormalizedPolars(): List<Polar> = listOf(N2S, NE2SW, E2W, SE2NW)

        /**
         * 获取反向极轴。
         */
        fun reverse(polar: Polar): Polar = when (polar) {
            N2S -> S2N
            NE2SW -> SW2NE
            E2W -> W2E
            SE2NW -> NW2SE
            S2N -> N2S
            SW2NE -> NE2SW
            W2E -> E2W
            NW2SE -> SE2NW
        }
    }
}