package org.oneui.compose.components.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.oneui.compose.theme.OneUiTheme

@Composable
fun OneUiSnackbar(
    visible: Boolean,
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    content: (@Composable RowScope.() -> Unit)? = null,
) {
    AnimatedVisibility(
        visible = visible,
        enter = if (OneUiTheme.reducedMotion) fadeIn() else fadeIn(animationSpec = org.oneui.compose.motion.OneUiMotion.quick()),
        exit = if (OneUiTheme.reducedMotion) fadeOut() else fadeOut(animationSpec = org.oneui.compose.motion.OneUiMotion.quick()),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = OneUiTheme.colors.surfaceElevated,
                    shape = RoundedCornerShape(18.dp),
                )
                .testTag("oneui-snackbar")
                .padding(horizontal = 18.dp, vertical = 14.dp),
        ) {
            Text(message, color = OneUiTheme.colors.primaryText)
            if (actionLabel != null && onAction != null) {
                androidx.compose.material3.TextButton(onClick = onAction) {
                    Text(actionLabel, color = OneUiTheme.colors.accent)
                }
            }
            content?.invoke(this)
        }
    }
}

