package org.bymc.gomoku.model.impl

import org.bymc.gomoku.model.abstraction.DropRecord
import org.bymc.gomoku.model.abstraction.History
import org.bymc.gomoku.model.common.param.Drop
import org.bymc.gomoku.model.common.param.Stone
import java.util.*
import kotlin.collections.ArrayList

/**
 * 棋局历史接口默认实现类。
 *
 * @author: zheng.chez
 * @since: 2022/09/23
 */
class HistoryImpl(

    /**
     * 棋局开始时间。
     */
    private val historyBeginTime: Date

) : History {

    /**
     * 落子记录。
     */
    private val records = LinkedList<DropRecord>()

    /**
     * 获取棋局开始时间。
     */
    override fun getBeginTime(): Date = historyBeginTime

    /**
     * 获取落子记录数。
     */
    override fun getRecordCount(): Int = records.size

    /**
     * 根据序号获取落子记录，序号从 0 开始。若序号越界将抛出异常。
     */
    override fun getRecord(order: Int): DropRecord {

        // 越界判定。
        if (order !in records.indices) {
            throw RuntimeException("order is out of range of history drop records")
        }

        return records[order]
    }

    /**
     * 获取第一条落子记录。
     */
    override fun getFirstRecord(): DropRecord? = records.firstOrNull()

    /**
     * 获取最后一条落子记录。
     */
    override fun getLastRecord(): DropRecord? = records.lastOrNull()

    /**
     * 追加落子记录。
     */
    override fun appendRecord(drop: Drop, dropTime: Date) {

        records.add(DropRecordImpl(this, records.size, drop, dropTime))
    }

    /**
     * 指定执行方的前提下进行悔棋，返回撤回的落子记录。
     */
    override fun retract(actor: Stone): List<DropRecord> {

        // 寻找撤销的最小序号。
        val target = records.findLast { it.getDrop().stone == actor } ?: return emptyList()

        // 从记录列表逆序删除，直到最小序号为止，并收集删除的记录用以返回。
        val retractedRecords = ArrayList<DropRecord>()
        while (records.size > target.getOrder()) {
            retractedRecords.add(records.removeLast())
        }
        return retractedRecords
    }
}