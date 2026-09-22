package org.oneui.compose.components.dialog

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.oneui.compose.theme.OneUiTheme

@Composable
fun OneUiDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    confirmLabel: String = "Done",
    dismissLabel: String? = "Cancel",
    onConfirm: () -> Unit = onDismissRequest,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (!visible) return
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        title = { Text(title, color = OneUiTheme.colors.primaryText) },
        text = { androidx.compose.foundation.layout.Column(content = content) },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onConfirm) { Text(confirmLabel) }
        },
        dismissButton = dismissLabel?.let { label ->
            {
                androidx.compose.material3.TextButton(onClick = onDismissRequest) { Text(label) }
            }
        },
    )
}

