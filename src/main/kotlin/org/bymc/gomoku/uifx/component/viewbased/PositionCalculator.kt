package org.bymc.gomoku.uifx.component.viewbased

import org.bymc.gomoku.uifx.view.base.View
import java.awt.Point

/**
 * 相对坐标计算器。
 *
 * @author: zheng.chez
 * @since: 2022/09/30
 */
class PositionCalculator(

    /**
     * 目标坐标的本系视图。
     */
    private val view: View,

    /**
     * 包含目标视图的容器视图（可以是多级容器），若该视图实际上并不包含本系视图，则换算结果恒为空。
     */
    private val container: View,

    /**
     * 坐标相对于容器视图的值。
     */
    private val positionOnContainer: Point

) {

    /**
     * 计算相对坐标。若本系视图的根不是当前根视图、不可见或不可交互，返回坐标为空。
     */
    fun calculate(): Point? {

        // 计算本系视图相对于容器视图的坐标。若本系视图的根不是当前根视图、不可见或不可交互，返回坐标为空。
        val offsetFromContainer = calculateOffset(view, container) ?: return null

        // 换算坐标并返回。
        return Point(positionOnContainer.x - offsetFromContainer.x, positionOnContainer.y - offsetFromContainer.y)
    }

    /**
     * 计算指定视图相对于容器视图的坐标。若指定视图的根不是当前根视图、不可见或不可交互，返回坐标为空。
     */
    private fun calculateOffset(view: View, container: View): Point? {

        // 若指定视图即容器视图，相对坐标为 (0, 0)。
        if (view == container) {
            return Point(0, 0)
        }

        // 若指定视图不可见或不可交互，返回空。
        if (!view.getShowing() || !view.getInteractive()) {
            return null
        }

        // 若回溯到视图树的根部依然没有找到容器视图，则返回空。
        val parent = view.getParentView() ?: return null

        // 计算父视图相对于容器视图的坐标，并累加返回。
        val parentOffset = calculateOffset(parent, container) ?: return null
        return Point(view.getArea().x + parentOffset.x, view.getArea().y + parentOffset.y)
    }
}