package org.oneui.compose.components.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import org.oneui.compose.theme.OneUiTheme

@Composable
fun OneUiTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    hint: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .testTag("oneui-text-field"),
        enabled = enabled,
        singleLine = singleLine,
        label = label?.let { { Text(it) } },
        placeholder = hint?.let { { Text(it, color = OneUiTheme.colors.secondaryText) } },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = OneUiTheme.colors.primaryText,
            unfocusedTextColor = OneUiTheme.colors.primaryText,
            focusedBorderColor = OneUiTheme.colors.accent,
            unfocusedBorderColor = OneUiTheme.colors.divider,
            cursorColor = OneUiTheme.colors.accent,
        ),
    )
}

