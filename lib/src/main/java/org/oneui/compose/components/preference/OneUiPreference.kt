package org.oneui.compose.components.preference

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.buttons.OneUiFilledButton
import org.oneui.compose.components.buttons.OneUiTextButton
import org.oneui.compose.components.list.OneUiListItem
import org.oneui.compose.theme.OneUiTheme

@Composable
fun OneUiPreference(
    title: String,
    modifier: Modifier = Modifier,
    summary: String? = null,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
) = OneUiListItem(
    title = title,
    summary = summary,
    modifier = modifier,
    enabled = enabled,
    onClick = onClick,
    trailing = trailing,
)

@Composable
fun OneUiPreferenceCategory(
    title: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (title != null) {
            Text(
                title,
                modifier = Modifier.padding(horizontal = 8.dp),
                color = OneUiTheme.colors.secondaryText,
                style = OneUiTheme.typography.sectionLabel,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(OneUiTheme.colors.surfaceElevated, RoundedCornerShape(22.dp))
                .testTag("oneui-preference-category"),
        ) { content() }
    }
}

@Composable
fun OneUiSuggestionCard(
    title: String,
    summary: String,
    actionLabel: String,
    onAction: () -> Unit,
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(OneUiTheme.colors.surfaceElevated, RoundedCornerShape(26.dp))
            .testTag("oneui-suggestion-card")
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                title,
                modifier = Modifier.weight(1f),
                color = OneUiTheme.colors.primaryText,
                style = OneUiTheme.typography.listTitle,
            )
            if (onDismiss != null) {
                OneUiTextButton(onClick = onDismiss) { Text("Close") }
            }
        }
        Text(summary, color = OneUiTheme.colors.secondaryText, style = OneUiTheme.typography.listSummary)
        OneUiFilledButton(onClick = onAction) { Text(actionLabel) }
    }
}
