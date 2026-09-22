package org.oneui.compose.components.slider

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/** Compose-native equivalent of the reference SeekBarPlus seamless/stepped variants. */
@Composable
fun OneUiSeekBarPlus(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier,
    seamless: Boolean = true,
    enabled: Boolean = true,
) {
    OneUiSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = if (seamless) 0 else {
            (valueRange.endInclusive - valueRange.start).toInt().coerceAtLeast(1) - 1
        }.coerceAtLeast(0),
    )
}

