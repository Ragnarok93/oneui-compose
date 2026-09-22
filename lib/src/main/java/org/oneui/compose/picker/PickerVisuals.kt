package org.oneui.compose.picker

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Palette and mask contract shared by the Compose picker wheels.
 *
 * The SESL wheel has a readable selected value and fades neighbouring rows into the hosting
 * surface. It does not put rectangular black scrims behind those rows.
 */
@Immutable
data class PickerWheelVisuals(
    val activeText: Color,
    val inactiveText: Color,
    val surface: Color,
)

@Immutable
data class PickerMaskStop(
    val position: Float,
    val alpha: Float,
)

/** Defaults used by [NumberPicker] and [StringPicker]. */
object PickerVisuals {
    /** Edge opacity while a wheel is settled. */
    const val IdleMaskEdgeAlpha = 0.84f

    /** A slightly lighter edge mask preserves motion legibility while a wheel is moving. */
    const val ScrollingMaskEdgeAlpha = 0.68f

    fun from(
        primaryText: Color,
        secondaryText: Color,
        surface: Color,
    ): PickerWheelVisuals = PickerWheelVisuals(
        activeText = primaryText,
        inactiveText = secondaryText,
        surface = surface,
    )

    fun maskStops(edgeAlpha: Float): List<PickerMaskStop> = listOf(
        PickerMaskStop(position = 0f, alpha = edgeAlpha.coerceIn(0f, 1f)),
        PickerMaskStop(position = 1f, alpha = 0f),
    )

    fun maskEdgeAlpha(isScrolling: Boolean): Float =
        if (isScrolling) ScrollingMaskEdgeAlpha else IdleMaskEdgeAlpha
}
