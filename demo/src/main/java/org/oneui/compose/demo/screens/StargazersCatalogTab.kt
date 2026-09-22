package org.oneui.compose.demo.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.components.buttons.OneUiTextButton
import org.oneui.compose.components.buttons.OneUiFloatingActionButton
import org.oneui.compose.components.qrcode.OneUiQrCode
import org.oneui.compose.components.progress.OneUiCircularProgress
import org.oneui.compose.components.progress.OneUiCircularProgressSize
import org.oneui.compose.components.feedback.OneUiTipPopup
import org.oneui.compose.icons.OneUiAnimatedIcons
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.patterns.list.OneUiProfile
import org.oneui.compose.patterns.list.OneUiProfileDetail
import org.oneui.compose.patterns.list.OneUiSelectableList
import org.oneui.compose.patterns.list.OneUiSelectableListItem
import org.oneui.compose.patterns.list.OneUiSelectableListState
import org.oneui.compose.theme.OneUiTheme

data class CatalogStargazer(
    val id: Long,
    val name: String,
    val login: String,
    val url: String,
    val location: String? = null,
    val company: String? = null,
    val email: String? = null,
    val bio: String? = null,
    val twitterUsername: String? = null,
    val blog: String? = null,
    val organizationsUrl: String? = null,
    val starredRepos: Set<String> = emptySet(),
    val avatarBase64: String? = null,
)

/** Stable local dataset keeps the catalog useful offline and deterministic under UI tests. */
val StargazersCatalogSamples: List<CatalogStargazer> = listOf(
    CatalogStargazer(
        id = 1,
        name = "Ada Lovelace",
        login = "ada",
        url = "https://example.com/profiles/ada",
        location = "London, UK",
        company = "Analytical Engine",
        email = "ada@example.com",
        bio = "Mathematics and early computing",
        twitterUsername = "ada_lovelace",
        blog = "https://ada.example.com",
        organizationsUrl = "https://example.com/profiles/ada/organizations",
        starredRepos = setOf("oneui-compose", "oneui-design"),
    ),
    CatalogStargazer(2, "Alan Turing", "alan", "https://example.com/profiles/alan", "Manchester, UK", "Computing Laboratory", "alan@example.com", "Computability and cryptanalysis"),
    CatalogStargazer(3, "Barbara Liskov", "barbara", "https://example.com/profiles/barbara", "Cambridge, MA", "Distributed Systems", "barbara@example.com", "Programming languages and abstraction"),
    CatalogStargazer(4, "Donald Knuth", "donald", "https://example.com/profiles/donald", "Stanford, CA", "Computer Science", "donald@example.com", "Algorithms and typesetting"),
    CatalogStargazer(5, "Edsger Dijkstra", "edsger", "https://example.com/profiles/edsger", "Austin, TX", "Computer Sciences", "edsger@example.com", "Structured programming and algorithms"),
    CatalogStargazer(6, "Grace Hopper", "grace", "https://example.com/profiles/grace", "Arlington, VA", "Compiler Systems", "grace@example.com", "Compilers and programming languages"),
    CatalogStargazer(7, "Katherine Johnson", "katherine", "https://example.com/profiles/katherine", "Hampton, VA", "Flight Research", "katherine@example.com", "Orbital mechanics and numerical analysis"),
    CatalogStargazer(8, "Margaret Hamilton", "margaret", "https://example.com/profiles/margaret", "Cambridge, MA", "Software Engineering", "margaret@example.com", "Guidance software and systems engineering"),
)

