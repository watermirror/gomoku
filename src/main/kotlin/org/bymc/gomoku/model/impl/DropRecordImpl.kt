package org.bymc.gomoku.model.impl

import org.bymc.gomoku.model.abstraction.DropRecord
import org.bymc.gomoku.model.abstraction.History
import org.bymc.gomoku.model.common.param.Drop
import java.util.*

/**
 * 落子记录实现类。
 *
 * @author: zheng.chez
 * @since: 2022/09/23
 */
data class DropRecordImpl(

    /**
     * 查询到该落子记录的 History 接口。
     */
    private val history: History,

    /**
     * 当前落子记录的序号。
     */
    private val recordOrder: Int,

    /**
     * 落子操作内容。
     */
    private val dropData: Drop,

    /**
     * 落子时间。
     */
    private val dropTime: Date

) : DropRecord {

    /**
     * 获取记录的序号，序号始于 0。
     */
    override fun getOrder(): Int = recordOrder

    /**
     * 获取落子操作。
     */
    override fun getDrop(): Drop = dropData

    /**
     * 获取落子时间。
     */
    override fun getTime(): Date = dropTime

    /**
     * 获取前一条落子记录。
     */
    override fun getPrevious(): DropRecord? {

        if (recordOrder - 1 < 0) {
            return null
        }
        return history.getRecord(recordOrder - 1)
    }

    /**
     * 获取后一条落子记录。
     */
    override fun getNext(): DropRecord? {

        if (recordOrder + 1 >= history.getRecordCount()) {
            return null
        }
        return history.getRecord(recordOrder + 1)
    }
}