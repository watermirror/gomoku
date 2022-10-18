package org.bymc.gomoku.model.abstraction

import java.util.*

/**
 * 棋局历史视图模型接口。
 *
 * @author: zheng.chez
 * @since: 2022/10/18
 */
interface HistoryViewModel {

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
}