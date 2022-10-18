package org.bymc.gomoku.uifx.util

import java.awt.Canvas
import java.awt.Frame
import java.awt.Rectangle

/**
 * UI 框架初优化器。
 *
 * @author: zheng.chez
 * @since: 2022/10/08
 */
object FrameworksOptimizer {

    /**
     * 优化标记。
     */
    private var optimized = false

    /**
     * 优化 UI 框架。
     */
    fun optimize() {

        // 避免重复优化。
        if (optimized) {
            return
        }

        // 各项优化调用。
        optimizeFont()
    }

    /**
     * 优化字体加载。
     */
    private fun optimizeFont() {

        // 创建一个临时窗口预渲染字体。
        val frame = Frame()
        val canvas = Canvas()
        frame.add(canvas)
        frame.isVisible = true
        frame.bounds = Rectangle(-128, -128, 0, 0)
        val img = canvas.createVolatileImage(1, 1)
        frame.isVisible = false
        val gfx = img.graphics
        gfx.drawString("", 0, 0)
        frame.dispose()
    }
}