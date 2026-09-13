package org.oneui.compose.demo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.oneui.compose.demo.screens.CatalogStargazerAction
import org.oneui.compose.demo.screens.executeStargazerAction
import org.oneui.compose.demo.screens.stargazerActionFeedback
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.patterns.list.OneUiSelectableListState

class StargazersActionModeParityTest {
    @Test
    fun actionsMatchPinnedReferenceMenuOrderLabelsAndIcons() {
        assertEquals(
            listOf("Share", "Delete", "Message", "Meet", "Block"),
            CatalogStargazerAction.entries.map(CatalogStargazerAction::label),
        )
        assertEquals(OneUiIcons.Share, CatalogStargazerAction.Share.icon)
        assertEquals(OneUiIcons.Delete, CatalogStargazerAction.Delete.icon)
        assertEquals(OneUiIcons.Message, CatalogStargazerAction.Message.icon)
        assertEquals(OneUiIcons.Meet, CatalogStargazerAction.Meet.icon)
        assertEquals(OneUiIcons.Block, CatalogStargazerAction.Block.icon)
    }

    @Test
    fun actionFeedbackMatchesPinnedReferenceText() {
        assertEquals(
            "3 contacts selected for Message",
            stargazerActionFeedback(3, CatalogStargazerAction.Message),
        )
    }

    @Test
    fun executingActionReportsSelectionThenExitsActionMode() {
        val selection = OneUiSelectableListState<Long>().apply {
            selectAll(listOf(1L, 2L, 3L))
        }

        val feedback = executeStargazerAction(
            selectionState = selection,
            action = CatalogStargazerAction.Meet,
        )

        assertEquals("3 contacts selected for Meet", feedback)
        assertEquals(0, selection.selectedCount)
        assertFalse(selection.isSelectionMode)
    }
}
