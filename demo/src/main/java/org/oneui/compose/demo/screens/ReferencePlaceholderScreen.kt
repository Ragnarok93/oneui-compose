package org.oneui.compose.demo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.theme.OneUiTheme

@Composable
fun ReferencePlaceholderScreen(
    title: String,
    testTag: String,
    referenceScope: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag(testTag)
            .padding(28.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = title,
            color = OneUiTheme.colors.primaryText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = referenceScope,
            color = OneUiTheme.colors.secondaryText,
            fontSize = 15.sp,
            lineHeight = 22.sp,
        )
        Text(
            text = "Parity implementation is tracked explicitly; this route remains incomplete until its public Compose APIs replace this diagnostic surface.",
            color = OneUiTheme.colors.destructive,
            fontSize = 13.sp,
            lineHeight = 19.sp,
        )
    }
}
