package org.bymc.gomoku.uifx.view.base

import java.awt.Point

/**
 * 滑鼠事件处理器。
 *
 * @author: zheng.chez
 * @since: 2022/10/08
 */
interface MouseEventHandler {

    /**
     * 滑鼠左键按下。
     */
    fun onLButtonPressed(sender: View, position: Point, pressedCount: Int)

    /**
     * 滑鼠左键释放。
     */
    fun onLButtonReleased(sender: View, position: Point)

    /**
     * 滑鼠右键按下。
     */
    fun onRButtonPressed(sender: View, position: Point, pressedCount: Int)

    /**
     * 滑鼠右键释放。
     */
    fun onRButtonReleased(sender: View, position: Point)

    /**
     * 滑鼠进入视图。
     */
    fun onMouseEntered(sender: View)

    /**
     * 滑鼠离开视图。
     */
    fun onMouseExited(sender: View)

    /**
     * 滑鼠在视图上移动。
     */
    fun onMouseMoved(sender: View, position: Point)

    /**
     * 成为捕获视图。
     */
    fun onCaptureGot(sender: View)

    /**
     * 不再是捕获视图。
     */
    fun onCaptureLost(sender: View)
}