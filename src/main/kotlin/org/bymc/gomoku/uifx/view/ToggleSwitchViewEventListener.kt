package org.bymc.gomoku.uifx.view

/**
 * Toggle switch view event listener.
 *
 * @author: zheng.chez
 * @since: 2026/09/09
 */
interface ToggleSwitchViewEventListener {

    /**
     * Toggle state changed by user interaction.
     */
    fun onToggleSwitchToggled(sender: ToggleSwitchView, checked: Boolean)
}
