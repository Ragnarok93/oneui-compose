package org.oneui.compose.demo

import org.junit.Assert.assertEquals
import org.junit.Test
import org.oneui.compose.demo.screens.CatalogStargazerAction
import org.oneui.compose.demo.screens.stargazerActionFeedback
import org.oneui.compose.icons.OneUiIcons

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
}
