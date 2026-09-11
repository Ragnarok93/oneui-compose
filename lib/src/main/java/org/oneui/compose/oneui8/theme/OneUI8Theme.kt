package org.oneui.compose.oneui8.theme

import androidx.compose.runtime.Composable
import org.oneui.compose.theme.OneUiColors
import org.oneui.compose.theme.OneUiDimensions
import org.oneui.compose.theme.OneUiTheme

typealias OneUI8Colors = OneUiColors
typealias OneUI8Dimensions = OneUiDimensions

/** Compatibility entry point. New code should use [OneUiTheme]. */
@Composable
fun OneUI8Theme(
    dynamicColors: Boolean = false,
    content: @Composable () -> Unit,
) {
    OneUiTheme(dynamicColors = dynamicColors, content = content)
}

/** Compatibility token facade for the feature-branch OneUI8 API. */
object OneUI8Theme {
    val colors: OneUI8Colors
        @Composable get() = OneUiTheme.colors

    val dimensions: OneUI8Dimensions
        @Composable get() = OneUiTheme.dimensions
}
