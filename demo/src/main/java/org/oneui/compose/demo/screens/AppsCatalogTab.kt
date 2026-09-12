package org.oneui.compose.demo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.components.buttons.OneUiButton
import org.oneui.compose.components.buttons.OneUiButtonDefaults
import org.oneui.compose.components.buttons.OneUiTextButton
import org.oneui.compose.components.selection.OneUiCheckbox
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.patterns.apppicker.OneUiAppPicker
import org.oneui.compose.patterns.apppicker.OneUiAppPickerListType
import org.oneui.compose.patterns.apppicker.OneUiAppPickerSelectionControl
import org.oneui.compose.patterns.apppicker.OneUiAppPickerState
import org.oneui.compose.theme.OneUiTheme

data class CatalogApp(
    val id: String,
    val label: String,
    val packageName: String,
)

/** Stable fixtures keep the app-picker catalog deterministic and available without package access. */
val AppsCatalogSamples: List<CatalogApp> = listOf(
    CatalogApp("calendar", "Calendar", "com.example.calendar"),
    CatalogApp("camera", "Camera", "com.example.camera"),
    CatalogApp("clock", "Clock", "com.example.clock"),
    CatalogApp("contacts", "Contacts", "com.example.contacts"),
    CatalogApp("gallery", "Gallery", "com.example.gallery"),
    CatalogApp("messages", "Messages", "com.example.messages"),
    CatalogApp("notes", "Notes", "com.example.notes"),
    CatalogApp("settings", "Settings", "com.example.settings"),
)

