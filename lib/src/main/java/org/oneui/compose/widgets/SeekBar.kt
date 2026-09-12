package org.oneui.compose.widgets

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.slider.OneUiSlider
import org.oneui.compose.components.slider.OneUiSliderColors
import org.oneui.compose.components.slider.OneUiSliderDefaults
import org.oneui.compose.components.slider.OneUiSliderOrientation
import org.oneui.compose.theme.OneUITheme

/**
 * Legacy compatibility facade for the stable One UI slider engine.
 */
@Deprecated(
    message = "Use OneUiSlider from org.oneui.compose.components.slider",
    replaceWith = ReplaceWith(
        "OneUiSlider(value, onValueChange, modifier, enabled = enabled)",
        "org.oneui.compose.components.slider.OneUiSlider",
    ),
)
@Composable
fun HorizontalSeekbar(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    colors: SeekBarColors = seekBarColors(),
    enabled: Boolean = true,
) {
    OneUiSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        colors = colors.asStable(),
    )
}

@Deprecated(
    message = "Use OneUiVerticalSlider from org.oneui.compose.components.slider",
    replaceWith = ReplaceWith(
        "OneUiVerticalSlider(value, onValueChange, modifier, enabled = enabled)",
        "org.oneui.compose.components.slider.OneUiVerticalSlider",
    ),
)
@Composable
fun VerticalSeekbar(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    colors: SeekBarColors = seekBarColors(),
    enabled: Boolean = true,
) {
    OneUiSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        orientation = OneUiSliderOrientation.Vertical,
        colors = colors.asStable(),
    )
}

/**
 * Compatibility name retained for the former expanding vertical seekbar. The stable engine uses
 * the SESL8 250 ms pressed-track expansion behavior.
 */
@Deprecated(
    message = "Use OneUiVerticalSlider from org.oneui.compose.components.slider",
    replaceWith = ReplaceWith(
        "OneUiVerticalSlider(value, onValueChange, modifier, enabled = enabled)",
        "org.oneui.compose.components.slider.OneUiVerticalSlider",
    ),
)
@Composable
fun VerticalSeekbarExpanding(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    colors: SeekBarColors = seekBarColors(),
    enabled: Boolean = true,
) {
    VerticalSeekbar(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        colors = colors,
        enabled = enabled,
    )
}

/** Compatibility name retained for the former expanding horizontal seekbar. */
@Deprecated(
    message = "Use OneUiSlider from org.oneui.compose.components.slider",
    replaceWith = ReplaceWith(
        "OneUiSlider(value, onValueChange, modifier, enabled = enabled)",
        "org.oneui.compose.components.slider.OneUiSlider",
    ),
)
@Composable
fun HorizontalSeekbarExpanding(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    colors: SeekBarColors = seekBarColors(),
    enabled: Boolean = true,
) {
    HorizontalSeekbar(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        colors = colors,
        enabled = enabled,
    )
}

@Deprecated(
    message = "Use OneUiSlider with warningRange",
    replaceWith = ReplaceWith(
        "OneUiSlider(value, onValueChange, modifier, enabled = enabled, warningRange = warningAt..1f)",
        "org.oneui.compose.components.slider.OneUiSlider",
    ),
)
@Composable
fun HorizontalSeekbarWarning(
    modifier: Modifier = Modifier,
    value: Float,
    warningAt: Float = 0.5f,
    onValueChange: (Float) -> Unit,
    colors: SeekBarColors = seekBarColors(),
    enabled: Boolean = true,
) {
    OneUiSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        warningRange = warningAt.coerceIn(0f, 1f)..1f,
        colors = colors.asStable(),
    )
}

/** Legacy color container retained for source compatibility. */
data class SeekBarColors(
    val color: Color,
    val ripple: Color,
    val trackColor: Color,
    val warningColor: Color,
    val trackWarningColor: Color,
)

@Composable
fun seekBarColors(
    color: Color = OneUITheme.colors.seslSeekbarControlColorActivated,
    ripple: Color = OneUITheme.colors.seslRippleColor,
    trackColor: Color = OneUITheme.colors.seslSeekbarOverlapColorDefault,
    warningColor: Color = OneUITheme.colors.seslSeekbarOverlapColorActivated,
    trackWarningColor: Color = OneUITheme.colors.seslSeekbarOverlapColorActivated,
): SeekBarColors = SeekBarColors(
    color = color,
    ripple = ripple,
    trackColor = trackColor,
    warningColor = warningColor,
    trackWarningColor = trackWarningColor,
)

/**
 * Legacy constants now point at the exact SESL8 seekbar geometry used by [OneUiSliderDefaults].
 */
object SeekBarDefaults {
    val thumbRadius = OneUiSliderDefaults.ThumbRadius
    val animThumbRadiusExtra = 0.dp
    val thumbRippleRadius = OneUiSliderDefaults.InteractionRadius
    val trackHeight = OneUiSliderDefaults.TrackThickness
    val trackHeightExpand = OneUiSliderDefaults.ExpandedTrackThickness
    val padding = PaddingValues(horizontal = 16.dp)
    const val animDuration = 250
}

private fun SeekBarColors.asStable(): OneUiSliderColors = OneUiSliderColors(
    activeTrack = color,
    inactiveTrack = trackColor,
    thumb = color,
    thumbStroke = Color.Transparent,
    interactionHalo = ripple,
    warning = warningColor,
    disabledActiveTrack = color.copy(alpha = 0.35f),
    disabledInactiveTrack = trackColor.copy(alpha = 0.18f),
    disabledThumb = color.copy(alpha = 0.45f),
)
