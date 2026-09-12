package org.oneui.compose.components.slider

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.oneui.compose.motion.OneUiMotion
import org.oneui.compose.theme.OneUiTheme

/** Orientation variants supported by the Compose-native SESL8 slider engine. */
enum class OneUiSliderOrientation {
    Horizontal,
    Vertical,
}

/**
 * SESL seekbar visual modes represented by the stable Compose slider.
 *
 * [Standard] keeps the 3dp track and regular thumb while pressed. [Expand] mirrors SESL
 * MODE_EXPAND / MODE_EXPAND_VERTICAL: the track expands from 3dp to 13dp while the thumb shrinks
 * to zero, using the exact independent track and thumb timing curves from SeslAbsSeekBar.
 */
enum class OneUiSliderMode {
    Standard,
    Expand,
}

/** Colors for [OneUiSlider]. */
@Immutable
data class OneUiSliderColors(
    val activeTrack: Color,
    val inactiveTrack: Color,
    /** Inner thumb fill. SESL uses a near-white fill in light mode and near-black fill in dark. */
    val thumb: Color,
    /** Thumb outline/tint; normally the activated/accent color. */
    val thumbStroke: Color,
    val interactionHalo: Color,
    val warning: Color,
    val disabledActiveTrack: Color,
    val disabledInactiveTrack: Color,
    val disabledThumb: Color,
)

/** SESL8 seekbar geometry and defaults mirrored by the Compose implementation. */
object OneUiSliderDefaults {
    val ThumbRadius = 6.5.dp
    val ThumbStroke = 2.dp
    val TrackThickness = 3.dp
    val ExpandedTrackThickness = 13.dp
    val InteractionRadius = 16.dp
    val TouchTarget = 48.dp
    val DefaultVerticalLength = 160.dp

    @Composable
    fun colors(
        activeTrack: Color = OneUiTheme.colors.accent,
        inactiveTrack: Color = OneUiTheme.colors.controlInactive.copy(alpha = 0.28f),
        thumb: Color = OneUiTheme.colors.surface,
        thumbStroke: Color = OneUiTheme.colors.accent,
        interactionHalo: Color = OneUiTheme.colors.accent.copy(alpha = 0.16f),
        warning: Color = OneUiTheme.colors.destructive,
        disabledActiveTrack: Color = OneUiTheme.colors.controlInactive.copy(alpha = 0.35f),
        disabledInactiveTrack: Color = OneUiTheme.colors.controlInactive.copy(alpha = 0.18f),
        disabledThumb: Color = OneUiTheme.colors.controlInactive.copy(alpha = 0.45f),
    ): OneUiSliderColors = OneUiSliderColors(
        activeTrack = activeTrack,
        inactiveTrack = inactiveTrack,
        thumb = thumb,
        thumbStroke = thumbStroke,
        interactionHalo = interactionHalo,
        warning = warning,
        disabledActiveTrack = disabledActiveTrack,
        disabledInactiveTrack = disabledInactiveTrack,
        disabledThumb = disabledThumb,
    )
}

/**
 * Compose-native One UI 8 slider based on SESL8 `SeslAbsSeekBar` geometry, state behavior and
 * motion.
 *
 * The component supports tap and drag input, horizontal RTL mirroring, vertical input, discrete
 * steps, keyboard/D-pad adjustment, progress semantics, SESL standard/expand modes and an optional
 * warning range. The caller owns [value]; all touch, key and accessibility changes are routed
 * through [onValueChange].
 */
