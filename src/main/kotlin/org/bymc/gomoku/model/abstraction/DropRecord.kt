package org.bymc.gomoku.model.abstraction

import org.bymc.gomoku.model.common.param.Drop
import java.util.Date

/**
 * 落子记录。
 *
 * @author: zheng.chez
 * @since: 2022/09/22
 */
interface DropRecord {

    /**
     * 获取记录的序号，序号始于 0。
     */
    fun getOrder(): Int

    /**
     * 获取落子操作。
     */
    fun getDrop(): Drop

    /**
     * 获取落子时间。
     */
    fun getTime(): Date

    /**
     * 获取前一条落子记录。
     */
    fun getPrevious(): DropRecord?

    /**
     * 获取后一条落子记录。
     */
    fun getNext(): DropRecord?
}