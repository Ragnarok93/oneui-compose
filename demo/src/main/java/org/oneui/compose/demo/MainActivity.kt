package org.oneui.compose.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.oneui.compose.oneui8.components.OneUI8Button
import org.oneui.compose.oneui8.components.OneUI8ButtonStyle
import org.oneui.compose.oneui8.components.OneUI8Card
import org.oneui.compose.oneui8.components.OneUI8IconButton
import org.oneui.compose.oneui8.components.OneUI8ListItem
import org.oneui.compose.oneui8.components.OneUI8NavigationBar
import org.oneui.compose.oneui8.components.OneUI8NavigationItem
import org.oneui.compose.oneui8.components.OneUI8Section
import org.oneui.compose.oneui8.components.OneUI8SelectionIndicator
import org.oneui.compose.oneui8.components.OneUI8Slider
import org.oneui.compose.oneui8.components.OneUI8Switch
import org.oneui.compose.oneui8.icons.OneUI8Icons
import org.oneui.compose.oneui8.motion.OneUI8Motion
import org.oneui.compose.oneui8.theme.OneUI8Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { OneUI8Theme { OneUI8Demo() } }
    }
}

@Composable
private fun OneUI8Demo() {
    var switchChecked by remember { mutableStateOf(true) }
    var sliderValue by remember { mutableFloatStateOf(0.68f) }
    var selectedRow by remember { mutableIntStateOf(0) }
    var selectedNav by remember { mutableIntStateOf(0) }
    var showMotionCard by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OneUI8Theme.colors.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = OneUI8Theme.dimensions.screenHorizontalPadding)
                .padding(top = 28.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(OneUI8Theme.dimensions.sectionSpacing),
        ) {
            DemoHeader()

            OneUI8Section(
                title = "Buttons & icons",
                subtitle = "SESL-style press scaling with rounded One UI surfaces",
            ) {
                OneUI8Card {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OneUI8Button(
                                text = "Primary",
                                onClick = {},
                                modifier = Modifier.weight(1f),
                            )
                            OneUI8Button(
                                text = "Tonal",
                                onClick = {},
                                style = OneUI8ButtonStyle.Tonal,
                                modifier = Modifier.weight(1f),
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OneUI8IconButton(OneUI8Icons.Search, "Search", onClick = {})
                            OneUI8IconButton(OneUI8Icons.Settings, "Settings", onClick = {})
                            OneUI8IconButton(OneUI8Icons.MoreVertical, "More", onClick = {})
                            OneUI8IconButton(OneUI8Icons.Add, "Add", onClick = {})
                        }
                    }
                }
            }

            OneUI8Section(
                title = "Controls",
                subtitle = "Interactive switch, slider and list-row patterns",
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OneUI8ListItem(
                        title = "Automatic sync",
                        subtitle = if (switchChecked) "Enabled" else "Disabled",
                        trailing = {
                            OneUI8Switch(
                                checked = switchChecked,
                                onCheckedChange = { switchChecked = it },
                            )
                        },
                        onClick = { switchChecked = !switchChecked },
                    )
                    OneUI8Card {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text("Intensity", color = OneUI8Theme.colors.primaryText, fontWeight = FontWeight.SemiBold)
                                Text("${(sliderValue * 100).toInt()}%", color = OneUI8Theme.colors.secondaryText)
                            }
                            OneUI8Slider(
                                value = sliderValue,
                                onValueChange = { sliderValue = it },
                            )
                        }
                    }
                }
            }

            OneUI8Section(
                title = "Selection",
                subtitle = "Two-stage 110 ms + 180 ms animated check timing",
            ) {
                OneUI8Card {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("Recent", "Favorites", "Shared").forEachIndexed { index, label ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                OneUI8SelectionIndicator(selected = selectedRow == index)
                                Spacer(Modifier.width(14.dp))
                                OneUI8Button(
                                    text = label,
                                    onClick = { selectedRow = index },
                                    style = OneUI8ButtonStyle.Neutral,
                                    modifier = Modifier.weight(1f),
                                )
                            }
                        }
                    }
                }
            }

            OneUI8Section(
                title = "Motion",
                subtitle = "Bottom-sheet timing ported to Compose transitions",
            ) {
                OneUI8Button(
                    text = if (showMotionCard) "Hide motion sample" else "Show motion sample",
                    leadingIcon = OneUI8Icons.Motion,
                    onClick = { showMotionCard = !showMotionCard },
                    modifier = Modifier.fillMaxWidth(),
                )
                AnimatedVisibility(
                    visible = showMotionCard,
                    enter = slideInVertically(
                        animationSpec = tween(
                            durationMillis = OneUI8Motion.Duration.SheetEnterTranslation,
                            easing = OneUI8Motion.StandardEasing,
                        ),
                        initialOffsetY = { it * 3 / 5 },
                    ) + fadeIn(
                        animationSpec = tween(
                            durationMillis = OneUI8Motion.Duration.SheetEnterAlpha,
                            easing = OneUI8Motion.StandardEasing,
                        ),
                    ),
                    exit = slideOutVertically(
                        animationSpec = tween(
                            durationMillis = OneUI8Motion.Duration.SheetExitTranslation,
                            easing = OneUI8Motion.StandardEasing,
                        ),
                        targetOffsetY = { it * 3 / 5 },
                    ) + fadeOut(
                        animationSpec = tween(
                            durationMillis = OneUI8Motion.Duration.SheetExitAlpha,
                            easing = OneUI8Motion.StandardEasing,
                        ),
                    ),
                ) {
                    OneUI8Card(containerColor = OneUI8Theme.colors.surfaceElevated) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                "SESL8 motion bridge",
                                color = OneUI8Theme.colors.primaryText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                "300 ms translation + 165 ms fade in; 200 ms translation + 140 ms fade out.",
                                color = OneUI8Theme.colors.secondaryText,
                                fontSize = 13.sp,
                            )
                        }
                    }
                }
            }

            OneUI8Section(
                title = "Vector set",
                subtitle = "Original 24×24 rounded vectors aligned to the design-kit grid",
            ) {
                OneUI8Card {
                    val icons = listOf(
                        "Search" to OneUI8Icons.Search,
                        "Settings" to OneUI8Icons.Settings,
                        "More" to OneUI8Icons.MoreVertical,
                        "Chevron" to OneUI8Icons.ChevronRight,
                        "Check" to OneUI8Icons.Check,
                        "Add" to OneUI8Icons.Add,
                        "Grid" to OneUI8Icons.Grid,
                        "Motion" to OneUI8Icons.Motion,
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        icons.chunked(4).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                row.forEach { (name, icon) -> IconCell(name, icon) }
                            }
                        }
                    }
                }
            }

            OneUI8Section(
                title = "Navigation",
                subtitle = "Compact expanding selected destination",
            ) {
                OneUI8NavigationBar(
                    items = listOf(
                        OneUI8NavigationItem("Home", OneUI8Icons.Grid),
                        OneUI8NavigationItem("Motion", OneUI8Icons.Motion),
                        OneUI8NavigationItem("Settings", OneUI8Icons.Settings),
                    ),
                    selectedIndex = selectedNav,
                    onSelected = { selectedNav = it },
                )
            }
        }
    }
}

@Composable
private fun DemoHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "OneUI Compose 8",
            color = OneUI8Theme.colors.primaryText,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-1).sp,
        )
        Text(
            text = "Compose-native One UI components, SESL8 motion and reusable design tokens.",
            color = OneUI8Theme.colors.secondaryText,
            fontSize = 15.sp,
        )
    }
}

@Composable
private fun IconCell(name: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    OneUI8Theme.colors.surfaceElevated,
                    RoundedCornerShape(16.dp),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = name,
                tint = OneUI8Theme.colors.primaryText,
                modifier = Modifier.size(24.dp),
            )
        }
        Text(name, color = OneUI8Theme.colors.secondaryText, fontSize = 10.sp)
    }
}
