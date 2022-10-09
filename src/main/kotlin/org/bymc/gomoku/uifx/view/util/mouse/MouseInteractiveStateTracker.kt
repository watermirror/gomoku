package org.bymc.gomoku.uifx.view.util.mouse

import org.bymc.gomoku.uifx.view.base.MouseEventHandler
import java.awt.Point

/**
 * 支持滑鼠交互视图的状态追踪器。
 *
 * @author: zheng.chez
 * @since: 2022/10/08
 */
class MouseInteractiveStateTracker(

    /**
     * 滑鼠交互视图区域。
     */
    private val viewArea: MouseInteractiveViewArea,

    /**
     * 滑鼠交互事件监听器。
     */
    private val listener: MouseInteractiveEventListener

) : MouseEventHandler {

    /**
     * 内部状态。
     */
    private var internalState: InternalState = InternalState.OUT_OF_RANGE

    /**
     * 滑鼠左键按下。
     */
    override fun onLButtonPressed(position: Point, pressedCount: Int) {

        if (!viewArea.getViewArea().contains(position)) {
            return
        }

        if (pressedCount % 2 == 0) {
            listener.onLeftDoubleClicked()
        }

        updateInternalState(
            when (internalState) {
                InternalState.OUT_OF_RANGE -> InternalState.LEFT_PRESSED_IN_RANGE
                InternalState.IN_RANGE -> InternalState.LEFT_PRESSED_IN_RANGE
                InternalState.LEFT_PRESSED_OUT_OF_RANGE -> InternalState.LEFT_PRESSED_IN_RANGE
                else -> internalState
            }
        )
    }

    /**
     * 滑鼠左键释放。
     */
    override fun onLButtonReleased(position: Point) {

        if (!viewArea.getViewArea().contains(position)) {
            return updateInternalState(
                when (internalState) {
                    InternalState.LEFT_PRESSED_IN_RANGE -> InternalState.OUT_OF_RANGE
                    InternalState.LEFT_PRESSED_OUT_OF_RANGE -> InternalState.OUT_OF_RANGE
                    else -> internalState
                }
            )
        }

        if (internalState == InternalState.LEFT_PRESSED_IN_RANGE) {
            listener.onLeftClicked()
        }

        updateInternalState(
            when (internalState) {
                InternalState.LEFT_PRESSED_IN_RANGE -> InternalState.IN_RANGE
                InternalState.LEFT_PRESSED_OUT_OF_RANGE -> InternalState.IN_RANGE
                else -> internalState
            }
        )
    }

    /**
     * 滑鼠右键按下。
     */
    override fun onRButtonPressed(position: Point, pressedCount: Int) {

        if (!viewArea.getViewArea().contains(position)) {
            return
        }

        if (pressedCount % 2 == 0) {
            listener.onRightDoubleClicked()
        }

        updateInternalState(
            when (internalState) {
                InternalState.OUT_OF_RANGE -> InternalState.RIGHT_PRESSED_IN_RANGE
                InternalState.IN_RANGE -> InternalState.RIGHT_PRESSED_IN_RANGE
                InternalState.RIGHT_PRESSED_OUT_OF_RANGE -> InternalState.RIGHT_PRESSED_IN_RANGE
                else -> internalState
            }
        )
    }

    /**
     * 滑鼠右键释放。
     */
    override fun onRButtonReleased(position: Point) {

        if (!viewArea.getViewArea().contains(position)) {
            return updateInternalState(
                when (internalState) {
                    InternalState.RIGHT_PRESSED_IN_RANGE -> InternalState.OUT_OF_RANGE
                    InternalState.RIGHT_PRESSED_OUT_OF_RANGE -> InternalState.OUT_OF_RANGE
                    else -> internalState
                }
            )
        }

        if (internalState == InternalState.RIGHT_PRESSED_IN_RANGE) {
            listener.onRightClicked()
        }

        updateInternalState(
            when (internalState) {
                InternalState.RIGHT_PRESSED_IN_RANGE -> InternalState.IN_RANGE
                InternalState.RIGHT_PRESSED_OUT_OF_RANGE -> InternalState.IN_RANGE
                else -> internalState
            }
        )
    }

    /**
     * 滑鼠进入视图。
     */
    override fun onMouseEntered() {

        updateInternalState(
            when (internalState) {
                InternalState.OUT_OF_RANGE -> InternalState.IN_RANGE
                InternalState.LEFT_PRESSED_OUT_OF_RANGE -> InternalState.LEFT_PRESSED_IN_RANGE
                InternalState.RIGHT_PRESSED_OUT_OF_RANGE -> InternalState.RIGHT_PRESSED_IN_RANGE
                else -> internalState
            }
        )
    }

    /**
     * 滑鼠离开视图。
     */
    override fun onMouseExited() {

        updateInternalState(
            when (internalState) {
                InternalState.IN_RANGE -> InternalState.OUT_OF_RANGE
                InternalState.LEFT_PRESSED_IN_RANGE -> InternalState.LEFT_PRESSED_OUT_OF_RANGE
                InternalState.RIGHT_PRESSED_IN_RANGE -> InternalState.RIGHT_PRESSED_OUT_OF_RANGE
                else -> internalState
            }
        )
    }

    /**
     * 滑鼠在视图上移动。
     */
    override fun onMouseMoved(position: Point) {

        when (viewArea.getViewArea().contains(position)) {
            true -> onMouseEntered()
            false -> onMouseExited()
        }
    }

    /**
     * 成为捕获视图。
     */
    override fun onCaptureGot() {}

    /**
     * 不再是捕获视图。
     */
    override fun onCaptureLost() {

        updateInternalState(
            when (internalState) {
                InternalState.LEFT_PRESSED_IN_RANGE -> InternalState.IN_RANGE
                InternalState.RIGHT_PRESSED_IN_RANGE -> InternalState.IN_RANGE
                InternalState.LEFT_PRESSED_OUT_OF_RANGE -> InternalState.OUT_OF_RANGE
                InternalState.RIGHT_PRESSED_OUT_OF_RANGE -> InternalState.OUT_OF_RANGE
                else -> internalState
            }
        )
    }

    /**
     * 更新内部状态。
     */
    private fun updateInternalState(newState: InternalState) {

        // 没有变化，不做处理。
        if (newState == internalState) {
            return
        }

        // 新老内部状态翻译为显示状态，并更新内部状态。
        val originalDisplayState = translate(internalState)
        val newDisplayState = translate(newState)
        internalState = newState

        // 事件通知。
        if (originalDisplayState != newDisplayState) {
            listener.onDisplayStateChanged(originalDisplayState, newDisplayState)
        }
    }

    /**
     * 将内部状态翻译成显示状态。
     */
    private fun translate(internalState: InternalState): DisplayState {

        return when (internalState) {
            InternalState.OUT_OF_RANGE -> DisplayState.NONE
            InternalState.IN_RANGE -> DisplayState.HOVERED
            InternalState.LEFT_PRESSED_IN_RANGE -> DisplayState.LEFT_PRESSED
            InternalState.RIGHT_PRESSED_IN_RANGE -> DisplayState.RIGHT_PRESSED
            InternalState.LEFT_PRESSED_OUT_OF_RANGE -> DisplayState.HOVERED
            InternalState.RIGHT_PRESSED_OUT_OF_RANGE -> DisplayState.HOVERED
        }
    }
}