package org.bymc.gomoku.ui.window

import org.bymc.gomoku.game.abstraction.Game
import org.bymc.gomoku.game.factory.GameFactory
import org.bymc.gomoku.model.common.param.DropLegality
import org.bymc.gomoku.model.common.param.GameState
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Size2D
import org.bymc.gomoku.model.common.param.Stone
import org.bymc.gomoku.model.common.util.GomokuVerifier
import org.bymc.gomoku.ui.animation.EnterGameAnimation
import org.bymc.gomoku.ui.animation.EnterGameAnimationEventListener
import org.bymc.gomoku.ui.animation.ExitGameAnimation
import org.bymc.gomoku.ui.animation.ExitGameAnimationEventListener
import org.bymc.gomoku.ui.animation.TitleAnimation
import org.bymc.gomoku.ui.view.BoardIntegrationView
import org.bymc.gomoku.ui.view.BoardSensorViewEventListener
import org.bymc.gomoku.ui.view.CoveringLayerView
import org.bymc.gomoku.ui.view.RuleSelectionOverlayView
import org.bymc.gomoku.ui.view.RuleSelectionOverlayViewEventListener
import org.bymc.gomoku.ui.view.SidePanelView
import org.bymc.gomoku.ui.view.SidePanelViewEventListener
import org.bymc.gomoku.ui.view.TitlePanelView
import org.bymc.gomoku.uifx.animation.easefunc.EaseFunctions
import org.bymc.gomoku.uifx.view.SimpleTextButtonView
import org.bymc.gomoku.uifx.view.base.ButtonViewBase
import org.bymc.gomoku.uifx.view.base.ButtonViewEventListener
import org.bymc.gomoku.uifx.view.common.FontConfig
import org.bymc.gomoku.uifx.window.RootViewWindowBase
import org.bymc.gomoku.uifx.window.WindowInitialConfig
import java.awt.Point
import java.awt.Rectangle
import java.util.Date

