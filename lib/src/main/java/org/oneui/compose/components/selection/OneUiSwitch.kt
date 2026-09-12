package org.oneui.compose.components.selection

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.oneui.compose.motion.OneUiMotion
import org.oneui.compose.theme.OneUiTheme

/** Stable Compose-native One UI switch colors. */
@Immutable
data class OneUiSwitchColors(
    val checkedTrackColor: Color,
    val uncheckedTrackColor: Color,
    val disabledCheckedTrackColor: Color,
    val disabledUncheckedTrackColor: Color,
    val checkedThumbColor: Color,
    val uncheckedThumbColor: Color,
    val disabledCheckedThumbColor: Color,
    val disabledUncheckedThumbColor: Color,
    val checkedThumbStrokeColor: Color,
    val uncheckedThumbStrokeColor: Color,
    val disabledThumbStrokeColor: Color,
    val haloColor: Color,
)

/** SESL8-derived switch geometry and default palette. */
object OneUiSwitchDefaults {
    val TrackWidth = 35.dp
    val TrackHeight = 20.dp
    val TrackRadius = 10.dp
    val ThumbDiameter = 16.dp
    val ThumbOutlineDiameter = 20.dp
    val ThumbStrokeWidth = 2.dp
    val TouchTargetSize = 48.dp
    val HaloRadius = 20.dp
    const val PressedThumbScale = 0.8f

    @Composable
    fun colors(
        checkedTrackColor: Color = OneUiTheme.colors.accent,
        uncheckedTrackColor: Color = OneUiTheme.colors.controlInactive.copy(alpha = 0.55f),
        checkedThumbColor: Color = Color(0xFFFCFCFF),
        uncheckedThumbColor: Color = Color(0xFFFCFCFF),
        checkedThumbStrokeColor: Color = OneUiTheme.colors.accent,
        uncheckedThumbStrokeColor: Color = OneUiTheme.colors.controlInactive,
        haloColor: Color = OneUiTheme.colors.accent,
    ): OneUiSwitchColors {
        val opacity = OneUiTheme.opacity
        return OneUiSwitchColors(
            checkedTrackColor = checkedTrackColor,
            uncheckedTrackColor = uncheckedTrackColor,
            disabledCheckedTrackColor = checkedTrackColor.copy(
                alpha = checkedTrackColor.alpha * opacity.disabledContainer,
            ),
            disabledUncheckedTrackColor = uncheckedTrackColor.copy(
                alpha = uncheckedTrackColor.alpha * opacity.disabledContainer,
            ),
            checkedThumbColor = checkedThumbColor,
            uncheckedThumbColor = uncheckedThumbColor,
            disabledCheckedThumbColor = checkedThumbColor.copy(
                alpha = checkedThumbColor.alpha * opacity.disabledContent,
            ),
            disabledUncheckedThumbColor = uncheckedThumbColor.copy(
                alpha = uncheckedThumbColor.alpha * opacity.disabledContent,
            ),
            checkedThumbStrokeColor = checkedThumbStrokeColor,
            uncheckedThumbStrokeColor = uncheckedThumbStrokeColor,
            disabledThumbStrokeColor = OneUiTheme.colors.controlInactive.copy(
                alpha = OneUiTheme.colors.controlInactive.alpha * opacity.disabledContent,
            ),
            haloColor = haloColor,
        )
    }
}

private data class SwitchVisualState(
    val checked: Boolean,
    val enabled: Boolean,
    val pressed: Boolean,
)

/**
 * Compose-native SESL8 switch.
 *
 * The measured SESL8 visual is a 35x20dp track with a 16dp fill and 20dp outlined thumb. Because
 * this library targets API 23+, thumb travel uses the SESL above-M 300ms path with the canonical
 * 0.22/0.25/0/1 interpolator. Press state is sourced from [interactionSource]; no delayed flags are
 * fabricated.
 */
