package org.oneui.compose.icons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.material3.LocalContentColor
import org.oneui.compose.motion.OneUiEasing
import org.oneui.compose.motion.OneUiMotion
import org.oneui.compose.theme.OneUiTheme

/**
 * Stable description of a reversible two-state icon transition.
 *
 * The icons remain ordinary [OneUiIcon] handles; this model only describes the visual relationship
 * between inactive and active states, keeping state fully hoisted by the caller.
 */
@Immutable
data class OneUiAnimatedIconSpec(
    val inactiveIcon: OneUiIcon,
    val activeIcon: OneUiIcon,
    val inactiveRotationDegrees: Float = 0f,
    val activeRotationDegrees: Float = 0f,
    val inactiveScale: Float = 1f,
    val activeScale: Float = 1f,
    val mirrorInRtl: Boolean = false,
)

/** Compose-native animated One UI glyph specifications and the selection check morph. */
object OneUiAnimatedIcons {
    /** Down-chevron to up-chevron transition for expandable content. */
    val ExpandCollapse = OneUiAnimatedIconSpec(
        inactiveIcon = OneUiIcons.ChevronDown,
        activeIcon = OneUiIcons.ChevronUp,
    )

    /** Play to pause transition for media controls. */
    val PlayPause = OneUiAnimatedIconSpec(
        inactiveIcon = OneUiIcons.Play,
        activeIcon = OneUiIcons.Pause,
        inactiveScale = 0.94f,
        activeScale = 1f,
    )

    /** Search to close transition for query surfaces. */
    val SearchClose = OneUiAnimatedIconSpec(
        inactiveIcon = OneUiIcons.Search,
        activeIcon = OneUiIcons.Close,
        inactiveRotationDegrees = 0f,
        activeRotationDegrees = 90f,
        inactiveScale = 1f,
        activeScale = 0.92f,
    )

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

/**
 * Renders a reversible state-driven icon transition.
 *
 * The inactive and active glyphs remain in the same measured bounds and crossfade/transform from a
 * single hoisted [active] value, so interrupted transitions naturally continue from their current
 * frame. Reduced-motion mode snaps directly to the requested state.
 */
@Composable
fun OneUiAnimatedIcon(
    icon: OneUiAnimatedIconSpec,
    active: Boolean,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    val reducedMotion = OneUiTheme.reducedMotion
    val layoutDirection = LocalLayoutDirection.current
    val progress by animateFloatAsState(
        targetValue = if (active) 1f else 0f,
        animationSpec = if (reducedMotion) snap() else OneUiMotion.standard(),
        label = "OneUi animated icon progress",
    )
    val rtlMirror = if (icon.mirrorInRtl && layoutDirection == LayoutDirection.Rtl) -1f else 1f
    val inactiveAlpha = 1f - progress
    val activeAlpha = progress
    val inactiveRotation = icon.inactiveRotationDegrees * progress
    val activeRotation = icon.activeRotationDegrees * (1f - progress)
    val inactiveScale = 1f + (icon.inactiveScale - 1f) * inactiveAlpha
    val activeScale = 1f + (icon.activeScale - 1f) * activeAlpha

    Box(
        modifier = modifier
            .size(24.dp)
            .then(
                if (contentDescription != null) {
                    Modifier.semantics { this.contentDescription = contentDescription }
                } else {
                    Modifier
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        OneUiIcon(
            icon = icon.inactiveIcon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    alpha = inactiveAlpha
                    rotationZ = inactiveRotation
                    scaleX = inactiveScale * rtlMirror
                    scaleY = inactiveScale
                },
        )
        OneUiIcon(
            icon = icon.activeIcon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    alpha = activeAlpha
                    rotationZ = activeRotation
                    scaleX = activeScale * rtlMirror
                    scaleY = activeScale
                },
        )
    }
}
