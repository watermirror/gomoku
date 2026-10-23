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
 * Enter game animation: title screen -> game screen.
 * Board slides from center to left-aligned, title panel and buttons slide off-screen left,
 * side panel slides in from off-screen right.
 */
class EnterGameAnimation(

    uiConfig: UiConfig,
    boardView: BoardIntegrationView,
    titlePanelView: TitlePanelView,
    selfStudyButtonView: SimpleTextButtonView,
    challengeAiButtonView: SimpleTextButtonView,
    dualButtonView: SimpleTextButtonView,
    sidePanelView: SidePanelView,
    private val coveringLayerView: CoveringLayerView

) : AnimationEventListener {

    private var listener: EnterGameAnimationEventListener? = null

    private val duration = uiConfig.getEnterGameAnimationDuration()

    // Overshoot easing for all elements; fixed base areas keep the overshoot bounded to a slight bounce.
    private val easeFunction = EaseFunctions.easeInOutBack()

    // Staggered start delays: title panel leaves first, then buttons one by one; side panel follows the board.
    private val panelDelay = uiConfig.getGameAnimationPanelDelay()
    private val buttonDelayStep = uiConfig.getGameAnimationButtonDelayStep()

    // Board: centered -> left-aligned (x=0)
    private val boardAnimator: Animator = Animators.viewAreaAnimator(
        duration, easeFunction, boardView,
        Rectangle(0, 0, uiConfig.getBoardSize().width, uiConfig.getBoardSize().height),
        Rectangle(
            (uiConfig.getMainWindowClientSize().width - uiConfig.getBoardSize().width) / 2,
            0,
            uiConfig.getBoardSize().width,
            uiConfig.getBoardSize().height
        )
    )

    // Title panel: center -> off-screen left (extra 10px margin to hide shadow)
    private val titlePanelAnimator: Animator = Animators.viewAreaAnimator(
        duration, easeFunction, titlePanelView,
        Rectangle(
            -uiConfig.getTitlePanelSize().width - 10,
            (uiConfig.getMainWindowClientSize().height - uiConfig.getTitlePanelSize().height) / 2,
            uiConfig.getTitlePanelSize().width,
            uiConfig.getTitlePanelSize().height
        ),
        Rectangle(
            (uiConfig.getMainWindowClientSize().width - uiConfig.getTitlePanelSize().width) / 2,
            (uiConfig.getMainWindowClientSize().height - uiConfig.getTitlePanelSize().height) / 2,
            uiConfig.getTitlePanelSize().width,
            uiConfig.getTitlePanelSize().height
        )
    )

    // Self-study button: center -> off-screen left (extra 10px margin to hide shadow)
    private val selfStudyBtnAnimator: Animator = Animators.viewAreaAnimator(
        duration, easeFunction, selfStudyButtonView,
        Rectangle(
            -uiConfig.getSelfStudyButtonSize().width - 10,
            uiConfig.getSelfStudyButtonTopMargin(),
            uiConfig.getSelfStudyButtonSize().width,
            uiConfig.getSelfStudyButtonSize().height
        ),
        Rectangle(
            (uiConfig.getMainWindowClientSize().width - uiConfig.getSelfStudyButtonSize().width) / 2,
            uiConfig.getSelfStudyButtonTopMargin(),
            uiConfig.getSelfStudyButtonSize().width,
            uiConfig.getSelfStudyButtonSize().height
        ),
        buttonDelayStep
    )

    // Challenge AI button: center -> off-screen left (extra 10px margin to hide shadow)
    private val challengeAiBtnAnimator: Animator = Animators.viewAreaAnimator(
        duration, easeFunction, challengeAiButtonView,
        Rectangle(
            -uiConfig.getChallengeAiButtonSize().width - 10,
            uiConfig.getChallengeAiButtonTopMargin(),
            uiConfig.getChallengeAiButtonSize().width,
            uiConfig.getChallengeAiButtonSize().height
        ),
        Rectangle(
            (uiConfig.getMainWindowClientSize().width - uiConfig.getChallengeAiButtonSize().width) / 2,
            uiConfig.getChallengeAiButtonTopMargin(),
            uiConfig.getChallengeAiButtonSize().width,
            uiConfig.getChallengeAiButtonSize().height
        ),
        buttonDelayStep.multipliedBy(2)
    )

    // Dual button: center -> off-screen left (extra 10px margin to hide shadow)
    private val dualBtnAnimator: Animator = Animators.viewAreaAnimator(
        duration, easeFunction, dualButtonView,
        Rectangle(
            -uiConfig.getDualButtonSize().width - 10,
            uiConfig.getDualButtonTopMargin(),
            uiConfig.getDualButtonSize().width,
            uiConfig.getDualButtonSize().height
        ),
        Rectangle(
            (uiConfig.getMainWindowClientSize().width - uiConfig.getDualButtonSize().width) / 2,
            uiConfig.getDualButtonTopMargin(),
            uiConfig.getDualButtonSize().width,
            uiConfig.getDualButtonSize().height
        ),
        buttonDelayStep.multipliedBy(3)
    )

    // Side panel: off-screen right -> right of board
    private val sidePanelAnimator: Animator = Animators.viewAreaAnimator(
        duration, easeFunction, sidePanelView,
        Rectangle(
            uiConfig.getBoardSize().width,
            0,
            uiConfig.getSidePanelWidth(),
            uiConfig.getSidePanelHeight()
        ),
        Rectangle(
            uiConfig.getMainWindowClientSize().width,
            0,
            uiConfig.getSidePanelWidth(),
            uiConfig.getSidePanelHeight()
        ),
        panelDelay
    )

    init {

        boardAnimator.addListener(this)
        titlePanelAnimator.addListener(this)
        selfStudyBtnAnimator.addListener(this)
        challengeAiBtnAnimator.addListener(this)
        dualBtnAnimator.addListener(this)
        sidePanelAnimator.addListener(this)
    }

    fun setEventListener(listener: EnterGameAnimationEventListener?) {

        this.listener = listener
    }

    fun play() {

        if (isPlaying()) { return }
        coveringLayerView.setShowing(true)
        boardAnimator.replay()
        titlePanelAnimator.replay()
        selfStudyBtnAnimator.replay()
        challengeAiBtnAnimator.replay()
        dualBtnAnimator.replay()
        sidePanelAnimator.replay()
    }

    override fun onFinished(animator: Animator) {

        if (isPlaying()) { return }
        coveringLayerView.setShowing(false)
        listener?.onFinished(this)
    }

    private fun isPlaying(): Boolean {

        return boardAnimator.getState() == PlayingState.PAYING ||
            titlePanelAnimator.getState() == PlayingState.PAYING ||
            selfStudyBtnAnimator.getState() == PlayingState.PAYING ||
            challengeAiBtnAnimator.getState() == PlayingState.PAYING ||
            dualBtnAnimator.getState() == PlayingState.PAYING ||
            sidePanelAnimator.getState() == PlayingState.PAYING
    }
}
