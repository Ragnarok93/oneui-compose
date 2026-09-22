package org.oneui.compose.components.sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.oneui.compose.theme.OneUiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneUiSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (!visible) return
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        containerColor = OneUiTheme.colors.surface,
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                title,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                color = OneUiTheme.colors.primaryText,
                fontWeight = FontWeight.Bold,
            )
            HorizontalDivider(color = OneUiTheme.colors.divider)
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                content = content,
            )
        }
    }
}

