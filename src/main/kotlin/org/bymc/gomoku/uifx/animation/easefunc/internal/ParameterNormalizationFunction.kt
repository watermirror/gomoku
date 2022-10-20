package org.bymc.gomoku.uifx.animation.easefunc.internal

import org.bymc.gomoku.uifx.animation.easefunc.EaseFunction

/**
 * 缓动函数参数规范化函数。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
class ParameterNormalizationFunction(

    /**
     * 被规范的缓动函数。
     */
    private val easeFunc: EaseFunction

) : EaseFunction {

    /**
     * 规范化过程将参数范围限定在区间 [0, 1]。
     */
    override fun getValue(x: Double): Double {

        return easeFunc.getValue(
            when {
                x < 0.0 -> 0.0
                x > 1.0 -> 1.0
                else -> x
            }
        )
    }
}