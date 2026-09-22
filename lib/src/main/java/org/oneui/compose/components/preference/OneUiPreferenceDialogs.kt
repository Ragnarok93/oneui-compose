package org.oneui.compose.components.preference

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.oneui.compose.base.Icon
import org.oneui.compose.preference.DropdownPreference
import org.oneui.compose.preference.EditTextPreference
import org.oneui.compose.preference.MultiSelectPreference
import org.oneui.compose.preference.SingleSelectPreference

@Composable
fun <T> OneUiDropdownPreference(
    title: String,
    item: T,
    items: List<T>,
    nameFor: (T) -> String,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: Icon? = null,
) = DropdownPreference(
    title = title,
    item = item,
    items = items,
    nameFor = nameFor,
    onItemSelected = onItemSelected,
    modifier = modifier,
    enabled = enabled,
    icon = icon,
)

@Composable
fun OneUiEditTextPreference(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: Icon? = null,
) = EditTextPreference(
    title = title,
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    enabled = enabled,
    icon = icon,
)

@Composable
fun <T> OneUiSingleSelectPreference(
    title: String,
    value: T,
    values: List<T>,
    onValueChange: (T) -> Unit,
    nameFor: (T) -> String = { it.toString() },
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: Icon? = null,
) = SingleSelectPreference(
    title = title,
    value = value,
    values = values,
    onValueChange = onValueChange,
    nameFor = nameFor,
    modifier = modifier,
    enabled = enabled,
    icon = icon,
)

@Composable
fun <T> OneUiMultiSelectPreference(
    title: String,
    selectedValues: List<T>,
    values: List<T>,
    onValuesChange: (List<T>) -> Unit,
    nameFor: (T) -> String = { it.toString() },
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: Icon? = null,
) = MultiSelectPreference(
    title = title,
    selectedValues = selectedValues,
    values = values,
    onValuesChange = onValuesChange,
    nameFor = nameFor,
    modifier = modifier,
    enabled = enabled,
    icon = icon,
)

