package org.bymc.gomoku.uifx.view

import org.bymc.gomoku.uifx.view.base.ViewBase
import org.bymc.gomoku.uifx.view.mouse.MouseInteractiveEventListener
import org.bymc.gomoku.uifx.view.mouse.MouseInteractiveStateTracker
import org.bymc.gomoku.uifx.view.mouse.MouseInteractiveViewArea
import org.bymc.gomoku.uifx.view.mouse.MouseInteractiveViewDisplayState
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.RenderingHints
import javax.swing.Timer
import kotlin.math.abs

/**
 * Toggle switch view with a sliding knob, drawn in monochrome style:
 * dark track with white knob when checked, light gray track when unchecked.
 *
 * @author: zheng.chez
 * @since: 2026/09/09
 */
class ToggleSwitchView(

    /**
     * Position and size.
     */
    area: Rectangle = Rectangle(0, 0, 0, 0),

    /**
     * Initial checked state.
     */
    checked: Boolean = false,

    /**
     * Visibility.
     */
    showing: Boolean = true

) : ViewBase(area, showing, null), MouseInteractiveViewArea, MouseInteractiveEventListener {

    companion object {

        /**
         * Track color when checked.
         */
        private val trackCheckedColor: Color = Color(64, 64, 64)

        /**
         * Track color when checked and hovered.
         */
        private val trackCheckedHoveredColor: Color = Color(45, 45, 45)

        /**
         * Track color when unchecked.
         */
        private val trackUncheckedColor: Color = Color(224, 224, 224)

        /**
         * Track color when unchecked and hovered.
         */
        private val trackUncheckedHoveredColor: Color = Color(206, 206, 206)

        /**
         * Track border color.
         */
        private val trackBorderColor: Color = Color.BLACK

        /**
         * Track border width in pixels.
         */
        private const val trackBorderWidth: Int = 2

        /**
         * Knob fill color.
         */
        private val knobColor: Color = Color.WHITE

        /**
         * Knob border color.
         */
        private val knobBorderColor: Color = Color(130, 130, 130)

        /**
         * Knob border width in pixels.
         */
        private const val knobBorderWidth: Int = 2

        /**
         * Knob travel step per animation tick.
         */
        private const val knobAnimationStep: Float = 0.15f

        /**
         * Knob nudge offset in pixels while pressed.
         */
        private const val pressedNudgePixels: Int = 2

        /**
         * Padding between the knob and the track border.
         */
        private const val knobPadding: Int = 5
    }

    /**
     * Event listener list.
     */
    private val listeners: MutableList<ToggleSwitchViewEventListener> = ArrayList()

    /**
     * Mouse state tracker.
     */
    private val mouseTracker = MouseInteractiveStateTracker(this, this)

    /**
     * Current checked state.
     */
    private var checked: Boolean = checked

    /**
     * Knob travel progress from unchecked (0.0) to checked (1.0).
     */
    private var knobProgress: Float = if (checked) 1.0f else 0.0f

    /**
     * Current mouse display state.
     */
    private var displayState: MouseInteractiveViewDisplayState = MouseInteractiveViewDisplayState.NONE

    /**
     * Knob slide animation timer.
     */
    private var knobTimer: Timer? = null

    init {

        // Register mouse event handler.
        this.addMouseEventHandler(mouseTracker)
    }

    /**
     * Add event listener.
     */
    fun addEventListener(listener: ToggleSwitchViewEventListener) {

        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    /**
     * Remove event listener.
     */
    fun removeEventListener(listener: ToggleSwitchViewEventListener) {

        val index = listeners.indexOf(listener)
        if (index < 0) {
            return
        }
        listeners.removeAt(index)
    }

    /**
     * Get current checked state.
     */
    fun isChecked(): Boolean = checked

    /**
     * Set checked state.
     */
    fun setChecked(checked: Boolean, animate: Boolean = true) {

        if (this.checked == checked) {
            return
        }
        this.checked = checked
        if (animate) {
            startKnobAnimation()
        } else {
            stopKnobAnimation()
            knobProgress = if (checked) 1.0f else 0.0f
            scheduleRender(null)
        }
    }

    /**
     * Toggle checked state.
     */
    fun toggle(animate: Boolean = true) = setChecked(!checked, animate)

    /**
     * Render the switch.
     */
    override fun onRender(g: Graphics, range: Rectangle) {

        val g2 = g.create() as Graphics2D
        val originalColor = g2.color
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        val width = getArea().width
        val height = getArea().height
        val hovered = displayState == MouseInteractiveViewDisplayState.HOVERED ||
                displayState == MouseInteractiveViewDisplayState.LEFT_PRESSED

        // Draw track pill with border.
        g2.color = when {
            checked && hovered -> trackCheckedHoveredColor
            checked -> trackCheckedColor
            hovered -> trackUncheckedHoveredColor
            else -> trackUncheckedColor
        }
        g2.fillRoundRect(0, 0, width, height, height, height)
        g2.color = trackBorderColor
        for (i in 0 until trackBorderWidth) {
            g2.drawRoundRect(i, i, width - 2 * i - 1, height - 2 * i - 1, height, height)
        }

        // Draw knob circle.
        val knobDiameter = height - 2 * knobPadding
        val knobTravel = width - knobDiameter - 2 * knobPadding
        val nudge = if (displayState == MouseInteractiveViewDisplayState.LEFT_PRESSED) {
            if (checked) pressedNudgePixels else -pressedNudgePixels
        } else {
            0
        }
        val knobX = knobPadding + (knobTravel * knobProgress).toInt() + nudge
        g2.color = knobColor
        g2.fillOval(knobX, knobPadding, knobDiameter, knobDiameter)
        g2.color = knobBorderColor
        for (i in 0 until knobBorderWidth) {
            g2.drawOval(knobX + i, knobPadding + i, knobDiameter - 2 * i, knobDiameter - 2 * i)
        }

        g2.color = originalColor
        g2.dispose()
    }

    /**
     * Get mouse interactive view area.
     */
    override fun getViewArea(): Rectangle = Rectangle(0, 0, getArea().width, getArea().height)

    /**
     * Left click toggles the state.
     */
    override fun onLeftClicked() {

        checked = !checked
        startKnobAnimation()
        listeners.forEach { it.onToggleSwitchToggled(this, checked) }
    }

    /**
     * Right click event, ignored.
     */
    override fun onRightClicked() {}

    /**
     * Left double click event, ignored.
     */
    override fun onLeftDoubleClicked() {}

    /**
     * Right double click event, ignored.
     */
    override fun onRightDoubleClicked() {}

    /**
     * Track mouse display state for hover/pressed feedback.
     */
    override fun onDisplayStateChanged(
        original: MouseInteractiveViewDisplayState, current: MouseInteractiveViewDisplayState
    ) {

        displayState = current
        scheduleRender(null)
    }

    /**
     * Animate knob travel toward the checked state.
     */
    private fun startKnobAnimation() {

        stopKnobAnimation()
        val target = if (checked) 1.0f else 0.0f
        knobTimer = Timer(16) { timer ->
            val diff = target - knobProgress
            knobProgress = if (abs(diff) <= knobAnimationStep) {
                target
            } else {
                knobProgress + if (diff > 0) knobAnimationStep else -knobAnimationStep
            }
            scheduleRender(null)
            if (knobProgress == target) {
                (timer as? Timer)?.stop()
            }
        }.also {
            it.start()
        }
    }

    /**
     * Stop the running knob animation, if any.
     */
    private fun stopKnobAnimation() {

        knobTimer?.stop()
        knobTimer = null
    }
}
