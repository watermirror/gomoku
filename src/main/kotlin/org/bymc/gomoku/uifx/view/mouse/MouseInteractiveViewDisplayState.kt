package org.bymc.gomoku.uifx.view.mouse

/**
 * 支持滑鼠交互视图的显示状态。
 *
 * @author: zheng.chez
 * @since: 2022/10/08
 */
enum class MouseInteractiveViewDisplayState {

    /**
     * 无状态。
     */
    NONE,

    /**
     * 悬停状态。
     */
    HOVERED,

    /**
     * 左键按下状态。
     */
    LEFT_PRESSED,

    /**
     * 右键按下状态。
     */
    RIGHT_PRESSED
}