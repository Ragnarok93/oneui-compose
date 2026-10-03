package org.oneui.compose.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.theme.OneUiTheme

/**
 * Domain-neutral desktop context content for the catalog shell.
 *
 * The slot demonstrates a breadcrumb-like label plus reusable search and action affordances.
 */
@Composable
fun CatalogDesktopContextBar(selectedLabel: String) {
    val colors = OneUiTheme.colors
    val typography = OneUiTheme.typography
    val spacing = OneUiTheme.spacing

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("catalog-desktop-context-bar"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.controlGap),
    ) {
        Text(
            text = "Catalog",
            color = colors.secondaryText,
            style = typography.navigationLabel,
        )
        OneUiIcon(
            icon = OneUiIcons.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = colors.secondaryText,
        )
        Text(
            text = selectedLabel,
            color = colors.primaryText,
            style = typography.navigationLabelSelected,
            modifier = Modifier.weight(1f),
        )
        OneUiIconButton(
            icon = OneUiIcons.Search,
            contentDescription = "Search catalog",
            onClick = {},
        )
        OneUiIconButton(
            icon = OneUiIcons.More,
            contentDescription = "More catalog actions",
            onClick = {},
        )
    }
}