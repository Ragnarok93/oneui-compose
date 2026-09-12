package org.oneui.compose.patterns.list

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OneUiSelectableListStateTest {
    @Test
    fun toggleEntersSelectionModeAndClearLeavesIt() {
        val state = OneUiSelectableListState<Long>()

        state.toggle(10L)
        assertTrue(state.isSelectionMode)
        assertEquals(setOf(10L), state.selectedKeys)

        state.clear()
        assertFalse(state.isSelectionMode)
        assertTrue(state.selectedKeys.isEmpty())
    }

    @Test
    fun selectAllUsesOnlySelectableKeysAndReportsAllSelected() {
        val state = OneUiSelectableListState<Long>()
        val selectable = listOf(1L, 2L, 4L)

        state.selectAll(selectable)

        assertEquals(selectable.toSet(), state.selectedKeys)
        assertTrue(state.allSelected(selectable))
        assertFalse(state.allSelected(listOf(1L, 2L, 3L, 4L)))
    }

    @Test
    fun replaceSelectionDropsKeysNoLongerPresent() {
        val state = OneUiSelectableListState<Long>()
        state.selectAll(listOf(1L, 2L, 3L))

        state.retainKeys(setOf(2L, 3L, 4L))

        assertEquals(setOf(2L, 3L), state.selectedKeys)
    }
}
