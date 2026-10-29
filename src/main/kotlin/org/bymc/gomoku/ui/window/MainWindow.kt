package org.bymc.gomoku.ui.window

import org.bymc.gomoku.game.abstraction.AiPlayer
import org.bymc.gomoku.game.abstraction.Game
import org.bymc.gomoku.game.factory.GameFactory
import org.bymc.gomoku.game.impl.RandomAiPlayer
import org.bymc.gomoku.model.abstraction.Rule
import org.bymc.gomoku.model.common.param.DropLegality
import org.bymc.gomoku.model.common.param.GameState
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Size2D
import org.bymc.gomoku.model.common.param.Stone
import org.bymc.gomoku.model.common.util.GomokuVerifier
import org.bymc.gomoku.model.impl.RuleImpl
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
import javax.swing.Timer

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
     * AI 执子颜色，null 表示研学模式（无 AI 对手）。
     */
    private var aiStone: Stone? = null

    /**
     * 当前对局使用的规则（AI 合法性校验用）。
     */
    private var gameRule: Rule? = null

    /**
     * 是否从"挑战 AI"入口进入设置界面。
     */
    private var challengeAiEntry: Boolean = false

    /**
     * AI 玩家。
     */
    private val aiPlayer: AiPlayer = RandomAiPlayer()

    /**
     * AI 落子定时器（单次触发，延迟后由 AI 落子）。
     */
    private val aiDropTimer: Timer =
        Timer(uiConfig.getAiDropDelay().toMillis().toInt()) { performAiDrop() }.apply { isRepeats = false }

    /**
     * 配置子视图。
     */
    init {

        getRootView().addSubView(boardView)
        getRootView().addSubView(titlePanelView)
        selfStudyButtonView.enableShadow()
        selfStudyButtonView.addEventListener(this)
        getRootView().addSubView(selfStudyButtonView)
        challengeAiButtonView.enableShadow()
        challengeAiButtonView.addEventListener(this)
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
            challengeAiButtonView -> onChallengeAiButtonClicked()
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
            DropLegality.LEGAL -> applyDrop(cellLocation)
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
    override fun onStartClicked(ttnhEnabled: Boolean, ffnhEnabled: Boolean, olnhEnabled: Boolean, userStone: Stone) {

        ruleSelectionOverlayView.setShowing(false)

        // Challenge-AI mode: the AI plays the opponent stone; self-study mode has no AI.
        aiStone = if (challengeAiEntry) Stone.getOpponent(userStone) else null
        challengeAiEntry = false
        gameRule = RuleImpl(ttnhEnabled, ffnhEnabled, olnhEnabled)

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

        challengeAiEntry = false
        ruleSelectionOverlayView.setShowing(false)
    }

    /**
     * 悔棋按钮点击。挑战 AI 模式一次撤两子（AI 一子与玩家一子），研学模式撤一子。
     */
    override fun onRetractClicked() {

        val currentGame = game ?: return

        val situationBefore = currentGame.getGameSituation()
        val roundActorIsAi = aiStone != null && situationBefore.roundActor == aiStone
        val stepCount =
            RetractionPolicy.getStepCount(aiStone != null, situationBefore.dropCount, roundActorIsAi)
        if (stepCount == 0) { return }
        repeat(stepCount) { currentGame.retractLast() }

        boardView.setViewModels(currentGame.getBoardViewModel(), currentGame.getHistoryViewModel())

        val situation = currentGame.getGameSituation()
        sidePanelView.updateGameInfo(situation.roundActor, situation.dropCount)
        updateRetractionAvailability()

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

        aiDropTimer.stop()
        // Null the AI stone immediately so a queued timer callback cannot drop after return.
        aiStone = null
        boardView.disable()
        // Clear stones before the animation starts, so the empty board slides back to the title screen.
        boardView.setViewModels(null, null)
        exitGameAnimation.play()
    }

    /**
     * 进入游戏动画完成。若 AI 执黑先行，动画结束后调度 AI 落子。
     */
    override fun onFinished(animation: EnterGameAnimation) {

        maybeScheduleAiDrop()
    }

    /**
     * 退出游戏动画完成。
     */
    override fun onFinished(animation: ExitGameAnimation) {

        // Clean up game state.
        game = null
        aiStone = null
        gameRule = null
        boardView.setViewModels(null, null)
        boardView.disable()
        sidePanelView.reset()
        ruleSelectionOverlayView.resetDefaults()
    }

    /**
     * 研学模式按钮点击。
     */
    private fun onSelfStudyButtonClicked() {

        challengeAiEntry = false
        ruleSelectionOverlayView.resetDefaults()
        ruleSelectionOverlayView.setStoneSelectionVisible(false)
        ruleSelectionOverlayView.setShowing(true)
    }

    /**
     * 挑战 AI 模式按钮点击。
     */
    private fun onChallengeAiButtonClicked() {

        challengeAiEntry = true
        ruleSelectionOverlayView.resetDefaults()
        ruleSelectionOverlayView.setStoneSelectionVisible(true)
        ruleSelectionOverlayView.setShowing(true)
    }

    /**
     * 应用最近一次落子后的局面刷新与终局处理，玩家与 AI 落子共用。
     */
    private fun applyDrop(lastDropLocation: Location2D) {

        val currentGame = game ?: return
        boardView.setViewModels(currentGame.getBoardViewModel(), currentGame.getHistoryViewModel())
        sidePanelView.clearStatus()

        val situation = currentGame.getGameSituation()
        sidePanelView.updateGameInfo(situation.roundActor, situation.dropCount)
        updateRetractionAvailability()

        when (situation.state) {
            GameState.BLACK_WON -> {
                val winLocs = GomokuVerifier(currentGame.getBoardViewModel(), lastDropLocation).getWinningLocations()
                boardView.setWinningLocations(winLocs)
                showVictory(Stone.BLACK)
                boardView.disable()
            }
            GameState.WHITE_WON -> {
                val winLocs = GomokuVerifier(currentGame.getBoardViewModel(), lastDropLocation).getWinningLocations()
                boardView.setWinningLocations(winLocs)
                showVictory(Stone.WHITE)
                boardView.disable()
            }
            GameState.DRAW -> {
                sidePanelView.showDraw()
                boardView.disable()
            }
            GameState.PLAYING -> maybeScheduleAiDrop()
        }
    }

    /**
     * 按模式展示胜负文案：挑战 AI 模式为玩家视角，研学模式为黑/白方视角。
     */
    private fun showVictory(winner: Stone) {

        val ai = aiStone
        if (ai != null) {
            sidePanelView.showVictory(winner, Stone.getOpponent(ai))
        } else {
            sidePanelView.showVictory(winner)
        }
    }

    /**
     * 更新悔棋按钮可用性：挑战 AI 模式要求已落子数不少于 2，研学模式不少于 1。
     */
    private fun updateRetractionAvailability() {

        val situation = game?.getGameSituation() ?: return
        val roundActorIsAi = aiStone != null && situation.roundActor == aiStone
        sidePanelView.setRetractionEnabled(RetractionPolicy.isEnabled(aiStone != null, situation.dropCount, roundActorIsAi))
    }

    /**
     * 若轮到 AI 且棋局进行中，禁用棋盘与悔棋并启动延迟落子定时器；否则恢复玩家操作。
     */
    private fun maybeScheduleAiDrop() {

        val currentAiStone = aiStone
        val currentGame = game
        if (currentAiStone == null || currentGame == null) { return }

        val situation = currentGame.getGameSituation()
        if (situation.state == GameState.PLAYING && situation.roundActor == currentAiStone) {
            boardView.disable()
            sidePanelView.setRetractionEnabled(false)
            aiDropTimer.restart()
        } else {
            boardView.enable()
            updateRetractionAvailability()
        }
    }

    /**
     * AI 定时器触发：随机选择合法落子点并落子；无合法点判和局。
     */
    private fun performAiDrop() {

        val currentGame = game ?: return
        val currentAiStone = aiStone ?: return
        val rule = gameRule ?: return

        if (currentGame.getGameSituation().state != GameState.PLAYING) { return }

        val location = aiPlayer.pickDropLocation(currentGame.getBoardViewModel(), rule, currentAiStone)
        if (location == null) {
            // AI has no legal location: judge as draw and refresh retraction availability.
            sidePanelView.showDraw()
            boardView.disable()
            updateRetractionAvailability()
            return
        }

        if (currentGame.dropStoneAt(location) == DropLegality.LEGAL) {
            applyDrop(location)
        } else {
            // Defensive: an unexpected illegal result must not leave the board locked.
            boardView.enable()
            updateRetractionAvailability()
        }
    }
}
