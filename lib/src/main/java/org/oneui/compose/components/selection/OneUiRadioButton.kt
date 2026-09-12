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
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.oneui.compose.motion.OneUiMotion
import org.oneui.compose.theme.OneUiTheme

/** Stable color contract for One UI radio controls. */
@Immutable
data class OneUiRadioButtonColors(
    val selectedColor: Color,
    val unselectedColor: Color,
    val disabledSelectedColor: Color,
    val disabledUnselectedColor: Color,
    val haloColor: Color,
)

object OneUiRadioButtonDefaults {
    val ControlSize = 32.dp
    val OuterRadius = 9.dp
    val SelectedStrokeWidth = 2.dp
    val UnselectedStrokeWidth = 1.5.dp
    val DotRadius = 5.dp
    val TouchTargetSize = 48.dp
    val HaloRadius = 20.dp
    const val PulseScale = 0.8f

    @Composable
    fun colors(
        selectedColor: Color = OneUiTheme.colors.accent,
        unselectedColor: Color = OneUiTheme.colors.controlInactive,
        haloColor: Color = OneUiTheme.colors.accent,
    ): OneUiRadioButtonColors {
        val opacity = OneUiTheme.opacity
        return OneUiRadioButtonColors(
            selectedColor = selectedColor,
            unselectedColor = unselectedColor,
            disabledSelectedColor = selectedColor.copy(
                alpha = selectedColor.alpha * opacity.disabledContainer,
            ),
            disabledUnselectedColor = unselectedColor.copy(
                alpha = unselectedColor.alpha * opacity.disabledContainer,
            ),
            haloColor = haloColor,
        )
    }
}

private data class RadioVisualState(
    val selected: Boolean,
    val enabled: Boolean,
    val pressed: Boolean,
)

/**
 * Compose-native SESL8 radio button using the measured 32dp visual, ~9dp ring and 5dp center dot.
 */
@Composable
fun OneUiRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: OneUiRadioButtonColors = OneUiRadioButtonDefaults.colors(),
) {
    val pressed by interactionSource.collectIsPressedAsState()
    val reducedMotion = OneUiTheme.reducedMotion
    val visualState = RadioVisualState(selected = selected, enabled = enabled, pressed = pressed)
    val transition = updateTransition(targetState = visualState, label = "OneUi radio")

    val selectionProgress by transition.animateFloat(
        transitionSpec = {
            when {
                reducedMotion || !targetState.enabled -> snap()
                targetState.selected -> OneUiMotion.selectionCheck()
                else -> OneUiMotion.selectionUncheck()
            }
        },
        label = "OneUi radio selection",
    ) { if (it.selected) 1f else 0f }
    val pressScale by transition.animateFloat(
        transitionSpec = {
            if (reducedMotion || !targetState.enabled) snap() else OneUiMotion.press()
        },
        label = "OneUi radio press",
    ) { if (it.pressed && it.enabled) 0.92f else 1f }
    val ringColor by transition.animateColor(
        transitionSpec = {
            if (reducedMotion || !targetState.enabled) snap() else OneUiMotion.standard()
        },
        label = "OneUi radio color",
    ) {
        when {
            !it.enabled && it.selected -> colors.disabledSelectedColor
            !it.enabled -> colors.disabledUnselectedColor
            it.selected -> colors.selectedColor
            else -> colors.unselectedColor
        }
    }
    val haloAlpha by transition.animateFloat(
        transitionSpec = {
            if (reducedMotion || !targetState.enabled) snap() else OneUiMotion.press()
        },
        label = "OneUi radio halo",
    ) { if (it.pressed && it.enabled) 0.12f else 0f }

    val interactiveModifier = if (onClick != null) {
        Modifier
            .oneUiSelectionKeyActivation(enabled = enabled, onActivate = onClick)
            .selectable(
                selected = selected,
                enabled = enabled,
                role = Role.RadioButton,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
    } else {
        Modifier.semantics {
            role = Role.RadioButton
            this.selected = selected
            if (!enabled) disabled()
        }
    }

    Box(
        modifier = modifier
            .defaultMinSize(
                minWidth = OneUiRadioButtonDefaults.TouchTargetSize,
                minHeight = OneUiRadioButtonDefaults.TouchTargetSize,
            )
            .then(interactiveModifier),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val pulseSplit = OneUiMotion.Duration.SelectionFirstStroke.toFloat() /
                OneUiMotion.Duration.SelectionCheck.toFloat()
            val pulseScale = when {
                selectionProgress <= 0f || selectionProgress >= 1f -> 1f
                selectionProgress < pulseSplit -> {
                    1f - (1f - OneUiRadioButtonDefaults.PulseScale) *
                        (selectionProgress / pulseSplit)
                }
                else -> {
                    OneUiRadioButtonDefaults.PulseScale +
                        (1f - OneUiRadioButtonDefaults.PulseScale) *
                        ((selectionProgress - pulseSplit) / (1f - pulseSplit))
                }
            } * pressScale
            val outerRadius = OneUiRadioButtonDefaults.OuterRadius.toPx() * pulseScale
            val strokeWidth = (
                OneUiRadioButtonDefaults.UnselectedStrokeWidth.toPx() +
                    (OneUiRadioButtonDefaults.SelectedStrokeWidth.toPx() -
                        OneUiRadioButtonDefaults.UnselectedStrokeWidth.toPx()) * selectionProgress
                )

            if (haloAlpha > 0f) {
                drawCircle(
                    color = colors.haloColor.copy(alpha = colors.haloColor.alpha * haloAlpha),
                    radius = OneUiRadioButtonDefaults.HaloRadius.toPx(),
                    center = center,
                )
            }
            drawCircle(
                color = ringColor,
                radius = outerRadius,
                center = center,
                style = Stroke(width = strokeWidth),
            )
            if (selectionProgress > 0f) {
                drawCircle(
                    color = ringColor,
                    radius = OneUiRadioButtonDefaults.DotRadius.toPx() * selectionProgress * pressScale,
                    center = center,
                )
            }
        }
    }
}
