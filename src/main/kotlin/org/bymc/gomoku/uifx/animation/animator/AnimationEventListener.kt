package org.bymc.gomoku.uifx.animation.animator

/**
 * 动画事件监听器。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
interface AnimationEventListener {

    /**
     * 动画结束事件。
     */
    fun onFinished(animator: Animator)
}