package org.oneui.compose.patterns.appbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.theme.OneUiTheme

/**
 * Compose-native One UI large-title app bar used by the catalog and reusable app shells.
 *
 * [expandedFraction] is hoisted so scrolling containers can drive the large/collapsed title state
 * without coupling this component to a particular lazy-list implementation.
 */
@Composable
fun OneUiLargeTitleAppBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    expandedFraction: Float = 1f,
    navigationIcon: OneUiIcon? = null,
    navigationContentDescription: String = "Navigation",
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val colors = OneUiTheme.colors
    val fraction = expandedFraction.coerceIn(0f, 1f)
    val titleSize = (22f + 14f * fraction).sp
    val horizontal = OneUiTheme.dimensions.screenHorizontalPadding

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontal, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (navigationIcon != null && onNavigationClick != null) {
                OneUiIconButton(
                    icon = navigationIcon,
                    contentDescription = navigationContentDescription,
                    onClick = onNavigationClick,
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = title,
                    color = colors.primaryText,
                    fontSize = titleSize,
                    lineHeight = (titleSize.value + 4f).sp,
                    fontWeight = FontWeight.Bold,
                )
                if (subtitle != null && fraction > 0.15f) {
                    Text(
                        text = subtitle,
                        color = colors.secondaryText,
                        fontSize = 14.sp,
                        lineHeight = 19.sp,
                    )
                }
            }
            actions()
        }
    }
}
