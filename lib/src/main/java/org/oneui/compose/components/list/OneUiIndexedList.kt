package org.oneui.compose.components.list

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.oneui.compose.motion.OneUiMotion
import org.oneui.compose.theme.OneUiTheme

/** A fast-scroll section label and the first backing item that belongs to it. */
@Immutable
data class OneUiIndexEntry(
    val label: String,
    val itemIndex: Int,
)

/** Visual representation used by [OneUiFastScroller]. */
enum class OneUiFastScrollerDisplayMode {
    Text,
    Dot,
}

/**
 * Builds stable alphabet/index entries in source order.
 *
 * Blank labels and labels beginning with punctuation share the `#` section. This mirrors the
 * useful part of SESL indexer behavior without coupling the Compose primitive to package-manager
 * or locale-specific data sources.
 */
fun <T> oneUiIndexEntries(
    items: List<T>,
    label: (T) -> String,
): List<OneUiIndexEntry> {
    val seen = linkedSetOf<String>()
    return buildList {
        items.forEachIndexed { index, item ->
            val first = label(item).trim().firstOrNull()
            val section = if (first != null && first.isLetterOrDigit()) {
                first.uppercaseChar().toString()
            } else {
                "#"
            }
            if (seen.add(section)) {
                add(OneUiIndexEntry(label = section, itemIndex = index))
            }
        }
    }
}

/**
 * Resolves caller padding into logical start/end values and reserves the fast-scroller rail on the
 * logical end side. Keeping this pure makes RTL behavior independently testable.
 */
internal fun oneUiIndexedListContentPadding(
    contentPadding: PaddingValues,
    layoutDirection: LayoutDirection,
    reserveRail: Boolean,
    railWidth: Dp = 28.dp,
): PaddingValues {
    val left = contentPadding.calculateLeftPadding(layoutDirection)
    val right = contentPadding.calculateRightPadding(layoutDirection)
    val logicalStart = if (layoutDirection == LayoutDirection.Ltr) left else right
    val logicalEnd = if (layoutDirection == LayoutDirection.Ltr) right else left

    return PaddingValues(
        start = logicalStart,
        top = contentPadding.calculateTopPadding(),
        end = logicalEnd + if (reserveRail) railWidth else 0.dp,
        bottom = contentPadding.calculateBottomPadding(),
    )
}

/**
 * Maps a physical pointer Y coordinate to the nearest proportional index-bar section.
 *
 * SESL's index scroll reacts continuously while the pointer is dragged along the rail. Keeping the
 * mapping pure avoids gesture-state edge cases and makes clamping/invalid geometry deterministic.
 */
internal fun oneUiFastScrollerEntryIndex(
    positionY: Float,
    height: Float,
    entryCount: Int,
): Int? {
    if (entryCount <= 0 || height <= 0f || !positionY.isFinite() || !height.isFinite()) return null
    if (entryCount == 1) return 0

    val fraction = (positionY / height).coerceIn(0f, 1f)
    return (fraction * entryCount)
        .toInt()
        .coerceIn(0, entryCount - 1)
}

/**
 * Reusable One UI indexed list with an optional alphabet rail.
 *
 * The data/key/content contract is intentionally generic so this can back contacts, app pickers,
 * media libraries, and other SESL-style indexed collections. Selecting or dragging across a rail
 * entry animates the list to that section's first item and exposes an accessibility action target
 * for every section.
 */
@Composable
fun <T, K : Any> OneUiIndexedList(
    items: List<T>,
    key: (T) -> K,
    label: (T) -> String,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    showFastScroller: Boolean = true,
    fastScrollerDisplayMode: OneUiFastScrollerDisplayMode = OneUiFastScrollerDisplayMode.Text,
    showFastScrollerPreview: Boolean = true,
    fastScrollerAutoHide: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(bottom = 12.dp),
    itemContent: @Composable LazyItemScope.(T) -> Unit,
) {
    val entries = oneUiIndexEntries(items, label)
    val scope = rememberCoroutineScope()
    val layoutDirection = LocalLayoutDirection.current
    val reserveRail = showFastScroller && entries.size > 1

    Box(modifier = modifier) {
        LazyColumn(
            state = state,
            modifier = Modifier.fillMaxSize(),
            contentPadding = oneUiIndexedListContentPadding(
                contentPadding = contentPadding,
                layoutDirection = layoutDirection,
                reserveRail = reserveRail,
            ),
        ) {
            items(
                items = items,
                key = { item -> key(item) },
            ) { item ->
                itemContent(item)
            }
        }

        if (reserveRail) {
            OneUiFastScroller(
                entries = entries,
                onIndexSelected = { itemIndex ->
                    scope.launch { state.animateScrollToItem(itemIndex) }
                },
                modifier = Modifier.align(Alignment.CenterEnd),
                displayMode = fastScrollerDisplayMode,
                showPreview = showFastScrollerPreview,
                autoHide = fastScrollerAutoHide,
                listScrollInProgress = state.isScrollInProgress,
            )
        }
    }
}

