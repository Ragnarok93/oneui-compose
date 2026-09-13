package org.oneui.compose.demo.screens

import org.oneui.compose.components.list.OneUiFastScrollerDisplayMode
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIcons

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

data class CatalogStargazersOptionsState(
    val committed: CatalogStargazersSettings,
    val draft: CatalogStargazersSettings = committed,
    val isOpen: Boolean = false,
) {
    fun open(): CatalogStargazersOptionsState = copy(
        draft = committed,
        isOpen = true,
    )

    fun updateDraft(settings: CatalogStargazersSettings): CatalogStargazersOptionsState =
        copy(draft = settings)

    fun cancel(): CatalogStargazersOptionsState = copy(
        draft = committed,
        isOpen = false,
    )

    fun apply(): CatalogStargazersOptionsState = copy(
        committed = draft,
        isOpen = false,
    )
}

enum class CatalogStargazerAction(
    val label: String,
    val icon: OneUiIcon,
) {
    Share("Share", OneUiIcons.Share),
    Delete("Delete", OneUiIcons.Delete),
    Message("Message", OneUiIcons.Message),
    Meet("Meet", OneUiIcons.Meet),
    Block("Block", OneUiIcons.Block),
}

fun stargazerActionFeedback(
    selectedCount: Int,
    action: CatalogStargazerAction,
): String = "$selectedCount contacts selected for ${action.label}"

data class StargazerFastScrollerConfig(
    val displayMode: OneUiFastScrollerDisplayMode,
    val autoHide: Boolean,
)

fun stargazerFastScrollerDisplayMode(
    settings: CatalogStargazersSettings,
): OneUiFastScrollerDisplayMode =
    if (settings.showIndexLetters) {
        OneUiFastScrollerDisplayMode.Text
    } else {
        OneUiFastScrollerDisplayMode.Dot
    }

fun stargazerFastScrollerConfig(
    settings: CatalogStargazersSettings,
): StargazerFastScrollerConfig = StargazerFastScrollerConfig(
    displayMode = stargazerFastScrollerDisplayMode(settings),
    autoHide = settings.autoHideIndexScroll,
)

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
