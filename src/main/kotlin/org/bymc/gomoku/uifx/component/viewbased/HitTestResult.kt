package org.bymc.gomoku.uifx.component.viewbased

import org.bymc.gomoku.uifx.view.base.View
import java.awt.Point

/**
 * 命中测试的结果。
 *
 * @author: zheng.chez
 * @since: 2022/09/30
 */
data class HitTestResult(

    /**
     * 命中的视图。
     */
    private val hitView: View?,

    /**
     * 命中点相对于命中视图的坐标。
     */
    private val relativePosition: Point?

) {

    /**
     * 获取命中视图。
     */
    fun getHitView(): View? = hitView

    /**
     * 获取命中点相对于命中视图的坐标。
     */
    fun getRelativePosition(): Point? = relativePosition

    /**
     * 是否为空结果。
     */
    fun isEmpty(): Boolean = hitView == null

    /**
     * 是否非空结果。
     */
    fun isNotEmpty(): Boolean = !isEmpty()

    companion object {

        /**
         * 构建命中结果。
         */
        fun build(view: View, position: Point): HitTestResult = HitTestResult(view, position)

        /**
         * 构建空结果。
         */
        fun buildEmpty(): HitTestResult = HitTestResult(null, null)
    }
}