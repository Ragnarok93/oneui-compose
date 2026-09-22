package org.oneui.compose.interaction

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.Indication
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.semantics.Role
import org.oneui.compose.motion.OneUiMotion
import org.oneui.compose.theme.OneUiShapes
import org.oneui.compose.theme.OneUiTheme

/** One UI feedback must use the same rounded geometry as the control it decorates. */
internal object OneUiInteractionDefaults {
    fun shape(shapes: OneUiShapes): Shape = shapes.control
}

/**
 * Shared Compose-native interaction behavior for stable One UI controls.
 *
 * State comes from the caller-owned [interactionSource]. Keyboard activation is handled explicitly
 * for desktop/DeX/D-pad use rather than being modeled with delayed or fabricated press flags.
 */
@Composable
internal fun Modifier.oneUiInteractive(
    enabled: Boolean,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource,
    role: Role? = null,
    shape: Shape = OneUiInteractionDefaults.shape(OneUiTheme.shapes),
    pressedScale: Float = 0.965f,
    hoveredScale: Float = 1f,
    indication: Indication? = ripple(bounded = true),
): Modifier {
    val pressed by interactionSource.collectIsPressedAsState()
    val hovered by interactionSource.collectIsHoveredAsState()
    var focused by remember { mutableStateOf(false) }
    val reducedMotion = OneUiTheme.reducedMotion
    val targetScale = when {
        !enabled -> 1f
        pressed -> pressedScale
        hovered -> hoveredScale
        else -> 1f
    }
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = if (reducedMotion) snap() else OneUiMotion.press(),
        label = "OneUi interaction scale",
    )
    val activationKeys = setOf(Key.Enter, Key.NumPadEnter, Key.Spacebar, Key.DirectionCenter)

    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clip(shape)
        .then(
            if (focused && enabled) {
                Modifier.border(
                    width = OneUiTheme.sizes.focusRingWidth,
                    color = OneUiTheme.colors.accent,
                    shape = shape,
                )
            } else {
                Modifier
            },
        )
        .hoverable(
            interactionSource = interactionSource,
            enabled = enabled,
        )
        .onFocusChanged { focused = it.isFocused }
        .focusable(
            enabled = enabled,
            interactionSource = interactionSource,
        )
        .onPreviewKeyEvent { event ->
            if (!enabled || event.key !in activationKeys) {
                false
            } else {
                if (event.type == KeyEventType.KeyUp) onClick()
                event.type == KeyEventType.KeyDown || event.type == KeyEventType.KeyUp
            }
        }
        .clickable(
            enabled = enabled,
            role = role,
            interactionSource = interactionSource,
            indication = indication,
            onClick = onClick,
        )
}
