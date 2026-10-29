package org.bymc.gomoku.ui.view

import org.bymc.gomoku.model.common.param.Stone
import org.bymc.gomoku.ui.window.UiConfig
import org.bymc.gomoku.uifx.view.LabelView
import org.bymc.gomoku.uifx.view.SimpleTextButtonView
import org.bymc.gomoku.uifx.view.base.ButtonViewBase
import org.bymc.gomoku.uifx.view.base.ButtonViewEventListener
import org.bymc.gomoku.uifx.view.base.ViewBase
import org.bymc.gomoku.uifx.view.common.FontConfig
import org.bymc.gomoku.uifx.view.common.HorizontalAlignment
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

/**
 * Side panel view for displaying game information and action buttons.
 */
class SidePanelView(

    /**
     * UI config.
     */
    private val uiConfig: UiConfig

) : ViewBase(
    Rectangle(
        uiConfig.getBoardSize().width + uiConfig.getSidePanelWidth(),
        0,
        uiConfig.getSidePanelWidth(),
        uiConfig.getSidePanelHeight()
    ),
    true,
    Color.WHITE
), ButtonViewEventListener {

    private val listeners: MutableList<SidePanelViewEventListener> = ArrayList()

    private val margin = uiConfig.getSidePanelMargin()
    private val spacing = uiConfig.getSidePanelSpacing()
    private val labelSize = uiConfig.getSidePanelLabelSize()
    private val labelFontSize = uiConfig.getSidePanelLabelFontSize()
    private val statusFontSize = uiConfig.getSidePanelStatusLabelFontSize()
    private val buttonSize = uiConfig.getSidePanelButtonSize()
    private val buttonFontSize = uiConfig.getSidePanelButtonFontSize()

    private val roundActorLabel: LabelView = LabelView(
        text = "黑方回合",
        area = Rectangle(
            (uiConfig.getSidePanelWidth() - labelSize.width) / 2,
            margin,
            labelSize.width,
            labelSize.height
        ),
        bgColor = null,
        fontConfig = FontConfig(fontSize = labelFontSize),
        horizontalAlignment = HorizontalAlignment.CENTER,
        interactive = false
    )

    private val dropCountLabel: LabelView = LabelView(
        text = "落子数: 0",
        area = Rectangle(
            (uiConfig.getSidePanelWidth() - labelSize.width) / 2,
            margin + labelSize.height + spacing,
            labelSize.width,
            labelSize.height
        ),
        bgColor = null,
        fontConfig = FontConfig(fontSize = labelFontSize),
        horizontalAlignment = HorizontalAlignment.CENTER,
        interactive = false
    )

    private val statusLabel: LabelView = LabelView(
        text = "",
        area = Rectangle(
            (uiConfig.getSidePanelWidth() - labelSize.width) / 2,
            margin + (labelSize.height + spacing) * 2,
            labelSize.width,
            labelSize.height
        ),
        bgColor = null,
        fontConfig = FontConfig(fontSize = statusFontSize),
        horizontalAlignment = HorizontalAlignment.CENTER,
        interactive = false
    )

    private val retractButton: SimpleTextButtonView = SimpleTextButtonView(
        text = "悔棋",
        area = Rectangle(
            (uiConfig.getSidePanelWidth() - buttonSize.width) / 2,
            uiConfig.getSidePanelHeight() - margin - buttonSize.height * 2 - spacing,
            buttonSize.width,
            buttonSize.height
        ),
        fontConfig = FontConfig(fontSize = buttonFontSize)
    )

    private val returnButton: SimpleTextButtonView = SimpleTextButtonView(
        text = "返回",
        area = Rectangle(
            (uiConfig.getSidePanelWidth() - buttonSize.width) / 2,
            uiConfig.getSidePanelHeight() - margin - buttonSize.height,
            buttonSize.width,
            buttonSize.height
        ),
        fontConfig = FontConfig(fontSize = buttonFontSize)
    )

    init {

        addSubView(roundActorLabel)
        addSubView(dropCountLabel)
        addSubView(statusLabel)

        retractButton.disable()
        retractButton.addEventListener(this)
        addSubView(retractButton)

        returnButton.addEventListener(this)
        addSubView(returnButton)
    }

    /**
     * Add event listener.
     */
    fun addEventListener(listener: SidePanelViewEventListener) {

        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    /**
     * Update game info display.
     */
    fun updateGameInfo(roundActor: Stone, dropCount: Int) {

        val actorText = when (roundActor) {
            Stone.BLACK -> "黑方回合"
            Stone.WHITE -> "白方回合"
        }
        roundActorLabel.setTest(actorText)
        dropCountLabel.setTest("落子数: $dropCount")
    }

    /**
     * Show victory message.
     */
    fun showVictory(winner: Stone) {

        val winnerText = when (winner) {
            Stone.BLACK -> "黑方胜利!"
            Stone.WHITE -> "白方胜利!"
        }
        statusLabel.setTest(winnerText)
        statusLabel.setTextColor(Color(200, 0, 0))
    }

    /**
     * Show victory message from the player's perspective (challenge-AI mode).
     */
    fun showVictory(winner: Stone, userStone: Stone) {

        val winnerText = if (winner == userStone) "你赢了!" else "你输了!"
        statusLabel.setTest(winnerText)
        statusLabel.setTextColor(Color(200, 0, 0))
    }

    /**
     * Show draw message.
     */
    fun showDraw() {

        statusLabel.setTest("和局!")
        statusLabel.setTextColor(Color(0, 0, 200))
    }

    /**
     * Show forbidden move hint.
     */
    fun showForbiddenHint() {

        statusLabel.setTest("禁手位置，无法落子")
        statusLabel.setTextColor(Color(200, 0, 0))
    }

    /**
     * Clear status label.
     */
    fun clearStatus() {

        statusLabel.setTest("")
    }

    /**
     * Set retract button enabled state.
     */
    fun setRetractionEnabled(enabled: Boolean) {

        if (enabled) {
            retractButton.enable()
        } else {
            retractButton.disable()
        }
    }

    /**
     * Reset to initial state.
     */
    fun reset() {

        roundActorLabel.setTest("黑方回合")
        dropCountLabel.setTest("落子数: 0")
        clearStatus()
        retractButton.disable()
    }

    /**
     * Render left border.
     */
    override fun onRender(g: Graphics, range: Rectangle) {

        val originalColor = g.color
        g.color = Color.BLACK
        g.drawLine(0, 0, 0, getArea().height)
        g.drawLine(1, 0, 1, getArea().height)
        g.color = originalColor
    }

    /**
     * Button click handler.
     */
    override fun onClicked(sender: ButtonViewBase) {

        when (sender) {
            retractButton -> listeners.forEach { it.onRetractClicked() }
            returnButton -> listeners.forEach { it.onReturnClicked() }
        }
    }
}
