package org.oneui.compose.components.list

import org.junit.Assert.assertEquals
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
}
