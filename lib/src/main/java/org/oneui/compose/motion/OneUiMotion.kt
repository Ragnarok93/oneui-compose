package org.oneui.compose.motion

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable

/** Compose equivalents of measured One UI/SESL curves used by this library. */
object OneUiEasing {
    /** oui_des_interpolator_22_25_0_100 from oneui-design@c9225f3. */
    val Standard: Easing = CubicBezierEasing(0.22f, 0.25f, 0f, 1f)

    /** Android platform accelerate/decelerate families used by referenced XML animations. */
    val Accelerate: Easing = CubicBezierEasing(0.4f, 0f, 1f, 1f)
    val Decelerate: Easing = CubicBezierEasing(0f, 0f, 0.2f, 1f)
    val Linear: Easing = LinearEasing

    /** Preserves the existing scaffold's direct-manipulation curve as a stable semantic token. */
    val Emphasized: Easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)

    /** Samsung/SESL path interpolators from SeslAnimationUtils. */
    val SeslSineInOut70: Easing = CubicBezierEasing(0.33f, 0f, 0.3f, 1f)
    val SeslSineInOut80: Easing = CubicBezierEasing(0.33f, 0f, 0.2f, 1f)
    val SeslSineInOut90: Easing = CubicBezierEasing(0.33f, 0f, 0.1f, 1f)
    val SeslSineOut70: Easing = CubicBezierEasing(0.17f, 0.17f, 0.3f, 1f)
    val SeslSineOut80: Easing = CubicBezierEasing(0.17f, 0.17f, 0.2f, 1f)
}

@Immutable
data class OneUiMotionScheme(
    val reducedMotion: Boolean = false,
    val standardEasing: Easing = OneUiEasing.Standard,
    val emphasizedEasing: Easing = OneUiEasing.Emphasized,
)

object OneUiMotion {
    object Duration {
        const val Press = 110
        const val Quick = 140
        const val Standard = 200
        /** SeslAbsSeekBar.SliderDrawable press/release expansion. */
        const val SliderPress = 250
        /** SeslAbsSeekBar.ThumbDrawable shrinks to zero linearly when pressed. */
        const val SliderThumbPress = 100
        /** SeslAbsSeekBar.ThumbDrawable restores with SINE_IN_OUT_90. */
        const val SliderThumbRelease = 300
        const val Fab = 400

        const val SheetEnterTranslation = 300
        const val SheetEnterAlpha = 165
        const val SheetExitTranslation = 200
        const val SheetExitAlpha = 140

        const val MenuTranslation = 300
        const val MenuAlpha = 150

        const val SelectionFirstStroke = 110
        const val SelectionSecondStroke = 180
        const val SelectionCheck = 290

        const val SelectionUncheckFirstStroke = 120
        const val SelectionUncheckSecondStroke = 59
        const val SelectionUncheck = 199
    }

    fun <T> press(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.Press, easing = OneUiEasing.Standard)

    fun <T> quick(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.Quick, easing = OneUiEasing.Standard)

    fun <T> standard(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.Standard, easing = OneUiEasing.Standard)

    fun <T> sliderPress(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.SliderPress, easing = OneUiEasing.SeslSineInOut80)

    fun <T> sliderThumbPress(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.SliderThumbPress, easing = OneUiEasing.Linear)

    fun <T> sliderThumbRelease(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.SliderThumbRelease, easing = OneUiEasing.SeslSineInOut90)

    fun <T> fab(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.Fab, easing = OneUiEasing.SeslSineInOut80)

    fun <T> selectionCheck(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.SelectionCheck, easing = OneUiEasing.Standard)

    fun <T> selectionUncheck(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.SelectionUncheck, easing = OneUiEasing.Accelerate)

    fun <T> sheetEnterTranslation(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.SheetEnterTranslation, easing = OneUiEasing.Decelerate)

    fun <T> sheetEnterAlpha(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.SheetEnterAlpha, easing = OneUiEasing.Decelerate)

    fun <T> sheetExitTranslation(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.SheetExitTranslation, easing = OneUiEasing.SeslSineOut80)

    fun <T> sheetExitAlpha(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.SheetExitAlpha, easing = OneUiEasing.SeslSineOut80)

    fun <T> menuTranslation(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.MenuTranslation, easing = OneUiEasing.SeslSineInOut80)

    fun <T> menuAlpha(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.MenuAlpha, easing = OneUiEasing.SeslSineInOut80)
}
