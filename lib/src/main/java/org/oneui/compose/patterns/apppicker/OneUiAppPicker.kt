package org.oneui.compose.patterns.apppicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.oneui.compose.components.selection.OneUiCheckbox
import org.oneui.compose.components.selection.OneUiRadioButton
import org.oneui.compose.components.selection.OneUiSwitch
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.theme.OneUiTheme

/** Selection/action affordance used by a reference app-picker row or grid tile. */
enum class OneUiAppPickerSelectionControl {
    None,
    Action,
    Checkbox,
    Radio,
    Switch,
}

/**
 * The seven list types exposed by the pinned One UI sample app picker.
 *
 * Labels intentionally match the sample switcher so a catalog or application can expose the same
 * modes without translating between a second enum.
 */
enum class OneUiAppPickerListType(
    val label: String,
    val isGrid: Boolean,
    val selectionControl: OneUiAppPickerSelectionControl,
) {
    List("List", false, OneUiAppPickerSelectionControl.None),
    ListActionButton("List + action button", false, OneUiAppPickerSelectionControl.Action),
    ListCheckbox("List + checkbox", false, OneUiAppPickerSelectionControl.Checkbox),
    ListRadio("List + radio button", false, OneUiAppPickerSelectionControl.Radio),
    ListSwitch("List + switch", false, OneUiAppPickerSelectionControl.Switch),
    Grid("Grid", true, OneUiAppPickerSelectionControl.None),
    GridCheckbox("Grid + checkbox", true, OneUiAppPickerSelectionControl.Checkbox),
}

/** Hoisted selection state shared by list, grid, and select-layout app picker modes. */
@Stable
class OneUiAppPickerState<K : Any>(
    initialSelectedKeys: Set<K> = emptySet(),
) {
    private var selectedKeysState by mutableStateOf(initialSelectedKeys.toSet())

    val selectedKeys: Set<K>
        get() = selectedKeysState

    val selectedCount: Int
        get() = selectedKeysState.size

    fun isSelected(key: K): Boolean = key in selectedKeysState

    fun setSelected(
        key: K,
        selected: Boolean,
        exclusive: Boolean = false,
    ) {
        selectedKeysState = when {
            selected && exclusive -> setOf(key)
            selected -> selectedKeysState + key
            else -> selectedKeysState - key
        }
    }

    fun toggle(
        key: K,
        exclusive: Boolean = false,
    ) {
        setSelected(
            key = key,
            selected = !isSelected(key),
            exclusive = exclusive,
        )
    }

    fun selectAll(keys: Iterable<K>) {
        selectedKeysState = keys.toSet()
    }

    fun allSelected(keys: Iterable<K>): Boolean {
        val requested = keys.toSet()
        return requested.isNotEmpty() && selectedKeysState.containsAll(requested)
    }

    fun clear() {
        selectedKeysState = emptySet()
    }
}

@Composable
fun <K : Any> rememberOneUiAppPickerState(
    initialSelectedKeys: Set<K> = emptySet(),
): OneUiAppPickerState<K> = remember(initialSelectedKeys) {
    OneUiAppPickerState(initialSelectedKeys)
}

/**
 * Compose-native app picker supporting the seven list/grid presentations in the SESL8 sample.
 *
 * Data and app metadata remain caller-owned. [state] holds only selection; search/filtering is also
 * deliberately hoisted so applications can use package-manager data, remote sources, or fixed
 * fixtures. List modes include an alphabet rail that behaves as a compact fast-scroll/index tip.
 */
@Composable
fun <T, K : Any> OneUiAppPicker(
    items: List<T>,
    listType: OneUiAppPickerListType,
    state: OneUiAppPickerState<K>,
    key: (T) -> K,
    label: (T) -> String,
    modifier: Modifier = Modifier,
    subLabel: (T) -> String? = { null },
    loading: Boolean = false,
    showIndexRail: Boolean = true,
    onItemClick: (T) -> Unit = {},
    onActionClick: (T) -> Unit = {},
    leadingContent: (@Composable (T) -> Unit)? = null,
    emptyContent: @Composable () -> Unit = {
        Text(
            text = "No apps found.",
            color = OneUiTheme.colors.secondaryText,
            fontSize = 15.sp,
        )
    },
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when {
            loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(44.dp),
                    color = OneUiTheme.colors.accent,
                )
            }

            items.isEmpty() -> emptyContent()

            listType.isGrid -> AppPickerGrid(
                items = items,
                listType = listType,
                state = state,
                key = key,
                label = label,
                subLabel = subLabel,
                leadingContent = leadingContent,
                showIndexRail = showIndexRail,
                onItemClick = onItemClick,
            )

            else -> AppPickerList(
                items = items,
                listType = listType,
                state = state,
                key = key,
                label = label,
                subLabel = subLabel,
                leadingContent = leadingContent,
                showIndexRail = showIndexRail,
                onItemClick = onItemClick,
                onActionClick = onActionClick,
            )
        }
    }
}