@Composable
fun OneUiSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    orientation: OneUiSliderOrientation = OneUiSliderOrientation.Horizontal,
    mode: OneUiSliderMode = OneUiSliderMode.Standard,
    warningRange: ClosedFloatingPointRange<Float>? = null,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: OneUiSliderColors = OneUiSliderDefaults.colors(),
) {
    require(valueRange.start.isFinite() && valueRange.endInclusive.isFinite()) {
        "valueRange must contain finite values"
    }
    require(valueRange.start <= valueRange.endInclusive) {
        "valueRange start must be <= endInclusive"
    }
    require(steps >= 0) { "steps must be >= 0" }

    val layoutDirection = LocalLayoutDirection.current
    val rtl = orientation == OneUiSliderOrientation.Horizontal && layoutDirection == LayoutDirection.Rtl
    val currentValue = snapSliderValue(value, valueRange, steps)
    val currentOnValueChange by rememberUpdatedState(onValueChange)
    val currentOnFinished by rememberUpdatedState(onValueChangeFinished)
    val pressed by interactionSource.collectIsPressedAsState()
    val reducedMotion = OneUiTheme.motion.reducedMotion
    val expandMode = mode == OneUiSliderMode.Expand

    val trackThickness by animateDpAsState(
        targetValue = if (expandMode && pressed) {
            OneUiSliderDefaults.ExpandedTrackThickness
        } else {
            OneUiSliderDefaults.TrackThickness
        },
        animationSpec = if (reducedMotion || !expandMode) snap() else OneUiMotion.sliderPress(),
        label = "One UI slider track expansion",
    )
    val thumbRadius by animateDpAsState(
        targetValue = if (expandMode && pressed) 0.dp else OneUiSliderDefaults.ThumbRadius,
        animationSpec = when {
            reducedMotion || !expandMode -> snap()
            pressed -> OneUiMotion.sliderThumbPress()
            else -> OneUiMotion.sliderThumbRelease()
        },
        label = "One UI slider expand thumb radius",
    )

    val rangeSpan = valueRange.endInclusive - valueRange.start
    val keyIncrement = when {
        rangeSpan == 0f -> 0f
        steps > 0 -> rangeSpan / (steps + 1)
        else -> rangeSpan / 20f
    }

    fun dispatchFraction(fraction: Float) {
        val next = sliderValueForFraction(fraction, valueRange, steps)
        if (next != currentValue) currentOnValueChange(next)
    }

    fun keyDelta(key: Key): Float? = when (orientation) {
        OneUiSliderOrientation.Horizontal -> when (key) {
            Key.DirectionLeft -> if (rtl) keyIncrement else -keyIncrement
            Key.DirectionRight -> if (rtl) -keyIncrement else keyIncrement
            Key.DirectionUp -> keyIncrement
            Key.DirectionDown -> -keyIncrement
            else -> null
        }
        OneUiSliderOrientation.Vertical -> when (key) {
            Key.DirectionUp -> keyIncrement
            Key.DirectionRight -> keyIncrement
            Key.DirectionDown -> -keyIncrement
            Key.DirectionLeft -> -keyIncrement
            else -> null
        }
    }

    val sizing = when (orientation) {
        OneUiSliderOrientation.Horizontal -> Modifier
            .fillMaxWidth()
            .height(OneUiSliderDefaults.TouchTarget)
        OneUiSliderOrientation.Vertical -> Modifier
            .width(OneUiSliderDefaults.TouchTarget)
            .heightIn(min = OneUiSliderDefaults.DefaultVerticalLength)
    }

    val input = Modifier
        .semantics {
            progressBarRangeInfo = ProgressBarRangeInfo(currentValue, valueRange, steps)
            if (!enabled) disabled()
            setProgress { target ->
                if (!enabled) {
                    false
                } else {
                    val next = snapSliderValue(target, valueRange, steps)
                    currentOnValueChange(next)
                    currentOnFinished?.invoke()
                    true
                }
            }
        }
        .onKeyEvent { event ->
            if (!enabled || event.type != KeyEventType.KeyDown) {
                false
            } else {
                val delta = keyDelta(event.key) ?: return@onKeyEvent false
                val next = snapSliderValue(currentValue + delta, valueRange, steps)
                if (next != currentValue) {
                    currentOnValueChange(next)
                    currentOnFinished?.invoke()
                }
                true
            }
        }
        .focusable(enabled = enabled, interactionSource = interactionSource)
        .hoverable(interactionSource = interactionSource, enabled = enabled)
        .then(
            if (enabled) {
                Modifier.pointerInput(valueRange, steps, orientation, rtl, interactionSource) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val press = PressInteraction.Press(down.position)
                        interactionSource.tryEmit(press)

                        fun update(position: Offset) {
                            val fraction = when (orientation) {
                                OneUiSliderOrientation.Horizontal -> sliderFraction(position.x, size.width.toFloat(), rtl)
                                OneUiSliderOrientation.Vertical -> {
                                    if (size.height <= 0) 0f
                                    else (1f - (position.y / size.height.toFloat())).coerceIn(0f, 1f)
                                }
                            }
                            dispatchFraction(fraction)
                        }

                        update(down.position)
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
                                update(change.position)
                                change.consume()
                            }
                        } finally {
                            if (released) {
                                interactionSource.tryEmit(PressInteraction.Release(press))
                            } else {
                                interactionSource.tryEmit(PressInteraction.Cancel(press))
                            }
                            currentOnFinished?.invoke()
                        }
                    }
                }
            } else {
                Modifier
            },
        )

    val fraction = sliderValueFraction(currentValue, valueRange)
    val inWarningRange = warningRange?.let { currentValue >= it.start && currentValue <= it.endInclusive } == true
    val activeColor = when {
        !enabled -> colors.disabledActiveTrack
        inWarningRange -> colors.warning
        else -> colors.activeTrack
    }
    val inactiveColor = if (enabled) colors.inactiveTrack else colors.disabledInactiveTrack
    val thumbFillColor = if (enabled) colors.thumb else colors.disabledThumb
    val thumbOutlineColor = when {
        !enabled -> colors.disabledThumb
        inWarningRange -> colors.warning
        else -> colors.thumbStroke
    }

    Canvas(modifier = modifier.then(sizing).then(input)) {
        val edge = OneUiSliderDefaults.InteractionRadius.toPx()
        val thickness = trackThickness.toPx()
        val actualThumbRadius = thumbRadius.toPx()
        val thumbStroke = OneUiSliderDefaults.ThumbStroke.toPx()
        val haloRadius = OneUiSliderDefaults.InteractionRadius.toPx()

        val center = when (orientation) {
            OneUiSliderOrientation.Horizontal -> {
                val start = edge.coerceAtMost(size.width / 2f)
                val end = (size.width - edge).coerceAtLeast(size.width / 2f)
                val physicalFraction = if (rtl) 1f - fraction else fraction
                val x = start + ((end - start) * physicalFraction)
                val y = size.height / 2f

                drawLine(inactiveColor, Offset(start, y), Offset(end, y), thickness, StrokeCap.Round)
                warningRange?.let { range ->
                    val warningStart = sliderValueFraction(range.start, valueRange)
                    val warningEnd = sliderValueFraction(range.endInclusive, valueRange)
                    val x1Fraction = if (rtl) 1f - warningStart else warningStart
                    val x2Fraction = if (rtl) 1f - warningEnd else warningEnd
                    drawLine(
                        color = colors.warning.copy(alpha = if (enabled) 0.24f else 0.12f),
                        start = Offset(start + ((end - start) * x1Fraction), y),
                        end = Offset(start + ((end - start) * x2Fraction), y),
                        strokeWidth = thickness,
                        cap = StrokeCap.Round,
                    )
                }
                val activeStart = if (rtl) Offset(end, y) else Offset(start, y)
                drawLine(activeColor, activeStart, Offset(x, y), thickness, StrokeCap.Round)
                Offset(x, y)
            }

            OneUiSliderOrientation.Vertical -> {
                val top = edge.coerceAtMost(size.height / 2f)
                val bottom = (size.height - edge).coerceAtLeast(size.height / 2f)
                val y = bottom - ((bottom - top) * fraction)
                val x = size.width / 2f

                drawLine(inactiveColor, Offset(x, top), Offset(x, bottom), thickness, StrokeCap.Round)
                warningRange?.let { range ->
                    val warningStart = sliderValueFraction(range.start, valueRange)
                    val warningEnd = sliderValueFraction(range.endInclusive, valueRange)
                    drawLine(
                        color = colors.warning.copy(alpha = if (enabled) 0.24f else 0.12f),
                        start = Offset(x, bottom - ((bottom - top) * warningStart)),
                        end = Offset(x, bottom - ((bottom - top) * warningEnd)),
                        strokeWidth = thickness,
                        cap = StrokeCap.Round,
                    )
                }
                drawLine(activeColor, Offset(x, bottom), Offset(x, y), thickness, StrokeCap.Round)
                Offset(x, y)
            }
        }

        if (pressed && enabled) {
            drawCircle(colors.interactionHalo, radius = haloRadius, center = center)
        }
        if (actualThumbRadius > 0f) {
            drawCircle(thumbFillColor, radius = actualThumbRadius, center = center)
            if (enabled && thumbStroke > 0f) {
                drawCircle(
                    color = thumbOutlineColor,
                    radius = (actualThumbRadius - (thumbStroke / 2f)).coerceAtLeast(0f),
                    center = center,
                    style = Stroke(width = thumbStroke),
                )
            }
        }
    }
}

/** Convenience vertical form of [OneUiSlider]. */
@Composable
fun OneUiVerticalSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    mode: OneUiSliderMode = OneUiSliderMode.Standard,
    warningRange: ClosedFloatingPointRange<Float>? = null,
    onValueChangeFinished: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: OneUiSliderColors = OneUiSliderDefaults.colors(),
) = OneUiSlider(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    enabled = enabled,
    valueRange = valueRange,
    steps = steps,
    orientation = OneUiSliderOrientation.Vertical,
    mode = mode,
    warningRange = warningRange,
    onValueChangeFinished = onValueChangeFinished,
    interactionSource = interactionSource,
    colors = colors,
)