@Composable
fun OneUiSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: OneUiSwitchColors = OneUiSwitchDefaults.colors(),
) {
    val pressed by interactionSource.collectIsPressedAsState()
    val reducedMotion = OneUiTheme.reducedMotion
    val layoutDirection = LocalLayoutDirection.current
    val visualState = SwitchVisualState(checked = checked, enabled = enabled, pressed = pressed)
    val transition = updateTransition(targetState = visualState, label = "OneUi switch")

    val thumbPosition by transition.animateFloat(
        transitionSpec = {
            if (reducedMotion || !targetState.enabled) snap() else OneUiMotion.switch()
        },
        label = "OneUi switch thumb position",
    ) { if (it.checked) 1f else 0f }
    val thumbScale by transition.animateFloat(
        transitionSpec = {
            if (reducedMotion || !targetState.enabled) snap() else OneUiMotion.press()
        },
        label = "OneUi switch thumb scale",
    ) { if (it.pressed && it.enabled) OneUiSwitchDefaults.PressedThumbScale else 1f }
    val trackColor by transition.animateColor(
        transitionSpec = {
            if (reducedMotion || !targetState.enabled) snap() else OneUiMotion.switch()
        },
        label = "OneUi switch track",
    ) {
        when {
            !it.enabled && it.checked -> colors.disabledCheckedTrackColor
            !it.enabled -> colors.disabledUncheckedTrackColor
            it.checked -> colors.checkedTrackColor
            else -> colors.uncheckedTrackColor
        }
    }
    val thumbColor by transition.animateColor(
        transitionSpec = {
            if (reducedMotion || !targetState.enabled) snap() else OneUiMotion.switch()
        },
        label = "OneUi switch thumb",
    ) {
        when {
            !it.enabled && it.checked -> colors.disabledCheckedThumbColor
            !it.enabled -> colors.disabledUncheckedThumbColor
            it.checked -> colors.checkedThumbColor
            else -> colors.uncheckedThumbColor
        }
    }
    val strokeColor by transition.animateColor(
        transitionSpec = {
            if (reducedMotion || !targetState.enabled) snap() else OneUiMotion.switch()
        },
        label = "OneUi switch stroke",
    ) {
        when {
            !it.enabled -> colors.disabledThumbStrokeColor
            it.checked -> colors.checkedThumbStrokeColor
            else -> colors.uncheckedThumbStrokeColor
        }
    }
    val haloAlpha by transition.animateFloat(
        transitionSpec = {
            if (reducedMotion || !targetState.enabled) snap() else OneUiMotion.press()
        },
        label = "OneUi switch halo",
    ) { if (it.pressed && it.enabled) 0.14f else 0f }

    val onToggle = onCheckedChange?.let { callback -> { callback(!checked) } }
    val interactiveModifier = if (onToggle != null) {
        Modifier
            .oneUiSelectionKeyActivation(enabled = enabled, onActivate = onToggle)
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                interactionSource = interactionSource,
                indication = null,
                onValueChange = onCheckedChange,
            )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .defaultMinSize(
                minWidth = OneUiSwitchDefaults.TouchTargetSize,
                minHeight = OneUiSwitchDefaults.TouchTargetSize,
            )
            .then(interactiveModifier),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val trackWidth = OneUiSwitchDefaults.TrackWidth.toPx()
            val trackHeight = OneUiSwitchDefaults.TrackHeight.toPx()
            val trackLeft = (size.width - trackWidth) / 2f
            val trackTop = (size.height - trackHeight) / 2f
            val centerY = size.height / 2f
            val outlineRadius = OneUiSwitchDefaults.ThumbOutlineDiameter.toPx() / 2f
            val fillRadius = OneUiSwitchDefaults.ThumbDiameter.toPx() / 2f
            val startX = trackLeft + outlineRadius
            val endX = trackLeft + trackWidth - outlineRadius
            val directionalProgress = if (layoutDirection == LayoutDirection.Rtl) {
                1f - thumbPosition
            } else {
                thumbPosition
            }
            val center = Offset(
                x = startX + ((endX - startX) * directionalProgress),
                y = centerY,
            )

            drawRoundRect(
                color = trackColor,
                topLeft = Offset(trackLeft, trackTop),
                size = Size(trackWidth, trackHeight),
                cornerRadius = CornerRadius(
                    OneUiSwitchDefaults.TrackRadius.toPx(),
                    OneUiSwitchDefaults.TrackRadius.toPx(),
                ),
            )
            if (haloAlpha > 0f) {
                drawCircle(
                    color = colors.haloColor.copy(alpha = colors.haloColor.alpha * haloAlpha),
                    radius = OneUiSwitchDefaults.HaloRadius.toPx(),
                    center = center,
                )
            }
            drawCircle(
                color = thumbColor,
                radius = fillRadius * thumbScale,
                center = center,
            )
            drawCircle(
                color = strokeColor,
                radius = (outlineRadius - OneUiSwitchDefaults.ThumbStrokeWidth.toPx() / 2f) * thumbScale,
                center = center,
                style = Stroke(width = OneUiSwitchDefaults.ThumbStrokeWidth.toPx()),
            )
        }
    }
}

internal fun Modifier.oneUiSelectionKeyActivation(
    enabled: Boolean,
    onActivate: () -> Unit,
): Modifier = onPreviewKeyEvent { event ->
    val isActivationKey = when (event.key) {
        Key.Enter,
        Key.NumPadEnter,
        Key.Spacebar,
        Key.DirectionCenter -> true
        else -> false
    }
    if (!enabled || !isActivationKey) {
        false
    } else {
        if (event.type == KeyEventType.KeyUp) onActivate()
        event.type == KeyEventType.KeyDown || event.type == KeyEventType.KeyUp
    }
}
