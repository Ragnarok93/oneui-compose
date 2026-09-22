package org.oneui.compose.demo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.oneui.compose.demo.screens.CatalogStargazer
import org.oneui.compose.demo.screens.CatalogStargazersFetchEvent
import org.oneui.compose.demo.screens.CatalogStargazersFetchState
import org.oneui.compose.demo.screens.CatalogStargazersFetchUiState
import org.oneui.compose.demo.screens.stargazerActionModeSearchFieldVisible
import org.oneui.compose.demo.screens.stargazerActionModeSelectableKeys
import org.oneui.compose.demo.screens.CatalogActionModeSearch
import org.oneui.compose.demo.screens.stargazerProfileActions
import org.oneui.compose.demo.screens.CatalogStargazerProfileAction
import org.oneui.compose.demo.screens.stargazerProfileActionTarget
import org.oneui.compose.demo.screens.stargazerVCardContent

class StargazersFetchStateTest {
    private val ada = CatalogStargazer(
        id = 1,
        name = "Ada Lovelace",
        login = "ada",
        url = "https://example.com/ada",
    )

    @Test
    fun initialFailureExposesRetryAndSuccessfulRetryPublishesRows() {
        val loading = CatalogStargazersFetchUiState().reduce(
            CatalogStargazersFetchEvent.BeginInitialLoad,
        )
        assertEquals(CatalogStargazersFetchState.INITING, loading.fetchState)
        assertTrue(loading.isLoading)
        assertFalse(loading.canRetry)

        val failed = loading.reduce(
            CatalogStargazersFetchEvent.Failed("Offline"),
        )
        assertEquals(CatalogStargazersFetchState.INIT_ERROR, failed.fetchState)
        assertEquals("Offline", failed.errorMessage)
        assertTrue(failed.canRetry)
        assertFalse(failed.isLoading)

        val retried = failed.reduce(CatalogStargazersFetchEvent.Retry)
        assertEquals(CatalogStargazersFetchState.INITING, retried.fetchState)
        val loaded = retried.reduce(
            CatalogStargazersFetchEvent.Succeeded(listOf(ada)),
        )
        assertEquals(CatalogStargazersFetchState.INITED, loaded.fetchState)
        assertEquals(listOf(ada), loaded.profiles)
        assertNull(loaded.errorMessage)
    }

    @Test
    fun refreshFailureRetainsCachedRowsAndRetryCanRecover() {
        val loaded = CatalogStargazersFetchUiState(
            profiles = listOf(ada),
            fetchState = CatalogStargazersFetchState.INITED,
        )
        val refreshing = loaded.reduce(CatalogStargazersFetchEvent.BeginRefresh)
        assertEquals(CatalogStargazersFetchState.REFRESHING, refreshing.fetchState)
        assertEquals(listOf(ada), refreshing.profiles)

        val failed = refreshing.reduce(
            CatalogStargazersFetchEvent.Failed("Rate limit"),
        )
        assertEquals(CatalogStargazersFetchState.REFRESH_ERROR, failed.fetchState)
        assertEquals(listOf(ada), failed.profiles)
        assertTrue(failed.canRetry)

        val recovered = failed
            .reduce(CatalogStargazersFetchEvent.Retry)
            .reduce(CatalogStargazersFetchEvent.Succeeded(listOf(ada.copy(name = "Ada Byron"))))
        assertEquals(CatalogStargazersFetchState.REFRESHED, recovered.fetchState)
        assertEquals("Ada Byron", recovered.profiles.single().name)
    }

    @Test
    fun initialErrorRetriesInitialLoadEvenWhenCachedRowsArePresent() {
        val failed = CatalogStargazersFetchUiState(
            profiles = listOf(ada),
            fetchState = CatalogStargazersFetchState.INIT_ERROR,
            errorMessage = "Offline",
        )

        val retried = failed.reduce(CatalogStargazersFetchEvent.Retry)

        assertEquals(CatalogStargazersFetchState.INITING, retried.fetchState)
        assertTrue(retried.isLoading)
    }

    @Test
    fun actionModeSearchAndSelectAllUseReferenceSemantics() {
        assertFalse(
            stargazerActionModeSearchFieldVisible(
                selectionMode = true,
                searchMode = CatalogActionModeSearch.DISMISS,
            ),
        )
        assertTrue(
            stargazerActionModeSearchFieldVisible(
                selectionMode = true,
                searchMode = CatalogActionModeSearch.NO_DISMISS,
            ),
        )
        assertTrue(
            stargazerActionModeSearchFieldVisible(
                selectionMode = true,
                searchMode = CatalogActionModeSearch.CONCURRENT,
            ),
        )
        assertEquals(listOf(1L), stargazerActionModeSelectableKeys(listOf(ada)))
    }

    @Test
    fun nullableProfileActionsIgnoreBlankReferenceFields() {
        val sparse = ada.copy(
            url = " ",
            email = " ",
            twitterUsername = "",
            blog = "   ",
        )
        assertEquals(
            listOf(CatalogStargazerProfileAction.GitHub),
            stargazerProfileActions(sparse),
        )
        assertNull(stargazerProfileActionTarget(sparse, CatalogStargazerProfileAction.GitHub))
        assertNull(stargazerProfileActionTarget(sparse, CatalogStargazerProfileAction.X))
        assertNull(stargazerProfileActionTarget(sparse, CatalogStargazerProfileAction.Email))
        assertNull(stargazerProfileActionTarget(sparse, CatalogStargazerProfileAction.Blog))
    }

    @Test
    fun vCardIncludesOptionalReferenceAvatarPayload() {
        val vCard = ada.copy(avatarBase64 = "AQID").let(::stargazerVCardContent)
        assertTrue(vCard.contains("PHOTO;ENCODING=BASE64;TYPE=PNG:AQID\n"))
    }
}