@Composable
fun StargazersCatalogTab(modifier: Modifier = Modifier) {
    val selectionState = remember { OneUiSelectableListState<Long>() }
    var optionsState by remember {
        mutableStateOf(
            CatalogStargazersOptionsState(
                committed = CatalogStargazersSettings(),
            ),
        )
    }
    var query by rememberSaveable { mutableStateOf("") }
    var profile by remember { mutableStateOf<CatalogStargazer?>(null) }
    var qrProfile by remember { mutableStateOf<CatalogStargazer?>(null) }
    var swipeFeedback by remember { mutableStateOf<CatalogStargazerSwipeFeedback?>(null) }
    var actionFeedback by remember { mutableStateOf<String?>(null) }
    var fabTipVisible by rememberSaveable { mutableStateOf(false) }
    var swipeTipVisible by rememberSaveable { mutableStateOf(false) }
    var swipeTipDismissed by rememberSaveable { mutableStateOf(false) }
    var fetchState by remember {
        mutableStateOf(
            CatalogStargazersFetchUiState(
                fetchState = CatalogStargazersFetchState.NOT_INIT,
            ),
        )
    }
    var failNextFetch by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(swipeTipDismissed) {
        if (swipeTipDismissed) return@LaunchedEffect
        kotlinx.coroutines.delay(1_000L)
        swipeTipVisible = true
    }

    LaunchedEffect(Unit) {
        fetchState = fetchState.reduce(CatalogStargazersFetchEvent.BeginInitialLoad)
    }

    BackHandler(enabled = profile != null) {
        profile = null
    }
    BackHandler(enabled = profile == null && selectionState.isSelectionMode) {
        selectionState.clear()
    }

    LaunchedEffect(fetchState.fetchState) {
        if (!fetchState.isLoading) return@LaunchedEffect
        kotlinx.coroutines.delay(250L)
        val next = if (failNextFetch) {
            failNextFetch = false
            fetchState.reduce(CatalogStargazersFetchEvent.Failed("Unable to reach the stargazers service."))
        } else {
            fetchState.reduce(CatalogStargazersFetchEvent.Succeeded(StargazersCatalogSamples))
        }
        fetchState = next
    }

    LaunchedEffect(selectionState.isSelectionMode, optionsState.committed.actionModeSearch) {
        if (selectionState.isSelectionMode &&
            optionsState.committed.actionModeSearch == CatalogActionModeSearch.DISMISS
        ) {
            query = ""
        }
    }

    val visibleProfiles = remember(query, fetchState.profiles) {
        val needle = query.trim()
        if (needle.isEmpty()) {
            fetchState.profiles
        } else {
            fetchState.profiles.filter { item ->
                item.name.contains(needle, ignoreCase = true) ||
                    item.login.contains(needle, ignoreCase = true) ||
                    item.url.contains(needle, ignoreCase = true)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag(RecyclerViewCatalogTab.Stargazers.testTag),
    ) {
        val currentProfile = profile
        if (currentProfile == null) {
            StargazerList(
                query = query,
                onQueryChange = { query = it },
                profiles = visibleProfiles,
                selectionState = selectionState,
                settings = optionsState.committed,
                fetchState = fetchState,
                onOpenProfile = { profile = it },
                onOpenOptions = { optionsState = optionsState.open() },
                onSwipeFeedback = { swipeFeedback = it },
                onActionFeedback = { actionFeedback = it },
                onRefresh = {
                    val event = when (fetchState.fetchState) {
                        CatalogStargazersFetchState.INIT_ERROR,
                        CatalogStargazersFetchState.REFRESH_ERROR,
                        -> CatalogStargazersFetchEvent.Retry
                        else -> if (fetchState.profiles.isEmpty()) {
                            CatalogStargazersFetchEvent.BeginInitialLoad
                        } else {
                            CatalogStargazersFetchEvent.BeginRefresh
                        }
                    }
                    fetchState = fetchState.reduce(event)
                },
                onSimulateFetchError = {
                    failNextFetch = true
                    fetchState = fetchState.reduce(CatalogStargazersFetchEvent.BeginRefresh)
                },
            )
        } else {
            StargazerProfile(
                profile = currentProfile,
                onBack = { profile = null },
                onShowQr = { qrProfile = currentProfile },
            )
        }

        StargazerSwipeFeedbackHost(
            feedback = swipeFeedback,
            onDismiss = { swipeFeedback = null },
            modifier = Modifier.align(Alignment.BottomCenter),
        )

        StargazerMessageHost(
            message = actionFeedback,
            onDismiss = { actionFeedback = null },
            modifier = Modifier.align(Alignment.BottomCenter),
        )

        if (currentProfile == null && !selectionState.isSelectionMode) {
            OneUiTipPopup(
                visible = swipeTipVisible && !fabTipVisible,
                title = "Stargazers",
                message = "Swipe left to call, swipe right to message.",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 92.dp)
                    .fillMaxWidth(0.9f),
                onDismiss = {
                    swipeTipVisible = false
                    swipeTipDismissed = true
                },
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 24.dp),
            ) {
                OneUiFloatingActionButton(
                    icon = OneUiIcons.Star,
                    contentDescription = "Star repositories",
                    onClick = {
                        swipeTipVisible = false
                        swipeTipDismissed = true
                        fabTipVisible = true
                    },
                )
                OneUiTipPopup(
                    visible = fabTipVisible,
                    title = "Stargazers",
                    message = "Star these github repositories:\n• OneUI Design lib\n• sesl-androidx\n• sesl-material.",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 68.dp)
                        .width(280.dp),
                    onDismiss = {
                        fabTipVisible = false
                        swipeTipVisible = false
                    },
                )
            }
        }
    }

    qrProfile?.let { selected ->
        StargazerQrSheet(
            profile = selected,
            onDismiss = { qrProfile = null },
        )
    }

    StargazersOptionsDialog(
        state = optionsState,
        onStateChange = { optionsState = it },
    )
}

