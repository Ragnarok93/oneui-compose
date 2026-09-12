package org.oneui.compose.components.slider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.awaitFirstDown
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.abs

private enum class RangeThumb { Start, End }

/** SESL8 Material RangeSlider geometry. */
object OneUiRangeSliderDefaults {
    val Height = 48.dp
    val SidePadding = 16.dp
    val TrackThickness = 4.dp
    val ThumbRadius = 10.dp
    val HaloRadius = 24.dp
}

/**
 * One UI range slider based on the SESL8 Material `RangeSlider` interaction model and dimensions.
 * The most recently touched bound becomes the keyboard/accessibility-adjustable bound.
 */
@Composable
fun OneUiRangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    minSeparation: Float = 0f,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: OneUiSliderColors = OneUiSliderDefaults.colors(),
) {
    require(valueRange.start.isFinite() && valueRange.endInclusive.isFinite())
    require(valueRange.start <= valueRange.endInclusive)
    require(steps >= 0)
    require(minSeparation >= 0f)

    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val currentOnValueChange by rememberUpdatedState(onValueChange)
    val currentOnFinished by rememberUpdatedState(onValueChangeFinished)
    var activeThumb by remember { mutableStateOf(RangeThumb.Start) }
    var pressedThumb by remember { mutableStateOf<RangeThumb?>(null) }

    val start = snapSliderValue(value.start, valueRange, steps)
    val end = snapSliderValue(value.endInclusive, valueRange, steps).coerceAtLeast(start)
    val span = valueRange.endInclusive - valueRange.start
    val keyIncrement = when {
        span == 0f -> 0f
        steps > 0 -> span / (steps + 1)
        else -> span / 20f
    }

    fun dispatch(thumb: RangeThumb, candidate: Float) {
        val snapped = snapSliderValue(candidate, valueRange, steps)
        when (thumb) {
            RangeThumb.Start -> {
                val maxStart = (end - minSeparation).coerceAtLeast(valueRange.start)
                currentOnValueChange(snapped.coerceAtMost(maxStart)..end)
            }
            RangeThumb.End -> {
                val minEnd = (start + minSeparation).coerceAtMost(valueRange.endInclusive)
                currentOnValueChange(start..snapped.coerceAtLeast(minEnd))
            }
        }
    }

    val activeValue = if (activeThumb == RangeThumb.Start) start else end
    val input = Modifier
        .semantics {
            stateDescription = "$start – $end"
            progressBarRangeInfo = ProgressBarRangeInfo(activeValue, valueRange, steps)
            if (!enabled) disabled()
            setProgress { target ->
                if (!enabled) false
                else {
                    dispatch(activeThumb, target)
                    currentOnFinished?.invoke()
                    true
                }
            }
        }
        .onKeyEvent { event ->
            if (!enabled || event.type != KeyEventType.KeyDown) return@onKeyEvent false
            val physicalDelta = when (event.key) {
                Key.DirectionLeft -> if (rtl) keyIncrement else -keyIncrement
                Key.DirectionRight -> if (rtl) -keyIncrement else keyIncrement
                Key.DirectionUp -> keyIncrement
                Key.DirectionDown -> -keyIncrement
                else -> return@onKeyEvent false
            }
            dispatch(activeThumb, activeValue + physicalDelta)
            currentOnFinished?.invoke()
            true
        }
        .focusable(enabled = enabled, interactionSource = interactionSource)
        .hoverable(interactionSource = interactionSource, enabled = enabled)
        .then(
            if (enabled) {
                Modifier.pointerInput(valueRange, steps, rtl, minSeparation, interactionSource) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val press = PressInteraction.Press(down.position)
                        interactionSource.emit(press)

                        val sidePaddingPx = OneUiRangeSliderDefaults.SidePadding.toPx()
                        val usableWidth = (size.width - (sidePaddingPx * 2f)).coerceAtLeast(1f)
                        fun valueForX(x: Float): Float {
                            val physical = ((x - sidePaddingPx) / usableWidth).coerceIn(0f, 1f)
                            val fraction = if (rtl) 1f - physical else physical
                            return sliderValueForFraction(fraction, valueRange, steps)
                        }

                        val downValue = valueForX(down.position.x)
                        val selected = if (abs(downValue - start) <= abs(downValue - end)) {
                            RangeThumb.Start
                        } else {
                            RangeThumb.End
                        }
                        activeThumb = selected
                        pressedThumb = selected
                        dispatch(selected, downValue)
                        down.consume()

                        var released = false
                        try {
                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull { it.id == down.id } ?: break
                                if (!change.pressed) {
                                    released = true
                                    break
                                }
                                dispatch(selected, valueForX(change.position.x))
                                change.consume()
                            }
                        } finally {
                            pressedThumb = null
                            if (released) interactionSource.emit(PressInteraction.Release(press))
                            else interactionSource.emit(PressInteraction.Cancel(press))
                            currentOnFinished?.invoke()
                        }
                    }
                }
            } else Modifier,
        )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(OneUiRangeSliderDefaults.Height)
            .then(input),
    ) {
        val side = OneUiRangeSliderDefaults.SidePadding.toPx()
        val left = side
        val right = (size.width - side).coerceAtLeast(left)
        val y = size.height / 2f
        val trackWidth = OneUiRangeSliderDefaults.TrackThickness.toPx()
        val thumbRadius = OneUiRangeSliderDefaults.ThumbRadius.toPx()
        val haloRadius = OneUiRangeSliderDefaults.HaloRadius.toPx()

        fun xFor(value: Float): Float {
            val logical = sliderValueFraction(value, valueRange)
            val physical = if (rtl) 1f - logical else logical
            return left + ((right - left) * physical)
        }

        val startX = xFor(start)
        val endX = xFor(end)
        val activeStart = minOf(startX, endX)
        val activeEnd = maxOf(startX, endX)
        val inactive = if (enabled) colors.inactiveTrack else colors.disabledInactiveTrack
        val active = if (enabled) colors.activeTrack else colors.disabledActiveTrack
        val thumb = if (enabled) colors.thumb else colors.disabledThumb

        drawLine(inactive, Offset(left, y), Offset(right, y), trackWidth, StrokeCap.Round)
        drawLine(active, Offset(activeStart, y), Offset(activeEnd, y), trackWidth, StrokeCap.Round)

        if (enabled && pressedThumb == RangeThumb.Start) {
            drawCircle(colors.interactionHalo, haloRadius, Offset(startX, y))
        }
        if (enabled && pressedThumb == RangeThumb.End) {
            drawCircle(colors.interactionHalo, haloRadius, Offset(endX, y))
        }
        drawCircle(thumb, thumbRadius, Offset(startX, y))
        drawCircle(thumb, thumbRadius, Offset(endX, y))
    }
}
