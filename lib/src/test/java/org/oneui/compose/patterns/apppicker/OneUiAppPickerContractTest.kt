package org.oneui.compose.patterns.apppicker

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OneUiAppPickerContractTest {
    @Test
    fun listTypesMatchPinnedReferenceExactly() {
        assertEquals(
            listOf(
                "List",
                "List + action button",
                "List + checkbox",
                "List + radio button",
                "List + switch",
                "Grid",
                "Grid + checkbox",
            ),
            OneUiAppPickerListType.entries.map { it.label },
        )
        assertFalse(OneUiAppPickerListType.List.isGrid)
        assertTrue(OneUiAppPickerListType.Grid.isGrid)
        assertTrue(OneUiAppPickerListType.GridCheckbox.isGrid)
        assertEquals(OneUiAppPickerSelectionControl.Radio, OneUiAppPickerListType.ListRadio.selectionControl)
    }

    @Test
    fun multiSelectionStateSupportsToggleSelectAllAndClear() {
        val state = OneUiAppPickerState<String>()

        state.toggle("calendar")
        assertEquals(setOf("calendar"), state.selectedKeys)

        state.selectAll(listOf("calendar", "clock", "gallery"))
        assertEquals(3, state.selectedCount)
        assertTrue(state.allSelected(listOf("calendar", "clock", "gallery")))

        state.clear()
        assertTrue(state.selectedKeys.isEmpty())
    }

    @Test
    fun exclusiveSelectionKeepsOnlyLatestItem() {
        val state = OneUiAppPickerState<String>()

        state.setSelected("calendar", selected = true, exclusive = true)
        state.setSelected("clock", selected = true, exclusive = true)

        assertEquals(setOf("clock"), state.selectedKeys)
    }
}
