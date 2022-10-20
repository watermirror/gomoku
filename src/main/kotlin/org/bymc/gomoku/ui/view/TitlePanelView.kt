package org.bymc.gomoku.ui.view

import org.bymc.gomoku.ui.window.UiConfig
import org.bymc.gomoku.uifx.view.LabelView
import org.bymc.gomoku.uifx.view.base.ViewBase
import org.bymc.gomoku.uifx.view.common.FontConfig
import org.bymc.gomoku.uifx.view.common.HorizontalAlignment
import java.awt.Color
import java.awt.Graphics
import java.awt.Point
import java.awt.Rectangle

/**
 * @author: zheng.chez
 * @since: 2022/10/20
 */
class TitlePanelView(

    /**
     * UI 配置。
     */
    private val uiConfig: UiConfig,

    /**
     * 面板位置。
     */
    position: Point,

) : ViewBase(
    theArea = Rectangle(
        position.x, position.y, uiConfig.getTitlePanelSize().width, uiConfig.getTitlePanelSize().height
    ),
    theBgColor = Color.WHITE,
    shadowEnabled = true
) {

    /**
     * 主标题视图。
     */
    private val majorTitleLabelView: LabelView = LabelView(
        text = uiConfig.getMajorTitleText(),
        area = Rectangle(
            (uiConfig.getTitlePanelSize().width - uiConfig.getMajorTitleLabelSize().width) / 2,
            uiConfig.getMajorTitleLabelTop(),
            uiConfig.getMajorTitleLabelSize().width,
            uiConfig.getMajorTitleLabelSize().height
        ),
        horizontalAlignment = HorizontalAlignment.CENTER,
        fontConfig = FontConfig(
            fontSize = uiConfig.getMajorTitleFontSize(),
            fontStyle = uiConfig.getMajorTitleFontStyle()
        )
    )

    /**
     * 副标题视图。
     */
    private val minorTitleLabelView: LabelView = LabelView(
        text = uiConfig.getMinorTitleText(),
        area = Rectangle(
            (uiConfig.getTitlePanelSize().width - uiConfig.getMinorTitleLabelSize().width) / 2,
            uiConfig.getMinorTitleLabelTop(),
            uiConfig.getMinorTitleLabelSize().width,
            uiConfig.getMinorTitleLabelSize().height
        ),
        horizontalAlignment = HorizontalAlignment.CENTER,
        fontConfig = FontConfig(
            fontSize = uiConfig.getMinorTitleFontSize(),
            fontStyle = uiConfig.getMinorTitleFontStyle()
        )
    )

    /**
     * 配置子视图。
     */
    init {

        addSubView(majorTitleLabelView)
        addSubView(minorTitleLabelView)
    }

    /**
     * 渲染。
     */
    override fun onRender(g: Graphics, range: Rectangle) {

        if (uiConfig.getTitlePanelBorderWidth() <= 0) { return }
        val originalColor = g.color
        g.color = Color.BLACK

        for (i in 0 .. uiConfig.getTitlePanelBorderWidth()) {

            g.drawRect(
                0 + i, 0 + i, uiConfig.getTitlePanelSize().width - 2 * i, uiConfig.getTitlePanelSize().height - 2 * i
            )
        }

        g.color = originalColor
    }
}