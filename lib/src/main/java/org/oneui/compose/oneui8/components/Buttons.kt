package org.oneui.compose.oneui8.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.buttons.OneUiButton
import org.oneui.compose.components.buttons.OneUiButtonColors
import org.oneui.compose.components.buttons.OneUiButtonDefaults
import org.oneui.compose.theme.OneUiTheme

enum class OneUI8ButtonStyle { Primary, Tonal, Neutral }

/** Compatibility forwarding surface for the feature-branch One UI 8 button. */
@Deprecated(
    message = "Use OneUiButton/OneUiFilledButton/OneUiOutlinedButton from components.buttons.",
)
@Composable
fun OneUI8Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: OneUI8ButtonStyle = OneUI8ButtonStyle.Primary,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
) {
    val colors = when (style) {
        OneUI8ButtonStyle.Primary -> OneUiButtonDefaults.filledColors()
        OneUI8ButtonStyle.Tonal -> OneUiButtonDefaults.tonalColors()
        OneUI8ButtonStyle.Neutral -> OneUiButtonDefaults.neutralColors()
    }

    OneUiButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        shape = OneUiTheme.shapes.button,
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(7.dp))
        }
        Text(text = text)
    }
}

/** Compatibility forwarding surface retaining the historical [ImageVector] signature. */
@Deprecated(
    message = "Use OneUiIconButton with OneUiIcon from the stable One UI Compose surface.",
)
@Composable
fun OneUI8IconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = org.oneui.compose.oneui8.theme.OneUI8Theme.colors.surfaceElevated,
    contentColor: Color = org.oneui.compose.oneui8.theme.OneUI8Theme.colors.primaryText,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val opacity = OneUiTheme.opacity
    val colors = OneUiButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = containerColor.copy(
            alpha = containerColor.alpha * opacity.disabledContainer,
        ),
        disabledContentColor = contentColor.copy(
            alpha = contentColor.alpha * opacity.disabledContent,
        ),
        pressedContainerColor = contentColor.copy(alpha = 0.14f),
        hoveredContainerColor = contentColor.copy(alpha = 0.08f),
        borderColor = Color.Transparent,
        disabledBorderColor = Color.Transparent,
        rippleColor = contentColor,
    )

    OneUiButton(
        onClick = onClick,
        modifier = modifier.size(48.dp),
        enabled = enabled,
        interactionSource = interactionSource,
        colors = colors,
        shape = OneUiTheme.shapes.iconButton,
        contentPadding = PaddingValues(0.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
        )
    }
}
