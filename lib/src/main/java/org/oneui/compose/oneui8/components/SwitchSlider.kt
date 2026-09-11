package org.oneui.compose.oneui8.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
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

@Composable
fun OneUI8Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true,
) {
    val colors = OneUI8Theme.colors
    Slider(
        value = normalizeOneUI8SliderValue(value, valueRange),
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        colors = SliderDefaults.colors(
            thumbColor = colors.accent,
            activeTrackColor = colors.accent,
            inactiveTrackColor = colors.controlInactive.copy(alpha = 0.28f),
            disabledThumbColor = colors.controlInactive.copy(alpha = 0.45f),
            disabledActiveTrackColor = colors.controlInactive.copy(alpha = 0.35f),
            disabledInactiveTrackColor = colors.controlInactive.copy(alpha = 0.18f),
        ),
    )
}

internal fun normalizeOneUI8SliderValue(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
): Float = value.coerceIn(valueRange.start, valueRange.endInclusive)
