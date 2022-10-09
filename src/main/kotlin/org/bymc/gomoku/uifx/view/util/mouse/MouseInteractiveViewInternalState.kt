package org.bymc.gomoku.uifx.view.util.mouse

/**
 * 支持滑鼠交互视图的内部状态。
 *
 * @author: zheng.chez
 * @since: 2022/10/08
 */
enum class MouseInteractiveViewInternalState {

    /**
     * 未捕获，滑鼠在域内。
     */
    IN_RANGE,

    /**
     * 未捕获，滑鼠在域外。
     */
    OUT_OF_RANGE,

    /**
     * 捕获、左键按压、滑鼠在域内。
     */
    LEFT_PRESSED_IN_RANGE,

    /**
     * 捕获、右键按压、滑鼠在域内。
     */
    RIGHT_PRESSED_IN_RANGE,

    /**
     * 捕获、左键按压、滑鼠在域外。
     */
    LEFT_PRESSED_OUT_OF_RANGE,

    /**
     * 捕获、右键按压、滑鼠在域外。
     */
    RIGHT_PRESSED_OUT_OF_RANGE,
}