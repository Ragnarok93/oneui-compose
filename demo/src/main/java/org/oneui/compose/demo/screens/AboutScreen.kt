package org.oneui.compose.demo.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.oneui.compose.patterns.about.OneUiAppInfo
import org.oneui.compose.patterns.about.OneUiAppInfoStatus

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
) {
    var statusIndex by remember { mutableIntStateOf(0) }
    val statuses = OneUiAppInfoStatus.entries
    OneUiAppInfo(
        appName = "OneUI Compose",
        version = "0.8.0-oneui8-demo",
        status = statuses[statusIndex],
        onStatusAction = { statusIndex = (statusIndex + 1) % statuses.size },
        onGithubClick = { },
        modifier = modifier,
        onBack = onBack,
        extraInfo = "OneUI Design version ce4f2ca",
    )
}