@Composable
private fun <T, K : Any> AppPickerList(
    items: List<T>,
    listType: OneUiAppPickerListType,
    state: OneUiAppPickerState<K>,
    key: (T) -> K,
    label: (T) -> String,
    subLabel: (T) -> String?,
    leadingContent: (@Composable (T) -> Unit)?,
    showIndexRail: Boolean,
    onItemClick: (T) -> Unit,
    onActionClick: (T) -> Unit,
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val indexEntries = appPickerIndexEntries(items, label)

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                end = if (showIndexRail && indexEntries.size > 1) 28.dp else 0.dp,
                bottom = 12.dp,
            ),
        ) {
            lazyItems(
                items = items,
                key = { item -> key(item) },
            ) { item ->
                AppPickerListRow(
                    item = item,
                    listType = listType,
                    state = state,
                    itemKey = key(item),
                    label = label(item),
                    subLabel = subLabel(item),
                    leadingContent = leadingContent,
                    onItemClick = { activateAppPickerItem(item, listType, state, key, onItemClick) },
                    onActionClick = { onActionClick(item) },
                )
            }
        }

        if (showIndexRail && indexEntries.size > 1) {
            AppPickerIndexRail(
                entries = indexEntries,
                modifier = Modifier.align(Alignment.CenterEnd),
                onIndexSelected = { index ->
                    scope.launch { listState.animateScrollToItem(index) }
                },
            )
        }
    }
}

@Composable
private fun <T, K : Any> AppPickerGrid(
    items: List<T>,
    listType: OneUiAppPickerListType,
    state: OneUiAppPickerState<K>,
    key: (T) -> K,
    label: (T) -> String,
    subLabel: (T) -> String?,
    leadingContent: (@Composable (T) -> Unit)?,
    showIndexRail: Boolean,
    onItemClick: (T) -> Unit,
) {
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()
    val indexEntries = appPickerIndexEntries(items, label)

    Box(Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 132.dp),
            state = gridState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 12.dp,
                top = 8.dp,
                end = if (showIndexRail && indexEntries.size > 1) 32.dp else 12.dp,
                bottom = 16.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            gridItems(
                items = items,
                key = { item -> key(item) },
            ) { item ->
                AppPickerGridTile(
                    item = item,
                    listType = listType,
                    state = state,
                    itemKey = key(item),
                    label = label(item),
                    subLabel = subLabel(item),
                    leadingContent = leadingContent,
                    onClick = { activateAppPickerItem(item, listType, state, key, onItemClick) },
                )
            }
        }

        if (showIndexRail && indexEntries.size > 1) {
            AppPickerIndexRail(
                entries = indexEntries,
                modifier = Modifier.align(Alignment.CenterEnd),
                onIndexSelected = { index ->
                    scope.launch { gridState.animateScrollToItem(index) }
                },
            )
        }
    }
}

