package org.bymc.gomoku.ui.animation

/**
 * 标题动画事件监听器。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
interface TitleAnimationEventListener {

    /**
     * 播放完成事件。
     */
    fun onFinished(animation: TitleAnimation)
}