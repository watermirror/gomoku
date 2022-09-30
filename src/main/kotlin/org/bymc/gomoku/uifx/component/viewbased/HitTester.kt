package org.bymc.gomoku.uifx.component.viewbased

import org.bymc.gomoku.uifx.view.base.View
import java.awt.Point
import java.awt.Rectangle

/**
 * 命中检测器。
 *
 * @author: zheng.chez
 * @since: 2022/09/30
 */
class HitTester(

    /**
     * 要检测的视图。
     */
    private val view: View,

    /**
     * 监测点相对于视图的坐标。
     */
    private val relativePosition: Point

) {

    /**
     * 命中检测。
     */
    fun test(): HitTestResult {

        // 如果视图不可见或不可交互，则无法命中。
        if (!view.getShowing() || !view.getInteractive()) {
            return HitTestResult.buildEmpty()
        }

        // 如果检测点不在视图范围内，则无法命中。
        if (!Rectangle(0, 0, view.getArea().width, view.getArea().height).contains(relativePosition)) {
            return HitTestResult.buildEmpty()
        }

        // 如果子视图命中检测通过，则返回。
        for (subView in view.getSubViews().reversed()) {
            val result = HitTester(
                subView,
                Point(relativePosition.x - subView.getArea().x, relativePosition.y - subView.getArea().y)
            ).test()
            if (result.isNotEmpty()) {
                return result
            }
        }

        // 子视图没有命中检测，则当前视图命中。
        return HitTestResult.build(view, relativePosition)
    }
}