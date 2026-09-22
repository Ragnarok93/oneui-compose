package org.oneui.compose.demo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.oneui.compose.components.buttons.OneUiButton
import org.oneui.compose.components.buttons.OneUiButtonDefaults
import org.oneui.compose.components.buttons.OneUiFilledButton
import org.oneui.compose.components.buttons.OneUiFloatingActionBar
import org.oneui.compose.components.buttons.OneUiFloatingActionItem
import org.oneui.compose.components.buttons.OneUiOutlinedButton
import org.oneui.compose.components.buttons.OneUiProgressButton
import org.oneui.compose.components.buttons.OneUiTextButton
import org.oneui.compose.components.feedback.OneUiBottomTip
import org.oneui.compose.components.input.OneUiSpinner
import org.oneui.compose.components.list.OneUiListItem
import org.oneui.compose.components.list.OneUiRadioItem
import org.oneui.compose.components.list.OneUiSwitchItem
import org.oneui.compose.components.menu.OneUiMenu
import org.oneui.compose.components.menu.OneUiMenuItem
import org.oneui.compose.components.preference.OneUiPreferenceCategory
import org.oneui.compose.components.progress.OneUiLinearProgress
import org.oneui.compose.components.selection.OneUiCheckbox
import org.oneui.compose.components.selection.OneUiRadioButton
import org.oneui.compose.components.selection.OneUiSwitch
import org.oneui.compose.demo.CatalogSection
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.patterns.appbar.OneUiActionMode
import org.oneui.compose.patterns.appbar.OneUiActionModeAction
import org.oneui.compose.patterns.appbar.OneUiSearchMode
import org.oneui.compose.patterns.appbar.OneUiSearchModeBehavior
import org.oneui.compose.patterns.appbar.OneUiSuggestAppBar
import org.oneui.compose.theme.OneUiTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun WidgetsScreen(modifier: Modifier = Modifier) {
    var switchChecked by remember { mutableStateOf(true) }
    var checkboxChecked by remember { mutableStateOf(true) }
    var radioSelected by remember { mutableIntStateOf(0) }
    var actionIndex by remember { mutableIntStateOf(1) }
    var spinnerIndex by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(true) }
    var popupExpanded by remember { mutableStateOf(false) }
    var actionMode by remember { mutableStateOf(false) }
    var actionSelection by remember { mutableIntStateOf(0) }
    var switchBarEnabled by remember { mutableStateOf(false) }
    var switchBarProgress by remember { mutableStateOf(false) }
    var suggestionVisible by remember { mutableStateOf(true) }
    var sheetVisible by remember { mutableStateOf(false) }

    LaunchedEffect(switchBarProgress) {
        if (switchBarProgress) {
            delay(3_000L)
            switchBarProgress = false
        }
    }

    LazyColumn(
        modifier = modifier.testTag("catalog-misc-widgets"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            CatalogSection(
                title = "Compound buttons",
                subtitle = "Stable Compose-native SESL8 switch, checkbox and radio controls.",
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    OneUiSwitch(
                        checked = switchChecked,
                        onCheckedChange = { switchChecked = it },
                    )
                    OneUiCheckbox(
                        checked = checkboxChecked,
                        onCheckedChange = { checkboxChecked = it },
                    )
                    OneUiRadioButton(
                        selected = radioSelected == 0,
                        onClick = { radioSelected = 0 },
                    )
                    OneUiRadioButton(
                        selected = radioSelected == 1,
                        onClick = { radioSelected = 1 },
                    )
                }
            }
        }
        item {
            CatalogSection(
                title = "Buttons",
                subtitle = "Reference button families are being consolidated onto the shared OneUiButton primitive.",
                modifier = Modifier.testTag("widget-seven-button-styles"),
            ) {
                OneUiButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = OneUiButtonDefaults.neutralColors(),
                ) { Text("Default") }
                OneUiFilledButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Contained primary") }
                OneUiOutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Outline") }
                OneUiTextButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Transparent") }
                OneUiButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = OneUiButtonDefaults.neutralColors(),
                ) { Text("Contained") }
                OneUiButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = OneUiButtonDefaults.filledColors(),
                ) { Text("Contained primary") }
                OneUiButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = OneUiButtonDefaults.textColors(),
                ) { Text("Contained transparent") }
            }
        }
        item {
            CatalogSection(
                title = "Spinner",
                subtitle = "Four reference entries with keyboard and popup selection.",
            ) {
                androidx.compose.foundation.layout.Box(Modifier.testTag("widget-spinner")) {
                    OneUiSpinner(
                        selectedIndex = spinnerIndex,
                        entries = listOf("Item 1", "Item 2", "Item 3", "Item 4"),
                        onSelected = { spinnerIndex = it },
                    )
                }
            }
        }
        item {
            CatalogSection(
                title = "SearchView",
                subtitle = "Search mode with back, clear and keyboard-friendly input.",
            ) {
                androidx.compose.foundation.layout.Box(Modifier.testTag("widget-search-view")) {
                    OneUiSearchMode(
                        active = searchActive,
                        query = searchQuery,
                        onActiveChange = { searchActive = it },
                        onQueryChange = { searchQuery = it },
                        hint = "SearchView",
                        behavior = OneUiSearchModeBehavior.NoDismiss,
                    )
                    if (!searchActive) {
                        OneUiTextButton(onClick = { searchActive = true }) { Text("Open SearchView") }
                    }
                }
            }
        }
        item {
            CatalogSection(
                title = "Custom item views",
                subtitle = "Card, separate-switch, icon-switch and radio rows.",
            ) {
                androidx.compose.foundation.layout.Column(Modifier.testTag("widget-card-switch-radio-rows")) {
                    OneUiListItem(
                        title = "CardItemView",
                        summary = "Summary and end action",
                        leading = { org.oneui.compose.icons.OneUiIcon(OneUiIcons.Info, null) },
                        trailing = {
                            org.oneui.compose.icons.OneUiIconButton(
                                icon = OneUiIcons.Forward,
                                contentDescription = "Open card item",
                                onClick = {},
                            )
                        },
                        onClick = {},
                    )
                    OneUiSwitchItem(
                        title = "SwitchItemView",
                        checked = switchChecked,
                        onCheckedChange = { switchChecked = it },
                    )
                    OneUiRadioItem(
                        title = "RadioItemView 1",
                        selected = radioSelected == 0,
                        onClick = { radioSelected = 0 },
                    )
                    OneUiRadioItem(
                        title = "RadioItemView 2",
                        selected = radioSelected == 1,
                        onClick = { radioSelected = 1 },
                    )
                    OneUiRadioItem(
                        title = "RadioItemView 3",
                        selected = radioSelected == 2,
                        onClick = { radioSelected = 2 },
                    )
                }
            }
        }
        item {
            CatalogSection(title = "Relative links") {
                androidx.compose.foundation.layout.Column(Modifier.testTag("widget-relative-links")) {
                    listOf("Related link 1", "Related link 2", "Related link 3").forEach { label ->
                        OneUiTextButton(onClick = {}) { Text(label) }
                    }
                }
            }
        }
        item {
            CatalogSection(title = "Bottom tip") {
                OneUiBottomTip(
                    title = "Tip",
                    summary = "This is a sample bottom tip description.",
                    linkLabel = "More details",
                    onLinkClick = {},
                    modifier = Modifier.testTag("widget-bottom-tip"),
                )
            }
        }
        item {
            CatalogSection(
                title = "Switch bar and suggest app bar",
                subtitle = "Transient progress and dismissible suggestion state.",
            ) {
                androidx.compose.foundation.layout.Column(Modifier.testTag("widget-switch-bar-progress")) {
                    OneUiSwitchItem(
                        title = "Switch bar",
                        summary = if (switchBarEnabled) "On" else "Off",
                        checked = switchBarEnabled,
                        onCheckedChange = {
                            switchBarEnabled = it
                            switchBarProgress = true
                        },
                    )
                    if (switchBarProgress) {
                        OneUiLinearProgress(progress = null, modifier = Modifier.fillMaxWidth())
                    }
                }
                if (suggestionVisible) {
                    OneUiSuggestAppBar(
                        title = "This is a suggestion view",
                        actionLabel = "Action Button",
                        onAction = {},
                        onDismiss = { suggestionVisible = false },
                        modifier = Modifier.testTag("widget-suggest-appbar"),
                    )
                }
            }
        }
        item {
            CatalogSection(title = "Popup menu, action mode and feedback") {
                androidx.compose.foundation.layout.Box(Modifier.testTag("catalog-popup-menu")) {
                    OneUiButton(onClick = { popupExpanded = true }) { Text("Open pop-up menu") }
                    OneUiMenu(
                        expanded = popupExpanded,
                        onDismissRequest = { popupExpanded = false },
                        items = (1..4).map { index ->
                            OneUiMenuItem(
                                id = index.toString(),
                                label = "Pop-up menu item $index",
                                selected = index == 2,
                            )
                        },
                        onItemClick = { popupExpanded = false },
                    )
                }
                androidx.compose.foundation.layout.Box(Modifier.testTag("catalog-action-mode")) {
                    if (!actionMode) {
                        OneUiTextButton(onClick = { actionMode = true }) { Text("Enter action mode") }
                    } else {
                        OneUiActionMode(
                            selectedCount = actionSelection,
                            allSelected = actionSelection == 3,
                            onSelectAll = { actionSelection = if (actionSelection == 3) 0 else 3 },
                            onCancel = { actionMode = false; actionSelection = 0 },
                            actions = listOf(
                                OneUiActionModeAction("share", "Share", OneUiIcons.Share),
                                OneUiActionModeAction("delete", "Delete", OneUiIcons.Delete),
                                OneUiActionModeAction("message", "Message", OneUiIcons.Message),
                            ),
                            onAction = { actionMode = false },
                            showCancelButton = true,
                        )
                    }
                }
                androidx.compose.foundation.layout.Box(Modifier.testTag("catalog-search-mode")) {
                    Text(
                        text = if (searchQuery.isBlank()) "Search mode is ready" else "Searching for $searchQuery",
                        color = OneUiTheme.colors.secondaryText,
                    )
                }
                OneUiProgressButton(
                    label = "Progress button",
                    progress = if (switchBarProgress) null else 0.65f,
                    onClick = { switchBarProgress = true },
                )
                OneUiTextButton(onClick = { sheetVisible = true }) { Text("Open bottom sheet") }
            }
        }
        item {
            CatalogSection(
                title = "Floating action bar",
                subtitle = "Two-option One UI selected-surface motion.",
            ) {
                OneUiFloatingActionBar(
                    selectedIndex = actionIndex,
                    items = listOf(
                        OneUiFloatingActionItem("Action #1", OneUiIcons.ArrowUp),
                        OneUiFloatingActionItem("Action #2", OneUiIcons.ArrowDown),
                    ),
                    onSelected = { actionIndex = it },
                )
            }
        }
    }

    if (sheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { sheetVisible = false },
            containerColor = OneUiTheme.colors.surface,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
            ) {
                Text("Bottom sheet", color = OneUiTheme.colors.primaryText, fontWeight = FontWeight.Bold)
                Text(
                    "Scrollable One UI sheet content",
                    modifier = Modifier.padding(top = 12.dp, bottom = 220.dp),
                    color = OneUiTheme.colors.secondaryText,
                )
            }
        }
    }
}
