package org.oneui.compose.patterns.appbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.theme.OneUiTheme

/** Search/action-mode policy used by One UI toolbar and list search surfaces. */
enum class OneUiSearchModeBehavior {
    Dismiss,
    NoDismiss,
    Concurrent,
}

@Immutable
data class OneUiSearchModeState(
    val active: Boolean = false,
    val query: String = "",
    val behavior: OneUiSearchModeBehavior = OneUiSearchModeBehavior.Dismiss,
) {
    fun open(): OneUiSearchModeState = copy(active = true)

    fun close(): OneUiSearchModeState = copy(
        active = false,
        query = if (behavior == OneUiSearchModeBehavior.Dismiss) "" else query,
    )

    fun updateQuery(value: String): OneUiSearchModeState = copy(query = value)
}

@Composable
fun rememberOneUiSearchModeState(
    behavior: OneUiSearchModeBehavior = OneUiSearchModeBehavior.Dismiss,
): MutableState<OneUiSearchModeState> = remember(behavior) {
    mutableStateOf(OneUiSearchModeState(behavior = behavior))
}

/**
 * Compose-native One UI search mode. The query and active state stay hoisted so the same surface
 * can be used by a toolbar, drawer, list, or action mode without hidden View state.
 */
@Composable
fun OneUiSearchMode(
    active: Boolean,
    query: String,
    onActiveChange: (Boolean) -> Unit,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "Search",
    behavior: OneUiSearchModeBehavior = OneUiSearchModeBehavior.Dismiss,
    onSearch: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    AnimatedVisibility(
        visible = active,
        enter = if (OneUiTheme.reducedMotion) EnterTransition.None else fadeIn(animationSpec = org.oneui.compose.motion.OneUiMotion.searchMode()),
        exit = if (OneUiTheme.reducedMotion) ExitTransition.None else fadeOut(animationSpec = org.oneui.compose.motion.OneUiMotion.searchMode()),
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .testTag("oneui-search-mode"),
        ) {
            OneUiIconButton(
                icon = OneUiIcons.Back,
                contentDescription = "Close search",
                onClick = {
                    onActiveChange(false)
                    if (behavior == OneUiSearchModeBehavior.Dismiss) onQueryChange("")
                },
            )
            OutlinedTextField(
                value = query,
                onValueChange = {
                    onQueryChange(it)
                    onSearch?.invoke()
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                placeholder = { Text(hint, color = OneUiTheme.colors.secondaryText) },
                trailingIcon = if (query.isBlank()) null else {
                    {
                        OneUiIconButton(
                            icon = OneUiIcons.Close,
                            contentDescription = "Clear search",
                            onClick = { onQueryChange("") },
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = OneUiTheme.colors.surfaceElevated,
                    unfocusedContainerColor = OneUiTheme.colors.surfaceElevated,
                    focusedBorderColor = OneUiTheme.colors.accent,
                    unfocusedBorderColor = OneUiTheme.colors.divider,
                    focusedTextColor = OneUiTheme.colors.primaryText,
                    unfocusedTextColor = OneUiTheme.colors.primaryText,
                    cursorColor = OneUiTheme.colors.accent,
                ),
            )
            actions()
        }
    }
}
