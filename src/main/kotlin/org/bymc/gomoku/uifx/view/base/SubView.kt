package org.bymc.gomoku.uifx.view.base

import java.awt.Point

/**
 * 子视图接口。
 *
 * @author: zheng.chez
 * @since: 2022/09/27
 */
interface SubView {

    /**
     * 设置父视图。
     */
    fun setParent(parentView: View?)

    /**
     * 启用阴影。
     */
    fun enableShadow()

    /**
     * 禁用阴影。
     */
    fun disableShadow()

    /**
     * 设置阴影偏移。
     */
    fun setShadowOffset(offset: Point)

    /**
     * 获取阴影视图。
     */
    fun getShadowView(): View?

    /**
     * 添加到父视图事件。
     */
    fun onAttached(parentView: View)

    /**
     * 从父视图移除事件。
     */
    fun onDetached(parentView: View)
}