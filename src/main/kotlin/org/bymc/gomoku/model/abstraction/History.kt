package org.bymc.gomoku.model.abstraction

import org.bymc.gomoku.model.common.Drop
import org.bymc.gomoku.model.common.Stone
import java.util.Date

/**
 * 棋局历史接口。
 *
 * @author: zheng.chez
 * @since: 2022/09/22
 */
interface History {

    /**
     * 获取棋局开始时间。
     */
    fun getBeginTime(): Date

    /**
     * 获取落子记录数。
     */
    fun getRecordCount(): Int

    /**
     * 根据序号获取落子记录，序号从 0 开始。若序号越界将抛出异常。
     */
    fun getRecord(order: Int): DropRecord

    /**
     * 获取第一条落子记录。
     */
    fun getFirstRecord(): DropRecord?

    /**
     * 获取最后一条落子记录。
     */
    fun getLastRecord(): DropRecord?

    /**
     * 追加落子记录。
     */
    fun appendRecord(drop: Drop, droppedTime: Date)

    /**
     * 指定执行方的前提下进行悔棋，返回撤回的落子记录。
     */
    fun retract(actingStone: Stone): List<DropRecord>
}