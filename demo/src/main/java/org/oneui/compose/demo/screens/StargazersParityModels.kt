package org.oneui.compose.demo.screens

import org.oneui.compose.components.list.OneUiFastScrollerDisplayMode
import org.oneui.compose.components.list.OneUiSwipeDirection
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.patterns.list.OneUiSelectableListState

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

/** State transitions exposed by the reference Stargazers repository and refresh UI. */
sealed interface CatalogStargazersFetchEvent {
    data object BeginInitialLoad : CatalogStargazersFetchEvent
    data object BeginRefresh : CatalogStargazersFetchEvent
    data object Retry : CatalogStargazersFetchEvent
    data class Succeeded(val profiles: List<CatalogStargazer>) : CatalogStargazersFetchEvent
    data class Failed(val message: String) : CatalogStargazersFetchEvent
}

data class CatalogStargazersFetchUiState(
    val profiles: List<CatalogStargazer> = emptyList(),
    val fetchState: CatalogStargazersFetchState = CatalogStargazersFetchState.NOT_INIT,
    val errorMessage: String? = null,
) {
    val isLoading: Boolean
        get() = fetchState == CatalogStargazersFetchState.INITING ||
            fetchState == CatalogStargazersFetchState.REFRESHING

    val canRetry: Boolean
        get() = fetchState == CatalogStargazersFetchState.INIT_ERROR ||
            fetchState == CatalogStargazersFetchState.REFRESH_ERROR

    fun reduce(event: CatalogStargazersFetchEvent): CatalogStargazersFetchUiState = when (event) {
        CatalogStargazersFetchEvent.BeginInitialLoad -> {
            if (fetchState == CatalogStargazersFetchState.NOT_INIT ||
                fetchState == CatalogStargazersFetchState.INIT_ERROR
            ) {
                copy(
                    fetchState = CatalogStargazersFetchState.INITING,
                    errorMessage = null,
                )
            } else {
                this
            }
        }

        CatalogStargazersFetchEvent.BeginRefresh -> {
            if (profiles.isNotEmpty() && fetchState in setOf(
                    CatalogStargazersFetchState.INITED,
                    CatalogStargazersFetchState.REFRESH_ERROR,
                    CatalogStargazersFetchState.REFRESHED,
                )
            ) {
                copy(
                    fetchState = CatalogStargazersFetchState.REFRESHING,
                    errorMessage = null,
                )
            } else {
                this
            }
        }

        CatalogStargazersFetchEvent.Retry -> {
            if (profiles.isEmpty() || fetchState == CatalogStargazersFetchState.INIT_ERROR) {
                reduce(CatalogStargazersFetchEvent.BeginInitialLoad)
            } else {
                reduce(CatalogStargazersFetchEvent.BeginRefresh)
            }
        }

        is CatalogStargazersFetchEvent.Succeeded -> when (fetchState) {
            CatalogStargazersFetchState.INITING -> copy(
                profiles = event.profiles,
                fetchState = CatalogStargazersFetchState.INITED,
                errorMessage = null,
            )

            CatalogStargazersFetchState.REFRESHING -> copy(
                profiles = event.profiles,
                fetchState = CatalogStargazersFetchState.REFRESHED,
                errorMessage = null,
            )

            else -> this
        }

        is CatalogStargazersFetchEvent.Failed -> when (fetchState) {
            CatalogStargazersFetchState.INITING -> copy(
                fetchState = CatalogStargazersFetchState.INIT_ERROR,
                errorMessage = event.message,
            )

            CatalogStargazersFetchState.REFRESHING -> copy(
                fetchState = CatalogStargazersFetchState.REFRESH_ERROR,
                errorMessage = event.message,
            )

            else -> this
        }
    }
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

fun <K> executeStargazerAction(
    selectionState: OneUiSelectableListState<K>,
    action: CatalogStargazerAction,
): String {
    val feedback = stargazerActionFeedback(selectionState.selectedCount, action)
    selectionState.clear()
    return feedback
}

/** Physical swipe actions used by the pinned Stargazers reference. */
enum class CatalogStargazerSwipeAction(
    val label: String,
    val containerArgb: Int,
) {
    Call("Call", 0xFF11A85F.toInt()),
    Message("Message", 0xFF31A5F3.toInt()),
}

/**
 * The reference preserves physical gesture meaning in RTL: swipe right calls, swipe left messages.
 */
fun stargazerSwipeAction(direction: OneUiSwipeDirection): CatalogStargazerSwipeAction? =
    when (direction) {
        OneUiSwipeDirection.Right -> CatalogStargazerSwipeAction.Call
        OneUiSwipeDirection.Left -> CatalogStargazerSwipeAction.Message
        OneUiSwipeDirection.None -> null
    }

data class CatalogStargazerSwipeFeedback(
    val message: String,
    val indeterminate: Boolean,
    val dismissAfterMillis: Long?,
    val progressStepDelayMillis: Long?,
    val progressStepCount: Int? = null,
)

fun stargazerSwipeFeedback(
    action: CatalogStargazerSwipeAction,
    name: String,
): CatalogStargazerSwipeFeedback = when (action) {
    CatalogStargazerSwipeAction.Call -> CatalogStargazerSwipeFeedback(
        message = "Calling $name...",
        indeterminate = true,
        dismissAfterMillis = 4_000L,
        progressStepDelayMillis = null,
        progressStepCount = null,
    )

    CatalogStargazerSwipeAction.Message -> CatalogStargazerSwipeFeedback(
        message = "Sending message to $name...",
        indeterminate = false,
        dismissAfterMillis = null,
        progressStepDelayMillis = 50L,
        progressStepCount = 101,
    )
}

/** Profile header actions in the same visual order as view_stargazer_buttons.xml. */
enum class CatalogStargazerProfileAction {
    GitHub,
    X,
    Email,
    Blog,
}

fun stargazerProfileActions(profile: CatalogStargazer): List<CatalogStargazerProfileAction> =
    buildList {
        add(CatalogStargazerProfileAction.GitHub)
        if (!profile.twitterUsername.isNullOrBlank()) add(CatalogStargazerProfileAction.X)
        if (!profile.email.isNullOrBlank()) add(CatalogStargazerProfileAction.Email)
        if (!profile.blog.isNullOrBlank()) add(CatalogStargazerProfileAction.Blog)
    }

fun stargazerProfileActionTarget(
    profile: CatalogStargazer,
    action: CatalogStargazerProfileAction,
): String? = when (action) {
    CatalogStargazerProfileAction.GitHub -> profile.url.takeIf(String::isNotBlank)
    CatalogStargazerProfileAction.X -> profile.twitterUsername
        ?.takeIf(String::isNotBlank)
        ?.let { "https://x.com/$it" }
    CatalogStargazerProfileAction.Email -> profile.email
        ?.takeIf(String::isNotBlank)
        ?.let { "mailto:$it" }
    CatalogStargazerProfileAction.Blog -> profile.blog?.takeIf(String::isNotBlank)
}

fun stargazerVCardFileName(profile: CatalogStargazer): String =
    "stargazer_${profile.id}_${profile.name}.vcf"

/** Source-derived vCard payload used by the reference profile sharing flow. */
fun stargazerVCardContent(profile: CatalogStargazer): String = buildString {
    appendLine("BEGIN:VCARD")
    appendLine("VERSION:2.1")
    appendLine("FN:${profile.name}")
    appendLine("NICKNAME:${profile.login}")
    appendLine("URL:${profile.url}")
    profile.email?.let { appendLine("EMAIL:$it") }
    profile.company?.let { appendLine("ORG:$it") }
    profile.location?.let { appendLine("ADR:$it") }
    profile.bio?.let { appendLine("TITLE:$it") }
    profile.blog?.let { appendLine("URL:$it") }
    profile.organizationsUrl?.let { appendLine("URL:$it") }
    profile.twitterUsername?.let { appendLine("X-TWITTER:https://x.com/$it") }
    profile.avatarBase64?.takeIf(String::isNotBlank)?.let {
        appendLine("PHOTO;ENCODING=BASE64;TYPE=PNG:$it")
    }
    appendLine("NOTE:Starred repos: ${profile.starredRepos.joinToString(", ")}")
    appendLine("END:VCARD")
}

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

fun stargazerActionModeSearchFieldVisible(
    selectionMode: Boolean,
    searchMode: CatalogActionModeSearch,
): Boolean = !selectionMode || searchMode != CatalogActionModeSearch.DISMISS

fun stargazerActionModeSelectableKeys(
    profiles: List<CatalogStargazer>,
): List<Long> = profiles.map(CatalogStargazer::id)

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
