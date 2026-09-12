package org.oneui.compose.demo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.demo.CatalogSection
import org.oneui.compose.theme.OneUiTheme

@Composable
fun ParityStatusScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.testTag("catalog-parity-status"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            CatalogSection(
                title = "Parity contract",
                subtitle = "Pinned to oneui-design@ce4f2cae8c0d712acd2f0b2926ee909fd5d8a434.",
            ) {
                StatusLine("Reference top-level routes", "8")
                StatusLine("Nested RecyclerViews tabs", "3")
                StatusLine("Completion rule", "Zero unresolved manifest entries")
            }
        }
        item {
            CatalogSection(
                title = "Current implementation",
                subtitle = "This screen is diagnostic only. Placeholder routes never count as parity-complete.",
            ) {
                StatusLine("Stable foundations", "Theme · motion · icons · buttons · sliders · selection")
                StatusLine("First catalog routes", "ProgressBars · SeekBars · Misc. Widgets")
                Text(
                    text = "Run `python scripts/audit-oneui8-parity.py --strict` for the final completion gate.",
                    color = OneUiTheme.colors.secondaryText,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                )
            }
        }
    }
}

@Composable
private fun StatusLine(label: String, value: String) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, color = OneUiTheme.colors.primaryText, fontSize = 14.sp)
        Text(
            text = value,
            color = OneUiTheme.colors.secondaryText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}
