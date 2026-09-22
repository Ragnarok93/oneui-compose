package org.oneui.compose.components.buttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.snap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.oneui.compose.interaction.oneUiInteractive
import org.oneui.compose.motion.OneUiMotion
import org.oneui.compose.theme.OneUiTheme

/** Complete visual-state palette for [OneUiButton]. */
@Immutable
data class OneUiButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val pressedContainerColor: Color,
    val hoveredContainerColor: Color,
    val borderColor: Color,
    val disabledBorderColor: Color,
    val rippleColor: Color,
)

/** Stable defaults and variants for Compose-native One UI buttons. */
object OneUiButtonDefaults {
    val ContentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
    val CompactContentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
    val MinHeight = 48.dp
    val MinWidth = 64.dp
    val OutlinedMinHeight = 36.dp
    val OutlinedMinWidth = 196.dp
    val BorderWidth = 1.dp
    val OutlinedContentPadding = PaddingValues(horizontal = 16.5.dp, vertical = 9.dp)

    @Composable
    fun filledColors(
        containerColor: Color = OneUiTheme.colors.accent,
        contentColor: Color = OneUiTheme.colors.onAccent,
    ): OneUiButtonColors {
        val opacity = OneUiTheme.opacity
        return OneUiButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(
                alpha = containerColor.alpha * opacity.disabledContainer,
            ),
            disabledContentColor = contentColor.copy(
                alpha = contentColor.alpha * opacity.disabledContent,
            ),
            pressedContainerColor = containerColor.copy(alpha = containerColor.alpha * 0.82f),
            hoveredContainerColor = containerColor.copy(alpha = containerColor.alpha * 0.92f),
            borderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            rippleColor = contentColor,
        )
    }

    @Composable
    fun tonalColors(
        containerColor: Color = OneUiTheme.colors.accent.copy(alpha = 0.14f),
        contentColor: Color = OneUiTheme.colors.accent,
    ): OneUiButtonColors {
        val opacity = OneUiTheme.opacity
        return OneUiButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(
                alpha = containerColor.alpha * opacity.disabledContainer,
            ),
            disabledContentColor = contentColor.copy(
                alpha = contentColor.alpha * opacity.disabledContent,
            ),
            pressedContainerColor = contentColor.copy(alpha = 0.22f),
            hoveredContainerColor = contentColor.copy(alpha = 0.17f),
            borderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            rippleColor = contentColor,
        )
    }

    @Composable
    fun neutralColors(
        containerColor: Color = OneUiTheme.colors.surfaceElevated,
        contentColor: Color = OneUiTheme.colors.primaryText,
    ): OneUiButtonColors {
        val colors = OneUiTheme.colors
        val opacity = OneUiTheme.opacity
        return OneUiButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(
                alpha = containerColor.alpha * opacity.disabledContainer,
            ),
            disabledContentColor = contentColor.copy(
                alpha = contentColor.alpha * opacity.disabledContent,
            ),
            pressedContainerColor = colors.surfacePressed,
            hoveredContainerColor = colors.surfacePressed.copy(alpha = 0.72f),
            borderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            rippleColor = contentColor,
        )
    }

    @Composable
    fun outlinedColors(
        contentColor: Color = OneUiTheme.colors.accent,
        borderColor: Color = OneUiTheme.colors.accent,
    ): OneUiButtonColors {
        val opacity = OneUiTheme.opacity
        return OneUiButtonColors(
            containerColor = Color.Transparent,
            contentColor = contentColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = contentColor.copy(
                alpha = contentColor.alpha * opacity.disabledContent,
            ),
            pressedContainerColor = contentColor.copy(alpha = 0.14f),
            hoveredContainerColor = contentColor.copy(alpha = 0.08f),
            borderColor = borderColor,
            disabledBorderColor = borderColor.copy(
                alpha = borderColor.alpha * opacity.disabledContainer,
            ),
            rippleColor = contentColor,
        )
    }

    @Composable
    fun textColors(
        contentColor: Color = OneUiTheme.colors.accent,
    ): OneUiButtonColors {
        val opacity = OneUiTheme.opacity
        return OneUiButtonColors(
            containerColor = Color.Transparent,
            contentColor = contentColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = contentColor.copy(
                alpha = contentColor.alpha * opacity.disabledContent,
            ),
            pressedContainerColor = contentColor.copy(alpha = 0.14f),
            hoveredContainerColor = contentColor.copy(alpha = 0.08f),
            borderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            rippleColor = contentColor,
        )
    }

    /** SESL ButtonStyleTransparent: no physical fill, while retaining normal label color. */
    @Composable
    fun transparentColors(
        contentColor: Color = OneUiTheme.colors.primaryText,
    ): OneUiButtonColors {
        val opacity = OneUiTheme.opacity
        return OneUiButtonColors(
            containerColor = Color.Transparent,
            contentColor = contentColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = contentColor.copy(
                alpha = contentColor.alpha * opacity.disabledContent,
            ),
            pressedContainerColor = OneUiTheme.colors.surfacePressed,
            hoveredContainerColor = OneUiTheme.colors.surfacePressed.copy(alpha = 0.72f),
            borderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            rippleColor = contentColor,
        )
    }

    @Composable
    fun toggleColors(selected: Boolean): OneUiButtonColors =
        if (selected) tonalColors() else neutralColors()
}

