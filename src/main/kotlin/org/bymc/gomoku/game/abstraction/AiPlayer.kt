package org.bymc.gomoku.game.abstraction

import org.bymc.gomoku.model.abstraction.BoardViewModel
import org.bymc.gomoku.model.abstraction.Rule
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Stone

/**
 * AI 玩家接口。为给定执子方在当前棋面上决定一个落子点。
 *
 * 实现约定：
 * - 调用方须保证棋局进行中且轮到 stone 方；
 * - 返回值必须为空位，且当 stone 为黑棋时必须通过 rule 的合法性校验；
 * - 棋枰无空位或无任何合法落子点时返回 null，调用方据此按和局处理；
 * - 纯查询，不得产生落子等副作用；
 * - 在 EDT 上调用，实现不得自行开启线程。
 *
 * @author: zheng.chez
 * @since: 2026/10/28
 */
interface AiPlayer {

    /**
     * 决定落子点。
     *
     * @param boardViewModel 棋枰视图模型。
     * @param rule 规则。
     * @param stone AI 执子颜色。
     * @return 合法落子点；无合法落子点时返回 null。
     */
    fun pickDropLocation(boardViewModel: BoardViewModel, rule: Rule, stone: Stone): Location2D?
}
