package org.bymc.gomoku.uifx.view.base

/**
 * 按钮视图事件监听器。
 *
 * @author: zheng.chez
 * @since: 2022/10/11
 */
interface ButtonViewEventListener {

    /**
     * 按钮点击事件。
     */
    fun onClicked(sender: ButtonViewBase)
}