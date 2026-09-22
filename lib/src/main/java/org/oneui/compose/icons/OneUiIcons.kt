package org.oneui.compose.icons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import dev.oneuiproject.oneui.R as OneUiIconResources
import kotlin.math.roundToInt
import org.oneui.compose.interaction.oneUiInteractive
import org.oneui.compose.theme.OneUiTheme

/**
 * Stable icon handle for the Compose One UI surface.
 *
 * [Resource] entries point directly at the MIT-licensed `io.github.oneuiproject:icons:1.1.0`
 * dependency. [Vector] is reserved for small clean-room fallbacks when that artifact has no
 * suitable glyph, so callers do not have to care where a glyph is sourced.
 */
@Immutable
sealed interface OneUiIcon {
    @Immutable
    data class Resource(@DrawableRes val id: Int) : OneUiIcon

    /** A resource selector whose selected state is explicitly controlled by Compose. */
    @Immutable
    data class StatefulResource(
        @DrawableRes val id: Int,
        val selected: Boolean,
    ) : OneUiIcon

    @Immutable
    data class Vector(val imageVector: ImageVector) : OneUiIcon
}

/** Curated, reusable One UI icon catalog. */
object OneUiIcons {
    val Add = resource(OneUiIconResources.drawable.ic_oui_add)
    val Remove = resource(OneUiIconResources.drawable.ic_oui_remove)
    val Search = resource(OneUiIconResources.drawable.ic_oui_search)
    val Settings = resource(OneUiIconResources.drawable.ic_oui_settings)
    val More = resource(OneUiIconResources.drawable.ic_oui_more)
    val MoreVertical = More

    val Back = resource(OneUiIconResources.drawable.ic_oui_back)
    val Forward = resource(OneUiIconResources.drawable.ic_oui_arrow_right)
    val ArrowLeft = resource(OneUiIconResources.drawable.ic_oui_arrow_left)
    val ArrowRight = resource(OneUiIconResources.drawable.ic_oui_arrow_right)
    val ArrowUp = resource(OneUiIconResources.drawable.ic_oui_arrow_up)
    val ArrowDown = resource(OneUiIconResources.drawable.ic_oui_arrow_down)
    val ChevronLeft = resource(OneUiIconResources.drawable.ic_oui_keyboard_arrow_left)
    val ChevronRight = resource(OneUiIconResources.drawable.ic_oui_keyboard_arrow_right)
    val ChevronUp = resource(OneUiIconResources.drawable.ic_oui_keyboard_arrow_up)
    val ChevronDown = resource(OneUiIconResources.drawable.ic_oui_keyboard_arrow_down)

    val Check = resource(OneUiIconResources.drawable.ic_oui_selected)
    val CheckboxChecked = resource(OneUiIconResources.drawable.ic_oui_checkbox_checked)
    val CheckboxUnchecked = resource(OneUiIconResources.drawable.ic_oui_checkbox_unchecked)
    val RadioSelected = OneUiIcon.Vector(radioVector(selected = true))
    val RadioUnselected = OneUiIcon.Vector(radioVector(selected = false))

    val Close = resource(OneUiIconResources.drawable.ic_oui_close)
    val Refresh = resource(OneUiIconResources.drawable.ic_oui_refresh)
    val Copy = resource(OneUiIconResources.drawable.ic_oui_copy)
    val Delete = resource(OneUiIconResources.drawable.ic_oui_delete)
    val Share = resource(OneUiIconResources.drawable.ic_oui_share)
    val Call = resource(OneUiIconResources.drawable.ic_oui_wifi_call)
    val Message = resource(OneUiIconResources.drawable.ic_oui_message_chat)
    val Meet = resource(OneUiIconResources.drawable.ic_oui_google_duo_outline)
    val Block = resource(OneUiIconResources.drawable.ic_oui_block)
    val Email = resource(OneUiIconResources.drawable.ic_oui_email)
    val Website = resource(OneUiIconResources.drawable.ic_oui_internet_website)
    val QrCode = resource(OneUiIconResources.drawable.ic_oui_qr_code)
    val Info = resource(OneUiIconResources.drawable.ic_oui_info)
    val Star = resource(OneUiIconResources.drawable.ic_oui_star)
    val Error = resource(OneUiIconResources.drawable.ic_oui_error)
    val Play = resource(OneUiIconResources.drawable.ic_oui_control_play)
    val Pause = resource(OneUiIconResources.drawable.ic_oui_control_pause)
    val Grid = resource(OneUiIconResources.drawable.ic_oui_list_grid)
    val Home = resource(OneUiIconResources.drawable.ic_oui_home)
    val Motion = resource(OneUiIconResources.drawable.ic_oui_motion)

