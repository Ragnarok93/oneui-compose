package org.oneui.compose.components.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.rememberScrollState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.interaction.oneUiInteractive
import org.oneui.compose.motion.OneUiMotion
import org.oneui.compose.theme.OneUiTheme

@Immutable
data class OneUiNavigationItem(
    val id: String,
    val label: String,
    val icon: OneUiIcon? = null,
)

@Composable
fun OneUiTabs(
    items: List<OneUiNavigationItem>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    scrollable: Boolean = false,
    showIcons: Boolean = false,
) {
    require(items.isNotEmpty()) { "OneUiTabs requires at least one item" }
    val rowModifier = modifier
        .fillMaxWidth()
        .then(if (scrollable) Modifier.horizontalScroll(rememberScrollState()) else Modifier)
        .testTag("oneui-tabs")
    Row(
        modifier = rowModifier,
        horizontalArrangement = if (scrollable) Arrangement.spacedBy(4.dp) else Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEachIndexed { index, item ->
            OneUiTab(
                item = item,
                selected = index == selectedIndex,
                onClick = { onSelected(index) },
                showIcon = showIcons,
                modifier = if (scrollable) Modifier else Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun RowScope.OneUiTab(
    item: OneUiNavigationItem,
    selected: Boolean,
    onClick: () -> Unit,
    showIcon: Boolean,
    modifier: Modifier = Modifier,
) {
    val color by animateColorAsState(
        targetValue = if (selected) OneUiTheme.colors.accent else OneUiTheme.colors.secondaryText,
        animationSpec = if (OneUiTheme.reducedMotion) androidx.compose.animation.core.snap() else OneUiMotion.standard(),
        label = "One UI tab color",
    )
    Column(
        modifier = modifier
            .semantics { this.selected = selected }
            .oneUiInteractive(
                enabled = true,
                onClick = onClick,
                interactionSource = androidx.compose.foundation.interaction.MutableInteractionSource(),
                role = Role.Tab,
                shape = RoundedCornerShape(18.dp),
                pressedScale = 0.98f,
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (showIcon && item.icon != null) {
            OneUiIcon(
                icon = item.icon,
                contentDescription = item.label,
                modifier = Modifier.size(22.dp),
                tint = color,
            )
        }
        Text(
            item.label,
            color = color,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
        )
        Box(
            modifier = Modifier
                .background(
                    if (selected) color else Color.Transparent,
                    RoundedCornerShape(2.dp),
                )
                .fillMaxWidth()
                .padding(vertical = 1.5.dp),
        )
    }
}

@Composable
fun OneUiBottomNavigation(
    items: List<OneUiNavigationItem>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxVisibleItems: Int = 5,
) {
    require(items.isNotEmpty()) { "OneUiBottomNavigation requires at least one item" }
    val overflow = items.size > maxVisibleItems
    val visible = if (overflow) items.take(maxVisibleItems - 1) else items
    val more = OneUiNavigationItem("__more__", "More", OneUiIcons.More)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(OneUiTheme.colors.surfaceElevated)
            .testTag("oneui-bottom-navigation")
            .padding(horizontal = 6.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        visible.forEachIndexed { index, item ->
            OneUiBottomNavigationItem(
                item = item,
                selected = index == selectedIndex,
                onClick = { onSelected(index) },
            )
        }
        if (overflow) {
            OneUiBottomNavigationItem(
                item = more,
                selected = false,
                onClick = { onSelected(maxVisibleItems - 1) },
            )
        }
    }
}

@Composable
private fun RowScope.OneUiBottomNavigationItem(
    item: OneUiNavigationItem,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val tint = if (selected) OneUiTheme.colors.accent else OneUiTheme.colors.secondaryText
    Column(
        modifier = Modifier
            .weight(1f)
            .semantics { this.selected = selected }
            .oneUiInteractive(
                enabled = true,
                onClick = onClick,
                interactionSource = androidx.compose.foundation.interaction.MutableInteractionSource(),
                role = Role.Tab,
                shape = RoundedCornerShape(18.dp),
            )
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item.icon?.let {
            OneUiIcon(
                icon = it,
                contentDescription = item.label,
                modifier = Modifier.size(24.dp),
                tint = tint,
            )
        }
        Text(item.label, color = tint, fontSize = 11.sp)
    }
}

@Composable
fun OneUiNavigationRail(
    items: List<OneUiNavigationItem>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(OneUiTheme.colors.surfaceElevated)
            .testTag("oneui-navigation-rail")
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items.forEachIndexed { index, item ->
            val tint = if (index == selectedIndex) OneUiTheme.colors.accent else OneUiTheme.colors.secondaryText
            OneUiIconButton(
                icon = item.icon ?: OneUiIcons.Info,
                contentDescription = item.label,
                onClick = { onSelected(index) },
                tint = tint,
            )
        }
    }
}
