package org.bymc.gomoku.uifx.util

import java.awt.Canvas
import java.awt.Frame
import java.awt.Rectangle

/**
 * UI 框架初始化器。
 *
 * @author: zheng.chez
 * @since: 2022/10/08
 */
object FrameworksInitializer {

    /**
     * 初始化标记。
     */
    private var initialized = false

    /**
     * 初始化 UI 框架。
     */
    fun initialize() {

        // 避免重复初始化。
        if (initialized) {
            return
        }

        // 各项初始化调用。
        initializeFont()
    }

    /**
     * 初始化字体。
     */
    private fun initializeFont() {

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