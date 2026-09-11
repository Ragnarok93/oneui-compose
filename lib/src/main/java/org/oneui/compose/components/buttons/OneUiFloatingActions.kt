package org.oneui.compose.components.buttons

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.snap
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.interaction.oneUiInteractive
import org.oneui.compose.motion.OneUiMotion
import org.oneui.compose.theme.OneUiTheme

/** One destination in the two-option floating action bar. */
@Immutable
data class OneUiFloatingActionItem(
    val label: String,
    val icon: OneUiIcon,
)

/** Stable icon-only button variant in the buttons package. */
@Composable
fun OneUiIconButton(
    icon: OneUiIcon,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = OneUiTheme.colors.primaryText,
    interactionSource: MutableInteractionSource? = null,
) {
    org.oneui.compose.icons.OneUiIconButton(
        icon = icon,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        tint = tint,
        interactionSource = interactionSource,
    )
}

/** Conventional circular One UI floating action button. */
@Composable
fun OneUiFloatingActionButton(
    icon: OneUiIcon,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    containerColor: Color = OneUiTheme.colors.accent,
    contentColor: Color = OneUiTheme.colors.onAccent,
) {
    OneUiButton(
        onClick = onClick,
        modifier = modifier.size(56.dp),
        enabled = enabled,
        interactionSource = interactionSource,
        colors = OneUiButtonDefaults.filledColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
    ) {
        org.oneui.compose.icons.OneUiIcon(
            icon = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp),
            tint = contentColor,
        )
    }
}

/**
 * Two-destination floating action bar with a movable selected surface.
 *
 * Logical item order stays stable; the selected surface maps to physical left/right position in
 * RTL and animates with the 400 ms One UI FAB motion token. Because the target offset is derived
 * from current constraints, width changes and interrupted selection changes continue from the
 * current animation state rather than restarting from a fabricated baseline.
 */
@Composable
fun OneUiFloatingActionBar(
    selectedIndex: Int,
    items: List<OneUiFloatingActionItem>,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    require(items.size == 2) { "OneUiFloatingActionBar requires exactly two destinations" }
    require(selectedIndex in items.indices) { "selectedIndex must reference an item" }

    val themeColors = OneUiTheme.colors
    val layoutDirection = LocalLayoutDirection.current
    val reducedMotion = OneUiTheme.reducedMotion
    val physicalIndex = if (layoutDirection == LayoutDirection.Rtl) {
        items.lastIndex - selectedIndex
    } else {
        selectedIndex
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(68.dp)
            .background(themeColors.surfaceElevated, RoundedCornerShape(28.dp))
            .padding(4.dp),
    ) {
        val segmentWidth = maxWidth / items.size.toFloat()
        val selectedOffset = segmentWidth * physicalIndex.toFloat()
        val animatedOffset = animateDpAsState(
            targetValue = selectedOffset,
            animationSpec = if (reducedMotion) snap() else OneUiMotion.fab(),
            label = "OneUi floating action selected surface",
        ).value

        Box(
            modifier = Modifier
                .absoluteOffset(x = animatedOffset)
                .width(segmentWidth)
                .fillMaxHeight()
                .clip(RoundedCornerShape(24.dp))
                .background(themeColors.accent.copy(alpha = 0.14f)),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex
                val interactionSource = remember(item.label) { MutableInteractionSource() }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(24.dp))
                        .oneUiInteractive(
                            enabled = enabled,
                            onClick = { onSelected(index) },
                            interactionSource = interactionSource,
                            role = Role.Tab,
                            shape = RoundedCornerShape(24.dp),
                            pressedScale = 0.96f,
                        )
                        .semantics { selected = isSelected }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    org.oneui.compose.icons.OneUiIcon(
                        icon = item.icon,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = if (isSelected) themeColors.accent else themeColors.secondaryText,
                    )
                    Text(
                        text = item.label,
                        color = if (isSelected) themeColors.accent else themeColors.secondaryText,
                        style = if (isSelected) {
                            OneUiTheme.typography.navigationLabelSelected
                        } else {
                            OneUiTheme.typography.navigationLabel
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}
