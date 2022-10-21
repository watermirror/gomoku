package org.bymc.gomoku.ui.animation

import org.bymc.gomoku.ui.view.BoardIntegrationView
import org.bymc.gomoku.ui.view.CoveringLayerView
import org.bymc.gomoku.ui.view.TitlePanelView
import org.bymc.gomoku.ui.window.UiConfig
import org.bymc.gomoku.uifx.animation.animator.AnimationEventListener
import org.bymc.gomoku.uifx.animation.animator.Animator
import org.bymc.gomoku.uifx.animation.animator.Animators
import org.bymc.gomoku.uifx.animation.animator.PlayingState
import org.bymc.gomoku.uifx.animation.easefunc.EaseFunction
import org.bymc.gomoku.uifx.animation.easefunc.EaseFunctions
import org.bymc.gomoku.uifx.view.SimpleTextButtonView
import java.awt.Rectangle
import java.time.Duration

/**
 * 标题动画。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
class TitleAnimation(

    /**
     * UI 配置。
     */
    uiConfig: UiConfig,

    /**
     * 棋盘视图。
     */
    boardView: BoardIntegrationView,

    /**
     * 标题面板视图。
     */
    titlePanelView: TitlePanelView,

    /**
     * 研学模式按钮。
     */
    selfStudyButtonView: SimpleTextButtonView,

    /**
     * AI 模式按钮。
     */
    challengeAiButtonView: SimpleTextButtonView,

    /**
     * 对战模式按钮。
     */
    dualButtonView: SimpleTextButtonView,

    /**
     * 遮盖视图。
     */
    private val coveringLayerView: CoveringLayerView,

    /**
     * 缓动函数。
     */
    easeFunction: EaseFunction = EaseFunctions.easeInOutBack(),

) : AnimationEventListener {

    /**
     * 事件监听器。
     */
    private var listener: TitleAnimationEventListener? = null

    /**
     * 棋枰动画。
     */
    private val boardViewAnimator: Animator = Animators.viewAreaAnimator(
        uiConfig.getTitleBoardAnimationDuration(),
        easeFunction,
        boardView,
        Rectangle(
            (uiConfig.getMainWindowClientSize().width - uiConfig.getBoardSize().width) / 2,
            0,
            uiConfig.getBoardSize().width,
            uiConfig.getBoardSize().height
        ),
        Rectangle(
            -uiConfig.getBoardSize().width,
            0,
            uiConfig.getBoardSize().width,
            uiConfig.getBoardSize().height
        ),
    )

    /**
     * 标题面板动画。
     */
    private val titlePanelViewAnimator: Animator = Animators.viewAreaAnimator(
        uiConfig.getTitlePanelAnimationDuration(),
        easeFunction,
        titlePanelView,
        Rectangle(
            (uiConfig.getMainWindowClientSize().width - uiConfig.getTitlePanelSize().width) / 2,
            (uiConfig.getMainWindowClientSize().height - uiConfig.getTitlePanelSize().height) / 2,
            uiConfig.getTitlePanelSize().width,
            uiConfig.getTitlePanelSize().height
        ),
        Rectangle(
            -uiConfig.getBoardSize().width + (uiConfig.getBoardSize().width - uiConfig.getTitlePanelSize().width) / 2,
            (uiConfig.getMainWindowClientSize().height - uiConfig.getTitlePanelSize().height) / 2,
            uiConfig.getTitlePanelSize().width,
            uiConfig.getTitlePanelSize().height
        ),
        uiConfig.getTitlePanelAnimationDelay()
    )

    /**
     * 研学模式按钮动画。
     */
    private val selfStudyButtonViewAnimator: Animator = Animators.viewAreaAnimator(
        uiConfig.getTitleButtonAnimationDuration(),
        easeFunction,
        selfStudyButtonView,
        Rectangle(
            (uiConfig.getMainWindowClientSize().width - uiConfig.getSelfStudyButtonSize().width) / 2,
            uiConfig.getSelfStudyButtonTopMargin(),
            uiConfig.getSelfStudyButtonSize().width,
            uiConfig.getSelfStudyButtonSize().height
        ),
        Rectangle(
            -uiConfig.getBoardSize().width +
                    (uiConfig.getBoardSize().width - uiConfig.getSelfStudyButtonSize().width) / 2,
            uiConfig.getSelfStudyButtonTopMargin(),
            uiConfig.getSelfStudyButtonSize().width,
            uiConfig.getSelfStudyButtonSize().height
        ),
        uiConfig.getTitleButtonAnimationDelay()
    )

    /**
     * AI 模式按钮动画。
     */
    private val challengeAiButtonViewAnimator: Animator = Animators.viewAreaAnimator(
        uiConfig.getTitleButtonAnimationDuration(),
        easeFunction,
        challengeAiButtonView,
        Rectangle(
            (uiConfig.getMainWindowClientSize().width - uiConfig.getChallengeAiButtonSize().width) / 2,
            uiConfig.getChallengeAiButtonTopMargin(),
            uiConfig.getChallengeAiButtonSize().width,
            uiConfig.getChallengeAiButtonSize().height
        ),
        Rectangle(
            -uiConfig.getBoardSize().width +
                    (uiConfig.getBoardSize().width - uiConfig.getChallengeAiButtonSize().width) / 2,
            uiConfig.getChallengeAiButtonTopMargin(),
            uiConfig.getChallengeAiButtonSize().width,
            uiConfig.getChallengeAiButtonSize().height
        ),
        uiConfig.getTitleButtonAnimationDelay() + uiConfig.getTitleButtonAnimationDelayAcceleration()
    )

    /**
     * 对战模式按钮动画。
     */
    private val dualButtonViewAnimator: Animator = Animators.viewAreaAnimator(
        uiConfig.getTitleButtonAnimationDuration(),
        easeFunction,
        dualButtonView,
        Rectangle(
            (uiConfig.getMainWindowClientSize().width - uiConfig.getDualButtonSize().width) / 2,
            uiConfig.getDualButtonTopMargin(),
            uiConfig.getDualButtonSize().width,
            uiConfig.getDualButtonSize().height
        ),
        Rectangle(
            -uiConfig.getBoardSize().width +
                    (uiConfig.getBoardSize().width - uiConfig.getDualButtonSize().width) / 2,
            uiConfig.getDualButtonTopMargin(),
            uiConfig.getDualButtonSize().width,
            uiConfig.getDualButtonSize().height
        ),
        uiConfig.getTitleButtonAnimationDelay() +
                Duration.ofMillis(uiConfig.getTitleButtonAnimationDelayAcceleration().toMillis() * 2)
    )

    /**
     * 配置动画管理器事件监听器。
     */
    init {

        boardViewAnimator.addListener(this)
        titlePanelViewAnimator.addListener(this)
        selfStudyButtonViewAnimator.addListener(this)
        challengeAiButtonViewAnimator.addListener(this)
        dualButtonViewAnimator.addListener(this)
    }

    /**
     * 设定事件监听器。null 表示移除监听器。
     */
    fun setEventListener(listener: TitleAnimationEventListener?) {

        this.listener = listener
    }

    /**
     * 播放。
     */
    fun play() {

        if (isPlaying()) { return }
        coveringLayerView.setShowing(true)
        boardViewAnimator.replay()
        titlePanelViewAnimator.replay()
        selfStudyButtonViewAnimator.replay()
        challengeAiButtonViewAnimator.replay()
        dualButtonViewAnimator.replay()
    }

    /**
     * 动画结束事件。
     */
    override fun onFinished(animator: Animator) {

        if (isPlaying()) { return }
        coveringLayerView.setShowing(false)
        listener?.onFinished(this)
    }

    /**
     * 判断标题动画是否正在播放。
     */
    private fun isPlaying(): Boolean {

        return boardViewAnimator.getState() == PlayingState.PAYING ||
                titlePanelViewAnimator.getState() == PlayingState.PAYING ||
                selfStudyButtonViewAnimator.getState() == PlayingState.PAYING ||
                challengeAiButtonViewAnimator.getState() == PlayingState.PAYING ||
                dualButtonViewAnimator.getState() == PlayingState.PAYING
    }
}