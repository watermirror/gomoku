package org.bymc.gomoku.model.common

/**
 * 落子合法性。
 *
 * @author: zheng.chez
 * @since: 2022/09/22
 */
enum class DropLegality {

    /**
     * 合法。
     */
    LEGAL,

    /**
     * 单元格被同色棋子占用。
     */
    OCCUPIED_BY_SAME_STONE,

    /**
     * 单元格被异色棋子占用。
     */
    OCCUPIED_BY_DIFFERENT_STONE,

    /**
     * 违反三三禁手规则。
     */
    FORBIDDEN_BY_3_3_RULE,

    /**
     * 指定单元格不存在。
     */
    CELL_NONEXISTENT
}