package org.oneui.compose.demo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.buttons.OneUiTextButton
import org.oneui.compose.components.input.OneUiSpinner
import org.oneui.compose.components.list.OneUiListItem
import org.oneui.compose.components.list.OneUiRadioItem
import org.oneui.compose.components.list.OneUiSwitchItem
import org.oneui.compose.components.preference.OneUiPreference
import org.oneui.compose.components.preference.OneUiPreferenceCategory
import org.oneui.compose.components.preference.OneUiSuggestionCard
import org.oneui.compose.components.preference.OneUiTipsCard
import org.oneui.compose.components.progress.OneUiCircularProgress
import org.oneui.compose.components.progress.OneUiCircularProgressSize
import org.oneui.compose.components.selection.OneUiCheckbox
import org.oneui.compose.components.slider.OneUiSlider
import org.oneui.compose.components.slider.OneUiSliderMode
import org.oneui.compose.demo.CatalogSection
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.patterns.cards.OneUiRelatedLink
import org.oneui.compose.patterns.cards.OneUiRelatedLinksCard
import org.oneui.compose.patterns.preferences.OneUiPreferencesScreen
import org.oneui.compose.theme.OneUiTheme

@Composable
fun PreferencesScreen(
    modifier: Modifier = Modifier,
    onOpenAbout: () -> Unit = {},
) {
    var suggestionVisible by remember { mutableStateOf(true) }
    var autoDarkMode by remember { mutableStateOf(true) }
    var darkMode by remember { mutableStateOf(0) }
    var switchValue by remember { mutableStateOf(false) }
    var screenSwitch by remember { mutableStateOf(true) }
    var checkValue by remember { mutableStateOf(true) }
    var editValue by remember { mutableStateOf("Default text") }
    var dropDownIndex by remember { mutableStateOf(0) }
    var listIndex by remember { mutableStateOf(1) }
    var multiValues by remember { mutableStateOf(setOf("Item 1", "Item 3")) }
    var color by remember { mutableStateOf("#0381FE") }
    var basicProgress by remember { mutableFloatStateOf(0.60f) }
    var expandedProgress by remember { mutableFloatStateOf(0.35f) }
    var levelProgress by remember { mutableFloatStateOf(6f) }
    var centerProgress by remember { mutableFloatStateOf(0.5f) }
    var updatable by remember { mutableStateOf(false) }
    var dialog by remember { mutableStateOf<PreferenceDialog?>(null) }

    androidx.compose.foundation.layout.Box(modifier.fillMaxWidth()) {
        OneUiPreferencesScreen(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("catalog-preferences"),
        ) {
            if (suggestionVisible) {
                item {
                    OneUiSuggestionCard(
                        title = "Suggestion",
                        summary = "Just a suggestion: you can turn me on anytime.",
                        actionLabel = "Turn on",
                        onAction = { suggestionVisible = false },
                        onDismiss = { suggestionVisible = false },
                    )
                }
            }
            item {
                OneUiTipsCard(
                    title = "TipsCardPreference",
                    summary = "This is a test summary.",
                    buttonLabel = "Button",
                    onButtonClick = { },
                )
            }
            item {
                CatalogSection(title = "Switch bar") {
                    OneUiSwitchItem(
                        title = "Switch bar preference",
                        summary = if (switchValue) "On" else "Off",
                        checked = switchValue,
                        onCheckedChange = { switchValue = it },
                    )
                }
            }
            item {
                OneUiPreferenceCategory(title = "Dark mode settings") {
                    listOf("Light", "Dark", "System default").forEachIndexed { index, label ->
                        OneUiRadioItem(
                            title = label,
                            selected = darkMode == index,
                            enabled = !autoDarkMode,
                            onClick = { darkMode = index },
                        )
                    }
                    OneUiSwitchItem(
                        title = "System default",
                        checked = autoDarkMode,
                        onCheckedChange = { autoDarkMode = it },
                    )
                }
            }
            item {
                OneUiPreferenceCategory(title = "Compound preferences") {
                    OneUiPreference(
                        title = "Updatable widget",
                        summary = if (updatable) "Updating" else "Click me",
                        onClick = { updatable = !updatable },
                        trailing = {
                            if (updatable) {
                                OneUiCircularProgress(
                                    progress = null,
                                    size = OneUiCircularProgressSize.Small,
                                )
                            } else {
                                Text("✓", color = OneUiTheme.colors.accent)
                            }
                        },
                    )
                    OneUiSwitchItem(
                        title = "SwitchPreference",
                        checked = switchValue,
                        onCheckedChange = { switchValue = it },
                    )
                    OneUiSwitchItem(
                        title = "SwitchPreferenceScreen",
                        summary = if (screenSwitch) "On" else "Off",
                        checked = screenSwitch,
                        onCheckedChange = { screenSwitch = it },
                    )
                    OneUiPreference(
                        title = "CheckBoxPreference",
                        summary = "Someone's still using this one?",
                        onClick = { checkValue = !checkValue },
                        trailing = {
                            OneUiCheckbox(
                                checked = checkValue,
                                onCheckedChange = { checkValue = it },
                            )
                        },
                    )
                }
            }
            item {
                OneUiPreferenceCategory(title = "Value preferences") {
                    OneUiPreference(
                        title = "EditTextPreference",
                        summary = editValue,
                        onClick = { dialog = PreferenceDialog.EditText },
                    )
                    OneUiPreference(
                        title = "DropDownPreference",
                        summary = "Item ${dropDownIndex + 1}",
                        trailing = {
                            OneUiSpinner(
                                selectedIndex = dropDownIndex,
                                entries = preferenceItems,
                                onSelected = { dropDownIndex = it },
                            )
                        },
                    )
                    OneUiPreference(
                        title = "ListPreference",
                        summary = preferenceItems[listIndex],
                        onClick = { dialog = PreferenceDialog.List },
                    )
                    OneUiPreference(
                        title = "MultiSelectListPreference",
                        summary = multiValues.joinToString(),
                        onClick = { dialog = PreferenceDialog.Multi },
                    )
                    OneUiPreference(
                        title = "ColorPickerPreference",
                        summary = color,
                        onClick = { dialog = PreferenceDialog.Color },
                    )
                }
            }
            item {
                OneUiPreferenceCategory(title = "SeekBar preferences") {
                    PreferenceSliderRow("Basic", basicProgress, 0f..1f) { basicProgress = it }
                    PreferenceSliderRow("Expanded", expandedProgress, 0f..1f, OneUiSliderMode.Expand) {
                        expandedProgress = it
                    }
                    PreferenceSliderRow("LevelBar 2…10", levelProgress, 2f..10f, steps = 7) {
                        levelProgress = it
                    }
                    PreferenceSliderRow("CenterBasedBar · seamless", centerProgress, 0f..1f) {
                        centerProgress = it
                    }
                }
            }
            item {
                OneUiPreference(
                    title = "About Sample App",
                    summary = "Open AppInfo layout",
                    onClick = onOpenAbout,
                    trailing = {
                        org.oneui.compose.icons.OneUiIcon(
                            icon = OneUiIcons.Forward,
                            contentDescription = "Open About",
                        )
                    },
                )
            }
            item {
                OneUiRelatedLinksCard(
                    title = "Useful links",
                    links = listOf(
                        OneUiRelatedLink("Related link 1") { },
                        OneUiRelatedLink("Related link 2") { },
                        OneUiRelatedLink("Related link 3") { },
                    ),
                )
            }
        }

        PreferenceDialogHost(
            dialog = dialog,
            editValue = editValue,
            onEditValueChange = { editValue = it },
            listIndex = listIndex,
            onListIndexChange = { listIndex = it },
            multiValues = multiValues,
            onMultiValuesChange = { multiValues = it },
            color = color,
            onColorChange = { color = it },
            onDismiss = { dialog = null },
        )
    }
}

