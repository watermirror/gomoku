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
}