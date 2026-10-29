package org.bymc.gomoku.ui.window

import org.bymc.gomoku.uifx.view.common.FontStyle
import java.awt.Dimension
import java.time.Duration

/**
 * 缺省 UI 配置接口实现。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
class DefaultUiConfig : UiConfig {

    /**
     * 获取单元格的尺寸。
     */
    override fun getCellSize(): Int = 41

    /**
     * 获取棋枰尺寸。
     */
    override fun getBoardSize(): Dimension = Dimension(
        Constants.edgeCapacity * getCellSize(), Constants.edgeCapacity * getCellSize()
    )

    /**
     * 获取控制面板尺寸。
     */
    override fun getControlPanelWidth(): Int = 300

    /**
     * 获取主窗口客户区尺寸。
     */
    override fun getMainWindowClientSize(): Dimension {

        return Dimension(getBoardSize().width + getControlPanelWidth(), getBoardSize().height)
    }

    /**
     * 获取标题动画中棋盘动画的时常。
     */
    override fun getTitleBoardAnimationDuration(): Duration {

        return Duration.ofSeconds(1)
    }

    /**
     * 获取标题动画中标题面板动画的时长。
     */
    override fun getTitlePanelAnimationDuration(): Duration {

        return Duration.ofMillis(1500)
    }

    /**
     * 获取标题动画中按钮动画的时长。
     */
    override fun getTitleButtonAnimationDuration(): Duration {

        return Duration.ofMillis(1500)
    }

    /**
     * 获取标题动画中标题面板动画的启动延迟。
     */
    override fun getTitlePanelAnimationDelay(): Duration {

        return Duration.ofMillis(200)
    }

    /**
     * 获取标题动画中第一个按钮动画的启动延迟。
     */
    override fun getTitleButtonAnimationDelay(): Duration {

        return Duration.ofMillis(400)
    }

    /**
     * 获取标题动画中按钮动画的启动延迟的累加量。
     */
    override fun getTitleButtonAnimationDelayAcceleration(): Duration {

        return Duration.ofMillis(100)
    }

    /**
     * 获取标题面板尺寸。
     */
    override fun getTitlePanelSize(): Dimension = Dimension(300, 350)

    /**
     * 获取标题面板变化宽度。
     */
    override fun getTitlePanelBorderWidth(): Int = 2

    /**
     * 获取主标题文案。
     */
    override fun getMajorTitleText(): String = "五目天国"

    /**
     * 获取副标题文案。
     */
    override fun getMinorTitleText(): String = "Gomoku Paradise"

    /**
     * 获取主标题标签尺寸。
     */
    override fun getMajorTitleLabelSize(): Dimension = Dimension(200, 40)

    /**
     * 获取副标题标签尺寸。
     */
    override fun getMinorTitleLabelSize(): Dimension = Dimension(200, 30)

    /**
     * 获取主标题标签上沿坐标。
     */
    override fun getMajorTitleLabelTop(): Int = 50

    /**
     * 获取副标题标签上沿坐标。
     */
    override fun getMinorTitleLabelTop(): Int = getMajorTitleLabelTop() + getMajorTitleLabelSize().height

    /**
     * 获取主标题文案字体尺寸。
     */
    override fun getMajorTitleFontSize(): Int = 45

    /**
     * 获取副标题文案字体尺寸。
     */
    override fun getMinorTitleFontSize(): Int = 15

    /**
     * 获取主标题文案字体风格。
     */
    override fun getMajorTitleFontStyle(): FontStyle = FontStyle.BOLD_ITALIC

    /**
     * 获取副标题文案字体风格。
     */
    override fun getMinorTitleFontStyle(): FontStyle = FontStyle.ITALIC

    /**
     * 研学模式按钮文案。
     */
    override fun getSelfStudyButtonText(): String = "研学模式"

    /**
     * 研学模式按钮尺寸。
     */
    override fun getSelfStudyButtonSize(): Dimension = Dimension(200, 40)

    /**
     * 研学模式按钮上沿坐标。
     */
    override fun getSelfStudyButtonTopMargin(): Int = 300

    /**
     * 研学模式按钮文案字体尺寸。
     */
    override fun getSelfStudyButtonFontSize(): Int = 20

    /**
     * AI 模式按钮文案。
     */
    override fun getChallengeAiButtonText(): String = "挑战机器人"

    /**
     * AI 模式按钮尺寸。
     */
    override fun getChallengeAiButtonSize(): Dimension = getSelfStudyButtonSize()

    /**
     * AI 模式按钮上沿坐标。
     */
    override fun getChallengeAiButtonTopMargin(): Int =
        getSelfStudyButtonTopMargin() + getSelfStudyButtonSize().height + 15

    /**
     * AI 模式按钮文案字体尺寸。
     */
    override fun getChallengeAiButtonFontSize(): Int = getSelfStudyButtonFontSize()

    /**
     * 对战模式按钮文案。
     */
    override fun getDualButtonText(): String = "网络对弈"

    /**
     * 对战模式按钮尺寸。
     */
    override fun getDualButtonSize(): Dimension = getChallengeAiButtonSize()

    /**
     * 对战模式按钮上沿坐标。
     */
    override fun getDualButtonTopMargin(): Int =
        getChallengeAiButtonTopMargin() + getChallengeAiButtonSize().height + 15

    /**
     * 对战模式按钮文案字体尺寸。
     */
    override fun getDualButtonFontSize(): Int = getChallengeAiButtonFontSize()

    /**
     * 获取进入游戏动画时长。
     */
    override fun getEnterGameAnimationDuration(): Duration = Duration.ofMillis(1000)

    /**
     * 获取退出游戏动画时长。
     */
    override fun getExitGameAnimationDuration(): Duration = Duration.ofMillis(1000)

    /**
     * 获取进入/退出游戏动画中面板类元素的启动延迟（进入=侧面板入场，退出=标题面板入场）。
     */
    override fun getGameAnimationPanelDelay(): Duration = Duration.ofMillis(200)

    /**
     * 获取退出游戏动画中第一个按钮动画的启动延迟。
     */
    override fun getGameAnimationButtonDelayBase(): Duration = Duration.ofMillis(300)

    /**
     * 获取进入/退出游戏动画中按钮动画的启动延迟递增步长。
     */
    override fun getGameAnimationButtonDelayStep(): Duration = Duration.ofMillis(100)

    /**
     * 获取 AI 落子延迟。
     */
    override fun getAiDropDelay(): Duration = Duration.ofMillis(500)

    /**
     * 获取侧面板宽度。
     */
    override fun getSidePanelWidth(): Int = getControlPanelWidth()

    /**
     * 获取侧面板高度。
     */
    override fun getSidePanelHeight(): Int = getBoardSize().height

    /**
     * 获取侧面板按钮尺寸。
     */
    override fun getSidePanelButtonSize(): Dimension = Dimension(200, 40)

    /**
     * 获取侧面板按钮字体尺寸。
     */
    override fun getSidePanelButtonFontSize(): Int = 18

    /**
     * 获取侧面板标签尺寸。
     */
    override fun getSidePanelLabelSize(): Dimension = Dimension(260, 36)

    /**
     * 获取侧面板标签字体尺寸。
     */
    override fun getSidePanelLabelFontSize(): Int = 18

    /**
     * 获取侧面板状态标签字体尺寸。
     */
    override fun getSidePanelStatusLabelFontSize(): Int = 16

    /**
     * 获取侧面板内边距。
     */
    override fun getSidePanelMargin(): Int = 20

    /**
     * 获取侧面板元素间距。
     */
    override fun getSidePanelSpacing(): Int = 15

    /**
     * 获取规则选择覆盖层尺寸。
     */
    override fun getRuleSelectionOverlaySize(): Dimension = Dimension(350, 400)

    /**
     * 获取规则选择覆盖层按钮尺寸。
     */
    override fun getRuleSelectionOverlayButtonSize(): Dimension = Dimension(120, 36)

    /**
     * 获取规则选择覆盖层按钮字体尺寸。
     */
    override fun getRuleSelectionOverlayButtonFontSize(): Int = 16

    /**
     * 获取规则选择覆盖层标签尺寸。
     */
    override fun getRuleSelectionOverlayLabelSize(): Dimension = Dimension(260, 36)

    /**
     * 获取规则选择覆盖层标签字体尺寸。
     */
    override fun getRuleSelectionOverlayLabelFontSize(): Int = 18

    /**
     * 获取规则选择覆盖层开关尺寸。
     */
    override fun getRuleSelectionOverlaySwitchSize(): Dimension = Dimension(46, 24)

    /**
     * 获取规则选择覆盖层执子选择按钮尺寸。
     */
    override fun getRuleSelectionOverlayStoneButtonSize(): Dimension = Dimension(70, 30)

    /**
     * 获取规则选择覆盖层内边距。
     */
    override fun getRuleSelectionOverlayMargin(): Int = 30

    /**
     * 获取规则选择覆盖层元素间距。
     */
    override fun getRuleSelectionOverlaySpacing(): Int = 15
}