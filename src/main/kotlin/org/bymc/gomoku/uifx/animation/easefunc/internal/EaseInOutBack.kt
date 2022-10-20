package org.bymc.gomoku.uifx.animation.easefunc.internal

import org.bymc.gomoku.uifx.animation.easefunc.EaseFunction

/**
 * Ease In Out Back 缓动函数。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
class EaseInOutBack : EaseFunction {

    /**
     * 函数求值。定义域为 [0, 1]，超出范围的 x 所得到的函数值具有不确定性。
     */
    override fun getValue(x: Double): Double {

        val seed = 1.70158 * 1.525
        val r = x / 0.5
        return when {
            r < 1.0 -> 0.5 * (r * r * ((seed + 1) * r - seed))
            else -> {
                val s = r - 2.0
                0.5 * (s * s * ((seed + 1) * s + seed) + 2)
            }
        }
    }
}