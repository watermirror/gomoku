package org.bymc.gomoku.game.impl

import org.bymc.gomoku.game.abstraction.Game
import org.bymc.gomoku.game.common.GameSituation
import org.bymc.gomoku.model.abstraction.Board
import org.bymc.gomoku.model.abstraction.BoardView
import org.bymc.gomoku.model.abstraction.History
import org.bymc.gomoku.model.abstraction.Rule
import org.bymc.gomoku.model.common.param.*
import java.util.Date

/**
 * 游戏接口默认实现类。
 *
 * @author: zheng.chez
 * @since: 2022/09/22
 */
class GameImpl(

    /**
     * 棋盘接口。
     */
    private val board: Board,

    /**
     * 棋局历史接口。
     */
    private val history: History,

    /**
     * 规则接口。
     */
    private val rule: Rule,

    /**
     * 初始回合执行方。
     */
    initialRoundActor: Stone = Stone.BLACK

) : Game {

    /**
     * 游戏情况。
     */
    private var situation: GameSituation

    /**
     * 初始化游戏。
     */
    init {

        // 初始化游戏情况。
        situation = GameSituation(
            rule.judgeGameState(board),
            initialRoundActor,
            0,
            false,
            Date(),
            null,
            null
        ).let { if (it.state != GameState.PLAYING) { it.copy(endTime = it.beginTime) } else { it } }
    }

    /**
     * 获取游戏情况。
     */
    override fun getGameSituation(): GameSituation = situation

    /**
     * 获取棋盘视图。
     */
    override fun getBoardView(): BoardView = board

    /**
     * 落子。返回 LEGAL 表示落子成功，否则不合法。请保障在棋局进行的状态下调用该方法，若 getGameSituation().state 不为 PLAYING，该方法
     * 将抛出异常。
     */
    override fun dropStoneAt(location: Location2D): DropLegality {

        // 断言棋局状态，必须为 PLAYING，即正在进行。
        if (situation.state != GameState.PLAYING) {
            throw RuntimeException("fails to drop on board caused by game situation is not PLAYING")
        }

        // 校验落子合法性。
        val drop = Drop(location, situation.roundActor)
        rule.checkDropLegality(board, drop).also { if(it != DropLegality.LEGAL) { return it } }

        // 落子并记入历史。
        board.dropStone(location, drop.stone)
        history.appendRecord(drop, Date())

        // 交换回合执行方。
        swapRoundActor()

        // 更新游戏情况。
        updateSituationAfterDrop(location)
        return DropLegality.LEGAL
    }

    /**
     * 执行悔棋。
     */
    override fun retract() {

        // 断言棋局状态，必须为 PLAYING，即正在进行。
        if (situation.state != GameState.PLAYING) {
            throw RuntimeException("fails to retract caused by game situation is not PLAYING")
        }

        // 从棋局历史中悔棋，并取得撤销的落子记录。
        val retractedDrops = history.retract(situation.roundActor)
        if (retractedDrops.isEmpty()) {
            throw RuntimeException("fails to retract caused by history.retract returning empty")
        }

        // 从棋盘上抹去要撤销的棋子。
        retractedDrops.forEach { board.cleanCell(it.getDrop().location) }

        // 更新游戏情况。
        updateSituationAfterRetraction()
    }

    /**
     * 交换回合执行方。
     */
    private fun swapRoundActor() {

        situation = situation.copy(roundActor = Stone.getOpponent(situation.roundActor))
    }

    /**
     * 落子后更新游戏情况。
     */
    private fun updateSituationAfterDrop(lastDropLocation: Location2D) {

        situation = situation.copy(
            state = rule.judgeGameState(board, lastDropLocation),
            dropCount = history.getRecordCount(),
            retractionAvailable = isRetractionAvailable(),
            lastDropTime = history.getLastRecord()?.getTime()
        ).let { if (it.state != GameState.PLAYING) { it.copy(endTime = Date()) } else { it } }
    }

    /**
     * 悔棋后更新游戏情况。
     */
    private fun updateSituationAfterRetraction() {

        situation = situation.copy(
            dropCount = history.getRecordCount(),
            retractionAvailable = isRetractionAvailable(),
            lastDropTime = history.getLastRecord()?.getTime()
        )
    }

    /**
     * 判定当前能否悔棋。
     */
    private fun isRetractionAvailable(): Boolean {

        return when {
            history.getRecordCount() > 1 -> true
            history.getRecordCount() == 0 -> false
            else -> history.getLastRecord()!!.getDrop().stone == situation.roundActor
        }
    }
}