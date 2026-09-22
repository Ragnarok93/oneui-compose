package org.oneui.compose.demo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.oneui.compose.components.progress.OneUiCircularProgress
import org.oneui.compose.components.progress.OneUiCircularProgressSize
import org.oneui.compose.components.progress.OneUiLinearProgress
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
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            ProgressGroup(
                label = "Indeterminate",
                testTag = "progress-indeterminate-group",
                progress = null,
            )
        }
        item {
            ProgressGroup(
                label = "Determinate",
                testTag = "progress-determinate-group",
                progress = progress,
            )
        }
    }
}

@Composable
private fun ProgressGroup(
    label: String,
    testTag: String,
    progress: Float?,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label,
            color = OneUiTheme.colors.secondaryText,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag)
                .background(
                    color = OneUiTheme.colors.surfaceElevated,
                    shape = RoundedCornerShape(26.dp),
                )
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OneUiCircularProgress(progress = progress, size = OneUiCircularProgressSize.Small)
                OneUiCircularProgress(progress = progress, size = OneUiCircularProgressSize.Medium)
                OneUiCircularProgress(progress = progress, size = OneUiCircularProgressSize.Large)
                OneUiCircularProgress(progress = progress, size = OneUiCircularProgressSize.XLarge)
            }
            OneUiLinearProgress(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(4.dp),
            )
        }
    }
}
