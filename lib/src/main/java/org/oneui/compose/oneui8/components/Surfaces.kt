package org.oneui.compose.oneui8.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.oneui8.theme.OneUI8Theme

@Composable
fun OneUI8Card(
    modifier: Modifier = Modifier,
    containerColor: Color = OneUI8Theme.colors.surface,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = containerColor,
                shape = RoundedCornerShape(OneUI8Theme.dimensions.cardRadius),
            )
            .padding(20.dp),
    ) { content() }
}

@Composable
fun OneUI8Section(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(
                text = title,
                color = OneUI8Theme.colors.primaryText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = OneUI8Theme.colors.secondaryText,
                    fontSize = 13.sp,
                )
            }
        }
        content()
    }
}

@Composable
fun OneUI8ListItem(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
) {
    val base = modifier
        .fillMaxWidth()
        .defaultMinSize(minHeight = OneUI8Theme.dimensions.listItemMinHeight)
        .background(
            OneUI8Theme.colors.surface,
            RoundedCornerShape(OneUI8Theme.dimensions.nestedRadius),
        )
    val clickable = if (onClick != null) {
        base.oneUI8Pressable(enabled = enabled, onClick = onClick, pressedScale = 0.985f)
    } else base

    Row(
        modifier = clickable.padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        if (leading != null) leading()
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = OneUI8Theme.colors.primaryText.copy(alpha = if (enabled) 1f else 0.45f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = OneUI8Theme.colors.secondaryText.copy(alpha = if (enabled) 1f else 0.45f),
                    fontSize = 13.sp,
                )
            }
        }
        if (trailing != null) Row { trailing() }
    }
}
