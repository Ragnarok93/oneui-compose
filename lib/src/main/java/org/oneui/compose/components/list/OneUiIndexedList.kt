package org.oneui.compose.components.list

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.oneui.compose.theme.OneUiTheme

/** A fast-scroll section label and the first backing item that belongs to it. */
@Immutable
data class OneUiIndexEntry(
    val label: String,
    val itemIndex: Int,
)

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
 * Reusable One UI indexed list with an optional alphabet rail.
 *
 * The data/key/content contract is intentionally generic so this can back contacts, app pickers,
 * media libraries, and other SESL-style indexed collections. Selecting a rail entry animates the
 * list to that section's first item and exposes an accessibility action target for every section.
 */
@Composable
fun <T, K : Any> OneUiIndexedList(
    items: List<T>,
    key: (T) -> K,
    label: (T) -> String,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    showFastScroller: Boolean = true,
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
            )
        }
    }
}

/**
 * Compact reusable One UI index rail.
 *
 * Callers decide how an index selection maps to scrolling, allowing reuse with lazy lists, grids,
 * paged data, or non-Compose containers. Each section is independently accessible.
 */
@Composable
fun OneUiFastScroller(
    entries: List<OneUiIndexEntry>,
    onIndexSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(28.dp)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly,
    ) {
        entries.forEach { entry ->
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .semantics {
                        contentDescription = "Scroll to ${entry.label}"
                    }
                    .clickable { onIndexSelected(entry.itemIndex) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = entry.label,
                    color = OneUiTheme.colors.accent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
