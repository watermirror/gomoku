package org.bymc.gomoku.uifx.animation.animator.internal

import org.bymc.gomoku.uifx.animation.animator.AnimationEventListener
import org.bymc.gomoku.uifx.animation.animator.Animator
import org.bymc.gomoku.uifx.animation.animator.PlayingState
import org.bymc.gomoku.uifx.animation.easefunc.EaseFunction
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.lang.Long.max
import java.time.Duration
import javax.swing.Timer

/**
 * 动画管理器基类。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
abstract class AnimatorBase(

    /**
     * 动画总时长。
     */
    private val duration: Duration,

    /**
     * 缓动函数。
     */
    private val easeFunction: EaseFunction,

    /**
     * 播放前的延迟。
     */
    private val delay: Duration = Duration.ZERO

) : Animator, ActionListener {

    /**
     * 监听器列表。
     */
    private val listeners: MutableList<AnimationEventListener> = ArrayList()

    /**
     * 上一次开始播放的游标。
     */
    private var lastBeginPlayingDuration: Duration = Duration.ZERO

    /**
     * 播放状态。
     */
    private var state: PlayingState = PlayingState.READY

    /**
     * 定时器。
     */
    private val timer: Timer = Timer(1, this)

    /**
     * 基准时间。
     */
    private var basedMillis: Long = 0L

    // 设定计时器模式。
    init {
        timer.isRepeats = true
    }

    /**
     * 帧时间点通知。
     */
    abstract fun onTick(progress: Double)

    /**
     * 添加事件监听器。
     */
    override fun addListener(listener: AnimationEventListener) {

        if (listeners.contains(listener)) { return }
        listeners.add(listener)
    }

    /**
     * 移除事件监听器。
     */
    override fun removeListener(listener: AnimationEventListener) {

        val index = listeners.indexOf(listener)
        if (index < 0) { return }
        listeners.removeAt(index)
    }

    /**
     * 获取动画总时长。
     */
    override fun getDuration(): Duration = duration

    /**
     * 获取播放延迟。
     */
    override fun getDelay(): Duration = delay

    /**
     * 获取播放游标。
     */
    override fun getCursor(): Duration {

        return when (state) {
            PlayingState.PAYING -> Duration.ofMillis(
                max(
                    0,
                    lastBeginPlayingDuration.toMillis() + (System.currentTimeMillis() - basedMillis) - delay.toMillis()
                )
            )
            PlayingState.PAUSED -> Duration.ofMillis(max(0, lastBeginPlayingDuration.toMillis() - delay.toMillis()))
            PlayingState.FINISHED -> duration
            PlayingState.READY -> Duration.ZERO
        }
    }

    /**
     * 获取播放状态。
     */
    override fun getState(): PlayingState = state

    /**
     * 从当前游标继续播放。
     */
    override fun play() {

        if (state in listOf(PlayingState.PAYING, PlayingState.FINISHED)) { return }
        basedMillis = System.currentTimeMillis()
        state = PlayingState.PAYING
        val cursorMillis =
            max(0, lastBeginPlayingDuration.toMillis() + (System.currentTimeMillis() - basedMillis) - delay.toMillis())
        val x = cursorMillis.toDouble() / duration.toMillis().toDouble()
        onTick(easeFunction.getValue(x))
        timer.start()
    }

    /**
     * 暂停。
     */
    override fun pause() {

        if (state != PlayingState.PAYING) { return }
        timer.stop()
        state = PlayingState.PAUSED
        lastBeginPlayingDuration =
            Duration.ofMillis(lastBeginPlayingDuration.toMillis() + (System.currentTimeMillis() - basedMillis))
    }

    /**
     * 从头播放。
     */
    override fun replay() {

        if (state == PlayingState.PAYING) {
            timer.stop()
        }
        lastBeginPlayingDuration = Duration.ZERO
        basedMillis = System.currentTimeMillis()
        state = PlayingState.PAYING
        timer.start()
    }

    /**
     * Invoked when an action occurs.
     * @param e the event to be processed
     */
    override fun actionPerformed(e: ActionEvent?) {

        if (state != PlayingState.PAYING) { return }
        val cursorMillis =
            max(0, lastBeginPlayingDuration.toMillis() + (System.currentTimeMillis() - basedMillis) - delay.toMillis())
        val x = cursorMillis.toDouble() / duration.toMillis().toDouble()
        onTick(easeFunction.getValue(x))
        if (x >= 1.0) {
            timer.stop()
            state = PlayingState.FINISHED
            listeners.forEach { it.onFinished(this) }
        }
    }
}