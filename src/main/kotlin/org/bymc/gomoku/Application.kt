package org.bymc.gomoku

import org.bymc.gomoku.ui.window.MainWindow
import org.bymc.gomoku.uifx.util.FrameworksOptimizer

/**
 * 主程序。
 */
fun main() {

    // 优化 UI 框架。
    FrameworksOptimizer.optimize()

    // 创建游戏主窗口。
    MainWindow()
}