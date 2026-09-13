package org.oneui.compose.demo.screens

import android.content.Intent
import android.net.Uri
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
import org.oneui.compose.components.qrcode.OneUiQrCode
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
    val location: String,
    val company: String,
    val email: String,
    val bio: String,
)

/** Stable local dataset keeps the catalog useful offline and deterministic under UI tests. */
val StargazersCatalogSamples: List<CatalogStargazer> = listOf(
    CatalogStargazer(1, "Ada Lovelace", "ada", "https://example.com/profiles/ada", "London, UK", "Analytical Engine", "ada@example.com", "Mathematics and early computing"),
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

    val visibleProfiles = remember(query) {
        val needle = query.trim()
        if (needle.isEmpty()) {
            StargazersCatalogSamples
        } else {
            StargazersCatalogSamples.filter { item ->
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
                onOpenProfile = { profile = it },
                onOpenOptions = { optionsState = optionsState.open() },
            )
        } else {
            StargazerProfile(
                profile = currentProfile,
                onBack = { profile = null },
                onShowQr = { qrProfile = currentProfile },
            )
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
    onOpenProfile: (CatalogStargazer) -> Unit,
    onOpenOptions: () -> Unit,
) {
    val fastScrollerConfig = remember(settings) { stargazerFastScrollerConfig(settings) }

    Column(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
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
            OneUiIconButton(
                icon = OneUiIcons.More,
                contentDescription = StargazersOptionsTestTags.TriggerDescription,
                onClick = onOpenOptions,
            )
        }

        if (selectionState.isSelectionMode) {
            StargazerActionModeBar(selectionState)
        }

        if (profiles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = if (query.isBlank()) "No stargazers yet." else "No results found.",
                    color = OneUiTheme.colors.secondaryText,
                )
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
                    OneUiSelectableListItem(
                        title = item.name,
                        subtitle = item.url,
                        selected = selected,
                        selectionMode = selectionMode,
                        leadingContent = { StargazerAvatar(item) },
                    )
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
private fun StargazerActionModeBar(selectionState: OneUiSelectableListState<Long>) {
    val allKeys = remember { StargazersCatalogSamples.map(CatalogStargazer::id) }
    val allSelected = selectionState.allSelected(allKeys)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
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
                details = listOf(
                    OneUiProfileDetail("Location", profile.location),
                    OneUiProfileDetail("Company", profile.company),
                    OneUiProfileDetail("Email", profile.email),
                    OneUiProfileDetail("Bio", profile.bio),
                ),
                modifier = Modifier.padding(top = 12.dp, bottom = 28.dp),
                avatar = {
                    StargazerAvatar(
                        profile = profile,
                        modifier = Modifier.testTag("stargazer-profile-avatar"),
                        size = 100.dp,
                    )
                },
                actions = {
                    OneUiIconButton(
                        icon = OneUiIcons.Website,
                        contentDescription = "Website",
                        onClick = { openWebsite(context, profile.url) },
                    )
                    OneUiIconButton(
                        icon = OneUiIcons.Email,
                        contentDescription = "Email",
                        onClick = { emailProfile(context, profile.email) },
                    )
                },
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(OneUiTheme.colors.surface)
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
                    onClick = { shareProfile(context, profile) },
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
        containerColor = OneUiTheme.colors.surface,
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
                onClick = { shareProfile(context, profile) },
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
            ) {
                Text("Share profile")
            }
        }
    }
}

private fun openWebsite(context: android.content.Context, url: String) {
    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

private fun emailProfile(context: android.content.Context, email: String) {
    context.startActivity(Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null)))
}

private fun shareProfile(context: android.content.Context, profile: CatalogStargazer) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, profile.name)
        putExtra(Intent.EXTRA_TEXT, profile.url)
    }
    context.startActivity(Intent.createChooser(intent, "Share profile"))
}
