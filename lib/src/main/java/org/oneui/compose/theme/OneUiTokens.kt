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
