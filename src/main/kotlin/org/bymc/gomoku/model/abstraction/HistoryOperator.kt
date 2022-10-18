package org.bymc.gomoku.model.abstraction

import org.bymc.gomoku.model.common.param.Drop
import org.bymc.gomoku.model.common.param.Stone
import java.util.*

/**
 * 棋局历史操作器接口。
 *
 * @author: zheng.chez
 * @since: 2022/10/18
 */
interface HistoryOperator {

    /**
     * 追加落子记录。
     */
    fun appendRecord(drop: Drop, dropTime: Date)

    /**
     * 指定执行方的前提下进行悔棋，返回撤回的落子记录。
     */
    fun retract(actor: Stone): List<DropRecord>
}