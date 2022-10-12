package org.bymc.gomoku.uifx.view.mouse

import org.bymc.gomoku.uifx.view.base.MouseEventHandler
import org.bymc.gomoku.uifx.view.base.View
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
    private var internalState: InternalState = MouseInteractiveViewInternalState.OUT_OF_RANGE

    /**
     * 滑鼠左键按下。
     */
    override fun onLButtonPressed(sender: View, position: Point, pressedCount: Int) {

        if (!viewArea.getViewArea().contains(position)) {
            return
        }

        if (pressedCount % 2 == 0) {
            listener.onLeftDoubleClicked()
        }

        updateInternalState(
            when (internalState) {
                MouseInteractiveViewInternalState.OUT_OF_RANGE -> MouseInteractiveViewInternalState.LEFT_PRESSED_IN_RANGE
                MouseInteractiveViewInternalState.IN_RANGE -> MouseInteractiveViewInternalState.LEFT_PRESSED_IN_RANGE
                MouseInteractiveViewInternalState.LEFT_PRESSED_OUT_OF_RANGE -> MouseInteractiveViewInternalState.LEFT_PRESSED_IN_RANGE
                else -> internalState
            }
        )
    }

    /**
     * 滑鼠左键释放。
     */
    override fun onLButtonReleased(sender: View, position: Point) {

        if (!viewArea.getViewArea().contains(position)) {
            return updateInternalState(
                when (internalState) {
                    MouseInteractiveViewInternalState.LEFT_PRESSED_IN_RANGE -> MouseInteractiveViewInternalState.OUT_OF_RANGE
                    MouseInteractiveViewInternalState.LEFT_PRESSED_OUT_OF_RANGE -> MouseInteractiveViewInternalState.OUT_OF_RANGE
                    else -> internalState
                }
            )
        }

        if (internalState == MouseInteractiveViewInternalState.LEFT_PRESSED_IN_RANGE) {
            listener.onLeftClicked()
        }

        updateInternalState(
            when (internalState) {
                MouseInteractiveViewInternalState.LEFT_PRESSED_IN_RANGE -> MouseInteractiveViewInternalState.IN_RANGE
                MouseInteractiveViewInternalState.LEFT_PRESSED_OUT_OF_RANGE -> MouseInteractiveViewInternalState.IN_RANGE
                else -> internalState
            }
        )
    }

    /**
     * 滑鼠右键按下。
     */
    override fun onRButtonPressed(sender: View, position: Point, pressedCount: Int) {

        if (!viewArea.getViewArea().contains(position)) {
            return
        }

        if (pressedCount % 2 == 0) {
            listener.onRightDoubleClicked()
        }

        updateInternalState(
            when (internalState) {
                MouseInteractiveViewInternalState.OUT_OF_RANGE -> MouseInteractiveViewInternalState.RIGHT_PRESSED_IN_RANGE
                MouseInteractiveViewInternalState.IN_RANGE -> MouseInteractiveViewInternalState.RIGHT_PRESSED_IN_RANGE
                MouseInteractiveViewInternalState.RIGHT_PRESSED_OUT_OF_RANGE -> MouseInteractiveViewInternalState.RIGHT_PRESSED_IN_RANGE
                else -> internalState
            }
        )
    }

    /**
     * 滑鼠右键释放。
     */
    override fun onRButtonReleased(sender: View, position: Point) {

        if (!viewArea.getViewArea().contains(position)) {
            return updateInternalState(
                when (internalState) {
                    MouseInteractiveViewInternalState.RIGHT_PRESSED_IN_RANGE -> MouseInteractiveViewInternalState.OUT_OF_RANGE
                    MouseInteractiveViewInternalState.RIGHT_PRESSED_OUT_OF_RANGE -> MouseInteractiveViewInternalState.OUT_OF_RANGE
                    else -> internalState
                }
            )
        }

        if (internalState == MouseInteractiveViewInternalState.RIGHT_PRESSED_IN_RANGE) {
            listener.onRightClicked()
        }

        updateInternalState(
            when (internalState) {
                MouseInteractiveViewInternalState.RIGHT_PRESSED_IN_RANGE -> MouseInteractiveViewInternalState.IN_RANGE
                MouseInteractiveViewInternalState.RIGHT_PRESSED_OUT_OF_RANGE -> MouseInteractiveViewInternalState.IN_RANGE
                else -> internalState
            }
        )
    }

    /**
     * 滑鼠进入视图。
     */
    override fun onMouseEntered(sender: View) {

        updateInternalState(
            when (internalState) {
                MouseInteractiveViewInternalState.OUT_OF_RANGE -> MouseInteractiveViewInternalState.IN_RANGE
                MouseInteractiveViewInternalState.LEFT_PRESSED_OUT_OF_RANGE -> MouseInteractiveViewInternalState.LEFT_PRESSED_IN_RANGE
                MouseInteractiveViewInternalState.RIGHT_PRESSED_OUT_OF_RANGE -> MouseInteractiveViewInternalState.RIGHT_PRESSED_IN_RANGE
                else -> internalState
            }
        )
    }

    /**
     * 滑鼠离开视图。
     */
    override fun onMouseExited(sender: View) {

        updateInternalState(
            when (internalState) {
                MouseInteractiveViewInternalState.IN_RANGE -> MouseInteractiveViewInternalState.OUT_OF_RANGE
                MouseInteractiveViewInternalState.LEFT_PRESSED_IN_RANGE -> MouseInteractiveViewInternalState.LEFT_PRESSED_OUT_OF_RANGE
                MouseInteractiveViewInternalState.RIGHT_PRESSED_IN_RANGE -> MouseInteractiveViewInternalState.RIGHT_PRESSED_OUT_OF_RANGE
                else -> internalState
            }
        )
    }

    /**
     * 滑鼠在视图上移动。
     */
    override fun onMouseMoved(sender: View, position: Point) {

        when (viewArea.getViewArea().contains(position)) {
            true -> onMouseEntered(sender)
            false -> onMouseExited(sender)
        }
    }

    /**
     * 成为捕获视图。
     */
    override fun onCaptureGot(sender: View) {}

    /**
     * 不再是捕获视图。
     */
    override fun onCaptureLost(sender: View) {

        updateInternalState(
            when (internalState) {
                MouseInteractiveViewInternalState.LEFT_PRESSED_IN_RANGE -> MouseInteractiveViewInternalState.IN_RANGE
                MouseInteractiveViewInternalState.RIGHT_PRESSED_IN_RANGE -> MouseInteractiveViewInternalState.IN_RANGE
                MouseInteractiveViewInternalState.LEFT_PRESSED_OUT_OF_RANGE -> MouseInteractiveViewInternalState.OUT_OF_RANGE
                MouseInteractiveViewInternalState.RIGHT_PRESSED_OUT_OF_RANGE -> MouseInteractiveViewInternalState.OUT_OF_RANGE
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
            MouseInteractiveViewInternalState.OUT_OF_RANGE -> MouseInteractiveViewDisplayState.NONE
            MouseInteractiveViewInternalState.IN_RANGE -> MouseInteractiveViewDisplayState.HOVERED
            MouseInteractiveViewInternalState.LEFT_PRESSED_IN_RANGE -> MouseInteractiveViewDisplayState.LEFT_PRESSED
            MouseInteractiveViewInternalState.RIGHT_PRESSED_IN_RANGE -> MouseInteractiveViewDisplayState.RIGHT_PRESSED
            MouseInteractiveViewInternalState.LEFT_PRESSED_OUT_OF_RANGE -> MouseInteractiveViewDisplayState.HOVERED
            MouseInteractiveViewInternalState.RIGHT_PRESSED_OUT_OF_RANGE -> MouseInteractiveViewDisplayState.HOVERED
        }
    }
}