/**
 * Compact reusable One UI index rail with drag selection and an optional transient section preview.
 *
 * The touch rail itself remains 28dp wide so it does not steal input from list content. The preview
 * intentionally overlays inward from the logical end edge, matching the reference index-scroll
 * interaction without requiring callers to reserve extra layout width. Individual sections remain
 * independently clickable and accessible for keyboard/touch-exploration users.
 *
 * When [autoHide] is enabled, the rail follows the SESL index-scroll timing: it remains visible
 * while the list or rail is active, waits 500 ms after becoming idle, then fades linearly over
 * 150 ms. Reduced-motion mode keeps the timing contract but snaps the alpha change.
 */
@Composable
fun OneUiFastScroller(
    entries: List<OneUiIndexEntry>,
    onIndexSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    displayMode: OneUiFastScrollerDisplayMode = OneUiFastScrollerDisplayMode.Text,
    showPreview: Boolean = true,
    autoHide: Boolean = false,
    listScrollInProgress: Boolean = false,
) {
    var railHeightPx by remember { mutableIntStateOf(0) }
    var activeEntryIndex by remember(entries) { mutableStateOf<Int?>(null) }
    var interactionGeneration by remember { mutableIntStateOf(0) }
    val activeEntry = activeEntryIndex?.let(entries::getOrNull)
    val reducedMotion = OneUiTheme.reducedMotion
    val railAlpha = remember(autoHide) { Animatable(1f) }

    LaunchedEffect(
        autoHide,
        listScrollInProgress,
        activeEntryIndex,
        interactionGeneration,
        reducedMotion,
    ) {
        if (!autoHide) {
            railAlpha.snapTo(1f)
            return@LaunchedEffect
        }

        railAlpha.snapTo(1f)
        if (!listScrollInProgress && activeEntryIndex == null) {
            delay(OneUiMotion.Delay.FastScrollerAutoHide.toLong())
            if (reducedMotion) {
                railAlpha.snapTo(0f)
            } else {
                railAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = OneUiMotion.fastScrollerFade(),
                )
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(84.dp)
            .alpha(railAlpha.value),
        contentAlignment = Alignment.CenterEnd,
    ) {
        if (showPreview && activeEntry != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(44.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(OneUiTheme.colors.surfaceElevated),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = activeEntry.label,
                    color = OneUiTheme.colors.primaryText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(28.dp)
                .padding(vertical = 8.dp)
                .onSizeChanged { railHeightPx = it.height }
                .pointerInput(entries, railHeightPx) {
                    if (entries.isEmpty() || railHeightPx <= 0) return@pointerInput
                    var lastDragIndex: Int? = null

                    fun selectAt(positionY: Float) {
                        val index = oneUiFastScrollerEntryIndex(
                            positionY = positionY,
                            height = railHeightPx.toFloat(),
                            entryCount = entries.size,
                        ) ?: return
                        activeEntryIndex = index
                        if (index != lastDragIndex) {
                            lastDragIndex = index
                            onIndexSelected(entries[index].itemIndex)
                        }
                    }

                    detectVerticalDragGestures(
                        onDragStart = { offset ->
                            interactionGeneration++
                            lastDragIndex = null
                            selectAt(offset.y)
                        },
                        onDragEnd = {
                            interactionGeneration++
                            lastDragIndex = null
                            activeEntryIndex = null
                        },
                        onDragCancel = {
                            interactionGeneration++
                            lastDragIndex = null
                            activeEntryIndex = null
                        },
                        onVerticalDrag = { change, _ ->
                            selectAt(change.position.y)
                            change.consume()
                        },
                    )
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly,
        ) {
            entries.forEachIndexed { index, entry ->
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .semantics {
                            contentDescription = "Scroll to ${entry.label}"
                        }
                        .clickable {
                            interactionGeneration++
                            activeEntryIndex = index
                            onIndexSelected(entry.itemIndex)
                            activeEntryIndex = null
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    when (displayMode) {
                        OneUiFastScrollerDisplayMode.Text -> Text(
                            text = entry.label,
                            color = OneUiTheme.colors.accent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        OneUiFastScrollerDisplayMode.Dot -> Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(OneUiTheme.colors.accent),
                        )
                    }
                }
            }
        }
    }
}
