package org.bymc.gomoku.uifx.view

/**
 * 标签视图事件监听器。
 *
 * @author: zheng.chez
 * @since: 2022/10/08
 */
interface LabelViewEventListener {

    /**
     * 滑鼠左键点击。
     */
    fun onLeftButtonClicked(sender: LabelView)

    /**
     * 滑鼠右键点击。
     */
    fun onRightButtonClicked(sender: LabelView)

    /**
     * 滑鼠左键双击。
     */
    fun onLeftButtonDoubleClicked(sender: LabelView)

    /**
     * 滑鼠右键双击。
     */
    fun onRightButtonDoubleClicked(sender: LabelView)
}