package org.oneui.compose.components.input

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.buttons.OneUiButton
import org.oneui.compose.components.buttons.OneUiButtonDefaults
import org.oneui.compose.components.menu.OneUiMenu
import org.oneui.compose.components.menu.OneUiMenuItem
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIcons

/** Spinner contract used by the reference widget and preference surfaces. */
@Composable
fun OneUiSpinner(
    selectedIndex: Int,
    entries: List<String>,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String = "Choose item",
) {
    require(entries.isNotEmpty()) { "OneUiSpinner requires at least one entry" }
    val safeIndex = selectedIndex.coerceIn(entries.indices)
    var expanded by remember { mutableStateOf(false) }

    androidx.compose.foundation.layout.Box(modifier) {
        OneUiButton(
            onClick = { expanded = true },
            enabled = enabled,
            modifier = Modifier
                .testTag("oneui-spinner")
                .then(Modifier),
            colors = OneUiButtonDefaults.neutralColors(),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(entries[safeIndex])
                Spacer(Modifier.width(8.dp))
                org.oneui.compose.icons.OneUiIcon(
                    icon = OneUiIcons.ChevronDown,
                    contentDescription = contentDescription,
                    modifier = Modifier.width(18.dp),
                )
            }
        }
        OneUiMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            items = entries.mapIndexed { index, label ->
                OneUiMenuItem(
                    id = index.toString(),
                    label = label,
                    selected = index == safeIndex,
                )
            },
            onItemClick = { item ->
                onSelected(item.id.toInt())
                expanded = false
            },
        )
    }
}

