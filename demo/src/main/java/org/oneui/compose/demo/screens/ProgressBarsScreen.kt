package org.oneui.compose.demo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.oneui.compose.components.progress.OneUiCircularProgress
import org.oneui.compose.components.progress.OneUiCircularProgressSize
import org.oneui.compose.components.progress.OneUiLinearProgress
import org.oneui.compose.demo.CatalogSection
import org.oneui.compose.theme.OneUiTheme

@Composable
fun ProgressBarsScreen(modifier: Modifier = Modifier) {
    val reducedMotion = OneUiTheme.reducedMotion
    var progress by remember(reducedMotion) { mutableFloatStateOf(if (reducedMotion) 0.5f else 0.01f) }
    if (!reducedMotion) {
        LaunchedEffect(Unit) {
            while (true) {
                for (step in 1..100) {
                    progress = step / 100f
                    delay(80)
                }
            }
        }
    }

    LazyColumn(
        modifier = modifier.testTag("catalog-progress-bars"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            CatalogSection(
                title = "Indeterminate circular",
                subtitle = "Small, medium, large and extra-large One UI progress indicators.",
            ) {
                ProgressSizeRow(progress = null)
            }
        }
        item {
            CatalogSection(
                title = "Indeterminate horizontal",
                subtitle = "Animated horizontal loading state.",
            ) {
                OneUiLinearProgress(
                    progress = null,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        item {
            CatalogSection(
                title = "Determinate circular",
                subtitle = "Reference sample cycles progress from 1 to 100 every 80 ms.",
            ) {
                ProgressSizeRow(progress = progress)
            }
        }
        item {
            CatalogSection(title = "Determinate horizontal") {
                OneUiLinearProgress(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    color = OneUiTheme.colors.secondaryText,
                    fontSize = 13.sp,
                )
            }
        }
    }
}

@Composable
private fun ProgressSizeRow(progress: Float?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        listOf(
            "S" to OneUiCircularProgressSize.Small,
            "M" to OneUiCircularProgressSize.Medium,
            "L" to OneUiCircularProgressSize.Large,
            "XL" to OneUiCircularProgressSize.XLarge,
        ).forEach { (label, size) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OneUiCircularProgress(progress = progress, size = size)
                Text(
                    text = label,
                    color = OneUiTheme.colors.secondaryText,
                    fontSize = 11.sp,
                )
            }
        }
    }
}
