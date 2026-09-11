package org.oneui.compose.oneui8.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.oneui.compose.theme.OneUITheme

@Immutable
data class OneUI8Colors(
    val accent: Color,
    val onAccent: Color,
    val background: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val surfacePressed: Color,
    val primaryText: Color,
    val secondaryText: Color,
    val divider: Color,
    val controlInactive: Color,
    val destructive: Color,
)

@Immutable
data class OneUI8Dimensions(
    val screenHorizontalPadding: Dp = 24.dp,
    val sectionSpacing: Dp = 20.dp,
    val cardRadius: Dp = 26.dp,
    val nestedRadius: Dp = 22.dp,
    val controlRadius: Dp = 18.dp,
    val iconSize: Dp = 24.dp,
    val compactIconSize: Dp = 20.dp,
    val touchTarget: Dp = 48.dp,
    val floatingBarHeight: Dp = 46.dp,
    val listItemMinHeight: Dp = 64.dp,
)

private val LocalOneUI8Colors = staticCompositionLocalOf<OneUI8Colors> {
    error("OneUI8Theme colors are unavailable outside OneUI8Theme")
}
private val LocalOneUI8Dimensions = staticCompositionLocalOf { OneUI8Dimensions() }

@Composable
fun OneUI8Theme(
    dynamicColors: Boolean = false,
    content: @Composable () -> Unit,
) {
    OneUITheme(dynamicColors = dynamicColors) {
        val legacy = OneUITheme.colors
        val dark = isSystemInDarkTheme()
        val colors = OneUI8Colors(
            accent = legacy.seslControlActivatedColor,
            onAccent = if (dark) Color.Black else Color.White,
            background = legacy.seslBackgroundColor,
            surface = legacy.seslPreferenceRelativeCardBackground,
            surfaceElevated = legacy.seslBackgroundFloating,
            surfacePressed = legacy.seslListRippleColor,
            primaryText = legacy.seslPrimaryTextColor,
            secondaryText = legacy.seslSecondaryTextColor,
            divider = legacy.seslListDividerColor,
            controlInactive = legacy.seslControlNormalColor,
            destructive = legacy.seslFunctionalRed,
        )

        val materialColors = if (dark) {
            darkColorScheme(
                primary = colors.accent,
                onPrimary = colors.onAccent,
                background = colors.background,
                onBackground = colors.primaryText,
                surface = colors.surface,
                onSurface = colors.primaryText,
                surfaceVariant = colors.surfaceElevated,
                onSurfaceVariant = colors.secondaryText,
                outline = colors.divider,
                error = colors.destructive,
            )
        } else {
            lightColorScheme(
                primary = colors.accent,
                onPrimary = colors.onAccent,
                background = colors.background,
                onBackground = colors.primaryText,
                surface = colors.surface,
                onSurface = colors.primaryText,
                surfaceVariant = colors.surfaceElevated,
                onSurfaceVariant = colors.secondaryText,
                outline = colors.divider,
                error = colors.destructive,
            )
        }

        CompositionLocalProvider(
            LocalOneUI8Colors provides colors,
            LocalOneUI8Dimensions provides OneUI8Dimensions(),
        ) {
            MaterialTheme(
                colorScheme = materialColors,
                shapes = MaterialTheme.shapes.copy(
                    small = RoundedCornerShape(12.dp),
                    medium = RoundedCornerShape(22.dp),
                    large = RoundedCornerShape(26.dp),
                ),
                content = content,
            )
        }
    }
}

object OneUI8Theme {
    val colors: OneUI8Colors
        @Composable get() = LocalOneUI8Colors.current

    val dimensions: OneUI8Dimensions
        @Composable get() = LocalOneUI8Dimensions.current
}
