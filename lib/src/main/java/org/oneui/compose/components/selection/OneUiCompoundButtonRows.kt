package org.oneui.compose.components.selection

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import org.oneui.compose.theme.OneUiTheme

/** SESL8 spacing for regular compound-button rows outside preference cards. */
object OneUiCompoundButtonDefaults {
    val MinHeight = 48.dp
    val VerticalMargin = 6.dp
    val ContentGap = 8.dp
}

/** Full-width SESL-style switch with its label at the logical start edge. */
@Composable
fun OneUiSwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = OneUiCompoundButtonDefaults.MinHeight)
            .padding(vertical = OneUiCompoundButtonDefaults.VerticalMargin)
            .semantics(mergeDescendants = true) {}
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                interactionSource = interactionSource,
                indication = null,
                onValueChange = onCheckedChange,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = OneUiTheme.colors.primaryText,
            style = OneUiTheme.typography.selectionLabel,
        )
        OneUiSwitch(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            interactionSource = interactionSource,
        )
    }
}

/** SESL-style checkbox whose visible indicator remains at the logical start edge. */
@Composable
fun OneUiCheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = OneUiCompoundButtonDefaults.MinHeight)
            .padding(vertical = OneUiCompoundButtonDefaults.VerticalMargin)
            .semantics(mergeDescendants = true) {}
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Checkbox,
                interactionSource = interactionSource,
                indication = null,
                onValueChange = onCheckedChange,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(OneUiCompoundButtonDefaults.ContentGap),
    ) {
        OneUiCheckbox(
            checked = checked,
            onCheckedChange = null,
            modifier = Modifier.clearAndSetSemantics {},
            enabled = enabled,
            interactionSource = interactionSource,
        )
        Text(
            text = label,
            color = OneUiTheme.colors.primaryText,
            style = OneUiTheme.typography.selectionLabel,
        )
    }
}

/** SESL-style radio control suitable for weighted horizontal [OneUiRadioButtonRow] groups. */
@Composable
fun OneUiRadioButtonRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = OneUiCompoundButtonDefaults.MinHeight)
            .padding(vertical = OneUiCompoundButtonDefaults.VerticalMargin)
            .semantics(mergeDescendants = true) {}
            .selectable(
                selected = selected,
                enabled = enabled,
                role = Role.RadioButton,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(OneUiCompoundButtonDefaults.ContentGap),
    ) {
        OneUiRadioButton(
            selected = selected,
            onClick = null,
            modifier = Modifier.clearAndSetSemantics {},
            enabled = enabled,
            interactionSource = interactionSource,
        )
        Text(
            text = label,
            color = OneUiTheme.colors.primaryText,
            style = OneUiTheme.typography.selectionLabel,
        )
        Spacer(Modifier.weight(1f))
    }
}
