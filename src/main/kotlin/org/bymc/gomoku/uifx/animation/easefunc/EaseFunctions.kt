package org.bymc.gomoku.uifx.animation.easefunc

import org.bymc.gomoku.uifx.animation.easefunc.internal.EaseInOut
import org.bymc.gomoku.uifx.animation.easefunc.internal.EaseInOutBack
import org.bymc.gomoku.uifx.animation.easefunc.internal.ParameterNormalizationFunction

/**
 * 缓动函数工厂类。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
object EaseFunctions {

    /**
     * Ease In Out 缓动函数。
     */
    fun easeInOut(): EaseFunction = ParameterNormalizationFunction(EaseInOut())

    /**
     * Ease In Out Back 缓动函数。
     */
    fun easeInOutBack(): EaseFunction = ParameterNormalizationFunction(EaseInOutBack())
}