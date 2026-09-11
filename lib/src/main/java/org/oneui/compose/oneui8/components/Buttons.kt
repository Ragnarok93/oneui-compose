package org.oneui.compose.oneui8.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.oneui8.motion.OneUI8Motion
import org.oneui.compose.oneui8.theme.OneUI8Theme

enum class OneUI8ButtonStyle { Primary, Tonal, Neutral }

@Composable
fun OneUI8Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: OneUI8ButtonStyle = OneUI8ButtonStyle.Primary,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
) {
    val colors = OneUI8Theme.colors
    val targetBackground = when (style) {
        OneUI8ButtonStyle.Primary -> colors.accent
        OneUI8ButtonStyle.Tonal -> colors.accent.copy(alpha = 0.14f)
        OneUI8ButtonStyle.Neutral -> colors.surfaceElevated
    }
    val targetContent = when (style) {
        OneUI8ButtonStyle.Primary -> colors.onAccent
        OneUI8ButtonStyle.Tonal -> colors.accent
        OneUI8ButtonStyle.Neutral -> colors.primaryText
    }
    val background by animateColorAsState(
        targetValue = if (enabled) targetBackground else targetBackground.copy(alpha = 0.45f),
        animationSpec = OneUI8Motion.standard(),
        label = "OneUI8 button background",
    )

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .background(background, RoundedCornerShape(24.dp))
            .oneUI8Pressable(enabled = enabled, onClick = onClick)
            .semantics { role = Role.Button }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = targetContent,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 2.dp),
            )
        }
        Text(
            text = text,
            color = targetContent.copy(alpha = if (enabled) 1f else 0.55f),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun OneUI8IconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = OneUI8Theme.colors.surfaceElevated,
    contentColor: Color = OneUI8Theme.colors.primaryText,
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .background(containerColor, CircleShape)
            .oneUI8Pressable(enabled = enabled, onClick = onClick, pressedScale = 0.92f)
            .semantics { role = Role.Button },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = contentColor.copy(alpha = if (enabled) 1f else 0.4f),
            modifier = Modifier.size(24.dp),
        )
    }
}
