package org.bymc.gomoku.uifx.animation.animator

/**
 * 播放状态。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
enum class PlayingState {

    /**
     * 准备播放，即初始状态。
     */
    READY,

    /**
     * 播放。
     */
    PAYING,

    /**
     * 暂停。
     */
    PAUSED,

    /**
     * 播完。
     */
    FINISHED
}