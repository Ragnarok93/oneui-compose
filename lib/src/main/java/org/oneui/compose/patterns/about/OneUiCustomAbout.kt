package org.oneui.compose.patterns.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.list.OneUiListItem
import org.oneui.compose.components.surface.OneUiSurface
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.theme.OneUiTheme
import kotlin.math.max

data class OneUiAboutLink(
    val label: String,
    val onClick: () -> Unit,
)

@Composable
fun OneUiCustomAbout(
    appName: String,
    version: String,
    contributors: List<String>,
    licenses: List<String>,
    links: List<OneUiAboutLink>,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    backProgress: Float = 0f,
) {
    val listState = androidx.compose.foundation.lazy.rememberLazyListState()
    val collapsedFraction by remember(listState) {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) {
                1f
            } else {
                (listState.firstVisibleItemScrollOffset / 200f).coerceIn(0f, 1f)
            }
        }
    }
    LazyColumn(
        modifier = modifier
            .testTag("oneui-custom-about")
            .fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        state = listState,
    ) {
        item {
            OneUiCustomAboutHeader(
                appName = appName,
                version = version,
                collapsedFraction = max(collapsedFraction, backProgress.coerceIn(0f, 1f)),
                onBack = onBack,
            )
        }
        item {
            OneUiAboutSection(title = "OneUI Project Team") {
                contributors.forEach { name ->
                    OneUiListItem(
                        title = name,
                        leading = {
                            OneUiIcon(
                                icon = OneUiIcons.Info,
                                contentDescription = null,
                                modifier = Modifier.size(38.dp),
                                tint = OneUiTheme.colors.accent,
                            )
                        },
                    )
                }
            }
        }
        item {
            OneUiAboutSection(title = "Open source licenses") {
                licenses.forEach { license -> OneUiListItem(title = license) }
            }
        }
        item {
            OneUiAboutSection(title = "Useful links") {
                links.forEach { link ->
                    OneUiListItem(title = link.label, onClick = link.onClick)
                }
            }
        }
    }
}

@Composable
private fun OneUiCustomAboutHeader(
    appName: String,
    version: String,
    collapsedFraction: Float,
    onBack: (() -> Unit)?,
) {
    OneUiSurface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("oneui-custom-about-header"),
        containerColor = OneUiTheme.colors.surfaceElevated,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            if (onBack != null) {
                androidx.compose.foundation.layout.Box(modifier = Modifier.weight(0.2f)) {
                    org.oneui.compose.icons.OneUiIconButton(
                        icon = OneUiIcons.Back,
                        contentDescription = "Back",
                        onClick = onBack,
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            ) {
                OneUiIcon(
                    icon = OneUiIcons.Info,
                    contentDescription = "Application icon",
                    modifier = Modifier.size((72f - 34f * collapsedFraction).dp),
                    tint = OneUiTheme.colors.accent,
                )
                Text(appName, color = OneUiTheme.colors.primaryText, fontWeight = FontWeight.Bold)
                Text("Version $version", color = OneUiTheme.colors.secondaryText)
            }
            if (onBack != null) androidx.compose.foundation.layout.Spacer(Modifier.weight(0.2f))
        }
    }
}

@Composable
private fun OneUiAboutSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(title, color = OneUiTheme.colors.secondaryText, style = OneUiTheme.typography.sectionLabel)
        OneUiSurface(modifier = Modifier.fillMaxWidth()) { content() }
    }
}
