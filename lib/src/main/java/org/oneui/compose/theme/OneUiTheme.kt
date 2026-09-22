package org.oneui.compose.theme

import android.provider.Settings
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.oneui.compose.motion.OneUiMotionScheme

private val LocalOneUiColors = staticCompositionLocalOf<OneUiColors> {
    error("OneUiTheme colors are unavailable outside OneUiTheme")
}
private val LocalOneUiTypography = staticCompositionLocalOf<OneUiTypography> {
    error("OneUiTheme typography is unavailable outside OneUiTheme")
}
private val LocalOneUiDimensions = staticCompositionLocalOf { OneUiDimensions() }
private val LocalOneUiShapes = staticCompositionLocalOf { OneUiShapes() }
private val LocalOneUiSpacing = staticCompositionLocalOf { OneUiSpacing() }
private val LocalOneUiSizes = staticCompositionLocalOf { OneUiSizes() }
private val LocalOneUiElevation = staticCompositionLocalOf { OneUiElevation() }
private val LocalOneUiOpacity = staticCompositionLocalOf { OneUiOpacity() }
private val LocalOneUiMotion = staticCompositionLocalOf { OneUiMotionScheme() }

@Composable
fun OneUiTheme(
    dynamicColors: Boolean = false,
    reducedMotion: Boolean? = null,
    content: @Composable () -> Unit,
) {
    OneUITheme(dynamicColors = dynamicColors) {
        val legacyColors = OneUITheme.colors
        val legacyTypes = OneUITheme.types
        val dark = isSystemInDarkTheme()
        val context = LocalContext.current
        val systemReducedMotion = remember(context) {
            runCatching {
                Settings.Global.getFloat(
                    context.contentResolver,
                    Settings.Global.ANIMATOR_DURATION_SCALE,
                    1f,
                ) == 0f
            }.getOrDefault(false)
        }

        val colors = oneUiColorsFromLegacy(legacyColors, dark)
        val typography = OneUiTypography(
            buttonLabel = legacyTypes.button,
            title = legacyTypes.appbarTitleCollapsed,
            largeTitle = legacyTypes.appbarTitleExtended,
            subtitle = legacyTypes.appbarSubtitle,
            sectionLabel = legacyTypes.textSeparator,
            listTitle = legacyTypes.preferenceTitle,
            listSummary = legacyTypes.preferenceSummary,
            dialogTitle = legacyTypes.dialogTitle,
            dialogBody = legacyTypes.dialogBody,
            dialogButtonLabel = legacyTypes.dialogButtonLabel,
            inputText = legacyTypes.editTextText,
            inputHint = legacyTypes.editTextHint,
            menuLabel = legacyTypes.menuLabel,
            menuLabelSelected = legacyTypes.menuLabelSelected,
            menuLabelDisabled = legacyTypes.menuLabelDisabled,
            selectionLabel = legacyTypes.checkboxLabel,
            navigationLabel = legacyTypes.bnbLabel,
            navigationLabelSelected = legacyTypes.tabItemSelected,
            searchText = legacyTypes.searchEdit,
            searchHint = legacyTypes.searchHint,
        )
        val dimensions = OneUiDimensions()
        val shapes = OneUiShapes()
        val spacing = OneUiSpacing()
        val sizes = OneUiSizes()
        val elevation = OneUiElevation()
        val opacity = OneUiOpacity()
        val motion = OneUiMotionScheme(reducedMotion = reducedMotion ?: systemReducedMotion)

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
            LocalOneUiColors provides colors,
            LocalOneUiTypography provides typography,
            LocalOneUiDimensions provides dimensions,
            LocalOneUiShapes provides shapes,
            LocalOneUiSpacing provides spacing,
            LocalOneUiSizes provides sizes,
            LocalOneUiElevation provides elevation,
            LocalOneUiOpacity provides opacity,
            LocalOneUiMotion provides motion,
        ) {
            MaterialTheme(
                colorScheme = materialColors,
                shapes = Shapes(
                    extraSmall = RoundedCornerShape(8.dp),
                    small = RoundedCornerShape(12.dp),
                    medium = RoundedCornerShape(22.dp),
                    large = RoundedCornerShape(26.dp),
                    extraLarge = RoundedCornerShape(32.dp),
                ),
                content = content,
            )
        }
    }
}

