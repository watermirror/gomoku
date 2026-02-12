package org.bymc.gomoku.ui.window

import org.bymc.gomoku.ui.animation.TitleAnimation
import org.bymc.gomoku.ui.view.BoardIntegrationView
import org.bymc.gomoku.ui.view.CoveringLayerView
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
), ButtonViewEventListener {

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
     * 配置子视图。
     */
    init {

        getRootView().addSubView(boardView)
        getRootView().addSubView(titlePanelView)
        selfStudyButtonView.enableShadow()
        getRootView().addSubView(selfStudyButtonView)
        challengeAiButtonView.disable()
        challengeAiButtonView.enableShadow()
        getRootView().addSubView(challengeAiButtonView)
        dualButtonView.disable()
        dualButtonView.enableShadow()
        getRootView().addSubView(dualButtonView)

        // 保证遮盖视图最后添加，使其处于顶层。
        getRootView().addSubView(coveringLayerView)

        // 测试按钮。
        titleAnimationTestButton?.let {
            it.enableShadow()
            it.addEventListener(this)
            getRootView().addSubView(it)
        }
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
            else -> {}
        }
    }
}