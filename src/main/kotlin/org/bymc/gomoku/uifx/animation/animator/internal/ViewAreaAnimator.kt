package org.bymc.gomoku.uifx.animation.animator.internal

import org.bymc.gomoku.uifx.animation.easefunc.EaseFunction
import org.bymc.gomoku.uifx.view.base.View
import java.awt.Rectangle
import java.time.Duration

/**
 * 视图区域动画管理器。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
class ViewAreaAnimator(

    /**
     * 动画总时长。
     */
    duration: Duration,

    /**
     * 缓动函数。
     */
    easeFunction: EaseFunction,

    /**
     * 执行动画的视图。
     */
    private val view: View,

    /**
     * 视图的目标区域。
     */
    private val targetArea: Rectangle,

    /**
     * 视图的起始区域。若为 null，则以视图当前区域为起始区域。
     */
    private val baseArea: Rectangle? = null,

    /**
     * 动画启动延迟。
     */
    delay: Duration = Duration.ZERO

) : AnimatorBase(duration, easeFunction, delay) {

    /**
     * 帧时间点通知。
     */
    override fun onTick(progress: Double) {

        val baseArea = this.baseArea ?: view.getArea()
        view.setArea(
            Rectangle(
                calculateCurrentValue(progress, baseArea.x, targetArea.x),
                calculateCurrentValue(progress, baseArea.y, targetArea.y),
                calculateCurrentValue(progress, baseArea.width, targetArea.width),
                calculateCurrentValue(progress, baseArea.height, targetArea.height)
            )
        )
    }

    /**
     * 根据起始值、目标值、归一化的进度值，计算当前值。
     */
    private fun calculateCurrentValue(progress: Double, base: Int, target: Int): Int {

        return (base + (target - base).toDouble() * progress).toInt()
    }
}