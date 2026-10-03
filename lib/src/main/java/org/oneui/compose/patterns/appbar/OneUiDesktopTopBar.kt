package org.oneui.compose.patterns.appbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import org.oneui.compose.theme.OneUiTheme

/**
 * Reusable One UI desktop title, context, and action bar.
 *
 * The context slot is structural and is intentionally free of navigation or domain assumptions.
 * Callers can provide One UI controls such as [org.oneui.compose.icons.OneUiIconButton] through
 * [actions] or [contextBar].
 */
@Composable
fun OneUiDesktopTopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    contextBar: (@Composable RowScope.() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val colors = OneUiTheme.colors
    val typography = OneUiTheme.typography
    val desktopMetrics = OneUiTheme.desktopMetrics
    val spacing = OneUiTheme.spacing

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.background)
            .statusBarsPadding()
            .padding(horizontal = desktopMetrics.topBarHorizontalPadding),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(desktopMetrics.topBarHeight),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacing.controlGap),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing.controlGap),
            ) {
                Text(
                    text = title,
                    color = colors.primaryText,
                    style = typography.title,
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        color = colors.secondaryText,
                        style = typography.subtitle,
                    )
                }
            }
            actions()
        }

        if (contextBar != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("oneui-shell-desktop-context-bar"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.controlGap),
            ) {
                contextBar()
            }
        }
    }
}
