package org.bymc.gomoku.ui.view

import org.bymc.gomoku.uifx.view.base.ViewBase
import java.awt.Rectangle

/**
 * 遮盖层视图，一个用以屏蔽所有视图使其不能被操作的顶层视图。
 *
 * @author: zheng.chez
 * @since: 2022/10/19
 */
class CoveringLayerView(area: Rectangle) : ViewBase(theArea = area)