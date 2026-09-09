package org.bymc.gomoku.ui.view

/**
 * Rule selection overlay event listener interface.
 */
interface RuleSelectionOverlayViewEventListener {

    /**
     * Start button clicked with selected rules.
     */
    fun onStartClicked(ttnhEnabled: Boolean, ffnhEnabled: Boolean, olnhEnabled: Boolean)

    /**
     * Cancel button clicked.
     */
    fun onCancelClicked()
}
