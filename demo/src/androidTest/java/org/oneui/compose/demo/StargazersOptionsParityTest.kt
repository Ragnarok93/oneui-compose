package org.oneui.compose.demo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oneui.compose.demo.screens.CatalogActionModeSearch
import org.oneui.compose.demo.screens.CatalogStargazersFetchState
import org.oneui.compose.demo.screens.CatalogStargazersSettings
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