/** Variants provided by SESL's OneUI.ContainedButton theme family. */
enum class OneUiContainedButtonStyle {
    Neutral,
    Primary,
    Transparent,
}

/** Geometry and palette defaults pinned to SESL8's contained-button styles. */
object OneUiContainedButtonDefaults {
    val MinWidth = 200.dp
    val MinHeight = 52.dp
    val CornerRadius = 26.dp
    val ContentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)

    @Composable
    fun colors(style: OneUiContainedButtonStyle): OneUiButtonColors = when (style) {
        OneUiContainedButtonStyle.Neutral -> OneUiButtonDefaults.neutralColors()
        OneUiContainedButtonStyle.Primary -> OneUiButtonDefaults.filledColors()
        OneUiContainedButtonStyle.Transparent -> OneUiButtonDefaults.transparentColors()
    }
}

/**
 * Shared One UI button primitive.
 *
 * The caller owns state; [loading] suppresses activation without changing the measured content
 * bounds. Real pressed/hovered/focused state comes from [interactionSource], and reduced-motion
 * mode snaps visual state instead of fabricating delayed flags.
 */
@Composable
fun OneUiButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: OneUiButtonColors = OneUiButtonDefaults.filledColors(),
    shape: Shape = OneUiTheme.shapes.control,
    contentPadding: PaddingValues = OneUiButtonDefaults.ContentPadding,
    minWidth: Dp = OneUiButtonDefaults.MinWidth,
    minHeight: Dp = OneUiButtonDefaults.MinHeight,
    content: @Composable RowScope.() -> Unit,
) {
    val pressed by interactionSource.collectIsPressedAsState()
    val hovered by interactionSource.collectIsHoveredAsState()
    val reducedMotion = OneUiTheme.reducedMotion
    val interactionEnabled = enabled && !loading

    val targetContainer = when {
        !enabled -> colors.disabledContainerColor
        pressed && interactionEnabled -> colors.pressedContainerColor
        hovered && interactionEnabled -> colors.hoveredContainerColor
        else -> colors.containerColor
    }
    val targetContent = if (enabled) colors.contentColor else colors.disabledContentColor
    val targetBorder = if (enabled) colors.borderColor else colors.disabledBorderColor
    val containerColor by animateColorAsState(
        targetValue = targetContainer,
        animationSpec = if (reducedMotion) snap() else OneUiMotion.quick(),
        label = "OneUi button container",
    )
    val contentColor by animateColorAsState(
        targetValue = targetContent,
        animationSpec = if (reducedMotion) snap() else OneUiMotion.quick(),
        label = "OneUi button content",
    )
    val borderColor by animateColorAsState(
        targetValue = targetBorder,
        animationSpec = if (reducedMotion) snap() else OneUiMotion.quick(),
        label = "OneUi button border",
    )

    Row(
        modifier = modifier
            .defaultMinSize(
                minWidth = minWidth,
                minHeight = minHeight,
            )
            .background(containerColor, shape)
            .border(OneUiButtonDefaults.BorderWidth, borderColor, shape)
            .clip(shape)
            .oneUiInteractive(
                enabled = interactionEnabled,
                onClick = onClick,
                interactionSource = interactionSource,
                role = Role.Button,
                shape = shape,
                indication = ripple(color = colors.rippleColor),
            )
            .semantics(mergeDescendants = true) {
                if (loading) stateDescription = "Loading"
            }
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Row(
                modifier = Modifier.alpha(if (loading) 0f else 1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    ProvideTextStyle(OneUiTheme.typography.buttonLabel) {
                        content()
                    }
                }
            }

            AnimatedContent(
                targetState = loading,
                modifier = Modifier.matchParentSize(),
                contentAlignment = Alignment.Center,
                transitionSpec = {
                    fadeIn(animationSpec = if (reducedMotion) snap() else OneUiMotion.quick()) togetherWith
                        fadeOut(animationSpec = if (reducedMotion) snap() else OneUiMotion.quick())
                },
                label = "OneUi button loading",
            ) { isLoading ->
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = contentColor,
                        strokeWidth = 2.dp,
                        trackColor = Color.Transparent,
                    )
                }
            }
        }
    }
}

