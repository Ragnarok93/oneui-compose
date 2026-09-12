package org.oneui.compose.oneui8.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.slider.OneUiSlider
import org.oneui.compose.oneui8.motion.OneUI8Motion
import org.oneui.compose.oneui8.theme.OneUI8Theme

@Composable
fun OneUI8Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = OneUI8Theme.colors
    val trackColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.controlInactive.copy(alpha = 0.22f)
            checked -> colors.accent
            else -> colors.controlInactive.copy(alpha = 0.28f)
        },
        animationSpec = OneUI8Motion.standard(),
        label = "OneUI8 switch track",
    )
    val thumbColor by animateColorAsState(
        targetValue = if (checked) colors.onAccent else colors.surface,
        animationSpec = OneUI8Motion.standard(),
        label = "OneUI8 switch thumb",
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 20.dp else 2.dp,
        animationSpec = OneUI8Motion.standard(),
        label = "OneUI8 switch thumb offset",
    )
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .width(50.dp)
            .height(32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(trackColor)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                interactionSource = interactionSource,
                indication = null,
                onValueChange = onCheckedChange,
            ),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = thumbOffset)
                .size(28.dp)
                .background(thumbColor, CircleShape),
        )
    }
}

/**
 * Compatibility facade for the stable [OneUiSlider].
 */
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
