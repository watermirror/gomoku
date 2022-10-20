package org.bymc.gomoku.uifx.animation.animator

import java.time.Duration

/**
 * 动画管理器接口。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
interface Animator {

    /**
     * 添加事件监听器。
     */
    fun addListener(listener: AnimationEventListener)

    /**
     * 移除事件监听器。
     */
    fun removeListener(listener: AnimationEventListener)

    /**
     * 获取动画总时长。
     */
    fun getDuration(): Duration

    /**
     * 获取播放延迟。
     */
    fun getDelay(): Duration

    /**
     * 获取播放游标。
     */
    fun getCursor(): Duration

    /**
     * 获取播放状态。
     */
    fun getState(): PlayingState

    /**
     * 从当前游标继续播放。
     */
    fun play()

    /**
     * 暂停。
     */
    fun pause()

    /**
     * 从头播放。
     */
    fun replay()
}