/**
 * 游戏主窗口。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
class MainWindow(

    /**
     * UI 配置接口。
     */
    private val uiConfig: UiConfig = DefaultUiConfig()

) : RootViewWindowBase(
    WindowInitialConfig(
        title = "${uiConfig.getMajorTitleText()} - ${uiConfig.getMinorTitleText()}",
        resizable = false,
        central = true,
        explicitClientSize = true,
        size = uiConfig.getMainWindowClientSize()
    )
), ButtonViewEventListener, BoardSensorViewEventListener, RuleSelectionOverlayViewEventListener,
    SidePanelViewEventListener, EnterGameAnimationEventListener, ExitGameAnimationEventListener {

    /**
     * 棋枰视图。
     */
    private val boardView: BoardIntegrationView = BoardIntegrationView(
        Rectangle(
            -uiConfig.getBoardSize().width,
            0,
            uiConfig.getBoardSize().width,
            uiConfig.getBoardSize().height
        ),
        uiConfig.getCellSize(),
        false
    )

    /**
     * 标题面板视图。
     */
    private val titlePanelView: TitlePanelView = TitlePanelView(
        uiConfig, Point(-uiConfig.getTitlePanelSize().width, 0)
    )

    /**
     * 研学模式按钮。
     */
    private val selfStudyButtonView: SimpleTextButtonView = SimpleTextButtonView(
        text = uiConfig.getSelfStudyButtonText(),
        area = Rectangle(
            -uiConfig.getSelfStudyButtonSize().width - 100,
            uiConfig.getSelfStudyButtonTopMargin(),
            uiConfig.getSelfStudyButtonSize().width,
            uiConfig.getSelfStudyButtonSize().height
        ),
        fontConfig = FontConfig(fontSize = uiConfig.getSelfStudyButtonFontSize())
    )

    /**
     * AI 模式按钮。
     */
    private val challengeAiButtonView: SimpleTextButtonView = SimpleTextButtonView(
        text = uiConfig.getChallengeAiButtonText(),
        area = Rectangle(
            -uiConfig.getChallengeAiButtonSize().width - 100,
            uiConfig.getChallengeAiButtonTopMargin(),
            uiConfig.getChallengeAiButtonSize().width,
            uiConfig.getChallengeAiButtonSize().height
        ),
        fontConfig = FontConfig(fontSize = uiConfig.getChallengeAiButtonFontSize())
    )

    /**
     * 对战模式按钮。
     */
    private val dualButtonView: SimpleTextButtonView = SimpleTextButtonView(
        text = uiConfig.getDualButtonText(),
        area = Rectangle(
            -uiConfig.getDualButtonSize().width - 100,
            uiConfig.getDualButtonTopMargin(),
            uiConfig.getDualButtonSize().width,
            uiConfig.getDualButtonSize().height
        ),
        fontConfig = FontConfig(fontSize = uiConfig.getDualButtonFontSize())
    )

    /**
     * 侧面板视图。
     */
    private val sidePanelView: SidePanelView = SidePanelView(uiConfig)

    /**
     * 规则选择覆盖层。
     */
    private val ruleSelectionOverlayView: RuleSelectionOverlayView = RuleSelectionOverlayView(uiConfig).also {
        val overlaySize = uiConfig.getRuleSelectionOverlaySize()
        it.setArea(
            Rectangle(
                (uiConfig.getMainWindowClientSize().width - overlaySize.width) / 2,
                (uiConfig.getMainWindowClientSize().height - overlaySize.height) / 2,
                overlaySize.width,
                overlaySize.height
            )
        )
    }

    /**
     * 遮盖视图。
     */
    private val coveringLayerView: CoveringLayerView = CoveringLayerView(
        Rectangle(0, 0, uiConfig.getMainWindowClientSize().width, uiConfig.getMainWindowClientSize().height)
    )

    /**
     * 标题动画测试按钮。
     */
    private val titleAnimationTestButton: SimpleTextButtonView? =
        if (System.getenv("GOMOKU_SHOW_TEST_BUTTON") == "true") {
            SimpleTextButtonView(
                text = "Test Title Animation",
                area = Rectangle(10, 10, 200, 30),
                showing = true
            )
        } else {
            null
        }

    /**
     * 标题动画。
     */
    private val titleAnimation: TitleAnimation = TitleAnimation(
        uiConfig, boardView, titlePanelView, selfStudyButtonView, challengeAiButtonView, dualButtonView,
        coveringLayerView, EaseFunctions.easeInOutBack()
    )

    /**
     * 进入游戏动画。
     */
    private val enterGameAnimation: EnterGameAnimation = EnterGameAnimation(
        uiConfig, boardView, titlePanelView, selfStudyButtonView, challengeAiButtonView, dualButtonView,
        sidePanelView, coveringLayerView
    )

    /**
     * 退出游戏动画。
     */
    private val exitGameAnimation: ExitGameAnimation = ExitGameAnimation(
        uiConfig, boardView, titlePanelView, selfStudyButtonView, challengeAiButtonView, dualButtonView,
        sidePanelView, coveringLayerView
    )

    /**
     * 当前游戏实例，null 表示未在游戏中。
     */
    private var game: Game? = null

    /**
     * 配置子视图。
     */
    init {

        getRootView().addSubView(boardView)
        getRootView().addSubView(titlePanelView)
        selfStudyButtonView.enableShadow()
        selfStudyButtonView.addEventListener(this)
        getRootView().addSubView(selfStudyButtonView)
        challengeAiButtonView.disable()
        challengeAiButtonView.enableShadow()
        getRootView().addSubView(challengeAiButtonView)
        dualButtonView.disable()
        dualButtonView.enableShadow()
        getRootView().addSubView(dualButtonView)

        // Add side panel (initially off-screen right).
        getRootView().addSubView(sidePanelView)

        // Add rule selection overlay (initially hidden, above board).
        ruleSelectionOverlayView.addEventListener(this)
        getRootView().addSubView(ruleSelectionOverlayView)

        // 保证遮盖视图最后添加，使其处于顶层。
        getRootView().addSubView(coveringLayerView)

        // 测试按钮。
        titleAnimationTestButton?.let {
            it.enableShadow()
            it.addEventListener(this)
            getRootView().addSubView(it)
        }

        // Register sensor and side panel listeners.
        boardView.addSensorEventListener(this)
        sidePanelView.addEventListener(this)

        // Register animation listeners.
        enterGameAnimation.setEventListener(this)
        exitGameAnimation.setEventListener(this)
    }

    /**
     * 启动标题动画。
     */
    init {

        titleAnimation.play()
    }

    /**
     * 按钮点击事件。
     */
    override fun onClicked(sender: ButtonViewBase) {

        when (sender) {
            titleAnimationTestButton -> titleAnimation.play()
            selfStudyButtonView -> onSelfStudyButtonClicked()
            else -> {}
        }
    }

    /**
     * 棋枰单元格点击事件。
     */
    override fun onCellClicked(cellLocation: Location2D) {

        val currentGame = game ?: return
        if (currentGame.getGameSituation().state != GameState.PLAYING) return

        val result = currentGame.dropStoneAt(cellLocation)
        when (result) {
            DropLegality.LEGAL -> {
                boardView.setViewModels(currentGame.getBoardViewModel(), currentGame.getHistoryViewModel())
                sidePanelView.clearStatus()
                val situation = currentGame.getGameSituation()
                sidePanelView.updateGameInfo(situation.roundActor, situation.dropCount)
                sidePanelView.setRetractionEnabled(situation.dropCount >= 1)

                when (situation.state) {
                    GameState.BLACK_WON -> {
                        val winLocs = GomokuVerifier(
                            currentGame.getBoardViewModel(), cellLocation
                        ).getWinningLocations()
                        boardView.setWinningLocations(winLocs)
                        sidePanelView.showVictory(Stone.BLACK)
                        boardView.disable()
                    }
                    GameState.WHITE_WON -> {
                        val winLocs = GomokuVerifier(
                            currentGame.getBoardViewModel(), cellLocation
                        ).getWinningLocations()
                        boardView.setWinningLocations(winLocs)
                        sidePanelView.showVictory(Stone.WHITE)
                        boardView.disable()
                    }
                    GameState.DRAW -> {
                        sidePanelView.showDraw()
                        boardView.disable()
                    }
                    GameState.PLAYING -> {}
                }
            }
            DropLegality.FORBIDDEN_BY_TTNH_RULE,
            DropLegality.FORBIDDEN_BY_FFNH_RULE,
            DropLegality.FORBIDDEN_BY_OLNH_RULE -> {
                boardView.flashCell(cellLocation)
                sidePanelView.showForbiddenHint()
            }
            else -> {
                // OCCUPIED_BY_SAME_STONE, OCCUPIED_BY_DIFFERENT_STONE — silently ignore.
            }
        }
    }

    /**
     * 规则选择覆盖层"开始"按钮点击。
     */
    override fun onStartClicked(ttnhEnabled: Boolean, ffnhEnabled: Boolean, olnhEnabled: Boolean) {

        ruleSelectionOverlayView.setShowing(false)

        // Create game.
        val newGame = GameFactory.createGame(
            Size2D(Constants.edgeCapacity, Constants.edgeCapacity),
            Date(),
            ttnhEnabled,
            ffnhEnabled,
            olnhEnabled
        )
        game = newGame

        // Bind board view models.
        boardView.setViewModels(newGame.getBoardViewModel(), newGame.getHistoryViewModel())
        boardView.enable()

        // Reset side panel.
        sidePanelView.reset()

        // Play enter animation.
        enterGameAnimation.play()
    }

    /**
     * 规则选择覆盖层"取消"按钮点击。
     */
    override fun onCancelClicked() {

        ruleSelectionOverlayView.setShowing(false)
    }

    /**
     * 悔棋按钮点击。
     */
    override fun onRetractClicked() {

        val currentGame = game ?: return

        currentGame.retractLast()
        boardView.setViewModels(currentGame.getBoardViewModel(), currentGame.getHistoryViewModel())

        val situation = currentGame.getGameSituation()
        sidePanelView.updateGameInfo(situation.roundActor, situation.dropCount)
        sidePanelView.setRetractionEnabled(situation.dropCount >= 1)

        // If game was in terminal state and is now PLAYING, re-enable board and clear status.
        if (situation.state == GameState.PLAYING) {
            boardView.enable()
            sidePanelView.clearStatus()
        }
    }

    /**
     * 返回按钮点击。
     */
    override fun onReturnClicked() {

        boardView.disable()
        exitGameAnimation.play()
    }

    /**
     * 进入游戏动画完成。
     */
    override fun onFinished(animation: EnterGameAnimation) {

        // Animation complete, game is ready.
    }

    /**
     * 退出游戏动画完成。
     */
    override fun onFinished(animation: ExitGameAnimation) {

        // Clean up game state.
        game = null
        boardView.setViewModels(null, null)
        boardView.disable()
        sidePanelView.reset()
        ruleSelectionOverlayView.resetDefaults()
    }

    /**
     * 研学模式按钮点击。
     */
    private fun onSelfStudyButtonClicked() {

        ruleSelectionOverlayView.resetDefaults()
        ruleSelectionOverlayView.setShowing(true)
    }
}
