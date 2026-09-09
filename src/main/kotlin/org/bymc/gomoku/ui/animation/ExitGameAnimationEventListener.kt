package org.bymc.gomoku.ui.animation

/**
 * Exit game animation event listener interface.
 */
interface ExitGameAnimationEventListener {

    /**
     * Animation finished.
     */
    fun onFinished(animation: ExitGameAnimation)
}
