package org.oneui.compose.components.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.oneui.compose.components.buttons.OneUiTextButton
import org.oneui.compose.theme.OneUiTheme

@Composable
fun OneUiTipPopup(
    visible: Boolean,
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null,
) {
    AnimatedVisibility(
        visible = visible,
        enter = if (OneUiTheme.reducedMotion) EnterTransition.None else fadeIn(animationSpec = org.oneui.compose.motion.OneUiMotion.tipPopup()),
        exit = if (OneUiTheme.reducedMotion) ExitTransition.None else fadeOut(animationSpec = org.oneui.compose.motion.OneUiMotion.tipPopup()),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .background(OneUiTheme.colors.surfaceElevated, RoundedCornerShape(22.dp))
                .testTag("oneui-tip-popup")
                .padding(18.dp),
        ) {
            Text(title, color = OneUiTheme.colors.primaryText, style = OneUiTheme.typography.listTitle)
            Text(
                message,
                modifier = Modifier.padding(top = 6.dp),
                color = OneUiTheme.colors.secondaryText,
                style = OneUiTheme.typography.listSummary,
            )
            if (onDismiss != null) {
                OneUiTextButton(
                    onClick = onDismiss,
                    modifier = Modifier.padding(top = 4.dp),
                ) { Text("Dismiss") }
            }
        }
    }
}
