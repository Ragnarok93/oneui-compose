package org.oneui.compose.demo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oneui.compose.components.list.OneUiFastScrollerDisplayMode
import org.oneui.compose.demo.screens.CatalogActionModeSearch
import org.oneui.compose.demo.screens.CatalogStargazersFetchState
import org.oneui.compose.demo.screens.CatalogStargazersOptionsState
import org.oneui.compose.demo.screens.CatalogStargazersSettings
import org.oneui.compose.demo.screens.stargazerFastScrollerConfig
import org.oneui.compose.demo.screens.stargazerFastScrollerDisplayMode
import org.oneui.compose.demo.screens.stargazerNoItemText

class StargazersOptionsParityTest {
    @Test
    fun defaultsMatchPinnedReference() {
        val settings = CatalogStargazersSettings()
        assertFalse(settings.showIndexLetters)
        assertTrue(settings.autoHideIndexScroll)
        assertFalse(settings.showCancelButton)
        assertEquals(CatalogActionModeSearch.DISMISS, settings.actionModeSearch)
    }

    @Test
    fun indexScrollSettingsMatchPinnedReference() {
        assertEquals(
            OneUiFastScrollerDisplayMode.Dot,
            stargazerFastScrollerDisplayMode(CatalogStargazersSettings()),
        )
        assertEquals(
            OneUiFastScrollerDisplayMode.Text,
            stargazerFastScrollerDisplayMode(
                CatalogStargazersSettings(showIndexLetters = true),
            ),
        )
    }

    @Test
    fun indexScrollRenderConfigMatchesPinnedReference() {
        val defaults = stargazerFastScrollerConfig(CatalogStargazersSettings())
        assertEquals(OneUiFastScrollerDisplayMode.Dot, defaults.displayMode)
        assertTrue(defaults.autoHide)

        val explicit = stargazerFastScrollerConfig(
            CatalogStargazersSettings(
                showIndexLetters = true,
                autoHideIndexScroll = false,
            ),
        )
        assertEquals(OneUiFastScrollerDisplayMode.Text, explicit.displayMode)
        assertFalse(explicit.autoHide)
    }

    @Test
    fun optionsStageChangesUntilApplyAndCancelDiscardsDraft() {
        val committed = CatalogStargazersSettings()
        val opened = CatalogStargazersOptionsState(committed = committed).open()

        assertTrue(opened.isOpen)
        assertEquals(committed, opened.committed)
        assertEquals(committed, opened.draft)

        val edited = opened.updateDraft(
            opened.draft.copy(
                showIndexLetters = true,
                autoHideIndexScroll = false,
                showCancelButton = true,
                actionModeSearch = CatalogActionModeSearch.CONCURRENT,
            ),
        )
        assertEquals(committed, edited.committed)
        assertTrue(edited.draft.showIndexLetters)
        assertFalse(edited.draft.autoHideIndexScroll)
        assertTrue(edited.draft.showCancelButton)
        assertEquals(CatalogActionModeSearch.CONCURRENT, edited.draft.actionModeSearch)

        val cancelled = edited.cancel()
        assertFalse(cancelled.isOpen)
        assertEquals(committed, cancelled.committed)
        assertEquals(committed, cancelled.draft)

        val reapplied = cancelled.open().updateDraft(
            committed.copy(showIndexLetters = true, actionModeSearch = CatalogActionModeSearch.NO_DISMISS),
        ).apply()
        assertFalse(reapplied.isOpen)
        assertEquals(reapplied.draft, reapplied.committed)
        assertTrue(reapplied.committed.showIndexLetters)
        assertEquals(CatalogActionModeSearch.NO_DISMISS, reapplied.committed.actionModeSearch)
    }

    @Test
    fun labelsMatchPinnedReference() {
        assertEquals("Stargazers options", CatalogStargazersSettings.DialogTitle)
        assertEquals("Show index letters", CatalogStargazersSettings.ShowIndexLettersLabel)
        assertEquals("Autohide IndexScrollView", CatalogStargazersSettings.AutoHideIndexScrollLabel)
        assertEquals("Show cancel button", CatalogStargazersSettings.ShowCancelButtonLabel)
        assertEquals("ActionModeSearch options", CatalogStargazersSettings.ActionModeSearchLabel)
        assertEquals(listOf("DISMISS", "NO_DISMISS", "CONCURRENT"), CatalogActionModeSearch.entries.map { it.name })
    }

    @Test
    fun noItemTextMatchesPinnedFetchStateContract() {
        assertEquals("Loading stargazers...", stargazerNoItemText(CatalogStargazersFetchState.INITING, ""))
        assertEquals("Error loading stargazers.", stargazerNoItemText(CatalogStargazersFetchState.INIT_ERROR, ""))
        assertEquals("No stargazers yet.", stargazerNoItemText(CatalogStargazersFetchState.INITED, ""))
        assertEquals("No results found.", stargazerNoItemText(CatalogStargazersFetchState.INITED, "ada"))
        assertEquals("", stargazerNoItemText(CatalogStargazersFetchState.REFRESHING, ""))
        assertEquals("", stargazerNoItemText(CatalogStargazersFetchState.REFRESH_ERROR, ""))
    }
}
