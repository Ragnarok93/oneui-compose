package org.oneui.compose.components.preference

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.oneui.compose.base.Icon
import org.oneui.compose.preference.CheckboxPreference
import org.oneui.compose.preference.SeekbarPreference
import org.oneui.compose.preference.SwitchPreference
import org.oneui.compose.preference.SwitchPreferenceScreen

@Composable
fun OneUiCheckboxPreference(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    summary: String? = null,
    enabled: Boolean = true,
    icon: Icon? = null,
) = CheckboxPreference(
    title = title,
    checked = checked,
    onCheckedChange = onCheckedChange,
    modifier = modifier,
    summary = summary,
    enabled = enabled,
    icon = icon,
)

@Composable
fun OneUiSwitchPreference(
    title: String,
    switched: Boolean,
    onSwitchedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    summary: String? = null,
    enabled: Boolean = true,
    icon: Icon? = null,
) = SwitchPreference(
    title = title,
    switched = switched,
    onSwitchedChange = onSwitchedChange,
    modifier = modifier,
    summary = summary,
    enabled = enabled,
    icon = icon,
)

@Composable
fun OneUiSwitchPreferenceScreen(
    title: String,
    switched: Boolean,
    onSwitch: (Boolean) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    summary: String? = null,
    enabled: Boolean = true,
    icon: Icon? = null,
) = SwitchPreferenceScreen(
    title = title,
    switched = switched,
    onSwitch = onSwitch,
    onClick = onClick,
    modifier = modifier,
    summary = summary,
    enabled = enabled,
    icon = icon,
)

@Composable
fun OneUiSeekBarPreference(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    summary: String? = null,
    enabled: Boolean = true,
    icon: Icon? = null,
) = SeekbarPreference(
    title = title,
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    summary = summary,
    enabled = enabled,
    icon = icon,
)

