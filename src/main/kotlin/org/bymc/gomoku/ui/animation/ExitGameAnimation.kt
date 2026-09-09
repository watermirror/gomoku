package org.bymc.gomoku.ui.animation

import org.bymc.gomoku.ui.view.BoardIntegrationView
import org.bymc.gomoku.ui.view.CoveringLayerView
import org.bymc.gomoku.ui.view.SidePanelView
import org.bymc.gomoku.ui.view.TitlePanelView
import org.bymc.gomoku.ui.window.UiConfig
import org.bymc.gomoku.uifx.animation.animator.AnimationEventListener
import org.bymc.gomoku.uifx.animation.animator.Animator
import org.bymc.gomoku.uifx.animation.animator.Animators
import org.bymc.gomoku.uifx.animation.animator.PlayingState
import org.bymc.gomoku.uifx.animation.easefunc.EaseFunctions
import org.bymc.gomoku.uifx.view.SimpleTextButtonView
import java.awt.Rectangle

/**
 * Exit game animation: game screen -> title screen.
 * Reverse of EnterGameAnimation: side panel slides out right, board slides back to center,
 * title panel and buttons slide in from left.
 */
class ExitGameAnimation(

    uiConfig: UiConfig,
    boardView: BoardIntegrationView,
    titlePanelView: TitlePanelView,
    selfStudyButtonView: SimpleTextButtonView,
    challengeAiButtonView: SimpleTextButtonView,
    dualButtonView: SimpleTextButtonView,
    sidePanelView: SidePanelView,
    private val coveringLayerView: CoveringLayerView

) : AnimationEventListener {

    private var listener: ExitGameAnimationEventListener? = null

    private val duration = uiConfig.getExitGameAnimationDuration()
    private val easeFunction = EaseFunctions.easeInOut()

    // Side panel: right of board -> off-screen right
    private val sidePanelAnimator: Animator = Animators.viewAreaAnimator(
        duration, easeFunction, sidePanelView,
        Rectangle(
            uiConfig.getMainWindowClientSize().width,
            0,
            uiConfig.getSidePanelWidth(),
            uiConfig.getSidePanelHeight()
        )
    )

    // Board: left-aligned -> center
    private val boardAnimator: Animator = Animators.viewAreaAnimator(
        duration, easeFunction, boardView,
        Rectangle(
            (uiConfig.getMainWindowClientSize().width - uiConfig.getBoardSize().width) / 2,
            0,
            uiConfig.getBoardSize().width,
            uiConfig.getBoardSize().height
        )
    )

    // Title panel: off-screen left -> center
    private val titlePanelAnimator: Animator = Animators.viewAreaAnimator(
        duration, easeFunction, titlePanelView,
        Rectangle(
            (uiConfig.getMainWindowClientSize().width - uiConfig.getTitlePanelSize().width) / 2,
            (uiConfig.getMainWindowClientSize().height - uiConfig.getTitlePanelSize().height) / 2,
            uiConfig.getTitlePanelSize().width,
            uiConfig.getTitlePanelSize().height
        )
    )

    // Self-study button: off-screen left -> center
    private val selfStudyBtnAnimator: Animator = Animators.viewAreaAnimator(
        duration, easeFunction, selfStudyButtonView,
        Rectangle(
            (uiConfig.getMainWindowClientSize().width - uiConfig.getSelfStudyButtonSize().width) / 2,
            uiConfig.getSelfStudyButtonTopMargin(),
            uiConfig.getSelfStudyButtonSize().width,
            uiConfig.getSelfStudyButtonSize().height
        )
    )

    // Challenge AI button: off-screen left -> center
    private val challengeAiBtnAnimator: Animator = Animators.viewAreaAnimator(
        duration, easeFunction, challengeAiButtonView,
        Rectangle(
            (uiConfig.getMainWindowClientSize().width - uiConfig.getChallengeAiButtonSize().width) / 2,
            uiConfig.getChallengeAiButtonTopMargin(),
            uiConfig.getChallengeAiButtonSize().width,
            uiConfig.getChallengeAiButtonSize().height
        )
    )

    // Dual button: off-screen left -> center
    private val dualBtnAnimator: Animator = Animators.viewAreaAnimator(
        duration, easeFunction, dualButtonView,
        Rectangle(
            (uiConfig.getMainWindowClientSize().width - uiConfig.getDualButtonSize().width) / 2,
            uiConfig.getDualButtonTopMargin(),
            uiConfig.getDualButtonSize().width,
            uiConfig.getDualButtonSize().height
        )
    )

    init {

        sidePanelAnimator.addListener(this)
        boardAnimator.addListener(this)
        titlePanelAnimator.addListener(this)
        selfStudyBtnAnimator.addListener(this)
        challengeAiBtnAnimator.addListener(this)
        dualBtnAnimator.addListener(this)
    }

    fun setEventListener(listener: ExitGameAnimationEventListener?) {

        this.listener = listener
    }

    fun play() {

        if (isPlaying()) { return }
        coveringLayerView.setShowing(true)
        sidePanelAnimator.replay()
        boardAnimator.replay()
        titlePanelAnimator.replay()
        selfStudyBtnAnimator.replay()
        challengeAiBtnAnimator.replay()
        dualBtnAnimator.replay()
    }

    override fun onFinished(animator: Animator) {

        if (isPlaying()) { return }
        coveringLayerView.setShowing(false)
        listener?.onFinished(this)
    }

    private fun isPlaying(): Boolean {

        return sidePanelAnimator.getState() == PlayingState.PAYING ||
            boardAnimator.getState() == PlayingState.PAYING ||
            titlePanelAnimator.getState() == PlayingState.PAYING ||
            selfStudyBtnAnimator.getState() == PlayingState.PAYING ||
            challengeAiBtnAnimator.getState() == PlayingState.PAYING ||
            dualBtnAnimator.getState() == PlayingState.PAYING
    }
}
