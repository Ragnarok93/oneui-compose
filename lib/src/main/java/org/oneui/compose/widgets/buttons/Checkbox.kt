package org.oneui.compose.widgets.buttons

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.selection.OneUiCheckbox
import org.oneui.compose.components.selection.OneUiCheckboxColors
import org.oneui.compose.theme.OneUITheme
import org.oneui.compose.util.enabledAlpha

/**
 * Compatibility wrapper for the stable Compose-native One UI checkbox.
 *
 * Existing label/click behavior and color contracts are preserved while drawing, animation,
 * keyboard/D-pad handling, semantics, disabled behavior, and reduced motion are delegated to
 * [OneUiCheckbox].
 */
@Composable
fun Checkbox(
    modifier: Modifier = Modifier,
    colors: CheckboxColors = checkboxColors(),
    onCheckedChange: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    checked: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    labelSpacing: Dp = CheckboxDefaults.spacing,
    label: (@Composable () -> Unit)? = null,
) {
    val mappedColors = OneUiCheckboxColors(
        checkedColor = colors.color,
        uncheckedColor = colors.unselectedColor,
        markColor = Color.White,
        disabledCheckedColor = colors.disabledColor,
        disabledUncheckedColor = colors.disabledUnselectedColor,
        disabledMarkColor = Color.White.copy(alpha = CheckboxDefaults.disabledAlphaValue),
        haloColor = colors.ripple,
    )
    val rowInteraction = if (onCheckedChange != null) {
        Modifier.toggleable(
            value = checked,
            enabled = enabled,
            role = Role.Checkbox,
            interactionSource = interactionSource,
            indication = ripple(
                bounded = false,
                radius = CheckboxDefaults.touchSize / 2,
                color = colors.ripple,
            ),
            onValueChange = onCheckedChange,
        )
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .then(rowInteraction)
            .padding(CheckboxDefaults.padding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        OneUiCheckbox(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            colors = mappedColors,
            modifier = if (onCheckedChange != null) {
                Modifier.clearAndSetSemantics { }
            } else {
                Modifier
            },
        )
        if (label != null) {
            Spacer(modifier = Modifier.width(labelSpacing))
            ProvideTextStyle(value = OneUITheme.types.checkboxLabel.enabledAlpha(enabled)) {
                label()
            }
        }
    }
}

/** Dialog/list convenience wrapper retaining the legacy signature. */
@Composable
fun ListCheckbox(
    modifier: Modifier = Modifier,
    colors: CheckboxColors = checkboxColors(),
    onCheckedChange: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    checked: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    label: (@Composable () -> Unit)? = null,
) {
    Checkbox(
        modifier = modifier.padding(CheckboxDefaults.listPadding),
        colors = colors,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        checked = checked,
        interactionSource = interactionSource,
        labelSpacing = CheckboxDefaults.listLabelSpacing,
        label = label,
    )
}

/** Legacy checkbox colors retained for source compatibility. */
@Immutable
data class CheckboxColors(
    val color: Color,
    val unselectedColor: Color,
    val disabledColor: Color,
    val disabledUnselectedColor: Color,
    val ripple: Color,
) {
    @Composable
    internal fun checkColor(enabled: Boolean, checked: Boolean, animDuration: Int): State<Color> {
        @Suppress("UNUSED_VARIABLE")
        val ignoredAnimationDuration = animDuration
        val target = when {
            enabled && checked -> color
            enabled -> unselectedColor
            checked -> disabledColor
            else -> disabledUnselectedColor
        }
        return rememberUpdatedState(target)
    }
}

@Composable
fun checkboxColors(
    color: Color = OneUITheme.colors.seslPrimaryColor,
    unselectedColor: Color = OneUITheme.colors.seslControlNormalColor,
    disabledColor: Color = color.copy(alpha = CheckboxDefaults.disabledAlphaValue),
    disabledUnselectedColor: Color = unselectedColor.copy(alpha = CheckboxDefaults.disabledAlphaValue),
    ripple: Color = OneUITheme.colors.seslRippleColor,
): CheckboxColors = CheckboxColors(
    color = color,
    unselectedColor = unselectedColor,
    disabledColor = disabledColor,
    disabledUnselectedColor = disabledUnselectedColor,
    ripple = ripple,
)

/** Legacy constants retained for source compatibility. */
object CheckboxDefaults {
    const val animDurationMillis = 300
    val padding = 2.dp
    const val strokeWidthPx = 3F
    val outlineSize = 20.dp
    val outlineSizeAnimDif = 3.dp
    val touchSize = 40.dp
    val spacing = 8.dp
    val listLabelSpacing = 22.dp
    val listPadding = PaddingValues(all = 16.dp)
    const val disabledAlphaValue = 0.38f
}
