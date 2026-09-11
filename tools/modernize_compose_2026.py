#!/usr/bin/env python3
"""Mechanical Compose 2026 source migration used for the AGP/Compose baseline upgrade.

This intentionally changes no public API. It replaces the removed ripple entry point and
moves AnchoredDraggable thresholds/animation specs to the current flingBehavior API.
"""
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SRC = ROOT / "lib/src/main/java"


def replace_once(text: str, old: str, new: str, label: str) -> str:
    count = text.count(old)
    if count != 1:
        raise RuntimeError(f"{label}: expected exactly one match, found {count}")
    return text.replace(old, new, 1)


# Compose 2026 makes rememberRipple a DeprecationLevel.ERROR. Material3 ripple has the
# same bounded/radius/color call shape, so preserve every call site through an import alias.
ripple_old = "import androidx.compose.material.ripple.rememberRipple"
ripple_new = "import androidx.compose.material3.ripple as rememberRipple"
ripple_files = 0
for path in SRC.rglob("*.kt"):
    text = path.read_text()
    if ripple_old in text:
        path.write_text(text.replace(ripple_old, ripple_new))
        ripple_files += 1
if ripple_files < 10:
    raise RuntimeError(f"expected legacy ripple imports across the old library; changed only {ripple_files}")

# Drawer state: current Foundation stores thresholds/settling policy in flingBehavior,
# while the state itself owns values/anchors/offset.
drawer = SRC / "org/oneui/compose/layout/internal/DrawerState.kt"
text = drawer.read_text()
text = replace_once(
    text,
    "import androidx.compose.foundation.gestures.AnchoredDraggableState\n",
    "import androidx.compose.foundation.gestures.AnchoredDraggableDefaults\nimport androidx.compose.foundation.gestures.AnchoredDraggableState\n",
    "drawer imports",
)
text = replace_once(
    text,
    """    internal val draggableState = AnchoredDraggableState(\n        initialValue = initial,\n        positionalThreshold = { distance: Float ->\n            distance / 2F\n        },\n        velocityThreshold = {\n            velocityThreshold\n        },\n        animationSpec = tween(\n            durationMillis = animDuration\n        )\n    )\n""",
    """    internal val draggableState = AnchoredDraggableState(\n        initialValue = initial\n    )\n\n    @Composable\n    internal fun flingBehavior() = AnchoredDraggableDefaults.flingBehavior(\n        state = draggableState,\n        positionalThreshold = { distance -> distance / 2F },\n        animationSpec = tween(durationMillis = animDuration)\n    )\n""",
    "drawer state constructor",
)
text = replace_once(
    text,
    """    ) = draggableState\n        .animateTo(target)\n""",
    """    ) = draggableState\n        .animateTo(\n            targetValue = target,\n            animationSpec = tween(durationMillis = animDuration)\n        )\n""",
    "drawer animateTo",
)
drawer.write_text(text)

# Pass the remembered settling policy to both drawer modifiers.
for rel, orientation in [
    ("org/oneui/compose/layout/internal/BaseNavigationRail.kt", "Horizontal"),
    ("org/oneui/compose/layout/internal/SlidingDrawerLayout.kt", "Horizontal"),
]:
    path = SRC / rel
    text = path.read_text()
    old = f"""                state = state.draggableState,\n                orientation = Orientation.{orientation}\n            )"""
    new = f"""                state = state.draggableState,\n                orientation = Orientation.{orientation},\n                flingBehavior = state.flingBehavior()\n            )"""
    text = replace_once(text, old, new, f"{path.name} anchoredDraggable")
    path.write_text(text)

# Collapsing toolbar: same AnchoredDraggable migration. Remove the preview-only Material
# icon dependency rather than adding another icon artifact to the stable library baseline.
toolbar = SRC / "org/oneui/compose/layout/toolbar/CollapsingToolbarLayout.kt"
text = toolbar.read_text()
text = replace_once(
    text,
    "import androidx.compose.foundation.gestures.AnchoredDraggableState\n",
    "import androidx.compose.foundation.gestures.AnchoredDraggableDefaults\nimport androidx.compose.foundation.gestures.AnchoredDraggableState\n",
    "toolbar imports",
)
for obsolete in [
    "import androidx.compose.material.icons.Icons\n",
    "import androidx.compose.material.icons.filled.MoreVert\n",
    "import org.oneui.compose.base.Icon\n",
    "import org.oneui.compose.widgets.buttons.IconButton\n",
]:
    if obsolete not in text:
        raise RuntimeError(f"toolbar expected import missing: {obsolete.strip()}")
    text = text.replace(obsolete, "", 1)
text = replace_once(
    text,
    """        .anchoredDraggable(\n            state = state.draggableState,\n            orientation = Orientation.Vertical\n        )""",
    """        .anchoredDraggable(\n            state = state.draggableState,\n            orientation = Orientation.Vertical,\n            flingBehavior = state.flingBehavior()\n        )""",
    "toolbar modifier",
)
text = replace_once(
    text,
    """    internal val draggableState = AnchoredDraggableState(\n        initialValue = initial,\n        positionalThreshold = { distance: Float ->\n            distance / 2F\n        },\n        velocityThreshold = {\n            velocityThreshold\n        },\n        animationSpec = tween()\n    )\n""",
    """    internal val draggableState = AnchoredDraggableState(\n        initialValue = initial\n    )\n\n    @Composable\n    internal fun flingBehavior() = AnchoredDraggableDefaults.flingBehavior(\n        state = draggableState,\n        positionalThreshold = { distance -> distance / 2F },\n        animationSpec = tween()\n    )\n""",
    "toolbar state constructor",
)
text = replace_once(
    text,
    """    ) = draggableState\n        .animateTo(target)\n""",
    """    ) = draggableState\n        .animateTo(\n            targetValue = target,\n            animationSpec = tween()\n        )\n""",
    "toolbar animateTo",
)
text = replace_once(
    text,
    """        toolbarSubtitle = "Subtitle",\n        appbarActions = {\n            IconButton(icon = Icon.Vector(Icons.Default.MoreVert))\n        }\n""",
    """        toolbarSubtitle = "Subtitle"\n""",
    "toolbar preview icon",
)
toolbar.write_text(text)

print(f"Migrated {ripple_files} ripple-import files and modernized anchored-drag states.")
