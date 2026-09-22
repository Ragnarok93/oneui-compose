package org.oneui.compose.patterns.appbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.components.buttons.OneUiTextButton
import org.oneui.compose.icons.OneUiAnimatedIcons
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.theme.OneUiTheme

@Immutable
data class OneUiActionModeAction(
    val id: String,
    val label: String,
    val icon: OneUiIcon,
)

/** Reusable action-mode contract for selection lists and toolbar content. */
@Composable
fun OneUiActionMode(
    selectedCount: Int,
    allSelected: Boolean,
    onSelectAll: () -> Unit,
    onCancel: () -> Unit,
    actions: List<OneUiActionModeAction>,
    onAction: (OneUiActionModeAction) -> Unit,
    modifier: Modifier = Modifier,
    showCancelButton: Boolean = false,
    visible: Boolean = true,
) {
    AnimatedVisibility(
        visible = visible,
        enter = if (OneUiTheme.reducedMotion) fadeIn() else fadeIn(animationSpec = org.oneui.compose.motion.OneUiMotion.quick()),
        exit = if (OneUiTheme.reducedMotion) fadeOut() else fadeOut(animationSpec = org.oneui.compose.motion.OneUiMotion.quick()),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("oneui-action-mode")
                .padding(horizontal = 12.dp, vertical = 4.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OneUiAnimatedIcons.CheckMorph(
                    checked = true,
                    modifier = Modifier,
                )
                Text(
                    text = "$selectedCount selected",
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp),
                    color = OneUiTheme.colors.primaryText,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                )
                OneUiTextButton(onClick = onSelectAll) {
                    Text(if (allSelected) "Clear all" else "Select all")
                }
                if (showCancelButton) {
                    OneUiTextButton(
                        onClick = onCancel,
                        modifier = Modifier.testTag("oneui-action-mode-cancel"),
                    ) {
                        Text("Cancel")
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                actions.forEach { action ->
                    OneUiIconButton(
                        icon = action.icon,
                        contentDescription = action.label,
                        onClick = { onAction(action) },
                        modifier = Modifier.testTag("oneui-action-${action.id}"),
                    )
                }
            }
        }
    }
}

