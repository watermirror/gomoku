package org.bymc.gomoku.model.common.param

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
    FORBIDDEN_BY_TTNH_RULE,

    /**
     * 违反四四禁手规则。
     */
    FORBIDDEN_BY_FFNH_RULE,

    /**
     * 违反长连禁手规则。
     */
    FORBIDDEN_BY_OLNH_RULE,

    /**
     * 落子在棋枰之外。
     */
    OUT_OF_BOARD
}