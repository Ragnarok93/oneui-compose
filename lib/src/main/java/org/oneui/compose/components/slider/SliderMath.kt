package org.oneui.compose.components.slider

import kotlin.math.round

/**
 * Coerces [value] into [valueRange] without throwing for non-finite input or a reversed range.
 *
 * Slider callers are expected to provide a finite range. This helper nevertheless remains stable
 * for defensive callers because it is also used by accessibility and keyboard input paths.
 */
internal fun coerceSliderValue(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
): Float {
    val start = valueRange.start
    val end = valueRange.endInclusive

    if (!start.isFinite() || !end.isFinite()) return if (start.isFinite()) start else 0f
    if (start == end) return start
    if (!value.isFinite()) return start

    return value.coerceIn(minOf(start, end), maxOf(start, end))
}

/** Maps a physical horizontal coordinate to a normalized slider fraction. */
internal fun sliderFraction(
    position: Float,
    length: Float,
    rtl: Boolean,
): Float {
    if (!position.isFinite() || !length.isFinite() || length <= 0f) return 0f
    val physical = (position / length).coerceIn(0f, 1f)
    return if (rtl) 1f - physical else physical
}

/** Maps a slider value to a normalized fraction relative to [valueRange]. */
internal fun sliderValueFraction(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
): Float {
    val start = valueRange.start
    val end = valueRange.endInclusive
    val span = end - start
    if (!start.isFinite() || !end.isFinite() || !span.isFinite() || span == 0f) return 0f

    val coerced = coerceSliderValue(value, valueRange)
    return ((coerced - start) / span).coerceIn(0f, 1f)
}

/**
 * Snaps [value] to the same discrete model used by Compose sliders: [steps] intermediate points
 * produce `steps + 1` equal intervals between the two range endpoints.
 */
internal fun snapSliderValue(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
): Float {
    val coerced = coerceSliderValue(value, valueRange)
    val start = valueRange.start
    val end = valueRange.endInclusive
    val span = end - start
    if (steps <= 0 || span == 0f || !span.isFinite()) return coerced

    val intervals = steps + 1
    val fraction = sliderValueFraction(coerced, valueRange)
    val snappedFraction = round(fraction * intervals) / intervals
    return start + (span * snappedFraction)
}

/** Converts a normalized fraction back into a value and applies step snapping. */
internal fun sliderValueForFraction(
    fraction: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
): Float {
    val start = valueRange.start
    val end = valueRange.endInclusive
    val safeFraction = if (fraction.isFinite()) fraction.coerceIn(0f, 1f) else 0f
    return snapSliderValue(start + ((end - start) * safeFraction), valueRange, steps)
}
