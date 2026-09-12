package org.oneui.compose.widgets.buttons

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.selection.OneUiSwitch
import org.oneui.compose.components.selection.OneUiSwitchColors
import org.oneui.compose.theme.OneUITheme

/**
 * Compatibility wrapper for the stable Compose-native One UI switch.
 *
 * The public signature is preserved, while interaction, motion, keyboard/D-pad handling,
 * accessibility semantics, RTL behavior, and SESL8-derived geometry are owned by [OneUiSwitch].
 */
@Composable
fun Switch(
    modifier: Modifier = Modifier,
    colors: SwitchColors = switchColors(),
    onSwitchedChange: (Boolean) -> Unit = { },
    enabled: Boolean = true,
    switched: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    OneUiSwitch(
        checked = switched,
        onCheckedChange = onSwitchedChange,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        colors = OneUiSwitchColors(
            checkedTrackColor = colors.track,
            uncheckedTrackColor = colors.trackOff,
            disabledCheckedTrackColor = colors.trackDisabled,
            disabledUncheckedTrackColor = colors.trackOffDisabled,
            checkedThumbColor = colors.thumb,
            uncheckedThumbColor = colors.thumbOff,
            disabledCheckedThumbColor = colors.thumbDisabled,
            disabledUncheckedThumbColor = colors.thumbOffDisabled,
            checkedThumbStrokeColor = colors.stroke,
            uncheckedThumbStrokeColor = colors.strokeOff,
            disabledThumbStrokeColor = colors.strokeOffDisabled,
            haloColor = colors.ripple,
        ),
    )
}

/** Contains colors for a [Switch]. */
data class SwitchColors(
    val thumb: Color,
    val thumbOff: Color,
    val thumbDisabled: Color,
    val thumbOffDisabled: Color,
    val track: Color,
    val trackOff: Color,
    val trackDisabled: Color,
    val trackOffDisabled: Color,
    val stroke: Color,
    val strokeOff: Color,
    val strokeDisabled: Color,
    val strokeOffDisabled: Color,
    val ripple: Color,
)

/** Compatibility state-resolved color tuple retained for source compatibility. */
data class ActualSwitchColors(
    val thumb: Color,
    val track: Color,
    val stroke: Color,
) {
    companion object {
        fun SwitchColors.forConfig(enabled: Boolean, switched: Boolean): ActualSwitchColors = when {
            enabled && switched -> ActualSwitchColors(thumb, track, stroke)
            !enabled && switched -> ActualSwitchColors(thumbDisabled, trackDisabled, strokeDisabled)
            enabled -> ActualSwitchColors(thumbOff, trackOff, strokeOff)
            else -> ActualSwitchColors(thumbOffDisabled, trackOffDisabled, strokeOffDisabled)
        }
    }
}

/** Constructs the legacy color contract used by [Switch]. */
@Composable
fun switchColors(
    thumb: Color = OneUITheme.colors.seslSwitchThumbOnColor,
    thumbOff: Color = OneUITheme.colors.seslSwitchThumbOffColor,
    thumbDisabled: Color = OneUITheme.colors.seslSwitchThumbOnDisabledColor,
    thumbOffDisabled: Color = OneUITheme.colors.seslSwitchThumbOffDisabledColor,
    track: Color = OneUITheme.colors.seslPrimaryColor,
    trackOff: Color = OneUITheme.colors.seslSwitchTrackOffColor,
    trackDisabled: Color = OneUITheme.colors.seslPrimaryColor.copy(alpha = 0.4F),
    trackOffDisabled: Color = OneUITheme.colors.seslSwitchTrackOffDisabledColor,
    stroke: Color = OneUITheme.colors.seslPrimaryColor,
    strokeOff: Color = OneUITheme.colors.seslSwitchThumbOffStrokeColor,
    strokeDisabled: Color = OneUITheme.colors.seslPrimaryColor.copy(alpha = 0.4F),
    strokeOffDisabled: Color = OneUITheme.colors.seslSwitchThumbOffDisabledStrokeColor,
    ripple: Color = OneUITheme.colors.seslRippleColor,
): SwitchColors = SwitchColors(
    thumb = thumb,
    thumbOff = thumbOff,
    thumbDisabled = thumbDisabled,
    thumbOffDisabled = thumbOffDisabled,
    track = track,
    trackOff = trackOff,
    trackDisabled = trackDisabled,
    trackOffDisabled = trackOffDisabled,
    stroke = stroke,
    strokeOff = strokeOff,
    strokeDisabled = strokeDisabled,
    strokeOffDisabled = strokeOffDisabled,
    ripple = ripple,
)

/**
 * Legacy constants retained for source compatibility. The stable component owns actual geometry
 * and motion; new code should use `OneUiSwitchDefaults`.
 */
object SwitchDefaults {
    const val animDuration = 150
    val strokeWidth = 1.dp
    val thumbSize = DpSize(width = 22.dp, height = 22.dp)
    val thumbOvershoot = 2.dp
    val trackSize = DpSize(width = 35.dp, height = 18.5.dp)
    val trackCornerRadius = 9.25.dp
    val rippleRadius = 20.dp
}
