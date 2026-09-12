package org.oneui.compose.components.selection

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import org.oneui.compose.motion.OneUiEasing
import org.oneui.compose.motion.OneUiMotion
import org.oneui.compose.theme.OneUiTheme

/** Stable color contract for One UI checkbox controls. */
@Immutable
data class OneUiCheckboxColors(
    val checkedColor: Color,
    val uncheckedColor: Color,
    val markColor: Color,
    val disabledCheckedColor: Color,
    val disabledUncheckedColor: Color,
    val disabledMarkColor: Color,
    val haloColor: Color,
)

object OneUiCheckboxDefaults {
    val ControlSize = 32.dp
    val IndicatorDiameter = 20.dp
    val OutlineWidth = 1.5.dp
    val MarkWidth = 2.4.dp
    val TouchTargetSize = 48.dp
    val HaloRadius = 20.dp
    const val PulseScale = 0.8f

    @Composable
    fun colors(
        checkedColor: Color = OneUiTheme.colors.accent,
        uncheckedColor: Color = OneUiTheme.colors.controlInactive,
        markColor: Color = Color.White,
        haloColor: Color = OneUiTheme.colors.accent,
    ): OneUiCheckboxColors {
        val opacity = OneUiTheme.opacity
        return OneUiCheckboxColors(
            checkedColor = checkedColor,
            uncheckedColor = uncheckedColor,
            markColor = markColor,
            disabledCheckedColor = checkedColor.copy(
                alpha = checkedColor.alpha * opacity.disabledContainer,
            ),
            disabledUncheckedColor = uncheckedColor.copy(
                alpha = uncheckedColor.alpha * opacity.disabledContainer,
            ),
            disabledMarkColor = markColor.copy(
                alpha = markColor.alpha * opacity.disabledContent,
            ),
            haloColor = haloColor,
        )
    }
}

/** Boolean convenience wrapper around [OneUiTriStateCheckbox]. */
@Composable
fun OneUiCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: OneUiCheckboxColors = OneUiCheckboxDefaults.colors(),
) {
    OneUiTriStateCheckbox(
        state = if (checked) ToggleableState.On else ToggleableState.Off,
        onClick = onCheckedChange?.let { callback -> { callback(!checked) } },
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        colors = colors,
    )
}

/**
 * Compose-native One UI tri-state checkbox.
 *
 * The visual stays in the measured 32dp SESL control bounds while the pointer/keyboard target is
 * at least 48dp. Check/uncheck strokes use the verified 110+180ms and 120/59ms timing model; an
 * indeterminate state shares the selected circle and morphs the mark to a centered dash.
 */
