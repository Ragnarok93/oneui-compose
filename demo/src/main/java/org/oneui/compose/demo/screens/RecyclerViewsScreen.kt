package org.oneui.compose.demo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.components.buttons.OneUiTextButton
import org.oneui.compose.icons.OneUiAnimatedIcons
import org.oneui.compose.icons.OneUiDrawableCatalog
import org.oneui.compose.icons.OneUiDrawableEntry
import org.oneui.compose.icons.OneUiDrawableRendering
import org.oneui.compose.icons.OneUiDrawableSource
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.theme.OneUiTheme

enum class RecyclerViewCatalogTab(
    val label: String,
    val testTag: String,
) {
    Icons("Icons", "catalog-icons"),
    Stargazers("Stargazers", "catalog-stargazers"),
    Apps("Apps", "catalog-apps"),
}

@Composable
fun RecyclerViewsScreen(modifier: Modifier = Modifier) {
    var selectedName by rememberSaveable { mutableStateOf(RecyclerViewCatalogTab.Icons.name) }
    val selectedTab = RecyclerViewCatalogTab.entries.firstOrNull { it.name == selectedName }
        ?: RecyclerViewCatalogTab.Icons

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("catalog-recycler-views"),
    ) {
        RecyclerViewTabs(
            selected = selectedTab,
            onSelected = { selectedName = it.name },
        )

        when (selectedTab) {
            RecyclerViewCatalogTab.Icons -> IconsCatalogTab(Modifier.weight(1f))
            RecyclerViewCatalogTab.Stargazers -> NestedRecyclerPlaceholder(
                tab = selectedTab,
                description = "Profile rows, QR/share actions, multi-selection and reference list behavior.",
                modifier = Modifier.weight(1f),
            )
            RecyclerViewCatalogTab.Apps -> NestedRecyclerPlaceholder(
                tab = selectedTab,
                description = "Seven app-picker list types, filtering, selection and app metadata presentation.",
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun RecyclerViewTabs(
    selected: RecyclerViewCatalogTab,
    onSelected: (RecyclerViewCatalogTab) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        RecyclerViewCatalogTab.entries.forEach { tab ->
            val active = tab == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(18.dp))
                    .combinedClickable(onClick = { onSelected(tab) })
                    .semantics { this.selected = active },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = tab.label,
                    color = if (active) OneUiTheme.colors.accent else OneUiTheme.colors.secondaryText,
                    fontSize = 14.sp,
                    fontWeight = if (active) FontWeight.SemiBold else FontWeight.Medium,
                )
                if (active) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 4.dp)
                            .width(28.dp)
                            .height(3.dp)
                            .clip(CircleShape)
                            .background(OneUiTheme.colors.accent),
                    )
                }
            }
        }
    }
    HorizontalDivider(color = OneUiTheme.colors.divider)
}

@Composable
private fun IconsCatalogTab(modifier: Modifier = Modifier) {
    val allIcons = remember {
        OneUiDrawableCatalog.entries.filter { entry ->
            entry.source == OneUiDrawableSource.IconsDependency &&
                entry.rendering is OneUiDrawableRendering.Icon
        }
    }
    var query by rememberSaveable { mutableStateOf("") }
    var selectedNames by remember { mutableStateOf<Set<String>>(emptySet()) }
    val actionMode = selectedNames.isNotEmpty()
    val visibleIcons = remember(query) {
        OneUiDrawableCatalog.search(query).filter { entry ->
            entry.source == OneUiDrawableSource.IconsDependency &&
                entry.rendering is OneUiDrawableRendering.Icon
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag(RecyclerViewCatalogTab.Icons.testTag),
    ) {
        IconsSearchField(
            query = query,
            onQueryChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
        )

        if (actionMode) {
            IconsActionModeBar(
                selectedCount = selectedNames.size,
                allSelected = selectedNames.size == allIcons.size,
                onSelectAll = {
                    selectedNames = if (selectedNames.size == allIcons.size) {
                        emptySet()
                    } else {
                        allIcons.mapTo(linkedSetOf(), OneUiDrawableEntry::name)
                    }
                },
            )
        }

        if (visibleIcons.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (query.isBlank()) "No icons" else "No results found.",
                    color = OneUiTheme.colors.secondaryText,
                    fontSize = 15.sp,
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    items = visibleIcons,
                    key = OneUiDrawableEntry::name,
                ) { entry ->
                    IconCatalogRow(
                        entry = entry,
                        selected = entry.name in selectedNames,
                        selectionMode = actionMode,
                        onClick = {
                            if (actionMode) selectedNames = selectedNames.toggled(entry.name)
                        },
                        onLongClick = {
                            selectedNames = selectedNames.toggled(entry.name)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun IconsSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.testTag("icons-search-field"),
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        placeholder = {
            Text("Search icon", color = OneUiTheme.colors.secondaryText)
        },
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
                    contentDescription = "Clear icon search",
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
private fun IconsActionModeBar(
    selectedCount: Int,
    allSelected: Boolean,
    onSelectAll: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OneUiAnimatedIcons.CheckMorph(
            checked = true,
            modifier = Modifier.size(24.dp),
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = "$selectedCount selected",
            modifier = Modifier.weight(1f),
            color = OneUiTheme.colors.primaryText,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
        )
        OneUiTextButton(onClick = onSelectAll) {
            Text(if (allSelected) "Clear all" else "Select all")
        }
    }
}

@Composable
private fun IconCatalogRow(
    entry: OneUiDrawableEntry,
    selected: Boolean,
    selectionMode: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    val rendering = entry.rendering as OneUiDrawableRendering.Icon
    val rowBackground = if (selected) OneUiTheme.colors.accent.copy(alpha = 0.10f) else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(rowBackground)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .semantics { this.selected = selected }
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(OneUiTheme.colors.surfaceElevated),
            contentAlignment = Alignment.Center,
        ) {
            OneUiIcon(
                icon = rendering.icon,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = OneUiTheme.colors.primaryText,
            )
        }
        Spacer(Modifier.width(14.dp))
        Text(
            text = entry.name,
            modifier = Modifier.weight(1f),
            color = OneUiTheme.colors.primaryText,
            fontSize = 14.sp,
            lineHeight = 19.sp,
        )
        if (selectionMode) {
            Spacer(Modifier.width(12.dp))
            OneUiAnimatedIcons.CheckMorph(
                checked = selected,
                modifier = Modifier.size(24.dp),
            )
        }
    }
    HorizontalDivider(
        modifier = Modifier.padding(start = 82.dp),
        color = OneUiTheme.colors.divider,
    )
}

@Composable
private fun NestedRecyclerPlaceholder(
    tab: RecyclerViewCatalogTab,
    description: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag(tab.testTag)
            .padding(28.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = tab.label,
                color = OneUiTheme.colors.primaryText,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
            )
            Text(
                text = description,
                color = OneUiTheme.colors.secondaryText,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            )
        }
    }
}

private fun Set<String>.toggled(name: String): Set<String> =
    if (name in this) this - name else this + name
