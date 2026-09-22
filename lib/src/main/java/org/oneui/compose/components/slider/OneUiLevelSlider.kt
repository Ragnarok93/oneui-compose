package org.oneui.compose.components.slider

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Level-bar facade preserving an arbitrary minimum/maximum while using the stable slider engine. */
@Composable
fun OneUiLevelSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    seamless: Boolean = false,
    showTickMark: Boolean = false,
    enabled: Boolean = true,
) {
    val steps = if (seamless || !showTickMark) 0 else {
        (valueRange.endInclusive - valueRange.start).toInt().coerceAtLeast(1) - 1
    }
    OneUiSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = steps.coerceAtLeast(0),
    )
}

