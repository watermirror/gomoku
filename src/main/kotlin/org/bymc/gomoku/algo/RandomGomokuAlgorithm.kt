package org.bymc.gomoku.algo

import org.bymc.gomoku.model.abstraction.BoardViewModel
import org.bymc.gomoku.model.abstraction.Rule
import org.bymc.gomoku.model.common.param.Drop
import org.bymc.gomoku.model.common.param.DropLegality
import org.bymc.gomoku.model.common.param.Location2D
import org.bymc.gomoku.model.common.param.Stone
import kotlin.random.Random
import kotlin.random.Random.Default

/**
 * 随机策略五子棋算法。从所有空位中均匀随机选择一个符合规则的落子点。
 *
 * 黑棋会排除禁手点；白棋无禁手约束。可注入随机种子以保证测试可复现。
 *
 * @author: zheng.chez
 * @since: 2026/10/29
 */
class RandomGomokuAlgorithm(

    /**
     * 随机源。
     */
    private val random: Random = Default

) : GomokuAlgorithm {

    /**
     * 决定落子点。
     *
     * @param boardViewModel 棋枰视图模型。
     * @param rule 规则。
     * @param stone AI 执子颜色。
     * @return 合法落子点；无合法落子点时返回 null。
     */
    override fun pickDropLocation(boardViewModel: BoardViewModel, rule: Rule, stone: Stone): Location2D? {

        val size = boardViewModel.getSize()
        val candidates = ArrayList<Location2D>()
        for (y in 0 until size.height) {
            for (x in 0 until size.width) {
                val location = Location2D(x, y)
                if (boardViewModel.getCell(location).isOccupied()) { continue }
                if (rule.checkDropLegality(boardViewModel, Drop(location, stone)) != DropLegality.LEGAL) {
                    continue
                }
                candidates.add(location)
            }
        }

        if (candidates.isEmpty()) { return null }
        return candidates[random.nextInt(candidates.size)]
    }

    companion object {

        /**
         * 以指定种子构建实例，用于测试复现。
         */
        operator fun invoke(seed: Long): RandomGomokuAlgorithm = RandomGomokuAlgorithm(Random(seed))
    }
}
