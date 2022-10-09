package org.bymc.gomoku.uifx.view.util.mouse

/**
 * 滑鼠交互事件监听器。
 *
 * @author: zheng.chez
 * @since: 2022/10/08
 */
interface MouseInteractiveEventListener {

    /**
     * 左键点击事件。
     */
    fun onLeftClicked()

    /**
     * 右键点击事件。
     */
    fun onRightClicked()

    /**
     * 左键双击事件。
     */
    fun onLeftDoubleClicked()

    /**
     * 右键双击事件。
     */
    fun onRightDoubleClicked()

    /**
     * 滑鼠交互显示状态变更事件。
     */
    fun onDisplayStateChanged(original: MouseInteractiveViewDisplayState, current: MouseInteractiveViewDisplayState)
}