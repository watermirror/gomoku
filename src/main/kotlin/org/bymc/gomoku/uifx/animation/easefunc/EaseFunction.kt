package org.bymc.gomoku.uifx.animation.easefunc

/**
 * 缓动函数。定义域为 [0, 1]。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
interface EaseFunction {

    /**
     * 函数求值。定义域为 [0, 1]，超出范围的 x 所得到的函数值具有不确定性。
     */
    fun getValue(x: Double): Double
}