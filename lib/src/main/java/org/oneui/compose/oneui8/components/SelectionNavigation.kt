package org.oneui.compose.oneui8.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.icons.OneUiAnimatedIcons
import org.oneui.compose.oneui8.motion.OneUI8Motion
import org.oneui.compose.oneui8.theme.OneUI8Theme

@Deprecated(
    message = "Use OneUiAnimatedIcons.CheckMorph from the stable One UI Compose surface.",
)
@Composable
fun OneUI8SelectionIndicator(
    selected: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OneUiAnimatedIcons.CheckMorph(
        checked = selected,
        modifier = modifier,
        enabled = enabled,
    )
}

data class OneUI8NavigationItem(
    val label: String,
    val icon: ImageVector,
)

@Composable
fun OneUI8NavigationBar(
    items: List<OneUI8NavigationItem>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    require(items.isNotEmpty()) { "OneUI8NavigationBar requires at least one item" }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                OneUI8Theme.colors.surfaceElevated,
                RoundedCornerShape(OneUI8Theme.dimensions.cardRadius),
            )
            .padding(horizontal = 8.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEachIndexed { index, item ->
            OneUI8NavigationDestination(
                item = item,
                selected = index == selectedIndex,
                onClick = { onSelected(index) },
            )
        }
    }
}

@Composable
private fun RowScope.OneUI8NavigationDestination(
    item: OneUI8NavigationItem,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val destinationShape = RoundedCornerShape(18.dp)
    val tint by animateColorAsState(
        targetValue = if (selected) OneUI8Theme.colors.accent else OneUI8Theme.colors.secondaryText,
        animationSpec = OneUI8Motion.standard(),
        label = "OneUI8 navigation tint",
    )
    Box(
        modifier = Modifier
            .weight(1f)
            .background(
                color = if (selected) OneUI8Theme.colors.accent.copy(alpha = 0.12f) else Color.Transparent,
                shape = destinationShape,
            )
            .oneUI8Pressable(
                enabled = true,
                onClick = onClick,
                shape = destinationShape,
                pressedScale = 0.94f,
            )
            .padding(vertical = 9.dp, horizontal = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp),
        ) {
            Icon(item.icon, contentDescription = item.label, tint = tint, modifier = Modifier.size(20.dp))
            if (selected) {
                Text(
                    text = item.label,
                    color = tint,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                )
            }
        }
    }
}