private enum class PreferenceDialog {
    EditText,
    List,
    Multi,
    Color,
}

private val preferenceItems = listOf("Item 1", "Item 2", "Item 3", "Item 4")

@Composable
private fun PreferenceSliderRow(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    mode: OneUiSliderMode = OneUiSliderMode.Standard,
    steps: Int = 0,
    onValueChange: (Float) -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(label, modifier = Modifier.weight(1f), color = OneUiTheme.colors.primaryText)
            Text(
                text = if (value % 1f == 0f) value.toInt().toString() else "%.2f".format(value),
                color = OneUiTheme.colors.secondaryText,
            )
        }
        OneUiSlider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            mode = mode,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun PreferenceDialogHost(
    dialog: PreferenceDialog?,
    editValue: String,
    onEditValueChange: (String) -> Unit,
    listIndex: Int,
    onListIndexChange: (Int) -> Unit,
    multiValues: Set<String>,
    onMultiValuesChange: (Set<String>) -> Unit,
    color: String,
    onColorChange: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    when (dialog) {
        PreferenceDialog.EditText -> AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("EditTextPreference") },
            text = {
                androidx.compose.material3.OutlinedTextField(
                    value = editValue,
                    onValueChange = onEditValueChange,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            confirmButton = { TextButton(onClick = onDismiss) { Text("Done") } },
            dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        )
        PreferenceDialog.List -> AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("ListPreference") },
            text = {
                Column {
                    preferenceItems.forEachIndexed { index, item ->
                        OneUiRadioItem(
                            title = item,
                            selected = index == listIndex,
                            onClick = {
                                onListIndexChange(index)
                                onDismiss()
                            },
                        )
                    }
                }
            },
            confirmButton = {},
        )
        PreferenceDialog.Multi -> AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("MultiSelectListPreference") },
            text = {
                Column {
                    preferenceItems.forEach { item ->
                        OneUiPreference(
                            title = item,
                            onClick = {
                                onMultiValuesChange(
                                    if (item in multiValues) multiValues - item else multiValues + item,
                                )
                            },
                            trailing = {
                                OneUiCheckbox(
                                    checked = item in multiValues,
                                    onCheckedChange = { checked ->
                                        onMultiValuesChange(
                                            if (checked) multiValues + item else multiValues - item,
                                        )
                                    },
                                )
                            },
                        )
                    }
                }
            },
            confirmButton = { TextButton(onClick = onDismiss) { Text("Done") } },
        )
        PreferenceDialog.Color -> AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("ColorPickerPreference") },
            text = {
                Column {
                    Text("Selected color", color = OneUiTheme.colors.secondaryText)
                    preferenceColors.forEach { value ->
                        OneUiRadioItem(
                            title = value,
                            selected = color == value,
                            onClick = {
                                onColorChange(value)
                                onDismiss()
                            },
                        )
                    }
                }
            },
            confirmButton = {},
        )
        null -> Unit
    }
}

private val preferenceColors = listOf("#0381FE", "#11A85F", "#F44336", "#9C27B0")
