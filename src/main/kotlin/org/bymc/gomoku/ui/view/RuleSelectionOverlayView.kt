package org.bymc.gomoku.ui.view

import org.bymc.gomoku.model.common.param.Stone
import org.bymc.gomoku.ui.window.UiConfig
import org.bymc.gomoku.uifx.view.LabelView
import org.bymc.gomoku.uifx.view.LabelViewEventListener
import org.bymc.gomoku.uifx.view.SimpleTextButtonView
import org.bymc.gomoku.uifx.view.ToggleSwitchView
import org.bymc.gomoku.uifx.view.base.ButtonViewBase
import org.bymc.gomoku.uifx.view.base.ButtonViewEventListener
import org.bymc.gomoku.uifx.view.base.ViewBase
import org.bymc.gomoku.uifx.view.common.FontConfig
import org.bymc.gomoku.uifx.view.common.HorizontalAlignment
import java.awt.Color
import java.awt.Graphics
import java.awt.Insets
import java.awt.Rectangle

/**
 * Rule selection overlay view for configuring forbidden hand rules.
 */
class RuleSelectionOverlayView(

    /**
     * UI config.
     */
    private val uiConfig: UiConfig

) : ViewBase(
    Rectangle(0, 0, uiConfig.getRuleSelectionOverlaySize().width, uiConfig.getRuleSelectionOverlaySize().height),
    false,
    Color.WHITE
), ButtonViewEventListener, LabelViewEventListener {

    private val listeners: MutableList<RuleSelectionOverlayViewEventListener> = ArrayList()

    private val margin = uiConfig.getRuleSelectionOverlayMargin()
    private val spacing = uiConfig.getRuleSelectionOverlaySpacing()
    private val labelSize = uiConfig.getRuleSelectionOverlayLabelSize()
    private val labelFontSize = uiConfig.getRuleSelectionOverlayLabelFontSize()
    private val buttonSize = uiConfig.getRuleSelectionOverlayButtonSize()
    private val buttonFontSize = uiConfig.getRuleSelectionOverlayButtonFontSize()
    private val switchSize = uiConfig.getRuleSelectionOverlaySwitchSize()
    private val stoneButtonSize = uiConfig.getRuleSelectionOverlayStoneButtonSize()

    /**
     * Title label.
     */
    private val titleLabel: LabelView = LabelView(
        text = "规则设置",
        area = Rectangle(
            (uiConfig.getRuleSelectionOverlaySize().width - labelSize.width) / 2,
            margin,
            labelSize.width,
            labelSize.height
        ),
        bgColor = null,
        fontConfig = FontConfig(fontSize = labelFontSize + 2),
        horizontalAlignment = HorizontalAlignment.CENTER,
        interactive = false
    )

    /**
     * Rule name labels, clicking one toggles the corresponding switch.
     */
    private val ttnhLabel: LabelView = createRuleLabel("3-3 禁手", rowTop(0))
    private val ffnhLabel: LabelView = createRuleLabel("4-4 禁手", rowTop(1))
    private val olnhLabel: LabelView = createRuleLabel("长连禁手", rowTop(2))

    /**
     * Switches for forbidden hand rules.
     */
    private val ttnhSwitch: ToggleSwitchView = createSwitch(rowTop(0))
    private val ffnhSwitch: ToggleSwitchView = createSwitch(rowTop(1))
    private val olnhSwitch: ToggleSwitchView = createSwitch(rowTop(2))

    /**
     * Stone selection row: label plus two mutually exclusive buttons. The selected one is
     * disabled (highlighted as current choice) while the other stays clickable.
     */
    private val stoneLabel: LabelView = LabelView(
        text = "执子",
        area = Rectangle(
            (uiConfig.getRuleSelectionOverlaySize().width - labelSize.width) / 2,
            rowTop(3),
            labelSize.width - switchSize.width - spacing,
            labelSize.height
        ),
        bgColor = null,
        fontConfig = FontConfig(fontSize = labelFontSize),
        insets = Insets(4, 12, 4, 4),
        horizontalAlignment = HorizontalAlignment.LEFT
    )

    private val blackStoneButton: SimpleTextButtonView = SimpleTextButtonView(
        text = "黑棋",
        area = Rectangle(
            (uiConfig.getRuleSelectionOverlaySize().width - labelSize.width) / 2 + labelSize.width -
                    stoneButtonSize.width * 2 - 8,
            rowTop(3) + (labelSize.height - stoneButtonSize.height) / 2,
            stoneButtonSize.width,
            stoneButtonSize.height
        ),
        fontConfig = FontConfig(fontSize = labelFontSize - 3)
    )

    private val whiteStoneButton: SimpleTextButtonView = SimpleTextButtonView(
        text = "白棋",
        area = Rectangle(
            (uiConfig.getRuleSelectionOverlaySize().width - labelSize.width) / 2 + labelSize.width -
                    stoneButtonSize.width,
            rowTop(3) + (labelSize.height - stoneButtonSize.height) / 2,
            stoneButtonSize.width,
            stoneButtonSize.height
        ),
        fontConfig = FontConfig(fontSize = labelFontSize - 3)
    )

    /**
     * Start and cancel buttons.
     */
    private val startButton: SimpleTextButtonView = SimpleTextButtonView(
        text = "开始",
        area = Rectangle(
            uiConfig.getRuleSelectionOverlaySize().width / 2 - buttonSize.width - spacing / 2,
            uiConfig.getRuleSelectionOverlaySize().height - margin - buttonSize.height,
            buttonSize.width,
            buttonSize.height
        ),
        fontConfig = FontConfig(fontSize = buttonFontSize)
    )

    private val cancelButton: SimpleTextButtonView = SimpleTextButtonView(
        text = "取消",
        area = Rectangle(
            uiConfig.getRuleSelectionOverlaySize().width / 2 + spacing / 2,
            uiConfig.getRuleSelectionOverlaySize().height - margin - buttonSize.height,
            buttonSize.width,
            buttonSize.height
        ),
        fontConfig = FontConfig(fontSize = buttonFontSize)
    )

    init {

        addSubView(titleLabel)
        addSubView(ttnhLabel)
        addSubView(ffnhLabel)
        addSubView(olnhLabel)
        addSubView(ttnhSwitch)
        addSubView(ffnhSwitch)
        addSubView(olnhSwitch)
        addSubView(stoneLabel)
        addSubView(blackStoneButton)
        addSubView(whiteStoneButton)

        ttnhLabel.addEventListener(this)
        ffnhLabel.addEventListener(this)
        olnhLabel.addEventListener(this)

        blackStoneButton.addEventListener(this)
        whiteStoneButton.addEventListener(this)

        startButton.addEventListener(this)
        cancelButton.addEventListener(this)
        addSubView(startButton)
        addSubView(cancelButton)

        selectStone(Stone.BLACK)
    }

    /**
     * Add event listener.
     */
    fun addEventListener(listener: RuleSelectionOverlayViewEventListener) {

        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    /**
     * Reset all switches to default checked state and stone selection to black.
     */
    fun resetDefaults() {

        ttnhSwitch.setChecked(true, animate = false)
        ffnhSwitch.setChecked(true, animate = false)
        olnhSwitch.setChecked(true, animate = false)
        selectStone(Stone.BLACK)
    }

    /**
     * Show or hide the stone selection row. Hidden by default for self-study mode.
     */
    fun setStoneSelectionVisible(visible: Boolean) {

        stoneLabel.setShowing(visible)
        blackStoneButton.setShowing(visible)
        whiteStoneButton.setShowing(visible)
    }

    /**
     * Render border.
     */
    override fun onRender(g: Graphics, range: Rectangle) {

        val originalColor = g.color
        g.color = Color.BLACK
        for (i in 0 until 2) {
            g.drawRect(i, i, getArea().width - 2 * i, getArea().height - 2 * i)
        }
        g.color = originalColor
    }

    /**
     * Button click handler.
     */
    override fun onClicked(sender: ButtonViewBase) {

        when (sender) {
            startButton -> listeners.forEach {
                it.onStartClicked(
                    ttnhSwitch.isChecked(),
                    ffnhSwitch.isChecked(),
                    olnhSwitch.isChecked(),
                    getSelectedStone()
                )
            }
            cancelButton -> listeners.forEach { it.onCancelClicked() }
            blackStoneButton -> selectStone(Stone.BLACK)
            whiteStoneButton -> selectStone(Stone.WHITE)
        }
    }

    /**
     * Select the given stone: disable the chosen button (marked as current) and enable the other.
     */
    private fun selectStone(stone: Stone) {

        when (stone) {
            Stone.BLACK -> {
                if (!blackStoneButton.isEnabled()) { return }
                blackStoneButton.disable()
                whiteStoneButton.enable()
            }
            Stone.WHITE -> {
                if (!whiteStoneButton.isEnabled()) { return }
                whiteStoneButton.disable()
                blackStoneButton.enable()
            }
        }
    }

    /**
     * Get the currently selected stone. The disabled one is the current selection.
     */
    private fun getSelectedStone(): Stone = if (!blackStoneButton.isEnabled()) Stone.BLACK else Stone.WHITE

    /**
     * Rule name label click handler, toggles the corresponding switch.
     */
    override fun onLeftButtonClicked(sender: LabelView) {

        when (sender) {
            ttnhLabel -> ttnhSwitch.toggle()
            ffnhLabel -> ffnhSwitch.toggle()
            olnhLabel -> olnhSwitch.toggle()
        }
    }

    override fun onRightButtonClicked(sender: LabelView) {}
    override fun onLeftButtonDoubleClicked(sender: LabelView) {}
    override fun onRightButtonDoubleClicked(sender: LabelView) {}

    /**
     * Top coordinate of the rule row at the given index.
     */
    private fun rowTop(index: Int): Int {

        return margin + labelSize.height + spacing * (index + 1) + labelSize.height * index
    }

    private fun createRuleLabel(text: String, top: Int): LabelView {

        val rowX = (uiConfig.getRuleSelectionOverlaySize().width - labelSize.width) / 2
        return LabelView(
            text = text,
            area = Rectangle(
                rowX,
                top,
                labelSize.width - switchSize.width - spacing,
                labelSize.height
            ),
            bgColor = null,
            fontConfig = FontConfig(fontSize = labelFontSize),
            insets = Insets(4, 12, 4, 4),
            horizontalAlignment = HorizontalAlignment.LEFT
        )
    }

    private fun createSwitch(top: Int): ToggleSwitchView {

        val rowX = (uiConfig.getRuleSelectionOverlaySize().width - labelSize.width) / 2
        return ToggleSwitchView(
            area = Rectangle(
                rowX + labelSize.width - switchSize.width,
                top + (labelSize.height - switchSize.height) / 2,
                switchSize.width,
                switchSize.height
            ),
            checked = true
        )
    }
}
