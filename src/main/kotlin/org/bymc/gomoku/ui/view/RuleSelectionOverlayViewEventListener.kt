package org.bymc.gomoku.ui.view

import org.bymc.gomoku.model.common.param.Stone

/**
 * Rule selection overlay event listener interface.
 */
interface RuleSelectionOverlayViewEventListener {

    /**
     * Start button clicked with selected rules and stone. The stone is meaningful only when
     * the stone selection row is visible (challenge-AI mode); callers may ignore it otherwise.
     */
    fun onStartClicked(ttnhEnabled: Boolean, ffnhEnabled: Boolean, olnhEnabled: Boolean, userStone: Stone)

    /**
     * Cancel button clicked.
     */
    fun onCancelClicked()
}
