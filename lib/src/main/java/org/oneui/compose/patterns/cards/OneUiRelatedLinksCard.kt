package org.oneui.compose.patterns.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.buttons.OneUiTextButton
import org.oneui.compose.theme.OneUiTheme

data class OneUiRelatedLink(
    val label: String,
    val onClick: () -> Unit,
)

@Composable
fun OneUiRelatedLinksCard(
    title: String,
    links: List<OneUiRelatedLink>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("oneui-related-links-card")
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Text(title, color = OneUiTheme.colors.primaryText, style = OneUiTheme.typography.listTitle)
        links.forEach { link ->
            OneUiTextButton(
                onClick = link.onClick,
                modifier = Modifier.padding(top = 4.dp),
            ) { Text(link.label) }
        }
    }
}