    val NavigationBack = resource(OneUiIconResources.drawable.ic_oui_sysbar_back)
    val NavigationHome = resource(OneUiIconResources.drawable.ic_oui_sysbar_home)
    val NavigationRecents = resource(OneUiIconResources.drawable.ic_oui_sysbar_recent)

    /** Selected-state resources used by the pinned SESL8 BottomTabLayout sample. */
    fun clockAlarmTab(selected: Boolean): OneUiIcon = OneUiIcon.StatefulResource(
        id = OneUiIconResources.drawable.ic_clock_alarm_tab,
        selected = selected,
    )

    fun clockTimerTab(selected: Boolean): OneUiIcon = OneUiIcon.StatefulResource(
        id = OneUiIconResources.drawable.ic_clock_timer_tab,
        selected = selected,
    )

    fun clockStopwatchTab(selected: Boolean): OneUiIcon = OneUiIcon.StatefulResource(
        id = OneUiIconResources.drawable.ic_clock_stopwatch_tab,
        selected = selected,
    )

    private fun resource(@DrawableRes id: Int): OneUiIcon = OneUiIcon.Resource(id)

    /** Original fallback on the same 24dp optical grid as the compatibility vectors. */
    private fun radioVector(selected: Boolean): ImageVector = ImageVector.Builder(
        name = if (selected) "RadioSelected" else "RadioUnselected",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ).apply {
        addPath(
            pathData = PathParser()
                .parsePathString("M12,3.75 A8.25,8.25 0,1 0,12,20.25 A8.25,8.25 0,1 0,12,3.75")
                .toNodes(),
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.8f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        )
        if (selected) {
            addPath(
                pathData = PathParser()
                    .parsePathString("M12,8 A4,4 0,1 0,12,16 A4,4 0,1 0,12,8")
                    .toNodes(),
                fill = SolidColor(Color.Black),
            )
        }
    }.build()
}

/** Renders a stable [OneUiIcon] regardless of whether its source is a drawable or vector. */
@Composable
fun OneUiIcon(
    icon: OneUiIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = OneUiTheme.colors.primaryText,
) {
    when (icon) {
        is OneUiIcon.Resource, is OneUiIcon.StatefulResource -> {
            val context = LocalContext.current
            val configuration = LocalConfiguration.current
            val density = LocalDensity.current.density
            val resourceId = when (icon) {
                is OneUiIcon.Resource -> icon.id
                is OneUiIcon.StatefulResource -> icon.id
                else -> error("unreachable")
            }
            val selected = (icon as? OneUiIcon.StatefulResource)?.selected
            val painter = remember(resourceId, selected, configuration.densityDpi, configuration.uiMode, density) {
                val drawable = requireNotNull(ContextCompat.getDrawable(context, resourceId)) {
                    "Unable to resolve One UI drawable resource $resourceId"
                }
                if (selected != null) {
                    drawable.state = intArrayOf(
                        if (selected) android.R.attr.state_selected else -android.R.attr.state_selected,
                    )
                }
                val fallbackSizePx = (24f * density).roundToInt().coerceAtLeast(1)
                val width = drawable.intrinsicWidth.takeIf { it > 0 } ?: fallbackSizePx
                val height = drawable.intrinsicHeight.takeIf { it > 0 } ?: fallbackSizePx
                BitmapPainter(drawable.toBitmap(width, height).asImageBitmap())
            }
            Image(
                painter = painter,
                contentDescription = contentDescription,
                modifier = modifier,
                colorFilter = ColorFilter.tint(tint),
            )
        }
        is OneUiIcon.Vector -> Icon(
            imageVector = icon.imageVector,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint,
        )
    }
}

/**
 * Accessible One UI icon action with a 48dp minimum touch target and shared press/focus behavior.
 * A description is mandatory because this component always performs an action.
 */
@Composable
fun OneUiIconButton(
    icon: OneUiIcon,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = OneUiTheme.colors.primaryText,
    interactionSource: MutableInteractionSource? = null,
) {
    val resolvedInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val colors = OneUiTheme.colors
    val sizes = OneUiTheme.sizes
    val opacity = OneUiTheme.opacity
    val resolvedTint = if (enabled) tint else colors.controlInactive.copy(alpha = opacity.disabledContent)

    Box(
        modifier = modifier
            .sizeIn(minWidth = sizes.touchTarget, minHeight = sizes.touchTarget)
            .clip(CircleShape)
            .oneUiInteractive(
                enabled = enabled,
                onClick = onClick,
                interactionSource = resolvedInteractionSource,
                role = Role.Button,
                shape = CircleShape,
                pressedScale = 0.92f,
            )
            .semantics(mergeDescendants = true) {
                this.contentDescription = contentDescription
            },
        contentAlignment = Alignment.Center,
    ) {
        OneUiIcon(
            icon = icon,
            contentDescription = null,
            modifier = Modifier.size(sizes.icon),
            tint = resolvedTint,
        )
    }
}
