package org.oneui.compose.oneui8.motion

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import org.oneui.compose.motion.OneUiEasing
import org.oneui.compose.motion.OneUiMotion

/** Compatibility facade. New code should use [OneUiMotion] and [OneUiEasing]. */
object OneUI8Motion {
    val StandardEasing: Easing = OneUiEasing.Standard
    val DirectManipulationEasing: Easing = OneUiEasing.Emphasized

    object Duration {
        const val Press = OneUiMotion.Duration.Press
        const val Quick = OneUiMotion.Duration.Quick
        const val SelectionFirstStroke = OneUiMotion.Duration.SelectionFirstStroke
        const val SelectionSecondStroke = OneUiMotion.Duration.SelectionSecondStroke
        const val SelectionTotal = OneUiMotion.Duration.SelectionCheck
        const val Standard = OneUiMotion.Duration.Standard
        const val SheetEnterAlpha = OneUiMotion.Duration.SheetEnterAlpha
        const val SheetEnterTranslation = OneUiMotion.Duration.SheetEnterTranslation
        const val SheetExitAlpha = OneUiMotion.Duration.SheetExitAlpha
        const val SheetExitTranslation = OneUiMotion.Duration.SheetExitTranslation
    }

    fun <T> press(): FiniteAnimationSpec<T> = OneUiMotion.press()
    fun <T> quick(): FiniteAnimationSpec<T> = OneUiMotion.quick()
    fun <T> selection(): FiniteAnimationSpec<T> = OneUiMotion.selectionCheck()
    fun <T> standard(): FiniteAnimationSpec<T> = OneUiMotion.standard()
    fun <T> sheetEnter(): FiniteAnimationSpec<T> = OneUiMotion.sheetEnterTranslation()
    fun <T> sheetExit(): FiniteAnimationSpec<T> = OneUiMotion.sheetExitTranslation()
}