@Composable
private fun StargazerList(
    query: String,
    onQueryChange: (String) -> Unit,
    profiles: List<CatalogStargazer>,
    selectionState: OneUiSelectableListState<Long>,
    settings: CatalogStargazersSettings,
    fetchState: CatalogStargazersFetchUiState,
    onOpenProfile: (CatalogStargazer) -> Unit,
    onOpenOptions: () -> Unit,
    onSwipeFeedback: (CatalogStargazerSwipeFeedback) -> Unit,
    onActionFeedback: (String) -> Unit,
    onRefresh: () -> Unit,
    onSimulateFetchError: () -> Unit,
) {
    val fastScrollerConfig = remember(settings) { stargazerFastScrollerConfig(settings) }

    Column(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (stargazerActionModeSearchFieldVisible(
                    selectionMode = selectionState.isSelectionMode,
                    searchMode = settings.actionModeSearch,
                )
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("stargazers-search-field"),
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    placeholder = { Text("Search contact", color = OneUiTheme.colors.secondaryText) },
                    leadingIcon = {
                        OneUiIcon(
                            icon = OneUiIcons.Search,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = OneUiTheme.colors.secondaryText,
                        )
                    },
                    trailingIcon = if (query.isBlank()) null else {
                        {
                            OneUiIconButton(
                                icon = OneUiIcons.Close,
                                contentDescription = "Clear contact search",
                                onClick = { onQueryChange("") },
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = OneUiTheme.colors.surfaceElevated,
                        unfocusedContainerColor = OneUiTheme.colors.surfaceElevated,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = OneUiTheme.colors.accent,
                        focusedTextColor = OneUiTheme.colors.primaryText,
                        unfocusedTextColor = OneUiTheme.colors.primaryText,
                    ),
                )
                Spacer(Modifier.width(4.dp))
            }
            OneUiIconButton(
                icon = OneUiIcons.More,
                contentDescription = StargazersOptionsTestTags.TriggerDescription,
                onClick = onOpenOptions,
            )
            OneUiIconButton(
                icon = OneUiIcons.Refresh,
                contentDescription = "Refresh stargazers",
                onClick = onRefresh,
                modifier = Modifier.testTag("stargazers-refresh"),
            )
            OneUiIconButton(
                icon = OneUiIcons.Error,
                contentDescription = "Simulate fetch error",
                onClick = onSimulateFetchError,
                modifier = Modifier.testTag("stargazers-simulate-error"),
            )
        }

        StargazerFetchStatus(
            state = fetchState,
            onRetry = onRefresh,
        )

        if (selectionState.isSelectionMode) {
            StargazerActionModeBar(
                selectionState = selectionState,
                visibleProfiles = profiles,
                settings = settings,
                onFeedback = onActionFeedback,
            )
        }

        if (profiles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (fetchState.isLoading) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        OneUiCircularProgress(
                            progress = null,
                            size = OneUiCircularProgressSize.Medium,
                            modifier = Modifier.size(42.dp),
                        )
                        Text(
                            text = stargazerNoItemText(fetchState.fetchState, query),
                            modifier = Modifier.padding(top = 12.dp),
                            color = OneUiTheme.colors.secondaryText,
                        )
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = fetchState.errorMessage
                                ?: stargazerNoItemText(fetchState.fetchState, query),
                            color = OneUiTheme.colors.secondaryText,
                        )
                        if (fetchState.canRetry) {
                            OneUiTextButton(
                                onClick = onRefresh,
                                modifier = Modifier.testTag("stargazers-retry"),
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        } else {
            OneUiSelectableList(
                items = profiles,
                state = selectionState,
                key = CatalogStargazer::id,
                onItemClick = onOpenProfile,
                modifier = Modifier.fillMaxSize(),
                indexLabel = CatalogStargazer::name,
                fastScrollerDisplayMode = fastScrollerConfig.displayMode,
                fastScrollerAutoHide = fastScrollerConfig.autoHide,
            ) { item, selected, selectionMode ->
                val itemIndex = profiles.indexOfFirst { it.id == item.id }
                val section = item.name.firstOrNull()?.uppercaseChar()?.toString().orEmpty()
                val previousSection = profiles.getOrNull(itemIndex - 1)
                    ?.name
                    ?.firstOrNull()
                    ?.uppercaseChar()
                    ?.toString()

                Column(Modifier.fillMaxWidth()) {
                    if (section.isNotEmpty() && section != previousSection) {
                        Text(
                            text = section,
                            modifier = Modifier.padding(start = 78.dp, top = 10.dp, bottom = 2.dp),
                            color = OneUiTheme.colors.accent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    StargazerSwipeRow(
                        profile = item,
                        enabled = !selectionMode,
                        onFeedback = onSwipeFeedback,
                        modifier = Modifier.testTag("stargazer-row-${item.id}"),
                    ) {
                        OneUiSelectableListItem(
                            title = item.name,
                            subtitle = item.url,
                            selected = selected,
                            selectionMode = selectionMode,
                            leadingContent = { StargazerAvatar(item) },
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 78.dp),
                        color = OneUiTheme.colors.divider,
                    )
                }
            }
        }
    }
}

@Composable
private fun StargazerFetchStatus(
    state: CatalogStargazersFetchUiState,
    onRetry: () -> Unit,
) {
    when {
        state.profiles.isNotEmpty() && state.fetchState == CatalogStargazersFetchState.INITING -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("stargazers-initial-progress")
                    .padding(horizontal = 20.dp, vertical = 4.dp),
            ) {
                Text(
                    text = "Loading stargazers...",
                    color = OneUiTheme.colors.secondaryText,
                    fontSize = 12.sp,
                )
                org.oneui.compose.components.progress.OneUiLinearProgress(
                    progress = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                )
            }
        }

        state.profiles.isNotEmpty() && state.fetchState == CatalogStargazersFetchState.INIT_ERROR -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("stargazers-initial-error")
                    .padding(start = 20.dp, end = 12.dp, top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = state.errorMessage ?: "Error loading stargazers.",
                    modifier = Modifier.weight(1f),
                    color = OneUiTheme.colors.secondaryText,
                    fontSize = 12.sp,
                )
                OneUiTextButton(onClick = onRetry) { Text("Retry") }
            }
        }

        state.fetchState == CatalogStargazersFetchState.REFRESHING -> {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("stargazers-refresh-progress")
                    .padding(horizontal = 20.dp, vertical = 4.dp),
            ) {
                Text(
                    text = "Refreshing stargazers...",
                    color = OneUiTheme.colors.secondaryText,
                    fontSize = 12.sp,
                )
                org.oneui.compose.components.progress.OneUiLinearProgress(
                    progress = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                )
            }
        }

        state.fetchState == CatalogStargazersFetchState.REFRESH_ERROR -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("stargazers-refresh-error")
                    .padding(start = 20.dp, end = 12.dp, top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = state.errorMessage ?: "Unable to refresh stargazers.",
                    modifier = Modifier.weight(1f),
                    color = OneUiTheme.colors.secondaryText,
                    fontSize = 12.sp,
                )
                OneUiTextButton(onClick = onRetry) {
                    Text("Retry")
                }
            }
        }
    }
}

