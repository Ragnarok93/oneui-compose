package org.oneui.compose.oneui8.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.ripple as rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import org.oneui.compose.oneui8.motion.OneUI8Motion

@Composable
internal fun Modifier.oneUI8Pressable(
    enabled: Boolean,
    onClick: () -> Unit,
    pressedScale: Float = 0.965f,
    indication: Indication? = rememberRipple(bounded = true),
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) pressedScale else 1f,
        animationSpec = OneUI8Motion.press(),
        label = "OneUI8 press scale",
    )
    return this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = indication,
            onClick = onClick,
        )
}
