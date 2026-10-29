package org.bymc.gomoku.ui.window

import org.bymc.gomoku.uifx.view.common.FontStyle
import java.awt.Dimension
import java.time.Duration

/**
 * UI 配置接口。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
interface UiConfig {

    /**
     * 获取单元格的尺寸。
     */
    fun getCellSize(): Int

    /**
     * 获取棋枰尺寸。
     */
    fun getBoardSize(): Dimension

    /**
     * 获取控制面板尺寸。
     */
    fun getControlPanelWidth(): Int

    /**
     * 获取主窗口客户区尺寸。
     */
    fun getMainWindowClientSize(): Dimension

    /**
     * 获取标题动画中棋盘动画的时长。
     */
    fun getTitleBoardAnimationDuration(): Duration

    /**
     * 获取标题动画中标题面板动画的时长。
     */
    fun getTitlePanelAnimationDuration(): Duration

    /**
     * 获取标题动画中按钮动画的时长。
     */
    fun getTitleButtonAnimationDuration(): Duration

    /**
     * 获取标题动画中标题面板动画的启动延迟。
     */
    fun getTitlePanelAnimationDelay(): Duration

    /**
     * 获取标题动画中第一个按钮动画的启动延迟。
     */
    fun getTitleButtonAnimationDelay(): Duration

    /**
     * 获取标题动画中按钮动画的启动延迟的累加量。
     */
    fun getTitleButtonAnimationDelayAcceleration(): Duration

    /**
     * 获取标题面板尺寸。
     */
    fun getTitlePanelSize(): Dimension

    /**
     * 获取标题面板变化宽度。
     */
    fun getTitlePanelBorderWidth(): Int

    /**
     * 获取主标题文案。
     */
    fun getMajorTitleText(): String

    /**
     * 获取副标题文案。
     */
    fun getMinorTitleText(): String

    /**
     * 获取主标题标签尺寸。
     */
    fun getMajorTitleLabelSize(): Dimension

    /**
     * 获取副标题标签尺寸。
     */
    fun getMinorTitleLabelSize(): Dimension

    /**
     * 获取主标题标签上沿坐标。
     */
    fun getMajorTitleLabelTop(): Int

    /**
     * 获取副标题标签上沿坐标。
     */
    fun getMinorTitleLabelTop(): Int

    /**
     * 获取主标题文案字体尺寸。
     */
    fun getMajorTitleFontSize(): Int

    /**
     * 获取副标题文案字体尺寸。
     */
    fun getMinorTitleFontSize(): Int

    /**
     * 获取主标题文案字体风格。
     */
    fun getMajorTitleFontStyle(): FontStyle

    /**
     * 获取副标题文案字体风格。
     */
    fun getMinorTitleFontStyle(): FontStyle

    /**
     * 研学模式按钮文案。
     */
    fun getSelfStudyButtonText(): String

    /**
     * 研学模式按钮尺寸。
     */
    fun getSelfStudyButtonSize(): Dimension

    /**
     * 研学模式按钮上沿坐标。
     */
    fun getSelfStudyButtonTopMargin(): Int

    /**
     * 研学模式按钮文案字体尺寸。
     */
    fun getSelfStudyButtonFontSize(): Int

    /**
     * AI 模式按钮文案。
     */
    fun getChallengeAiButtonText(): String

    /**
     * AI 模式按钮尺寸。
     */
    fun getChallengeAiButtonSize(): Dimension

    /**
     * AI 模式按钮上沿坐标。
     */
    fun getChallengeAiButtonTopMargin(): Int

    /**
     * AI 模式按钮文案字体尺寸。
     */
    fun getChallengeAiButtonFontSize(): Int

    /**
     * 对战模式按钮文案。
     */
    fun getDualButtonText(): String

    /**
     * 对战模式按钮尺寸。
     */
    fun getDualButtonSize(): Dimension

    /**
     * 对战模式按钮上沿坐标。
     */
    fun getDualButtonTopMargin(): Int

    /**
     * 对战模式按钮文案字体尺寸。
     */
    fun getDualButtonFontSize(): Int

    /**
     * 获取进入游戏动画时长。
     */
    fun getEnterGameAnimationDuration(): Duration

    /**
     * 获取退出游戏动画时长。
     */
    fun getExitGameAnimationDuration(): Duration

    /**
     * 获取进入/退出游戏动画中面板类元素的启动延迟（进入=侧面板入场，退出=标题面板入场）。
     */
    fun getGameAnimationPanelDelay(): Duration

    /**
     * 获取退出游戏动画中第一个按钮动画的启动延迟。
     */
    fun getGameAnimationButtonDelayBase(): Duration

    /**
     * 获取进入/退出游戏动画中按钮动画的启动延迟递增步长。
     */
    fun getGameAnimationButtonDelayStep(): Duration

    /**
     * 获取 AI 落子延迟。
     */
    fun getAiDropDelay(): Duration

    /**
     * 获取侧面板宽度。
     */
    fun getSidePanelWidth(): Int

    /**
     * 获取侧面板高度。
     */
    fun getSidePanelHeight(): Int

    /**
     * 获取侧面板按钮尺寸。
     */
    fun getSidePanelButtonSize(): Dimension

    /**
     * 获取侧面板按钮字体尺寸。
     */
    fun getSidePanelButtonFontSize(): Int

    /**
     * 获取侧面板标签尺寸。
     */
    fun getSidePanelLabelSize(): Dimension

    /**
     * 获取侧面板标签字体尺寸。
     */
    fun getSidePanelLabelFontSize(): Int

    /**
     * 获取侧面板状态标签字体尺寸。
     */
    fun getSidePanelStatusLabelFontSize(): Int

    /**
     * 获取侧面板内边距。
     */
    fun getSidePanelMargin(): Int

    /**
     * 获取侧面板元素间距。
     */
    fun getSidePanelSpacing(): Int

    /**
     * 获取规则选择覆盖层尺寸。
     */
    fun getRuleSelectionOverlaySize(): Dimension

    /**
     * 获取规则选择覆盖层按钮尺寸。
     */
    fun getRuleSelectionOverlayButtonSize(): Dimension

    /**
     * 获取规则选择覆盖层按钮字体尺寸。
     */
    fun getRuleSelectionOverlayButtonFontSize(): Int

    /**
     * 获取规则选择覆盖层标签尺寸。
     */
    fun getRuleSelectionOverlayLabelSize(): Dimension

    /**
     * 获取规则选择覆盖层标签字体尺寸。
     */
    fun getRuleSelectionOverlayLabelFontSize(): Int

    /**
     * 获取规则选择覆盖层开关尺寸。
     */
    fun getRuleSelectionOverlaySwitchSize(): Dimension

    /**
     * 获取规则选择覆盖层执子选择按钮尺寸。
     */
    fun getRuleSelectionOverlayStoneButtonSize(): Dimension

    /**
     * 获取规则选择覆盖层内边距。
     */
    fun getRuleSelectionOverlayMargin(): Int

    /**
     * 获取规则选择覆盖层元素间距。
     */
    fun getRuleSelectionOverlaySpacing(): Int
}