@Composable
fun AppsCatalogTab(modifier: Modifier = Modifier) {
    val pickerState = remember { OneUiAppPickerState<String>() }
    var query by rememberSaveable { mutableStateOf("") }
    var listTypeName by rememberSaveable { mutableStateOf(OneUiAppPickerListType.List.name) }
    var selectLayoutMode by rememberSaveable { mutableStateOf(false) }
    var typeMenuExpanded by remember { mutableStateOf(false) }
    var feedback by remember { mutableStateOf<String?>(null) }

    val listType = OneUiAppPickerListType.entries.firstOrNull { it.name == listTypeName }
        ?: OneUiAppPickerListType.List
    val effectiveListType = if (selectLayoutMode) {
        OneUiAppPickerListType.ListCheckbox
    } else {
        listType
    }
    val visibleApps = remember(query) {
        val needle = query.trim()
        if (needle.isEmpty()) {
            AppsCatalogSamples
        } else {
            AppsCatalogSamples.filter { app ->
                app.label.contains(needle, ignoreCase = true) ||
                    app.packageName.contains(needle, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag(RecyclerViewCatalogTab.Apps.testTag),
    ) {
        AppsSearchField(
            query = query,
            onQueryChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (!selectLayoutMode) {
                Box {
                    OneUiButton(
                        onClick = { typeMenuExpanded = true },
                        modifier = Modifier.semantics {
                            contentDescription = "Change app picker type"
                        },
                        colors = OneUiButtonDefaults.neutralColors(),
                    ) {
                        Text(listType.label)
                        Spacer(Modifier.width(8.dp))
                        OneUiIcon(
                            icon = OneUiIcons.ChevronDown,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = OneUiTheme.colors.secondaryText,
                        )
                    }
                    DropdownMenu(
                        expanded = typeMenuExpanded,
                        onDismissRequest = { typeMenuExpanded = false },
                    ) {
                        OneUiAppPickerListType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = type.label,
                                        color = OneUiTheme.colors.primaryText,
                                    )
                                },
                                onClick = {
                                    listTypeName = type.name
                                    typeMenuExpanded = false
                                    pickerState.clear()
                                    feedback = null
                                },
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))
            OneUiTextButton(
                onClick = {
                    selectLayoutMode = !selectLayoutMode
                    pickerState.clear()
                    feedback = null
                },
            ) {
                Text(if (selectLayoutMode) "Single picker mode" else "Select layout mode")
            }
        }

        if (selectLayoutMode) {
            SelectedAppsPanel(
                selectedApps = AppsCatalogSamples.filter { pickerState.isSelected(it.id) },
                onClear = pickerState::clear,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
            )
        }

        if (effectiveListType.supportsSelectAll()) {
            SelectAllAppsRow(
                allApps = visibleApps,
                pickerState = pickerState,
            )
        }

        feedback?.let { message ->
            Text(
                text = message,
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 4.dp),
                color = OneUiTheme.colors.secondaryText,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        OneUiAppPicker(
            items = visibleApps,
            listType = effectiveListType,
            state = pickerState,
            key = CatalogApp::id,
            label = CatalogApp::label,
            subLabel = CatalogApp::packageName,
            modifier = Modifier
                .weight(1f)
                .testTag(if (effectiveListType.isGrid) "apps-grid" else "apps-list"),
            showIndexRail = true,
            onItemClick = { app -> feedback = "${app.label} clicked" },
            onActionClick = { app -> feedback = "${app.label} action clicked" },
            leadingContent = { app -> CatalogAppGlyph(app) },
        )
    }
}

@Composable
private fun AppsSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.testTag("apps-search-field"),
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        placeholder = { Text("Search app", color = OneUiTheme.colors.secondaryText) },
        leadingIcon = {
            OneUiIcon(
                icon = OneUiIcons.Search,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = OneUiTheme.colors.secondaryText,
            )
        },
        trailingIcon = if (query.isBlank()) null else {
            {
                OneUiIconButton(
                    icon = OneUiIcons.Close,
                    contentDescription = "Clear app search",
                    onClick = { onQueryChange("") },
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = OneUiTheme.colors.surfaceElevated,
            unfocusedContainerColor = OneUiTheme.colors.surfaceElevated,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = OneUiTheme.colors.accent,
            focusedTextColor = OneUiTheme.colors.primaryText,
            unfocusedTextColor = OneUiTheme.colors.primaryText,
        ),
    )
}

@Composable
private fun SelectedAppsPanel(
    selectedApps: List<CatalogApp>,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .testTag("apps-selected-panel")
            .clip(RoundedCornerShape(24.dp))
            .background(OneUiTheme.colors.surfaceElevated)
            .padding(horizontal = 18.dp, vertical = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${selectedApps.size} selected",
                modifier = Modifier.weight(1f),
                color = OneUiTheme.colors.primaryText,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
            if (selectedApps.isNotEmpty()) {
                OneUiTextButton(onClick = onClear) {
                    Text("Clear")
                }
            }
        }
        Text(
            text = if (selectedApps.isEmpty()) {
                "Choose apps from the list below."
            } else {
                selectedApps.joinToString(separator = " • ", transform = CatalogApp::label)
            },
            color = OneUiTheme.colors.secondaryText,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun SelectAllAppsRow(
    allApps: List<CatalogApp>,
    pickerState: OneUiAppPickerState<String>,
) {
    val keys = allApps.map(CatalogApp::id)
    val allSelected = pickerState.allSelected(keys)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (allSelected) {
                    keys.forEach { key -> pickerState.setSelected(key, selected = false) }
                } else {
                    keys.forEach { key -> pickerState.setSelected(key, selected = true) }
                }
            }
            .padding(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 4.dp)
            .heightIn(min = 48.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OneUiCheckbox(
            checked = allSelected,
            onCheckedChange = { checked ->
                keys.forEach { key -> pickerState.setSelected(key, selected = checked) }
            },
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = "All apps",
            modifier = Modifier.weight(1f),
            color = OneUiTheme.colors.primaryText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = "${pickerState.selectedCount}/${AppsCatalogSamples.size}",
            color = OneUiTheme.colors.secondaryText,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun CatalogAppGlyph(app: CatalogApp) {
    Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(OneUiTheme.colors.accent.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = app.label.firstOrNull()?.uppercaseChar()?.toString().orEmpty(),
            color = OneUiTheme.colors.accent,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

private fun OneUiAppPickerListType.supportsSelectAll(): Boolean =
    selectionControl == OneUiAppPickerSelectionControl.Checkbox ||
        selectionControl == OneUiAppPickerSelectionControl.Switch
