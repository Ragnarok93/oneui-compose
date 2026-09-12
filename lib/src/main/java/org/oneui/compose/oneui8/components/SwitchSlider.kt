package org.oneui.compose.oneui8.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.oneui.compose.components.selection.OneUiSwitch
import org.oneui.compose.components.slider.OneUiSlider

/** Compatibility facade for the stable [OneUiSwitch]. */
@Deprecated(
    message = "Use OneUiSwitch from org.oneui.compose.components.selection",
    replaceWith = ReplaceWith(
        "OneUiSwitch(checked, onCheckedChange, modifier, enabled)",
        "org.oneui.compose.components.selection.OneUiSwitch",
    ),
)
@Composable
fun OneUI8Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OneUiSwitch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
    )
}

/** Compatibility facade for the stable [OneUiSlider]. */
@Deprecated(
    message = "Use OneUiSlider from org.oneui.compose.components.slider",
    replaceWith = ReplaceWith(
        "OneUiSlider(value, onValueChange, modifier, enabled, valueRange, steps)",
        "org.oneui.compose.components.slider.OneUiSlider",
    ),
)
@Composable
fun OneUI8Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true,
) {
    OneUiSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
    )
}

internal fun normalizeOneUI8SliderValue(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
): Float = value.coerceIn(valueRange.start, valueRange.endInclusive)
