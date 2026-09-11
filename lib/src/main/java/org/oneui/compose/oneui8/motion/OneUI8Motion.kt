package org.oneui.compose.oneui8.motion

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween

/**
 * Compose-native motion tokens aligned with the SESL8/One UI design resources used by
 * the companion oneui-design fork.
 */
object OneUI8Motion {
    /** oui_des_interpolator_22_25_0_100 */
    val StandardEasing: Easing = CubicBezierEasing(0.22f, 0.25f, 0f, 1f)

    /** A slightly faster response for direct manipulation without changing the curve family. */
    val DirectManipulationEasing: Easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)

    object Duration {
        const val Press = 110
        const val Quick = 140
        const val SelectionFirstStroke = 110
        const val SelectionSecondStroke = 180
        const val SelectionTotal = SelectionFirstStroke + SelectionSecondStroke
        const val Standard = 200
        const val SheetEnterAlpha = 165
        const val SheetEnterTranslation = 300
        const val SheetExitAlpha = 140
        const val SheetExitTranslation = 200
    }

    fun <T> press(): FiniteAnimationSpec<T> = tween(Duration.Press, easing = StandardEasing)
    fun <T> quick(): FiniteAnimationSpec<T> = tween(Duration.Quick, easing = StandardEasing)
    fun <T> selection(): FiniteAnimationSpec<T> = tween(Duration.SelectionTotal, easing = StandardEasing)
    fun <T> standard(): FiniteAnimationSpec<T> = tween(Duration.Standard, easing = StandardEasing)
    fun <T> sheetEnter(): FiniteAnimationSpec<T> = tween(Duration.SheetEnterTranslation, easing = StandardEasing)
    fun <T> sheetExit(): FiniteAnimationSpec<T> = tween(Duration.SheetExitTranslation, easing = StandardEasing)
}
