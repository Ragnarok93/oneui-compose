package org.oneui.compose.components.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OneUiIndexedListModelTest {
    @Test
    fun indexEntriesKeepFirstOccurrenceOfEachSection() {
        assertEquals(
            listOf(
                OneUiIndexEntry(label = "A", itemIndex = 0),
                OneUiIndexEntry(label = "B", itemIndex = 2),
                OneUiIndexEntry(label = "#", itemIndex = 3),
            ),
            oneUiIndexEntries(
                items = listOf("Alpha", "Another", "Beta", "# tools"),
                label = { it },
            ),
        )
    }

    @Test
    fun blankLabelsUseFallbackSection() {
        assertEquals(
            listOf(OneUiIndexEntry(label = "#", itemIndex = 0)),
            oneUiIndexEntries(
                items = listOf("", "   "),
                label = { it },
            ),
        )
    }

    @Test
    fun railReservationUsesLogicalEndInLtrAndRtl() {
        val original = PaddingValues(start = 10.dp, top = 3.dp, end = 20.dp, bottom = 4.dp)

        val ltr = oneUiIndexedListContentPadding(
            contentPadding = original,
            layoutDirection = LayoutDirection.Ltr,
            reserveRail = true,
        )
        assertEquals(10.dp, ltr.calculateLeftPadding(LayoutDirection.Ltr))
        assertEquals(48.dp, ltr.calculateRightPadding(LayoutDirection.Ltr))

        val rtl = oneUiIndexedListContentPadding(
            contentPadding = original,
            layoutDirection = LayoutDirection.Rtl,
            reserveRail = true,
        )
        assertEquals(48.dp, rtl.calculateLeftPadding(LayoutDirection.Rtl))
        assertEquals(10.dp, rtl.calculateRightPadding(LayoutDirection.Rtl))
    }

    @Test
    fun noRailPreservesLogicalPadding() {
        val original = PaddingValues(start = 7.dp, top = 2.dp, end = 11.dp, bottom = 5.dp)
        val resolved = oneUiIndexedListContentPadding(
            contentPadding = original,
            layoutDirection = LayoutDirection.Rtl,
            reserveRail = false,
        )

        assertEquals(11.dp, resolved.calculateLeftPadding(LayoutDirection.Rtl))
        assertEquals(7.dp, resolved.calculateRightPadding(LayoutDirection.Rtl))
        assertEquals(2.dp, resolved.calculateTopPadding())
        assertEquals(5.dp, resolved.calculateBottomPadding())
    }

    @Test
    fun fastScrollerMapsTouchPositionToSection() {
        assertEquals(0, oneUiFastScrollerEntryIndex(positionY = 0f, height = 100f, entryCount = 5))
        assertEquals(2, oneUiFastScrollerEntryIndex(positionY = 50f, height = 100f, entryCount = 5))
        assertEquals(4, oneUiFastScrollerEntryIndex(positionY = 100f, height = 100f, entryCount = 5))
    }

    @Test
    fun fastScrollerClampsTouchOutsideRailBounds() {
        assertEquals(0, oneUiFastScrollerEntryIndex(positionY = -25f, height = 100f, entryCount = 5))
        assertEquals(4, oneUiFastScrollerEntryIndex(positionY = 160f, height = 100f, entryCount = 5))
    }

    @Test
    fun fastScrollerRejectsInvalidGeometry() {
        assertNull(oneUiFastScrollerEntryIndex(positionY = 20f, height = 100f, entryCount = 0))
        assertNull(oneUiFastScrollerEntryIndex(positionY = 20f, height = 0f, entryCount = 5))
        assertNull(oneUiFastScrollerEntryIndex(positionY = Float.NaN, height = 100f, entryCount = 5))
        assertNull(oneUiFastScrollerEntryIndex(positionY = 20f, height = Float.POSITIVE_INFINITY, entryCount = 5))
    }
}
