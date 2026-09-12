package org.oneui.compose.patterns.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.buttons.OneUiTextButton
import org.oneui.compose.theme.OneUiTheme

/** Label/value item rendered in a [OneUiProfile] details card. */
@Immutable
data class OneUiProfileDetail(
    val label: String,
    val value: String,
)

/**
 * Compose-native profile surface for contact/person detail flows.
 *
 * Avatar, actions, and data ownership stay with the caller; this component supplies the One UI
 * spacing, typography, grouped detail card, and action layout used by the catalog reference flow.
 */
@Composable
fun OneUiProfile(
    name: String,
    subtitle: String? = null,
    details: List<OneUiProfileDetail> = emptyList(),
    modifier: Modifier = Modifier,
    avatar: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = OneUiTheme.spacing.screenHorizontal),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (avatar != null) {
            avatar()
        }

        Text(
            text = name,
            modifier = Modifier.padding(top = if (avatar != null) 16.dp else 0.dp),
            style = OneUiTheme.typography.title,
            color = OneUiTheme.colors.primaryText,
            textAlign = TextAlign.Center,
        )

        if (!subtitle.isNullOrBlank()) {
            Text(
                text = subtitle,
                modifier = Modifier.padding(top = 4.dp),
                style = OneUiTheme.typography.listSummary,
                color = OneUiTheme.colors.secondaryText,
                textAlign = TextAlign.Center,
            )
        }

        Row(
            modifier = Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = actions,
        )

        if (details.isNotEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                shape = OneUiTheme.shapes.card,
                color = OneUiTheme.colors.surface,
            ) {
                Column {
                    details.forEachIndexed { index, detail ->
                        if (index > 0) {
                            HorizontalDivider(
                                modifier = Modifier.padding(start = 20.dp),
                                color = OneUiTheme.colors.divider,
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                        ) {
                            Text(
                                text = detail.label,
                                style = OneUiTheme.typography.sectionLabel,
                                color = OneUiTheme.colors.secondaryText,
                            )
                            Text(
                                text = detail.value,
                                modifier = Modifier.padding(top = 3.dp),
                                style = OneUiTheme.typography.listTitle,
                                color = OneUiTheme.colors.primaryText,
                            )
                        }
                    }
                }
            }
        }
    }
}

/** Standard text action used by [OneUiProfile]. */
@Composable
fun OneUiProfileAction(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OneUiTextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        Text(label)
    }
}
