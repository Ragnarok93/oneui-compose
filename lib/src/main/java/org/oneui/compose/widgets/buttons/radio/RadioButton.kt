/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */

package org.oneui.compose.widgets.buttons.radio

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
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
import org.oneui.compose.components.selection.OneUiRadioButton
import org.oneui.compose.components.selection.OneUiRadioButtonColors
import org.oneui.compose.theme.OneUITheme

/**
 * Compatibility wrapper for the stable Compose-native One UI radio control.
 *
 * The generic legacy value/group API is retained, while visual state, motion, semantics,
 * keyboard/D-pad activation, disabled behavior, and reduced motion are delegated to
 * [OneUiRadioButton].
 */
@Composable
fun <T> RadioButton(
    modifier: Modifier = Modifier,
    colors: RadioButtonColors = radioButtonColors(),
    onClick: ((T) -> Unit)? = null,
    enabled: Boolean = true,
    value: T,
    groupValue: T = value,
    animDurationMillis: Int = RadioButtonDefaults.animDurationMillis,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    labelSpacing: Dp = RadioButtonDefaults.labelSpacing,
    label: (@Composable () -> Unit)? = null,
) {
    @Suppress("UNUSED_VARIABLE")
    val ignoredAnimationDuration = animDurationMillis
    val selected = value == groupValue
    val mappedColors = OneUiRadioButtonColors(
        selectedColor = colors.color,
        unselectedColor = colors.unselectedColor,
        disabledSelectedColor = colors.disabledColor,
        disabledUnselectedColor = colors.disabledUnselectedColor,
        haloColor = colors.ripple,
    )
    val rowInteraction = if (onClick != null) {
        Modifier.selectable(
            selected = selected,
            enabled = enabled,
            role = Role.RadioButton,
            interactionSource = interactionSource,
            indication = ripple(
                bounded = false,
                radius = RadioButtonDefaults.touchSize / 2,
                color = colors.ripple,
            ),
            onClick = { onClick(value) },
        )
    } else {
        Modifier
    }

    Row(
        modifier = modifier.then(rowInteraction),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        OneUiRadioButton(
            selected = selected,
            onClick = null,
            enabled = enabled,
            colors = mappedColors,
            modifier = if (onClick != null) {
                Modifier.clearAndSetSemantics { }
            } else {
                Modifier
            },
        )
        if (label != null) {
            Spacer(modifier = Modifier.width(labelSpacing))
            ProvideTextStyle(value = OneUITheme.types.radioLabel) {
                label()
            }
        }
    }
}

/** Dialog/list convenience wrapper retaining the legacy signature. */
@Composable
fun <T> ListRadioButton(
    modifier: Modifier = Modifier,
    colors: RadioButtonColors = radioButtonColors(),
    onClick: ((T) -> Unit)? = null,
    enabled: Boolean = true,
    value: T,
    groupValue: T = value,
    animDurationMillis: Int = RadioButtonDefaults.animDurationMillis,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    label: (@Composable () -> Unit)? = null,
) {
    RadioButton(
        modifier = modifier.padding(RadioButtonDefaults.listPadding),
        colors = colors,
        onClick = onClick,
        enabled = enabled,
        value = value,
        groupValue = groupValue,
        animDurationMillis = animDurationMillis,
        interactionSource = interactionSource,
        labelSpacing = RadioButtonDefaults.listLabelSpacing,
        label = label,
    )
}

/** Defaults used in the compatibility [RadioButton]. */
internal object RadioButtonDefaults {
    const val animDurationMillis = 300
    val padding = 2.dp
    val dotSize = 12.dp
    const val strokeWidthPx = 3F
    val outlineSize = 20.dp
    val outlineSizeAnimDif = 3.dp
    val touchSize = 40.dp
    val labelSpacing = 8.dp
    val listLabelSpacing = 22.dp
    val listPadding = PaddingValues(all = 16.dp)
    const val disabledAlphaValue = 0.38f
}

@Immutable
data class RadioButtonColors(
    val color: Color,
    val unselectedColor: Color,
    val disabledColor: Color,
    val disabledUnselectedColor: Color,
    val ripple: Color,
) {
    @Composable
    internal fun radioColor(enabled: Boolean, selected: Boolean, animDuration: Int): State<Color> {
        @Suppress("UNUSED_VARIABLE")
        val ignoredAnimationDuration = animDuration
        val target = when {
            enabled && selected -> color
            enabled -> unselectedColor
            selected -> disabledColor
            else -> disabledUnselectedColor
        }
        return rememberUpdatedState(target)
    }
}

@Composable
fun radioButtonColors(
    color: Color = OneUITheme.colors.seslPrimaryColor,
    unselectedColor: Color = OneUITheme.colors.seslControlNormalColor,
    disabledColor: Color = color.copy(alpha = RadioButtonDefaults.disabledAlphaValue),
    disabledUnselectedColor: Color = unselectedColor.copy(alpha = RadioButtonDefaults.disabledAlphaValue),
    ripple: Color = OneUITheme.colors.seslRippleColor,
): RadioButtonColors = RadioButtonColors(
    color = color,
    unselectedColor = unselectedColor,
    disabledColor = disabledColor,
    disabledUnselectedColor = disabledUnselectedColor,
    ripple = ripple,
)
