package org.bymc.gomoku.model.impl

import org.bymc.gomoku.model.abstraction.BoardView
import org.bymc.gomoku.model.abstraction.Rule
import org.bymc.gomoku.model.common.param.*
import org.bymc.gomoku.model.common.util.FfnhVerifier
import org.bymc.gomoku.model.common.util.GomokuVerifier
import org.bymc.gomoku.model.common.util.OlnhVerifier
import org.bymc.gomoku.model.common.util.TtnhVerifier

/**
 * 规则接口实现类。
 *
 * @author: zheng.chez
 * @since: 2022/09/23
 */
class RuleImpl(

    /**
     * 是否启用三三禁手规则。
     */
    private val ttnhEnabled: Boolean,

    /**
     * 是否启用四四禁手规则。
     */
    private val ffnhEnabled: Boolean,

    /**
     * 是否启用长连禁手规则。
     */
    private val olnhEnables: Boolean

) : Rule {

    /**
     * 检查落子的合法性。
     */
    override fun checkDropLegality(boardView: BoardView, drop: Drop): DropLegality {

        return when {

            // 越界。
            drop.location.x !in 0 until boardView.getSize().width -> DropLegality.OUT_OF_BOARD
            drop.location.y !in 0 until boardView.getSize().height -> DropLegality.OUT_OF_BOARD

            // 被同色棋子占据。
            boardView.getCell(drop.location).getStone() == drop.stone -> DropLegality.OCCUPIED_BY_SAME_STONE

            // 被异色棋子占用。
            boardView.getCell(drop.location)
                .getStone() == Stone.getOpponent(drop.stone) -> DropLegality.OCCUPIED_BY_DIFFERENT_STONE

            // 三三禁手。
            ttnhEnabled && TtnhVerifier(boardView, drop).verify() -> DropLegality.FORBIDDEN_BY_TTNH_RULE

            // 四四禁手。
            ffnhEnabled && FfnhVerifier(boardView, drop).verify() -> DropLegality.FORBIDDEN_BY_FFNH_RULE

            // 长连禁手。
            olnhEnables && OlnhVerifier(boardView, drop).verify() -> DropLegality.FORBIDDEN_BY_OLNH_RULE

            // 合法。
            else -> DropLegality.LEGAL
        }
    }

    /**
     * 根据最后落子的单元格坐标判定棋局状态。
     */
    override fun judgeGameState(boardView: BoardView, lastDropLocation: Location2D): GameState {

        return when {

            // 越界。
            lastDropLocation.x !in 0 until boardView.getSize().width -> throw RuntimeException("illegal judgement")
            lastDropLocation.y !in 0 until boardView.getSize().height -> throw RuntimeException("illegal judgement")

            // 黑棋或白棋五目获胜。
            GomokuVerifier(boardView, lastDropLocation).verify() -> {
                when (boardView.getCell(lastDropLocation).getStone()!!) {
                    Stone.BLACK -> GameState.BLACK_WON
                    Stone.WHITE -> GameState.WHITE_WON
                }
            }

            // 平局。
            boardView.getSize().width * boardView.getSize().height == boardView.getStoneCount() -> GameState.DRAW

            // 棋局进行中。
            else -> GameState.PLAYING
        }
    }

    /**
     * 判定棋局状态。该方法性能较差。
     */
    override fun judgeGameState(boardView: BoardView): GameState {

        for (x in 0 until boardView.getSize().width) {
            for (y in 0 until boardView.getSize().height) {
                val state = judgeGameState(boardView, Location2D(x, y))
                if (state != GameState.PLAYING) {
                    return state
                }
            }
        }

        return GameState.PLAYING
    }
}