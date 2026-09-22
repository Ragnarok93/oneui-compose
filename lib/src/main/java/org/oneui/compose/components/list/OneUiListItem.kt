package org.oneui.compose.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.selection.OneUiRadioButton
import org.oneui.compose.components.selection.OneUiSwitch
import org.oneui.compose.interaction.oneUiInteractive
import org.oneui.compose.theme.OneUiTheme

/** Reusable Compose-native card/list row matching the reference custom item views. */
@Composable
fun OneUiListItem(
    title: String,
    modifier: Modifier = Modifier,
    summary: String? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val interactive = if (onClick == null) {
        modifier
    } else {
        modifier.oneUiInteractive(
            enabled = enabled,
            onClick = onClick,
            interactionSource = interactionSource,
            role = Role.Button,
            shape = OneUiTheme.shapes.nestedCard,
            pressedScale = 0.985f,
        )
    }
    Row(
        modifier = interactive
            .defaultMinSize(minHeight = OneUiTheme.sizes.listItemMinHeight)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        leading?.invoke()
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = OneUiTheme.colors.primaryText.copy(alpha = if (enabled) 1f else 0.45f),
                style = OneUiTheme.typography.listTitle,
            )
            if (!summary.isNullOrBlank()) {
                Text(
                    text = summary,
                    color = OneUiTheme.colors.secondaryText.copy(alpha = if (enabled) 1f else 0.45f),
                    style = OneUiTheme.typography.listSummary,
                )
            }
        }
        trailing?.invoke(this)
    }
}

@Composable
fun OneUiSwitchItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    summary: String? = null,
    enabled: Boolean = true,
) = OneUiListItem(
    title = title,
    summary = summary,
    modifier = modifier,
    enabled = enabled,
    trailing = {
        OneUiSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
        )
    },
    onClick = { onCheckedChange(!checked) },
)

@Composable
fun OneUiRadioItem(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    summary: String? = null,
    enabled: Boolean = true,
) = OneUiListItem(
    title = title,
    summary = summary,
    modifier = modifier,
    enabled = enabled,
    trailing = {
        OneUiRadioButton(
            selected = selected,
            onClick = onClick,
            enabled = enabled,
        )
    },
    onClick = onClick,
)
