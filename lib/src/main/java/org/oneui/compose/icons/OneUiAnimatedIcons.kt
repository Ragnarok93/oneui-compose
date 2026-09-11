package org.oneui.compose.icons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import org.oneui.compose.motion.OneUiEasing
import org.oneui.compose.motion.OneUiMotion
import org.oneui.compose.theme.OneUiTheme

/** Compose-native animated One UI glyphs whose motion is not represented by a static drawable. */
object OneUiAnimatedIcons {
    /**
     * Two-stroke selection check based on the pinned SESL/One UI reference timings.
     *
     * Checking uses 110 ms for the first stroke and 180 ms for the second, ending at 290 ms.
     * Unchecking uses the reference 120 ms / 59 ms stages with the second stage beginning at
     * 140 ms, ending at 199 ms. Reduced-motion and disabled states snap to their final geometry.
     */
    @Composable
    fun CheckMorph(
        checked: Boolean,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        checkedColor: Color = OneUiTheme.colors.accent,
        checkColor: Color = OneUiTheme.colors.onAccent,
        uncheckedColor: Color = OneUiTheme.colors.controlInactive,
    ) {
        val reducedMotion = OneUiTheme.reducedMotion
        val opacity = OneUiTheme.opacity
        val shouldSnap = reducedMotion || !enabled

        val firstStroke by animateFloatAsState(
            targetValue = if (checked) 1f else 0f,
            animationSpec = when {
                shouldSnap -> snap()
                checked -> tween(
                    durationMillis = OneUiMotion.Duration.SelectionFirstStroke,
                    delayMillis = InitialStrokeDelay,
                    easing = OneUiEasing.Standard,
                )
                else -> tween(
                    durationMillis = OneUiMotion.Duration.SelectionUncheckFirstStroke,
                    delayMillis = InitialStrokeDelay,
                    easing = OneUiEasing.Accelerate,
                )
            },
            label = "OneUi check first stroke",
        )
        val secondStroke by animateFloatAsState(
            targetValue = if (checked) 1f else 0f,
            animationSpec = when {
                shouldSnap -> snap()
                checked -> tween(
                    durationMillis = OneUiMotion.Duration.SelectionSecondStroke,
                    delayMillis = OneUiMotion.Duration.SelectionFirstStroke,
                    easing = OneUiEasing.Standard,
                )
                else -> tween(
                    durationMillis = OneUiMotion.Duration.SelectionUncheckSecondStroke,
                    delayMillis = UncheckSecondStrokeDelay,
                    easing = OneUiEasing.Accelerate,
                )
            },
            label = "OneUi check second stroke",
        )

        val targetBackground = when {
            checked && enabled -> checkedColor
            checked -> checkedColor.copy(alpha = checkedColor.alpha * opacity.disabledContainer)
            enabled -> uncheckedColor.copy(alpha = uncheckedColor.alpha * opacity.inactiveControl)
            else -> uncheckedColor.copy(
                alpha = uncheckedColor.alpha * opacity.inactiveControl * opacity.disabledContainer,
            )
        }
        val background by animateColorAsState(
            targetValue = targetBackground,
            animationSpec = if (shouldSnap) snap() else OneUiMotion.standard(),
            label = "OneUi check background",
        )
        val resolvedCheckColor = if (enabled) {
            checkColor
        } else {
            checkColor.copy(alpha = checkColor.alpha * opacity.disabledContent)
        }

        Canvas(
            modifier = modifier
                .size(28.dp)
                .background(background, CircleShape),
        ) {
            if (firstStroke <= 0f && secondStroke <= 0f) return@Canvas

            val start = Offset(size.width * 0.27f, size.height * 0.52f)
            val middle = Offset(size.width * 0.44f, size.height * 0.68f)
            val end = Offset(size.width * 0.75f, size.height * 0.34f)
            val strokeWidth = size.minDimension * 0.085f

            if (firstStroke > 0f) {
                drawLine(
                    color = resolvedCheckColor,
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
                    color = resolvedCheckColor,
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
    }

    private const val InitialStrokeDelay = 1
    private const val UncheckSecondStrokeDelay = 140
}
