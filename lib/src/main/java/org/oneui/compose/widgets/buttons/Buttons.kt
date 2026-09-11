package org.oneui.compose.widgets.buttons

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.buttons.OneUiButton
import org.oneui.compose.components.buttons.OneUiButtonColors
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.util.enabledAlpha

/**
 * Legacy button signature forwarded to the stable Compose-native One UI button engine.
 */
@Composable
fun Button(
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    padding: PaddingValues = ButtonDefaults.padding,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    onClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    leadingIcon: (@Composable () -> Unit)? = null,
    colors: ButtonColors = defaultButtonColors(),
) {
    OneUiButton(
        onClick = { onClick?.invoke() },
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        colors = colors.toStableColors(),
        shape = shape,
        contentPadding = padding,
    ) {
        if (leadingIcon != null) {
            leadingIcon()
            Spacer(Modifier.width(ButtonDefaults.leadingIconSpacing))
        }
        label()
    }
}

/** Legacy text overload retained source-compatible and forwarded to [Button]. */
@Composable
fun Button(
    modifier: Modifier = Modifier,
    label: String,
    padding: PaddingValues = ButtonDefaults.padding,
    textOverflow: TextOverflow = ButtonDefaults.textOverflow,
    maxLines: Int = ButtonDefaults.maxLines,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    textStyle: TextStyle = OneUITheme.types.button,
    onClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    leadingIcon: (@Composable () -> Unit)? = null,
    colors: ButtonColors = defaultButtonColors(),
) = Button(
    modifier = modifier,
    label = {
        BasicText(
            text = label,
            style = textStyle.copy(color = colors.text).enabledAlpha(enabled),
            overflow = textOverflow,
            maxLines = maxLines,
        )
    },
    padding = padding,
    enabled = enabled,
    shape = shape,
    onClick = onClick,
    interactionSource = interactionSource,
    leadingIcon = leadingIcon,
    colors = colors,
)

/** Stores the historical color contract for source compatibility. */
data class ButtonColors(
    val ripple: Color,
    val background: Color,
    val backgroundDisabled: Color,
    val text: Color,
)

@Composable
private fun ButtonColors.toStableColors(): OneUiButtonColors = OneUiButtonColors(
    containerColor = background,
    contentColor = text,
    disabledContainerColor = backgroundDisabled,
    disabledContentColor = text.copy(alpha = text.alpha * ButtonDefaults.disabledAlpha),
    pressedContainerColor = OneUITheme.colors.seslListRippleColor,
    hoveredContainerColor = OneUITheme.colors.seslListRippleColor.copy(alpha = 0.72f),
    borderColor = Color.Transparent,
    disabledBorderColor = Color.Transparent,
    rippleColor = ripple,
)

/** Constructs the button colors for the historical default variant. */
@Composable
fun defaultButtonColors(
    ripple: Color = OneUITheme.colors.seslRippleColor,
    background: Color = OneUITheme.colors.buttonDefaultBackground,
    backgroundDisabled: Color = background.copy(alpha = ButtonDefaults.disabledAlpha),
    text: Color = OneUITheme.colors.seslPrimaryTextColor,
): ButtonColors = ButtonColors(
    ripple = ripple,
    background = background,
    backgroundDisabled = backgroundDisabled,
    text = text,
)

/** Constructs the button colors for the historical colored variant. */
@Composable
fun coloredButtonColors(
    ripple: Color = OneUITheme.colors.seslRippleColor,
    background: Color = OneUITheme.colors.seslPrimaryColor,
    backgroundDisabled: Color = background.copy(alpha = ButtonDefaults.disabledAlpha),
    text: Color = OneUITheme.colors.seslWhite,
): ButtonColors = ButtonColors(
    ripple = ripple,
    background = background,
    backgroundDisabled = backgroundDisabled,
    text = text,
)

/** Constructs the button colors for the historical transparent variant. */
@Composable
fun transparentButtonColors(
    ripple: Color = OneUITheme.colors.seslRippleColor,
    background: Color = Color.Transparent,
    backgroundDisabled: Color = Color.Transparent,
    text: Color = OneUITheme.colors.seslPrimaryTextColor,
): ButtonColors = ButtonColors(
    ripple = ripple,
    background = background,
    backgroundDisabled = backgroundDisabled,
    text = text,
)

/** Stores historical defaults for source compatibility. */
object ButtonDefaults {
    val padding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    val leadingIconSpacing = 16.dp
    val textOverflow = TextOverflow.Ellipsis
    const val maxLines = 2
    val shape = RoundedCornerShape(18.dp)
    const val disabledAlpha: Float = 0.4F
}
