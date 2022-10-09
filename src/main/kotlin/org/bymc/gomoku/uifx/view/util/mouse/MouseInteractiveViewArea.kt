package org.bymc.gomoku.uifx.view.util.mouse

import java.awt.Rectangle

/**
 * 支持滑鼠交互视图的视图区域。
 *
 * @author: zheng.chez
 * @since: 2022/10/08
 */
interface MouseInteractiveViewArea {

    /**
     * 获取滑鼠交互的视图区域，应与滑鼠事件处于同一个坐标系内。
     */
    fun getViewArea(): Rectangle
}