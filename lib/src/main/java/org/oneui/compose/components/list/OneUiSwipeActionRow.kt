package org.oneui.compose.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIcon as OneUiIconView

/** Physical swipe direction. This intentionally does not mirror in RTL. */
enum class OneUiSwipeDirection {
    None,
    Left,
    Right,
}

/** Metadata rendered behind a swipeable row. */
@Immutable
data class OneUiSwipeAction(
    val label: String,
    val icon: OneUiIcon,
    val containerColor: Color,
    val contentColor: Color = Color.White,
)

/** Pure threshold mapping used by the gesture layer and unit tests. */
fun oneUiSwipeDirection(
    offsetPx: Float,
    thresholdPx: Float,
): OneUiSwipeDirection {
    if (!offsetPx.isFinite() || !thresholdPx.isFinite() || thresholdPx <= 0f) {
        return OneUiSwipeDirection.None
    }
    return when {
        offsetPx >= thresholdPx -> OneUiSwipeDirection.Right
        offsetPx <= -thresholdPx -> OneUiSwipeDirection.Left
        else -> OneUiSwipeDirection.None
    }
}

/**
 * Compose-native One UI swipe action row.
 *
 * Direction is based on the physical pointer axis rather than logical start/end so callers can
 * model SESL interactions such as the reference Stargazers sample where physical swipe-right is
 * always Call and physical swipe-left is always Message, including in RTL layouts.
 */
@Composable
fun OneUiSwipeActionRow(
    leftAction: OneUiSwipeAction,
    rightAction: OneUiSwipeAction,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    threshold: Dp = 64.dp,
    maxReveal: Dp = 96.dp,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val thresholdPx = with(density) { threshold.toPx() }
    val maxRevealPx = with(density) { maxReveal.toPx() }
    var dragOffsetPx by remember { mutableFloatStateOf(0f) }
    var activeDirection by remember { mutableStateOf(OneUiSwipeDirection.None) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = when (activeDirection) {
                    OneUiSwipeDirection.Left -> leftAction.label
                    OneUiSwipeDirection.Right -> rightAction.label
                    OneUiSwipeDirection.None -> ""
                }
            },
    ) {
        when {
            dragOffsetPx > 0f -> SwipeActionBackground(
                action = rightAction,
                modifier = Modifier.align(Alignment.CenterStart),
            )
            dragOffsetPx < 0f -> SwipeActionBackground(
                action = leftAction,
                modifier = Modifier.align(Alignment.CenterEnd),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(dragOffsetPx.roundToInt(), 0) }
                .pointerInput(enabled, thresholdPx, maxRevealPx) {
                    if (!enabled) return@pointerInput
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            when (oneUiSwipeDirection(dragOffsetPx, thresholdPx)) {
                                OneUiSwipeDirection.Left -> onSwipeLeft()
                                OneUiSwipeDirection.Right -> onSwipeRight()
                                OneUiSwipeDirection.None -> Unit
                            }
                            dragOffsetPx = 0f
                            activeDirection = OneUiSwipeDirection.None
                        },
                        onDragCancel = {
                            dragOffsetPx = 0f
                            activeDirection = OneUiSwipeDirection.None
                        },
                    ) { change, dragAmount ->
                        change.consume()
                        dragOffsetPx = (dragOffsetPx + dragAmount)
                            .coerceIn(-maxRevealPx, maxRevealPx)
                        activeDirection = when {
                            dragOffsetPx < 0f -> OneUiSwipeDirection.Left
                            dragOffsetPx > 0f -> OneUiSwipeDirection.Right
                            else -> OneUiSwipeDirection.None
                        }
                    }
                },
        ) {
            content()
        }
    }
}

@Composable
private fun SwipeActionBackground(
    action: OneUiSwipeAction,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(action.containerColor)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OneUiIconView(
            icon = action.icon,
            contentDescription = action.label,
            modifier = Modifier.size(24.dp),
            tint = action.contentColor,
        )
    }
}
