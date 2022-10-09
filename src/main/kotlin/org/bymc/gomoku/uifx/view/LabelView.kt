package org.bymc.gomoku.uifx.view

import org.bymc.gomoku.uifx.view.base.ViewBase
import org.bymc.gomoku.uifx.view.common.BorderConfig
import org.bymc.gomoku.uifx.view.common.FontConfig
import org.bymc.gomoku.uifx.view.common.HorizontalAlignment
import org.bymc.gomoku.uifx.view.common.VerticalAlignment
import org.bymc.gomoku.uifx.view.util.mouse.MouseInteractiveViewDisplayState
import org.bymc.gomoku.uifx.view.util.mouse.MouseInteractiveEventListener
import org.bymc.gomoku.uifx.view.util.mouse.MouseInteractiveStateTracker
import org.bymc.gomoku.uifx.view.util.mouse.MouseInteractiveViewArea
import org.bymc.gomoku.uifx.view.util.painter.TextPainter
import java.awt.Color
import java.awt.Graphics
import java.awt.Insets
import java.awt.Rectangle

/**
 * 标签视图。
 *
 * @author: zheng.chez
 * @since: 2022/09/29
 */
class LabelView(

    /**
     * 文本内容。
     */
    private var text: String,

    /**
     * 文本颜色。
     */
    private var textColor: Color = Color.BLACK,

    /**
     * 位置尺寸。
     */
    area: Rectangle = Rectangle(0, 0, 0, 0),

    /**
     * 可见性。
     */
    showing: Boolean = true,

    /**
     * 背景色。
     */
    bgColor: Color? = Color.WHITE,

    /**
     * 可交互性。
     */
    interactive: Boolean = true,

    /**
     * 内嵌尺寸。
     */
    private var insets: Insets = Insets(4, 4, 4, 4),

    /**
     * 水平对齐方式。
     */
    private var horizontalAlignment: HorizontalAlignment = HorizontalAlignment.LEFT,

    /**
     * 垂直对齐方式。
     */
    private var verticalAlignment: VerticalAlignment = VerticalAlignment.CENTER,

    /**
     * 字体配置。
     */
    private var fontConfig: FontConfig = FontConfig(),

    /**
     * 边框配置。
     */
    private var borderConfig: BorderConfig = BorderConfig()

) : ViewBase(area, showing, bgColor, interactive), MouseInteractiveViewArea, MouseInteractiveEventListener {

    /**
     * 事件监听器列表。
     */
    private val listeners: MutableList<LabelViewEventListener> = ArrayList()

    /**
     * 滑鼠状态跟踪器。
     */
    private val mouseTracker = MouseInteractiveStateTracker(this, this)

    init {

        /**
         * 注册滑鼠事件处理器。
         */
        this.addMouseEventHandler(mouseTracker)
    }

    /**
     * 添加事件监听器。
     */
    fun addEventListener(listener: LabelViewEventListener) {

        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    /**
     * 移除事件监听器。
     */
    fun removeEventListener(listener: LabelViewEventListener) {

        val index = listeners.indexOf(listener)
        if (index < 0) {
            return
        }
        listeners.removeAt(index)
    }

    /**
     * 获取标签内容。
     */
    fun getText(): String = text

    /**
     * 设置标签内容。
     */
    fun setTest(text: String) {

        this.text = text
        scheduleRender(null)
    }

    /**
     * 获取文本颜色。
     */
    fun getTextColor(): Color = textColor

    /**
     * 设置文本颜色。
     */
    fun setTextColor(textColor: Color) {

        this.textColor = textColor
        scheduleRender(null)
    }

    /**
     * 获取内嵌尺寸。
     */
    fun getInsets(): Insets = insets

    /**
     * 设置内嵌尺寸。
     */
    fun setInsets(insets: Insets) {

        this.insets = insets
        scheduleRender(null)
    }

    /**
     * 获取水平对齐。
      */
    fun getHorizontalAlignment(): HorizontalAlignment = horizontalAlignment

    /**
     * 设置水平对齐。
     */
    fun setHorizontalAlignment(alignment: HorizontalAlignment) {

        this.horizontalAlignment = alignment
        scheduleRender(null)
    }

    /**
     * 获取垂直对齐。
     */
    fun getVerticalAlignment(): VerticalAlignment = verticalAlignment

    /**
     * 设置垂直对齐。
     */
    fun setVerticalAlignment(alignment: VerticalAlignment) {

        this.verticalAlignment = alignment
        scheduleRender(null)
    }

    /**
     * 获取字体配置。
     */
    fun getFontConfig(): FontConfig = fontConfig

    /**
     * 设置字体配置。
     */
    fun setFontConfig(fontConfig: FontConfig) {

        this.fontConfig = fontConfig
        scheduleRender(null)
    }

    /**
     * 获取边框配置。
     */
    fun getBorderConfig(): BorderConfig = borderConfig

    /**
     * 设置边框配置。
     */
    fun setBorderConfig(borderConfig: BorderConfig) {

        this.borderConfig = borderConfig
        scheduleRender(null)
    }

    /**
     * 渲染。
     */
    override fun onRender(g: Graphics, range: Rectangle) {

        // 绘制边框。
        if (borderConfig.borderWidth > 0) {
            renderBorder(g)
        }

        // 计算内嵌后的绘制区域。
        val area = Rectangle(
            insets.left, insets.top,
            getArea().width - insets.left - insets.right, getArea().height - insets.top - insets.bottom
        )

        // 绘制文本。
        TextPainter(text, textColor, area, horizontalAlignment, verticalAlignment, fontConfig).paint(g)
    }

    /**
     * 获取滑鼠交互的视图区域，应与滑鼠事件处于同一个坐标系内。
     */
    override fun getViewArea(): Rectangle = Rectangle(0, 0, getArea().width, getArea().height)

    /**
     * 左键点击事件。
     */
    override fun onLeftClicked() = listeners.forEach { it.onLeftButtonClicked(this) }

    /**
     * 右键点击事件。
     */
    override fun onRightClicked() = listeners.forEach { it.onRightButtonClicked(this) }

    /**
     * 左键双击事件。
     */
    override fun onLeftDoubleClicked() = listeners.forEach { it.onLeftButtonDoubleClicked(this) }

    /**
     * 右键双击事件。
     */
    override fun onRightDoubleClicked() = listeners.forEach { it.onRightButtonDoubleClicked(this) }

    /**
     * 滑鼠交互显示状态变更事件。
     */
    override fun onDisplayStateChanged(original: MouseInteractiveViewDisplayState, current: MouseInteractiveViewDisplayState) {}

    /**
     * 绘制边框。
     */
    private fun renderBorder(g: Graphics) {

        val originalColor = g.color
        g.color = borderConfig.borderColor
        for (i in 0 until borderConfig.borderWidth) {
            g.drawRect(i, i, getArea().width - 2 * i, getArea().height - 2 * i)
        }
        g.color = originalColor
    }
}