/**
 * Compose-native equivalent of SESL's 200dp-wide OneUI.ContainedButton.
 *
 * It retains normal button focus, keyboard/D-pad, reduced-motion and clipped-ripple behavior.
 */
@Composable
fun OneUiContainedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: OneUiContainedButtonStyle = OneUiContainedButtonStyle.Neutral,
    enabled: Boolean = true,
    loading: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) = OneUiButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    loading = loading,
    interactionSource = interactionSource,
    colors = OneUiContainedButtonDefaults.colors(style),
    shape = RoundedCornerShape(OneUiContainedButtonDefaults.CornerRadius),
    contentPadding = OneUiContainedButtonDefaults.ContentPadding,
    minWidth = OneUiContainedButtonDefaults.MinWidth,
    minHeight = OneUiContainedButtonDefaults.MinHeight,
    content = content,
)

@Composable
fun OneUiFilledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) = OneUiButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    loading = loading,
    interactionSource = interactionSource,
    colors = OneUiButtonDefaults.filledColors(),
    content = content,
)

@Composable
fun OneUiOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) = OneUiButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    loading = loading,
    interactionSource = interactionSource,
    colors = OneUiButtonDefaults.outlinedColors(),
    shape = OneUiTheme.shapes.control,
    contentPadding = OneUiButtonDefaults.OutlinedContentPadding,
    minWidth = OneUiButtonDefaults.OutlinedMinWidth,
    minHeight = OneUiButtonDefaults.OutlinedMinHeight,
    content = content,
)

@Composable
fun OneUiTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) = OneUiButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    loading = loading,
    interactionSource = interactionSource,
    colors = OneUiButtonDefaults.textColors(),
    contentPadding = OneUiButtonDefaults.CompactContentPadding,
    content = content,
)

@Composable
fun OneUiToggleButton(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    OneUiButton(
        onClick = { onSelectedChange(!selected) },
        modifier = modifier.semantics {
            this.selected = selected
            stateDescription = if (selected) "Selected" else "Not selected"
        },
        enabled = enabled,
        interactionSource = interactionSource,
        colors = OneUiButtonDefaults.toggleColors(selected),
        content = content,
    )
}