@Composable
private fun <T, K : Any> AppPickerListRow(
    item: T,
    listType: OneUiAppPickerListType,
    state: OneUiAppPickerState<K>,
    itemKey: K,
    label: String,
    subLabel: String?,
    leadingContent: (@Composable (T) -> Unit)?,
    onItemClick: () -> Unit,
    onActionClick: () -> Unit,
) {
    val selected = state.isSelected(itemKey)
    val selectionMode = listType.selectionControl in setOf(
        OneUiAppPickerSelectionControl.Checkbox,
        OneUiAppPickerSelectionControl.Radio,
        OneUiAppPickerSelectionControl.Switch,
    )
    val background = if (selected && selectionMode) {
        OneUiTheme.colors.accent.copy(alpha = 0.08f)
    } else {
        Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .clickable(onClick = onItemClick)
            .then(
                if (selectionMode) Modifier.semantics { this.selected = selected } else Modifier,
            )
            .padding(start = 20.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
            .heightIn(min = 64.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (leadingContent != null) {
                leadingContent(item)
            } else {
                DefaultAppGlyph(label = label, size = 44)
            }
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = OneUiTheme.colors.primaryText,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (!subLabel.isNullOrBlank()) {
                Text(
                    text = subLabel,
                    modifier = Modifier.padding(top = 2.dp),
                    color = OneUiTheme.colors.secondaryText,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Spacer(Modifier.width(8.dp))
        when (listType.selectionControl) {
            OneUiAppPickerSelectionControl.None -> Unit
            OneUiAppPickerSelectionControl.Action -> OneUiIconButton(
                icon = OneUiIcons.Settings,
                contentDescription = "$label action",
                onClick = onActionClick,
            )
            OneUiAppPickerSelectionControl.Checkbox -> OneUiCheckbox(
                checked = selected,
                onCheckedChange = { checked -> state.setSelected(itemKey, checked) },
            )
            OneUiAppPickerSelectionControl.Radio -> OneUiRadioButton(
                selected = selected,
                onClick = { state.setSelected(itemKey, selected = true, exclusive = true) },
            )
            OneUiAppPickerSelectionControl.Switch -> OneUiSwitch(
                checked = selected,
                onCheckedChange = { checked -> state.setSelected(itemKey, checked) },
            )
        }
    }
}

@Composable
private fun <T, K : Any> AppPickerGridTile(
    item: T,
    listType: OneUiAppPickerListType,
    state: OneUiAppPickerState<K>,
    itemKey: K,
    label: String,
    subLabel: String?,
    leadingContent: (@Composable (T) -> Unit)?,
    onClick: () -> Unit,
) {
    val selected = state.isSelected(itemKey)
    val shape = RoundedCornerShape(24.dp)
    val container = if (selected) {
        OneUiTheme.colors.accent.copy(alpha = 0.10f)
    } else {
        OneUiTheme.colors.surfaceElevated
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 150.dp)
            .clip(shape)
            .background(container)
            .clickable(onClick = onClick)
            .semantics { this.selected = selected }
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter,
        ) {
            if (leadingContent != null) {
                Box(
                    modifier = Modifier.size(64.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    leadingContent(item)
                }
            } else {
                DefaultAppGlyph(label = label, size = 58)
            }

            if (listType.selectionControl == OneUiAppPickerSelectionControl.Checkbox) {
                Box(modifier = Modifier.align(Alignment.TopEnd)) {
                    OneUiCheckbox(
                        checked = selected,
                        onCheckedChange = { checked -> state.setSelected(itemKey, checked) },
                    )
                }
            }
        }
        Text(
            text = label,
            modifier = Modifier.padding(top = 10.dp),
            color = OneUiTheme.colors.primaryText,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        if (!subLabel.isNullOrBlank()) {
            Text(
                text = subLabel,
                modifier = Modifier.padding(top = 3.dp),
                color = OneUiTheme.colors.secondaryText,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun DefaultAppGlyph(
    label: String,
    size: Int,
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape((size * 0.28f).dp))
            .background(OneUiTheme.colors.accent.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label.firstOrNull()?.uppercaseChar()?.toString().orEmpty(),
            color = OneUiTheme.colors.accent,
            fontSize = if (size >= 56) 22.sp else 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

private data class AppPickerIndexEntry(
    val label: String,
    val itemIndex: Int,
)

private fun <T> appPickerIndexEntries(
    items: List<T>,
    label: (T) -> String,
): List<AppPickerIndexEntry> {
    val seen = linkedSetOf<String>()
    return buildList {
        items.forEachIndexed { index, item ->
            val value = label(item).trim().firstOrNull()?.uppercaseChar()?.toString() ?: "#"
            if (seen.add(value)) add(AppPickerIndexEntry(value, index))
        }
    }
}

@Composable
private fun AppPickerIndexRail(
    entries: List<AppPickerIndexEntry>,
    onIndexSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(28.dp)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        entries.forEach { entry ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable { onIndexSelected(entry.itemIndex) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = entry.label,
                    color = OneUiTheme.colors.accent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

private fun <T, K : Any> activateAppPickerItem(
    item: T,
    listType: OneUiAppPickerListType,
    state: OneUiAppPickerState<K>,
    key: (T) -> K,
    onItemClick: (T) -> Unit,
) {
    val itemKey = key(item)
    when (listType.selectionControl) {
        OneUiAppPickerSelectionControl.Checkbox,
        OneUiAppPickerSelectionControl.Switch -> state.toggle(itemKey)
        OneUiAppPickerSelectionControl.Radio -> state.setSelected(
            itemKey,
            selected = true,
            exclusive = true,
        )
        OneUiAppPickerSelectionControl.None,
        OneUiAppPickerSelectionControl.Action -> onItemClick(item)
    }
}
