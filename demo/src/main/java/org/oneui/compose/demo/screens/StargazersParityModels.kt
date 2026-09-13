package org.oneui.compose.demo.screens

enum class CatalogActionModeSearch {
    DISMISS,
    NO_DISMISS,
    CONCURRENT,
}

data class CatalogStargazersSettings(
    val showIndexLetters: Boolean = false,
    val autoHideIndexScroll: Boolean = true,
    val actionModeSearch: CatalogActionModeSearch = CatalogActionModeSearch.DISMISS,
    val showCancelButton: Boolean = false,
) {
    companion object {
        const val DialogTitle = "Stargazers options"
        const val ShowIndexLettersLabel = "Show index letters"
        const val AutoHideIndexScrollLabel = "Autohide IndexScrollView"
        const val ShowCancelButtonLabel = "Show cancel button"
        const val ActionModeSearchLabel = "ActionModeSearch options"
    }
}

enum class CatalogStargazersFetchState {
    NOT_INIT,
    INITING,
    INIT_ERROR,
    INITED,
    REFRESHING,
    REFRESH_ERROR,
    REFRESHED,
}

fun stargazerNoItemText(
    fetchState: CatalogStargazersFetchState,
    query: String,
): String = when (fetchState) {
    CatalogStargazersFetchState.INITING -> "Loading stargazers..."
    CatalogStargazersFetchState.INIT_ERROR -> "Error loading stargazers."
    CatalogStargazersFetchState.INITED,
    CatalogStargazersFetchState.REFRESHED,
    -> if (query.isEmpty()) "No stargazers yet." else "No results found."

    CatalogStargazersFetchState.NOT_INIT,
    CatalogStargazersFetchState.REFRESHING,
    CatalogStargazersFetchState.REFRESH_ERROR,
    -> ""
}
