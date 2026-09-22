package org.oneui.compose.components.menu

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.widgets.menu.MenuItem
import org.oneui.compose.widgets.menu.PopupMenu
import org.oneui.compose.widgets.menu.SelectableMenuItem

@Immutable
data class OneUiMenuItem(
    val id: String,
    val label: String,
    val icon: OneUiIcon? = null,
    val enabled: Boolean = true,
    val selected: Boolean = false,
)

/** Compose-native One UI popup menu with stable item identity and selection semantics. */
@Composable
fun OneUiMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<OneUiMenuItem>,
    onItemClick: (OneUiMenuItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!expanded) return

    PopupMenu(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
    ) {
        items.forEach { item ->
            if (item.selected || item.icon != null) {
                SelectableMenuItem(
                    label = item.label,
                    enabled = item.enabled,
                    selected = item.selected,
                    onSelect = {
                        onItemClick(item)
                        onDismissRequest()
                    },
                )
            } else {
                MenuItem(
                    label = item.label,
                    enabled = item.enabled,
                    onClick = {
                        onItemClick(item)
                        onDismissRequest()
                    },
                )
            }
        }
    }
}

/** A small reusable menu label row for callers that need icon-aware custom menu content. */
@Composable
fun OneUiMenuItemRow(
    item: OneUiMenuItem,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        item.icon?.let {
            OneUiIcon(icon = it, contentDescription = null)
            Spacer(Modifier.width(10.dp))
        }
        Text(item.label)
    }
}
