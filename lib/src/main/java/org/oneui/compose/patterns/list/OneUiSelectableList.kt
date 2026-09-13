package org.oneui.compose.patterns.list

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.list.OneUiFastScrollerDisplayMode
import org.oneui.compose.components.list.OneUiIndexedList
import org.oneui.compose.components.selection.OneUiCheckbox
import org.oneui.compose.theme.OneUiTheme

/**
 * Reusable One UI selectable list pattern.
 *
 * A long-press enters selection mode by selecting the pressed item. While selection mode is active,
 * taps toggle selection instead of invoking [onItemClick]. The caller owns [state], so selection can
 * survive recomposition, filtering, or route-level state restoration.
 *
 * Supplying [indexLabel] enables the same reusable indexed-list/fast-scroller engine used by other
 * One UI collections instead of maintaining a separate selectable-list scrolling implementation.
 */
@Composable
fun <T, K : Any> OneUiSelectableList(
    items: List<T>,
    state: OneUiSelectableListState<K>,
    key: (T) -> K,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    indexLabel: ((T) -> String)? = null,
    fastScrollerDisplayMode: OneUiFastScrollerDisplayMode = OneUiFastScrollerDisplayMode.Text,
    fastScrollerAutoHide: Boolean = false,
    itemContent: @Composable (item: T, selected: Boolean, selectionMode: Boolean) -> Unit,
) {
    OneUiIndexedList(
        items = items,
        key = key,
        label = indexLabel ?: { "" },
        modifier = modifier,
        showFastScroller = indexLabel != null,
        fastScrollerDisplayMode = fastScrollerDisplayMode,
        fastScrollerAutoHide = fastScrollerAutoHide,
        contentPadding = contentPadding,
    ) { item ->
        val itemKey = key(item)
        val isSelected = state.isSelected(itemKey)
        val selectionMode = state.isSelectionMode

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        if (state.isSelectionMode) {
                            state.toggle(itemKey)
                        } else {
                            onItemClick(item)
                        }
                    },
                    onLongClick = { state.toggle(itemKey) },
                )
                .semantics(mergeDescendants = true) {
                    selected = isSelected
                    role = Role.Button
                },
        ) {
            itemContent(item, isSelected, selectionMode)
        }
    }
}

/**
 * Standard profile/list row used by selectable One UI list patterns.
 *
 * The selection affordance is inserted before the leading content while action mode is active,
 * mirroring the reference SelectableLinearLayout behavior without coupling callers to RecyclerView.
 */
@Composable
fun OneUiSelectableListItem(
    title: String,
    subtitle: String? = null,
    selected: Boolean,
    selectionMode: Boolean,
    modifier: Modifier = Modifier,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = OneUiTheme.sizes.listItemMinHeight)
            .semantics(mergeDescendants = true) {
                this.selected = selected
            }
            .padding(
                horizontal = OneUiTheme.spacing.listHorizontal,
                vertical = 8.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (selectionMode) {
            OneUiCheckbox(
                checked = selected,
                onCheckedChange = null,
            )
            Spacer(Modifier.width(4.dp))
        }

        if (leadingContent != null) {
            leadingContent()
            Spacer(Modifier.width(14.dp))
        }

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                style = OneUiTheme.typography.listTitle,
                color = OneUiTheme.colors.primaryText,
            )
            if (!subtitle.isNullOrBlank()) {
                Text(
                    text = subtitle,
                    style = OneUiTheme.typography.listSummary,
                    color = OneUiTheme.colors.secondaryText,
                )
            }
        }

        if (trailingContent != null) {
            Spacer(Modifier.width(12.dp))
            trailingContent()
        }
    }
}
