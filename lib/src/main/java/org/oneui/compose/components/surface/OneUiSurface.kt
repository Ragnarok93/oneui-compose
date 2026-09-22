package org.oneui.compose.components.surface

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.oneui.compose.theme.OneUiColors
import org.oneui.compose.theme.OneUiTheme

/** Defaults shared by generic Compose-native One UI physical surfaces. */
internal object OneUiSurfaceDefaults {
    fun containerColor(colors: OneUiColors): Color = colors.surfaceElevated
}

@Composable
fun OneUiSurface(
    modifier: Modifier = Modifier,
    containerColor: Color = OneUiSurfaceDefaults.containerColor(OneUiTheme.colors),
    shape: androidx.compose.ui.graphics.Shape = OneUiTheme.shapes.card,
    content: @Composable ColumnScope.() -> Unit,
) {
    androidx.compose.foundation.layout.Column(
        modifier = modifier
            .clip(shape)
            .background(containerColor)
            .padding(20.dp),
        content = content,
    )
}

@Composable
fun OneUiSurfaceBox(
    modifier: Modifier = Modifier,
    containerColor: Color = OneUiSurfaceDefaults.containerColor(OneUiTheme.colors),
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(22.dp),
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(containerColor),
    ) {
        content()
    }
}