@Composable
fun OneUiTriStateCheckbox(
    state: ToggleableState,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: OneUiCheckboxColors = OneUiCheckboxDefaults.colors(),
) {
    val reducedMotion = OneUiTheme.reducedMotion
    val pressed by interactionSource.collectIsPressedAsState()
    val selected = state != ToggleableState.Off
    val shouldSnap = reducedMotion || !enabled

    val fillProgress by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = when {
            shouldSnap -> snap()
            selected -> OneUiMotion.selectionCheck()
            else -> OneUiMotion.selectionUncheck()
        },
        label = "OneUi checkbox fill",
    )
    val firstStroke by animateFloatAsState(
        targetValue = if (state == ToggleableState.On) 1f else 0f,
        animationSpec = when {
            shouldSnap -> snap()
            state == ToggleableState.On -> tween(
                durationMillis = OneUiMotion.Duration.SelectionFirstStroke,
                delayMillis = 1,
                easing = OneUiEasing.Standard,
            )
            else -> tween(
                durationMillis = OneUiMotion.Duration.SelectionUncheckFirstStroke,
                delayMillis = 1,
                easing = OneUiEasing.Accelerate,
            )
        },
        label = "OneUi checkbox first stroke",
    )
    val secondStroke by animateFloatAsState(
        targetValue = if (state == ToggleableState.On) 1f else 0f,
        animationSpec = when {
            shouldSnap -> snap()
            state == ToggleableState.On -> tween(
                durationMillis = OneUiMotion.Duration.SelectionSecondStroke,
                delayMillis = OneUiMotion.Duration.SelectionFirstStroke,
                easing = OneUiEasing.Standard,
            )
            else -> tween(
                durationMillis = OneUiMotion.Duration.SelectionUncheckSecondStroke,
                delayMillis = 140,
                easing = OneUiEasing.Accelerate,
            )
        },
        label = "OneUi checkbox second stroke",
    )
    val dashProgress by animateFloatAsState(
        targetValue = if (state == ToggleableState.Indeterminate) 1f else 0f,
        animationSpec = if (shouldSnap) snap() else OneUiMotion.standard(),
        label = "OneUi checkbox indeterminate mark",
    )
    val pressScale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.92f else 1f,
        animationSpec = if (shouldSnap) snap() else OneUiMotion.press(),
        label = "OneUi checkbox press",
    )
    val haloAlpha by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.12f else 0f,
        animationSpec = if (shouldSnap) snap() else OneUiMotion.press(),
        label = "OneUi checkbox halo",
    )
    val indicatorColor by animateColorAsState(
        targetValue = when {
            selected && enabled -> colors.checkedColor
            selected -> colors.disabledCheckedColor
            enabled -> colors.uncheckedColor
            else -> colors.disabledUncheckedColor
        },
        animationSpec = if (shouldSnap) snap() else OneUiMotion.standard(),
        label = "OneUi checkbox color",
    )
    val markColor = if (enabled) colors.markColor else colors.disabledMarkColor

    val interactiveModifier = if (onClick != null) {
        Modifier
            .oneUiSelectionKeyActivation(enabled = enabled, onActivate = onClick)
            .triStateToggleable(
                state = state,
                enabled = enabled,
                role = Role.Checkbox,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
    } else {
        Modifier.semantics {
            role = Role.Checkbox
            toggleableState = state
            if (!enabled) disabled()
        }
    }

    Box(
        modifier = modifier
            .defaultMinSize(
                minWidth = OneUiCheckboxDefaults.TouchTargetSize,
                minHeight = OneUiCheckboxDefaults.TouchTargetSize,
            )
            .then(interactiveModifier),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val baseRadius = OneUiCheckboxDefaults.IndicatorDiameter.toPx() / 2f
            val pulseSplit = OneUiMotion.Duration.SelectionFirstStroke.toFloat() /
                OneUiMotion.Duration.SelectionCheck.toFloat()
            val pulseScale = when {
                fillProgress <= 0f || fillProgress >= 1f -> 1f
                fillProgress < pulseSplit -> {
                    1f - (1f - OneUiCheckboxDefaults.PulseScale) * (fillProgress / pulseSplit)
                }
                else -> {
                    OneUiCheckboxDefaults.PulseScale +
                        (1f - OneUiCheckboxDefaults.PulseScale) *
                        ((fillProgress - pulseSplit) / (1f - pulseSplit))
                }
            } * pressScale
            val radius = baseRadius * pulseScale

            if (haloAlpha > 0f) {
                drawCircle(
                    color = colors.haloColor.copy(alpha = colors.haloColor.alpha * haloAlpha),
                    radius = OneUiCheckboxDefaults.HaloRadius.toPx(),
                    center = center,
                )
            }

            if (fillProgress > 0f) {
                drawCircle(
                    color = indicatorColor.copy(alpha = indicatorColor.alpha * fillProgress),
                    radius = radius,
                    center = center,
                )
            }
            if (fillProgress < 1f) {
                drawCircle(
                    color = indicatorColor.copy(alpha = indicatorColor.alpha * (1f - fillProgress)),
                    radius = radius - OneUiCheckboxDefaults.OutlineWidth.toPx() / 2f,
                    center = center,
                    style = Stroke(width = OneUiCheckboxDefaults.OutlineWidth.toPx()),
                )
            }

            val strokeWidth = OneUiCheckboxDefaults.MarkWidth.toPx()
            if (firstStroke > 0f || secondStroke > 0f) {
                val start = Offset(center.x - baseRadius * 0.50f, center.y + baseRadius * 0.02f)
                val middle = Offset(center.x - baseRadius * 0.14f, center.y + baseRadius * 0.36f)
                val end = Offset(center.x + baseRadius * 0.58f, center.y - baseRadius * 0.42f)
                if (firstStroke > 0f) {
                    drawLine(
                        color = markColor,
                        start = start,
                        end = Offset(
                            x = start.x + (middle.x - start.x) * firstStroke,
                            y = start.y + (middle.y - start.y) * firstStroke,
                        ),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round,
                    )
                }
                if (secondStroke > 0f) {
                    drawLine(
                        color = markColor,
                        start = middle,
                        end = Offset(
                            x = middle.x + (end.x - middle.x) * secondStroke,
                            y = middle.y + (end.y - middle.y) * secondStroke,
                        ),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round,
                    )
                }
            }
            if (dashProgress > 0f) {
                val halfDash = baseRadius * 0.48f * dashProgress
                drawLine(
                    color = markColor.copy(alpha = markColor.alpha * dashProgress),
                    start = Offset(center.x - halfDash, center.y),
                    end = Offset(center.x + halfDash, center.y),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                )
            }
        }
    }
}
