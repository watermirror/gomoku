package org.bymc.gomoku.model

import org.bymc.gomoku.uifx.ViewTestingWindow
import org.bymc.gomoku.uifx.util.FrameworksInitializer

/**
 * 主程序。
 */
fun main() {

    // 初始化 UI 框架。
    FrameworksInitializer.initialize()

    // 创建视图框架集成测试窗口。
    ViewTestingWindow()
}