@Composable
private fun StargazerActionModeBar(
    selectionState: OneUiSelectableListState<Long>,
    visibleProfiles: List<CatalogStargazer>,
    settings: CatalogStargazersSettings,
    onFeedback: (String) -> Unit,
) {
    val allKeys = remember(visibleProfiles) { stargazerActionModeSelectableKeys(visibleProfiles) }
    val allSelected = selectionState.allSelected(allKeys)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("stargazer-action-mode")
            .padding(horizontal = 12.dp, vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OneUiAnimatedIcons.CheckMorph(
                checked = true,
                modifier = Modifier.size(24.dp),
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "${selectionState.selectedCount} selected",
                modifier = Modifier.weight(1f),
                color = OneUiTheme.colors.primaryText,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
            )
            OneUiTextButton(
                onClick = {
                    if (allSelected) selectionState.clear() else selectionState.selectAll(allKeys)
                },
            ) {
                Text(if (allSelected) "Clear all" else "Select all")
            }
            if (settings.showCancelButton) {
                OneUiTextButton(
                    onClick = selectionState::clear,
                    modifier = Modifier.testTag("stargazer-action-mode-cancel"),
                ) {
                    Text("Cancel")
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            CatalogStargazerAction.entries.forEach { action ->
                OneUiIconButton(
                    icon = action.icon,
                    contentDescription = action.label,
                    onClick = {
                        onFeedback(executeStargazerAction(selectionState, action))
                    },
                    modifier = Modifier.testTag("stargazer-action-${action.name.lowercase()}"),
                )
            }
        }
    }
}

@Composable
private fun StargazerAvatar(
    profile: CatalogStargazer,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(OneUiTheme.colors.accent.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = profile.name
                .split(' ')
                .take(2)
                .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                .joinToString(""),
            color = OneUiTheme.colors.accent,
            fontWeight = FontWeight.Bold,
            fontSize = if (size >= 96.dp) 28.sp else 15.sp,
        )
    }
}

