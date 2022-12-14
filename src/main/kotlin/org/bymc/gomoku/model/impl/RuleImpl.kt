package org.bymc.gomoku.model.impl

import org.bymc.gomoku.model.abstraction.BoardViewModel
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
    override fun checkDropLegality(boardViewModel: BoardViewModel, drop: Drop): DropLegality {

        return when {

            // 越界。
            drop.location.x !in 0 until boardViewModel.getSize().width -> DropLegality.OUT_OF_BOARD
            drop.location.y !in 0 until boardViewModel.getSize().height -> DropLegality.OUT_OF_BOARD

            // 被同色棋子占据。
            boardViewModel.getCell(drop.location).getStone() == drop.stone -> DropLegality.OCCUPIED_BY_SAME_STONE

            // 被异色棋子占用。
            boardViewModel.getCell(drop.location)
                .getStone() == Stone.getOpponent(drop.stone) -> DropLegality.OCCUPIED_BY_DIFFERENT_STONE

            // 三三禁手。
            ttnhEnabled && TtnhVerifier(boardViewModel, drop).verify() -> DropLegality.FORBIDDEN_BY_TTNH_RULE

            // 四四禁手。
            ffnhEnabled && FfnhVerifier(boardViewModel, drop).verify() -> DropLegality.FORBIDDEN_BY_FFNH_RULE

            // 长连禁手。
            olnhEnables && OlnhVerifier(boardViewModel, drop).verify() -> DropLegality.FORBIDDEN_BY_OLNH_RULE

            // 合法。
            else -> DropLegality.LEGAL
        }
    }

    /**
     * 根据最后落子的单元格坐标判定棋局状态。
     */
    override fun judgeGameState(boardViewModel: BoardViewModel, lastDropLocation: Location2D): GameState {

        return when {

            // 越界。
            lastDropLocation.x !in 0 until boardViewModel.getSize().width -> throw RuntimeException("illegal judgement")
            lastDropLocation.y !in 0 until boardViewModel.getSize().height -> throw RuntimeException("illegal judgement")

            // 黑棋或白棋五目获胜。
            GomokuVerifier(boardViewModel, lastDropLocation).verify() -> {
                when (boardViewModel.getCell(lastDropLocation).getStone()!!) {
                    Stone.BLACK -> GameState.BLACK_WON
                    Stone.WHITE -> GameState.WHITE_WON
                }
            }

            // 平局。
            boardViewModel.getSize().width * boardViewModel.getSize().height == boardViewModel.getStoneCount() ->
                GameState.DRAW

            // 棋局进行中。
            else -> GameState.PLAYING
        }
    }

    /**
     * 判定棋局状态。该方法性能较差。
     */
    override fun judgeGameState(boardViewModel: BoardViewModel): GameState {

        for (x in 0 until boardViewModel.getSize().width) {
            for (y in 0 until boardViewModel.getSize().height) {
                if (!boardViewModel.getCell(Location2D(x, y)).isOccupied()) {
                    continue
                }
                val state = judgeGameState(boardViewModel, Location2D(x, y))
                if (state != GameState.PLAYING) {
                    return state
                }
            }
        }

        return GameState.PLAYING
    }
}