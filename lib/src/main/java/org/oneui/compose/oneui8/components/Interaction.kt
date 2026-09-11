package org.oneui.compose.oneui8.components

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import org.oneui.compose.interaction.oneUiInteractive

/** Compatibility bridge for existing OneUI8 scaffold components. */
@Composable
internal fun Modifier.oneUI8Pressable(
    enabled: Boolean,
    onClick: () -> Unit,
    pressedScale: Float = 0.965f,
    indication: Indication? = ripple(bounded = true),
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    return oneUiInteractive(
        enabled = enabled,
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RectangleShape,
        pressedScale = pressedScale,
        indication = indication,
    )
}
