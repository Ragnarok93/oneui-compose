package org.oneui.compose.patterns.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.buttons.OneUiFilledButton
import org.oneui.compose.components.buttons.OneUiOutlinedButton
import org.oneui.compose.components.progress.OneUiCircularProgress
import org.oneui.compose.components.progress.OneUiCircularProgressSize
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.theme.OneUiTheme

enum class OneUiAppInfoStatus {
    Loading,
    Failed,
    UnstableConnection,
    NoConnection,
    NoUpdate,
    NotUpdatable,
    UpdateAvailable,
    UpdateDownloaded,
    Unset,
}

@Composable
fun OneUiAppInfo(
    appName: String,
    version: String,
    status: OneUiAppInfoStatus,
    onStatusAction: () -> Unit,
    onGithubClick: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    extraInfo: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("oneui-app-info")
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            if (onBack != null) {
                OneUiIconButton(
                    icon = OneUiIcons.Back,
                    contentDescription = "Back",
                    onClick = onBack,
                )
            }
        }
        OneUiIcon(
            icon = OneUiIcons.Info,
            contentDescription = "Application icon",
            modifier = Modifier.size(72.dp),
            tint = OneUiTheme.colors.accent,
        )
        Text(appName, color = OneUiTheme.colors.primaryText, fontWeight = FontWeight.Bold)
        Text("Version $version", color = OneUiTheme.colors.secondaryText)
        if (extraInfo != null) Text(extraInfo, color = OneUiTheme.colors.secondaryText)
        AppInfoStatus(status)
        OneUiFilledButton(onClick = onStatusAction, modifier = Modifier.testTag("about-status-button")) {
            Text("Status")
        }
        OneUiOutlinedButton(onClick = onGithubClick, modifier = Modifier.testTag("about-github-button")) {
            Text("GitHub")
        }
    }
}

@Composable
private fun AppInfoStatus(status: OneUiAppInfoStatus) {
    when (status) {
        OneUiAppInfoStatus.Loading -> OneUiCircularProgress(
            progress = null,
            size = OneUiCircularProgressSize.Medium,
        )
        else -> Text(status.label, color = OneUiTheme.colors.secondaryText)
    }
}

private val OneUiAppInfoStatus.label: String
    get() = when (this) {
        OneUiAppInfoStatus.Loading -> "Loading"
        OneUiAppInfoStatus.Failed -> "Failed"
        OneUiAppInfoStatus.UnstableConnection -> "Unstable connection"
        OneUiAppInfoStatus.NoConnection -> "No connection"
        OneUiAppInfoStatus.NoUpdate -> "No update"
        OneUiAppInfoStatus.NotUpdatable -> "Not updatable"
        OneUiAppInfoStatus.UpdateAvailable -> "Update available"
        OneUiAppInfoStatus.UpdateDownloaded -> "Update downloaded"
        OneUiAppInfoStatus.Unset -> ""
    }

