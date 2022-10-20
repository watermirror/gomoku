package org.bymc.gomoku.uifx.animation.animator

import org.bymc.gomoku.uifx.animation.animator.internal.ViewAreaAnimator
import org.bymc.gomoku.uifx.animation.easefunc.EaseFunction
import org.bymc.gomoku.uifx.view.base.View
import java.awt.Rectangle
import java.time.Duration

/**
 * 动画管理器工厂类。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
object Animators {

    /**
     * 创建视图区域动画管理器。
     */
    fun viewAreaAnimator(
        duration: Duration, easeFunction: EaseFunction, view: View, targetArea: Rectangle, baseArea: Rectangle? = null,
        delay: Duration = Duration.ZERO
    ): Animator {

        return ViewAreaAnimator(duration, easeFunction, view, targetArea, baseArea, delay)
    }
}