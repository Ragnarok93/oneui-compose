package org.oneui.compose.motion

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable

/** Compose equivalents of measured One UI/SESL curves used by this library. */
object OneUiEasing {
    /** oui_des_interpolator_22_25_0_100 from the pinned oneui-design reference. */
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
        /** SwitchCompat above-M thumb movement; minSdk 23 means this is the library path. */
        const val Switch = 300
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

        /** SeslIndexScrollView thumb alpha transition. */
        const val FastScrollerFade = 150
        /** SeslIndexScrollView thumb position animation. */
        const val FastScrollerPosition = 300
    }

    object Delay {
        /** Reference selection AVD starts the first stage after a 1 ms offset. */
        const val SelectionInitialStroke = 1

        /** The second checking path begins immediately after the 110 ms first path. */
        const val SelectionCheckSecondStroke = 110

        /** The reference unchecking second path starts at 140 ms. */
        const val SelectionUncheckSecondStroke = 140

        /** SeslIndexScrollView waits this long after idle before fading the scroll affordance. */
        const val FastScrollerAutoHide = 500
    }

    fun <T> press(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.Press, easing = OneUiEasing.Standard)

    fun <T> quick(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.Quick, easing = OneUiEasing.Standard)

    fun <T> standard(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.Standard, easing = OneUiEasing.Standard)

    fun <T> switch(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.Switch, easing = OneUiEasing.Standard)

    fun <T> sliderPress(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.SliderPress, easing = OneUiEasing.SeslSineInOut80)

    fun <T> sliderThumbPress(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.SliderThumbPress, easing = OneUiEasing.Linear)

    fun <T> sliderThumbRelease(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.SliderThumbRelease, easing = OneUiEasing.SeslSineInOut90)

    fun <T> fab(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.Fab, easing = OneUiEasing.SeslSineInOut80)

    /**
     * First stage of `oui_des_list_item_selection_anim_checking`: 1 ms delay, 110 ms linear.
     */
    fun <T> selectionCheckFirstStroke(): FiniteAnimationSpec<T> = tween(
        durationMillis = Duration.SelectionFirstStroke,
        delayMillis = Delay.SelectionInitialStroke,
        easing = OneUiEasing.Linear,
    )

    /**
     * Second stage of `oui_des_list_item_selection_anim_checking`: starts at 110 ms and uses the
     * Android platform decelerate interpolator for 180 ms.
     */
    fun <T> selectionCheckSecondStroke(): FiniteAnimationSpec<T> = tween(
        durationMillis = Duration.SelectionSecondStroke,
        delayMillis = Delay.SelectionCheckSecondStroke,
        easing = OneUiEasing.Decelerate,
    )

    /**
     * First stage of `oui_des_list_item_selection_anim_unchecking`: 1 ms delay, 120 ms linear.
     */
    fun <T> selectionUncheckFirstStroke(): FiniteAnimationSpec<T> = tween(
        durationMillis = Duration.SelectionUncheckFirstStroke,
        delayMillis = Delay.SelectionInitialStroke,
        easing = OneUiEasing.Linear,
    )

    /**
     * Second stage of `oui_des_list_item_selection_anim_unchecking`: starts at 140 ms, 59 ms using
     * the Android platform accelerate interpolator.
     */
    fun <T> selectionUncheckSecondStroke(): FiniteAnimationSpec<T> = tween(
        durationMillis = Duration.SelectionUncheckSecondStroke,
        delayMillis = Delay.SelectionUncheckSecondStroke,
        easing = OneUiEasing.Accelerate,
    )

    /** Envelope timing retained for callers that animate one scalar for the whole check action. */
    fun <T> selectionCheck(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.SelectionCheck, easing = OneUiEasing.Standard)

    /** Envelope timing retained for callers that animate one scalar for the whole uncheck action. */
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

    /** SeslIndexScrollView fades its thumb linearly over 150 ms. */
    fun <T> fastScrollerFade(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.FastScrollerFade, easing = OneUiEasing.Linear)

    /** SeslIndexScrollView moves its thumb with SINE_OUT_70 over 300 ms. */
    fun <T> fastScrollerPosition(): FiniteAnimationSpec<T> =
        tween(durationMillis = Duration.FastScrollerPosition, easing = OneUiEasing.SeslSineOut70)
}
