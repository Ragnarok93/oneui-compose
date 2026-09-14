package org.oneui.compose.demo.screens

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import java.io.File
import kotlinx.coroutines.delay
import org.oneui.compose.components.list.OneUiSwipeAction
import org.oneui.compose.components.list.OneUiSwipeActionRow
import org.oneui.compose.components.progress.OneUiLinearProgress
import org.oneui.compose.icons.OneUiDrawableCatalog
import org.oneui.compose.icons.OneUiDrawableRendering
import org.oneui.compose.icons.OneUiIcon
import org.oneui.compose.icons.OneUiIconButton
import org.oneui.compose.icons.OneUiIcons
import org.oneui.compose.theme.OneUiTheme

@Composable
internal fun StargazerSwipeRow(
    profile: CatalogStargazer,
    enabled: Boolean,
    onFeedback: (CatalogStargazerSwipeFeedback) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val call = CatalogStargazerSwipeAction.Call
    val message = CatalogStargazerSwipeAction.Message
    OneUiSwipeActionRow(
        leftAction = OneUiSwipeAction(
            label = message.label,
            icon = OneUiIcons.Message,
            containerColor = Color(message.containerArgb),
        ),
        rightAction = OneUiSwipeAction(
            label = call.label,
            icon = OneUiIcons.Call,
            containerColor = Color(call.containerArgb),
        ),
        onSwipeLeft = { onFeedback(stargazerSwipeFeedback(message, profile.name)) },
        onSwipeRight = { onFeedback(stargazerSwipeFeedback(call, profile.name)) },
        modifier = modifier,
        enabled = enabled,
        content = content,
    )
}

@Composable
internal fun StargazerSwipeFeedbackHost(
    feedback: CatalogStargazerSwipeFeedback?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (feedback == null) return

    var progress by remember(feedback) { mutableFloatStateOf(0f) }
    LaunchedEffect(feedback) {
        if (feedback.indeterminate) {
            delay(feedback.dismissAfterMillis ?: 4_000L)
            onDismiss()
        } else {
            val stepDelay = feedback.progressStepDelayMillis ?: 50L
            repeat(20) { step ->
                delay(stepDelay)
                progress = (step + 1) / 20f
            }
            onDismiss()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .background(
                color = OneUiTheme.colors.surfaceElevated,
                shape = RoundedCornerShape(18.dp),
            )
            .testTag("stargazer-swipe-feedback")
            .padding(horizontal = 18.dp, vertical = 14.dp),
    ) {
        Text(
            text = feedback.message,
            color = OneUiTheme.colors.primaryText,
            style = OneUiTheme.typography.listTitle,
        )
        OneUiLinearProgress(
            progress = if (feedback.indeterminate) null else progress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
        )
    }
}

@Composable
internal fun StargazerProfileActionButtons(profile: CatalogStargazer) {
    val context = LocalContext.current
    stargazerProfileActions(profile).forEach { action ->
        OneUiIconButton(
            icon = stargazerProfileActionIcon(action),
            contentDescription = action.accessibilityLabel,
            onClick = { openStargazerProfileAction(context, profile, action) },
        )
    }
}

private val CatalogStargazerProfileAction.accessibilityLabel: String
    get() = when (this) {
        // Preserve the existing catalog accessibility contract while the action targets GitHub.
        CatalogStargazerProfileAction.GitHub -> "Website"
        CatalogStargazerProfileAction.X -> "X"
        CatalogStargazerProfileAction.Email -> "Email"
        CatalogStargazerProfileAction.Blog -> "Blog"
    }

private fun stargazerProfileActionIcon(action: CatalogStargazerProfileAction): OneUiIcon = when (action) {
    CatalogStargazerProfileAction.GitHub -> dependencyIcon("github") ?: OneUiIcons.Website
    CatalogStargazerProfileAction.X -> dependencyIcon("twitter") ?: OneUiIcons.Website
    CatalogStargazerProfileAction.Email -> OneUiIcons.Email
    CatalogStargazerProfileAction.Blog -> dependencyIcon("blog") ?: OneUiIcons.Website
}

private fun dependencyIcon(query: String): OneUiIcon? =
    OneUiDrawableCatalog.search(query)
        .asSequence()
        .mapNotNull { entry -> (entry.rendering as? OneUiDrawableRendering.Icon)?.icon }
        .firstOrNull()

internal fun openStargazerProfileAction(
    context: Context,
    profile: CatalogStargazer,
    action: CatalogStargazerProfileAction,
) {
    val target = stargazerProfileActionTarget(profile, action) ?: return
    val intentAction = if (action == CatalogStargazerProfileAction.Email) {
        Intent.ACTION_SENDTO
    } else {
        Intent.ACTION_VIEW
    }
    context.startActivity(Intent(intentAction, Uri.parse(target)))
}

internal fun shareStargazerVCard(context: Context, profile: CatalogStargazer) {
    val file = File(context.cacheDir, stargazerVCardFileName(profile)).apply {
        writeText(stargazerVCardContent(profile))
    }
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file,
    )
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/x-vcard"
        putExtra(Intent.EXTRA_SUBJECT, profile.name)
        putExtra(Intent.EXTRA_STREAM, uri)
        clipData = ClipData.newRawUri(profile.name, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Share profile"))
}