/**
 * Maps the pinned SESL semantic palette into the Compose-native token surface.
 *
 * Keeping this mapping pure makes the light/dark contract independently testable and prevents
 * Material defaults or dynamic color providers from silently replacing One UI values.
 */
internal fun oneUiColorsFromLegacy(
    legacyColors: org.oneui.compose.theme.color.OneUIColorTheme,
    dark: Boolean,
): OneUiColors = OneUiColors(
    accent = legacyColors.seslControlActivatedColor,
    onAccent = if (dark) Color.Black else Color.White,
    background = legacyColors.seslBackgroundColor,
    surface = legacyColors.seslPreferenceRelativeCardBackground,
    surfaceElevated = legacyColors.seslBackgroundFloating,
    surfacePressed = legacyColors.seslListRippleColor,
    primaryText = legacyColors.seslPrimaryTextColor,
    secondaryText = legacyColors.seslSecondaryTextColor,
    divider = legacyColors.seslListDividerColor,
    controlInactive = legacyColors.seslControlNormalColor,
    destructive = legacyColors.seslFunctionalRed,
    accentStrong = legacyColors.seslSeekbarControlColorActivated,
    functionalPositive = legacyColors.seslFunctionalGreen,
    functionalWarning = legacyColors.seslFunctionalOrange,
    tooltipBackground = Color(0xff474747),
    tooltipContent = Color(0xfffafafa),
    tooltipActionBackground = Color(0x1a000000),
    progressTrack = legacyColors.seslProgressControlColorBackground,
    progressActive = legacyColors.seslProgressControlColorActivated,
    progressSecondary = legacyColors.seslLoadingProgressColor1,
    seekOverlapTrack = legacyColors.seslSeekbarOverlapColorDefault,
    seekOverlapActive = legacyColors.seslSeekbarOverlapColorActivated,
    seekDisabledActive = legacyColors.seslSeekbarDisableColorActivated,
    navigationBackground = legacyColors.seslNavigationBarBackground,
    navigationIcon = legacyColors.seslNavigationBarIcon,
    navigationText = legacyColors.seslNavigationBarText,
    navigationSelectedText = legacyColors.seslNavigationBarTextText,
    navigationRipple = legacyColors.seslNavigationBarRipple,
    tabIndicator = legacyColors.seslTablayoutMainTabIndicatorColor,
    tabSubIndicator = legacyColors.seslTablayoutSubtabIndicatorBackground,
    fabBackground = if (dark) Color(0xff3a3a3d) else Color(0xfffcfcff),
)

object OneUiTheme {
    val colors: OneUiColors
        @Composable get() = LocalOneUiColors.current

    val typography: OneUiTypography
        @Composable get() = LocalOneUiTypography.current

    val dimensions: OneUiDimensions
        @Composable get() = LocalOneUiDimensions.current

    val shapes: OneUiShapes
        @Composable get() = LocalOneUiShapes.current

    val spacing: OneUiSpacing
        @Composable get() = LocalOneUiSpacing.current

    val sizes: OneUiSizes
        @Composable get() = LocalOneUiSizes.current

    val elevation: OneUiElevation
        @Composable get() = LocalOneUiElevation.current

    val opacity: OneUiOpacity
        @Composable get() = LocalOneUiOpacity.current

    val motion: OneUiMotionScheme
        @Composable get() = LocalOneUiMotion.current

    val reducedMotion: Boolean
        @Composable get() = LocalOneUiMotion.current.reducedMotion
}