@Composable
private fun StargazerProfile(
    profile: CatalogStargazer,
    onBack: () -> Unit,
    onShowQr: () -> Unit,
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("stargazer-profile"),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OneUiIconButton(
                    icon = OneUiIcons.Back,
                    contentDescription = "Back to stargazers",
                    onClick = onBack,
                )
                Text(
                    text = "Profile",
                    modifier = Modifier.padding(start = 8.dp),
                    color = OneUiTheme.colors.primaryText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            OneUiProfile(
                name = profile.name,
                subtitle = profile.url,
                details = buildList {
                    profile.location?.takeIf(String::isNotEmpty)?.let {
                        add(OneUiProfileDetail("Location", it))
                    }
                    profile.company?.takeIf(String::isNotEmpty)?.let {
                        add(OneUiProfileDetail("Company", it))
                    }
                    profile.email?.takeIf(String::isNotEmpty)?.let {
                        add(OneUiProfileDetail("Email", it))
                    }
                    profile.bio?.takeIf(String::isNotEmpty)?.let {
                        add(OneUiProfileDetail("Bio", it))
                    }
                },
                modifier = Modifier.padding(top = 12.dp, bottom = 28.dp),
                avatar = {
                    StargazerAvatar(
                        profile = profile,
                        modifier = Modifier.testTag("stargazer-profile-avatar"),
                        size = 100.dp,
                    )
                },
                actions = {
                    StargazerProfileActionButtons(profile)
                },
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(OneUiTheme.colors.surfaceElevated)
                .testTag("stargazer-profile-bottom-actions"),
        ) {
            HorizontalDivider(color = OneUiTheme.colors.divider)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StargazerBottomAction(
                    label = "QR code",
                    icon = OneUiIcons.QrCode,
                    onClick = onShowQr,
                )
                StargazerBottomAction(
                    label = "Share",
                    icon = OneUiIcons.Share,
                    onClick = { shareStargazerVCard(context, profile) },
                )
            }
        }
    }
}

@Composable
private fun StargazerBottomAction(
    label: String,
    icon: org.oneui.compose.icons.OneUiIcon,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.width(96.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OneUiIconButton(
            icon = icon,
            contentDescription = label,
            onClick = onClick,
        )
        Text(
            text = label,
            color = OneUiTheme.colors.primaryText,
            fontSize = 12.sp,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StargazerQrSheet(
    profile: CatalogStargazer,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = OneUiTheme.colors.surfaceElevated,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("stargazer-qr-sheet")
                .padding(horizontal = 28.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = profile.name,
                color = OneUiTheme.colors.primaryText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Scan this QR code on another device to view ${profile.name}'s profile.",
                modifier = Modifier.padding(top = 8.dp),
                color = OneUiTheme.colors.secondaryText,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
            )
            OneUiQrCode(
                data = profile.url,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .size(240.dp)
                    .clip(RoundedCornerShape(24.dp)),
                foregroundColor = Color.Black,
                backgroundColor = Color.White,
                contentDescription = "QR code for ${profile.name}",
            )
            OneUiTextButton(
                onClick = { shareStargazerVCard(context, profile) },
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
            ) {
                Text("Share profile")
            }
        }
    }
}
