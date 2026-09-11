package org.oneui.compose.oneui8.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.oneui8.motion.OneUI8Motion
import org.oneui.compose.oneui8.theme.OneUI8Theme

@Composable
fun OneUI8SelectionIndicator(
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = OneUI8Theme.colors
    val progress by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = OneUI8Motion.selection(),
        label = "OneUI8 selection check",
    )
    val background by animateColorAsState(
        targetValue = if (selected) colors.accent else colors.controlInactive.copy(alpha = 0.16f),
        animationSpec = OneUI8Motion.standard(),
        label = "OneUI8 selection background",
    )
    val firstStrokeEnd = OneUI8Motion.Duration.SelectionFirstStroke.toFloat() / OneUI8Motion.Duration.SelectionTotal
    val first = (progress / firstStrokeEnd).coerceIn(0f, 1f)
    val second = ((progress - firstStrokeEnd) / (1f - firstStrokeEnd)).coerceIn(0f, 1f)

    Canvas(
        modifier = modifier
            .size(28.dp)
            .background(background, RoundedCornerShape(14.dp)),
    ) {
        if (progress <= 0f) return@Canvas
        val start = Offset(size.width * 0.27f, size.height * 0.52f)
        val mid = Offset(size.width * 0.44f, size.height * 0.68f)
        val end = Offset(size.width * 0.75f, size.height * 0.34f)
        drawLine(
            color = colors.onAccent,
            start = start,
            end = Offset(
                x = start.x + (mid.x - start.x) * first,
                y = start.y + (mid.y - start.y) * first,
            ),
            strokeWidth = size.minDimension * 0.085f,
            cap = StrokeCap.Round,
        )
        if (second > 0f) {
            drawLine(
                color = colors.onAccent,
                start = mid,
                end = Offset(
                    x = mid.x + (end.x - mid.x) * second,
                    y = mid.y + (end.y - mid.y) * second,
                ),
                strokeWidth = size.minDimension * 0.085f,
                cap = StrokeCap.Round,
            )
        }
    }
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
                shape = RoundedCornerShape(18.dp),
            )
            .oneUI8Pressable(enabled = true, onClick = onClick, pressedScale = 0.94f)
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
