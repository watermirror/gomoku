package org.bymc.gomoku.ui.test

import org.bymc.gomoku.game.factory.GameFactory
import org.bymc.gomoku.model.common.param.DropLegality
import org.bymc.gomoku.model.common.param.GameState
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Size2D
import org.bymc.gomoku.ui.view.BoardIntegrationView
import org.bymc.gomoku.ui.view.BoardSensorViewEventListener
import org.bymc.gomoku.uifx.window.RootViewWindowBase
import org.bymc.gomoku.uifx.window.WindowInitialConfig
import java.awt.Dimension
import java.awt.Rectangle
import java.util.*

/**
 * 测试窗口。
 *
 * @author: zheng.chez
 * @since: 2022/10/17
 */
class BoardTestingWindow : RootViewWindowBase(
    WindowInitialConfig(
        title = "Board Testing Window",
        resizable = false,
        central = true,
        explicitClientSize = true,
        size = Dimension(edgeCapacity * cellSize, edgeCapacity * cellSize)
    )
), BoardSensorViewEventListener {

    companion object {

        private const val cellSize: Int = 41
        private const val edgeCapacity: Int = 15
    }

    private val boardView: BoardIntegrationView = BoardIntegrationView(
        Rectangle(0, 0, edgeCapacity * cellSize, edgeCapacity * cellSize), cellSize, true
    )

    private var game = GameFactory.createGame(Size2D(edgeCapacity, edgeCapacity), Date())

    init {

        getRootView().addSubView(boardView)
        boardView.setViewModels(game.getBoardViewModel(), game.getHistoryViewModel())
        boardView.addSensorEventListener(this)
    }

    /**
     * 单元格点击事件。
     */
    override fun onCellClicked(cellLocation: Location2D) {

        val legality = game.dropStoneAt(cellLocation)
        println(legality)
        if (legality != DropLegality.LEGAL) {
            return
        }

        if (game.getGameSituation().state != GameState.PLAYING) {
            println(game.getGameSituation())
            game = GameFactory.createGame(Size2D(edgeCapacity, edgeCapacity), Date())
            boardView.setViewModels(game.getBoardViewModel(), game.getHistoryViewModel())
        }

        boardView.scheduleRender()
    }
}