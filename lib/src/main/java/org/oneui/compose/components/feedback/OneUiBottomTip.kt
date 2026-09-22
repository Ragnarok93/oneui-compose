package org.oneui.compose.components.feedback

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.buttons.OneUiTextButton
import org.oneui.compose.theme.OneUiTheme

@Composable
fun OneUiBottomTip(
    title: String,
    summary: String,
    linkLabel: String,
    onLinkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("oneui-bottom-tip")
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Text(title, color = OneUiTheme.colors.primaryText, style = OneUiTheme.typography.listTitle)
        Text(
            summary,
            modifier = Modifier.padding(top = 6.dp),
            color = OneUiTheme.colors.secondaryText,
            style = OneUiTheme.typography.listSummary,
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            OneUiTextButton(
                onClick = onLinkClick,
                modifier = Modifier.padding(top = 6.dp),
            ) { Text(linkLabel) }
        }
    }
}

