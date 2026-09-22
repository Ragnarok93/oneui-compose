package org.oneui.compose.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Stable One UI color contract used by Compose-native components. */
@Immutable
data class OneUiColors(
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
    /** Strong activated blue used by progress and seekbar tracks in the SESL palette. */
    val accentStrong: Color = Color(0xff0381fe),
    val functionalPositive: Color = Color(0xff14a866),
    val functionalWarning: Color = Color(0xffef5e16),
    val tooltipBackground: Color = Color(0xff474747),
    val tooltipContent: Color = Color(0xfffafafa),
    val tooltipActionBackground: Color = Color(0x1a000000),
    val progressTrack: Color = Color(0x66cacaca),
    val progressActive: Color = Color(0xff0381fe),
    val progressSecondary: Color = Color(0xff00d694),
    val seekOverlapTrack: Color = Color(0x66cacaca),
    val seekOverlapActive: Color = Color(0xffef5e16),
    val seekDisabledActive: Color = Color(0xffd2d2d2),
    val navigationBackground: Color = Color(0xfff2f2f2),
    val navigationIcon: Color = Color(0xff454545),
    val navigationText: Color = Color(0xff636363),
    val navigationSelectedText: Color = Color(0xff0072de),
    val navigationRipple: Color = Color(0x0d000000),
    val tabIndicator: Color = Color(0xff252525),
    val tabSubIndicator: Color = Color(0x0d000000),
    val tabSelectedText: Color = Color(0xff010101),
    val tabUnselectedText: Color = Color(0xff8c8c8c),
    val tabSubSelectedText: Color = Color(0xff252525),
    val tabSubUnselectedText: Color = Color(0xff8c8c8c),
    val tabRoundedBackground: Color = Color(0xffe4e4e7),
    val tabRoundedSelectedText: Color = Color(0xfffafaff),
    val tabRoundedUnselectedText: Color = Color(0xff848487),
    val fabBackground: Color = Color(0xfffcfcfc),
)

/** Component-oriented typography translated from the existing One UI theme surface. */
@Immutable
data class OneUiTypography(
    val buttonLabel: TextStyle,
    val title: TextStyle,
    val largeTitle: TextStyle,
    val subtitle: TextStyle,
    val sectionLabel: TextStyle,
    val listTitle: TextStyle,
    val listSummary: TextStyle,
    val dialogTitle: TextStyle,
    val dialogBody: TextStyle,
    val dialogButtonLabel: TextStyle,
    val inputText: TextStyle,
    val inputHint: TextStyle,
    val menuLabel: TextStyle,
    val menuLabelSelected: TextStyle,
    val menuLabelDisabled: TextStyle,
    val selectionLabel: TextStyle,
    val navigationLabel: TextStyle,
    val navigationLabelSelected: TextStyle,
    val searchText: TextStyle,
    val searchHint: TextStyle,
)

@Immutable
data class OneUiDimensions(
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

@Immutable
data class OneUiShapes(
    val card: Shape = RoundedCornerShape(26.dp),
    val nestedCard: Shape = RoundedCornerShape(22.dp),
    val control: Shape = RoundedCornerShape(18.dp),
    val button: Shape = RoundedCornerShape(24.dp),
    val iconButton: Shape = CircleShape,
    val sheet: Shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
)

@Immutable
data class OneUiSpacing(
    val screenHorizontal: Dp = 24.dp,
    val section: Dp = 20.dp,
    val cardContent: Dp = 20.dp,
    val listHorizontal: Dp = 18.dp,
    val listVertical: Dp = 12.dp,
    val controlGap: Dp = 8.dp,
)

@Immutable
data class OneUiSizes(
    val icon: Dp = 24.dp,
    val compactIcon: Dp = 20.dp,
    val touchTarget: Dp = 48.dp,
    val floatingBarHeight: Dp = 46.dp,
    val listItemMinHeight: Dp = 64.dp,
    val focusRingWidth: Dp = 2.dp,
)

/** Library-level elevation defaults. These are not presented as extracted Samsung measurements. */
@Immutable
data class OneUiElevation(
    val flat: Dp = 0.dp,
    val raised: Dp = 2.dp,
    val floating: Dp = 6.dp,
)

/** Existing scaffold opacity values centralized for stable components. */
@Immutable
data class OneUiOpacity(
    val disabledContainer: Float = 0.45f,
    val disabledContent: Float = 0.55f,
    val subtleSurface: Float = 0.14f,
    val inactiveControl: Float = 0.16f,
)
