# One UI 8 Compose Desktop Mode Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add a reusable SESL8/One UI 8 Compose desktop shell that shares the existing phone-mode components and content contracts while providing permanent navigation, a desktop top/context bar, and a bounded workspace.

**Architecture:** Keep OneUiAppShell as the single adaptive entry point. Add a pure window classifier and desktop metrics token group, then route the shell into compact or desktop presentation from current window constraints or an explicit test/preview override. Desktop-specific behavior is generic shell composition only; no Flux state, file-browser types, or storage semantics enter the library.

**Tech Stack:** Kotlin 2.4.20, Jetpack Compose BOM 2026.08.00, Compose Material 3, Compose Foundation/UI, JUnit 4, Compose UI tests, Android library/demo modules, GitHub Actions Gradle verification.

**Spec:** `docs/superpowers/specs/2026-10-02-oneui8-compose-desktop-mode-design.md`

## Global Constraints

- Keep library minSdk 23, compileSdk 37, Java/Kotlin toolchain 21, and existing dependency versions.
- Do not add Flux or file-browser dependencies to `:lib`.
- Desktop is selected at `(width >= 840.dp && height >= 480.dp) || (width >= 720.dp && height >= 600.dp)`; all other windows remain compact.
- Preserve existing `OneUiAppShell` call sites and compact behavior when the new parameters are omitted.
- Use SESL8-mapped `OneUiTheme.colors`, `OneUiTheme.typography`, `OneUiTheme.shapes`, `OneUiTheme.motion`, and interaction semantics as the source of truth.
- Desktop metrics are centralized; do not scatter pane, toolbar, gutter, or workspace-radius literals across UI files.
- Public APIs must be domain-neutral and reusable by future Compose projects.
- Verify with the repository's library unit tests, library instrumentation compilation/tests, demo unit tests, demo instrumentation compilation, parity audit, lint, and debug/release builds.

## Review Focus

- Boundary windows at 720/840 dp width and 480/600 dp height must classify exactly according to the spec; cover every branch in Task 1.
- A forced mode or a live window-mode change must not create a second navigation state model or leave the compact overlay visible in desktop mode; cover shell structure/state in Task 2.
- RTL layout must keep the permanent navigation pane and compact drawer on the leading edge while preserving action order; cover the shell's existing RTL branch in Task 2.
- Dark SESL8 surfaces must remain opaque/readable and must not reuse the translucent preference overlay as a desktop card/workspace color; cover token ownership and shell surfaces in Tasks 1–2.
- Reduced-motion mode must not introduce a second animated path for the permanent pane/context bar; cover the shell and desktop top-bar semantics in Task 3.

---

## File and interface map

- Create `lib/src/main/java/org/oneui/compose/patterns/shell/OneUiLayoutMode.kt`: pure compact/desktop window classifier.
- Modify `lib/src/main/java/org/oneui/compose/theme/OneUiTokens.kt`: add immutable desktop layout metrics.
- Modify `lib/src/main/java/org/oneui/compose/theme/OneUiTheme.kt`: provide desktop metrics through the existing theme composition locals.
- Modify `lib/src/main/java/org/oneui/compose/patterns/shell/OneUiAppShell.kt`: add optional layout override/context slot and route the two presentations.
- Create `lib/src/main/java/org/oneui/compose/patterns/appbar/OneUiDesktopTopBar.kt`: reusable domain-neutral desktop title/context/action bar.
- Create `lib/src/test/java/org/oneui/compose/patterns/shell/OneUiLayoutModeTest.kt`: classifier boundary tests.
- Create `lib/src/androidTest/java/org/oneui/compose/patterns/shell/OneUiAppShellTest.kt`: compact/desktop shell structure and interaction tests.
- Modify `demo/src/main/java/org/oneui/compose/demo/CatalogApp.kt`: accept an optional layout override and exercise the desktop context slot.
- Create `demo/src/main/java/org/oneui/compose/demo/CatalogDesktopContextBar.kt`: domain-neutral catalog breadcrumb/search/action example.
- Create `demo/src/androidTest/java/org/oneui/compose/demo/CatalogDesktopModeTest.kt`: deterministic desktop catalog coverage.
- Modify `GETTING_STARTED.md`: document adaptive shell usage and the domain-neutral desktop slot.
- Modify `docs/superpowers/specs/2026-10-02-oneui8-compose-desktop-mode-design.md` only if implementation discovers a contract discrepancy; do not silently diverge from it.

## Task 1: Add the pure layout classifier and centralized desktop metrics

**Files:**
- Create: `lib/src/main/java/org/oneui/compose/patterns/shell/OneUiLayoutMode.kt`
- Modify: `lib/src/main/java/org/oneui/compose/theme/OneUiTokens.kt`
- Modify: `lib/src/main/java/org/oneui/compose/theme/OneUiTheme.kt`
- Test: `lib/src/test/java/org/oneui/compose/patterns/shell/OneUiLayoutModeTest.kt`

**Interfaces:**
- Produces `OneUiLayoutMode.Compact`, `OneUiLayoutMode.Desktop`, and `OneUiLayoutMode.Companion.fromWindow(width: Dp, height: Dp): OneUiLayoutMode`.
- Produces `OneUiDesktopMetrics` with defaults `navigationPaneWidth = 292.dp`, `workspaceGutter = 12.dp`, `topBarHeight = 58.dp`, `topBarHorizontalPadding = 12.dp`, `contentRadius = 18.dp`, `navigationRowMinHeight = 48.dp`, and `navigationRowHorizontalPadding = 14.dp`.
- Produces `OneUiTheme.desktopMetrics: OneUiDesktopMetrics` through a composition local, with no change to existing color/typography/shapes/motion defaults.

- [ ] **Step 1: Write the failing classifier tests.**
  
  Add tests named `width840AndHeight480SelectsDesktop`, `width720AndHeight600SelectsDesktop`, `width839OrHeight479RemainsCompact`, `width719AndHeight600RemainsCompact`, and `desktopMetricsUseTheDocumentedLibraryDefaults`. Assert the exact enum result and metric values.

- [ ] **Step 2: Run the focused unit test and confirm the intended failure.**
  
  Run: `./gradlew :lib:testDebugUnitTest --tests org.oneui.compose.patterns.shell.OneUiLayoutModeTest`
  
  Expected: compilation/test failure because the classifier and metrics contracts do not yet exist.

- [ ] **Step 3: Implement the minimal contracts.**
  
  Make `fromWindow` pure and use the two exact threshold branches. Add `OneUiDesktopMetrics` as an immutable token group and provide it from `OneUiTheme` alongside the existing locals. Do not introduce a dependency or read device type/orientation.

- [ ] **Step 4: Run the focused unit test and confirm it passes.**
  
  Run: `./gradlew :lib:testDebugUnitTest --tests org.oneui.compose.patterns.shell.OneUiLayoutModeTest`
  
  Expected: all classifier and token-default tests pass.

- [ ] **Step 5: Commit the slice.**
  
  Commit through the GitHub connector with message `feat: add One UI window mode and desktop metrics`.

## Task 2: Refactor OneUiAppShell around the shared mode contract

**Files:**
- Modify: `lib/src/main/java/org/oneui/compose/patterns/shell/OneUiAppShell.kt`
- Test: `lib/src/androidTest/java/org/oneui/compose/patterns/shell/OneUiAppShellTest.kt`

**Interfaces:**
- Extends `OneUiAppShell` with `layoutMode: OneUiLayoutMode? = null` and `desktopContextBar: (@Composable RowScope.() -> Unit)? = null` while retaining existing parameter defaults and call-site compatibility.
- Uses `OneUiLayoutMode.fromWindow(maxWidth, maxHeight)` when `layoutMode == null`.
- Produces stable test semantics/tags: `oneui-shell-compact`, `oneui-shell-desktop`, `oneui-shell-compact-drawer`, `oneui-shell-desktop-navigation`, `oneui-shell-desktop-content`, and `oneui-shell-desktop-context-bar`.
- Retains destination selection, header action, action slot, RTL leading-edge behavior, and hoisted caller state.

- [ ] **Step 1: Write failing Compose shell tests.**
  
  Add a test fixture that renders `OneUiTheme` and `OneUiAppShell` with two destinations, a clickable header action, an action slot, and a context-bar test node. Add tests named `desktopOverrideRendersPermanentPaneAndContextBar`, `compactOverrideRendersOverlayNavigationAffordance`, `desktopDestinationSelectionUsesTheExistingCallback`, `rtlDesktopPaneRemainsOnTheLeadingEdge`, and `reducedMotionDesktopStructureDoesNotAnimateThePermanentPane`. Assert the exact tags/content descriptions and callback results.

- [ ] **Step 2: Run the focused instrumentation test and confirm the intended failure.**
  
  Run: `./gradlew :lib:connectedDebugAndroidTest --tests org.oneui.compose.patterns.shell.OneUiAppShellTest`
  
  Expected: compilation/test failure because the new shell parameters, structure tags, and desktop branch do not yet exist.

- [ ] **Step 3: Implement the mode-aware shell.**
  
  Replace the current `expanded = maxWidth >= 840.dp` condition with the new mode contract. In desktop mode, keep the existing `DrawerPanel` as the permanent leading pane, render the new desktop top bar in the workspace, and wrap content with the tokenized gutter/radius/elevated surface. In compact mode, preserve the existing large-title app bar, overlay drawer, drawer motion, and close-on-destination-selection behavior. Add only structural test tags/semantics and route `desktopContextBar` to the desktop top bar.

- [ ] **Step 4: Update drawer geometry to use library-owned tokens without changing compact semantics.**
  
  Replace desktop-relevant pane width, row height, row inset, and interaction shape literals with `OneUiTheme.desktopMetrics` or existing One UI shape tokens. Keep the compact drawer's existing width cap, overlay alpha, and `OneUiMotion.drawer()` path. Ensure the workspace uses the opaque SESL elevated surface rather than the translucent preference-relative overlay.

- [ ] **Step 5: Run the focused instrumentation test and confirm it passes.**
  
  Run: `./gradlew :lib:connectedDebugAndroidTest --tests org.oneui.compose.patterns.shell.OneUiAppShellTest`
  
  Expected: compact and desktop structure tests pass, including destination callback, RTL structure, and reduced-motion assertions.

- [ ] **Step 6: Commit the slice.**
  
  Commit through the GitHub connector with message `feat: make One UI app shell adaptive`.

## Task 3: Add the reusable SESL8 desktop top bar

**Files:**
- Create: `lib/src/main/java/org/oneui/compose/patterns/appbar/OneUiDesktopTopBar.kt`
- Modify: `lib/src/main/java/org/oneui/compose/patterns/shell/OneUiAppShell.kt`
- Test: `lib/src/androidTest/java/org/oneui/compose/patterns/shell/OneUiAppShellTest.kt`

**Interfaces:**
- Produces `OneUiDesktopTopBar(title: String, modifier: Modifier = Modifier, subtitle: String? = null, contextBar: (@Composable RowScope.() -> Unit)? = null, actions: @Composable RowScope.() -> Unit = {})`.
- Consumes `OneUiTheme.typography`, `OneUiTheme.colors`, `OneUiTheme.desktopMetrics`, `OneUiIconButton`, and the existing One UI interaction contracts.
- Keeps the context slot desktop-only when invoked from `OneUiAppShell`; the reusable component itself remains domain-neutral.

- [ ] **Step 1: Add focused failing top-bar assertions to the shell test.**
  
  Assert that the desktop title, subtitle, context-bar node, and action content are displayed in desktop mode; assert that the context-bar node is absent in compact mode. Assert that the top bar exposes no Flux/file-browser labels.

- [ ] **Step 2: Implement `OneUiDesktopTopBar`.**
  
  Use a status-bar-aware column with a title/action row and an optional context row. Use existing One UI typography roles and SESL colors. Use the documented 58 dp toolbar metric and 12 dp horizontal inset. Keep the permanent pane structural; do not add visibility animation to the top bar or context row. Preserve readable dark surfaces and the existing reduced-motion contract.

- [ ] **Step 3: Re-run the shell instrumentation tests.**
  
  Run: `./gradlew :lib:connectedDebugAndroidTest --tests org.oneui.compose.patterns.shell.OneUiAppShellTest`
  
  Expected: all desktop top-bar, compact-mode, interaction, RTL, and reduced-motion assertions pass.

- [ ] **Step 4: Commit the slice.**
  
  Commit through the GitHub connector with message `feat: add reusable One UI desktop top bar`.

## Task 4: Make the demo a shared phone/desktop template

**Files:**
- Modify: `demo/src/main/java/org/oneui/compose/demo/CatalogApp.kt`
- Create: `demo/src/main/java/org/oneui/compose/demo/CatalogDesktopContextBar.kt`
- Test: `demo/src/androidTest/java/org/oneui/compose/demo/CatalogDesktopModeTest.kt`
- Modify: `GETTING_STARTED.md`

**Interfaces:**
- Extends `CatalogApp` with `layoutMode: OneUiLayoutMode? = null` for previews/tests while leaving `CatalogActivity` automatic by default.
- Produces a domain-neutral `CatalogDesktopContextBar(selectedLabel: String)` that demonstrates breadcrumbs/search/action affordances without importing Flux types or behavior.
- Keeps all existing screen routing and `CatalogDestination` state unchanged.

- [ ] **Step 1: Write the failing deterministic desktop catalog test.**
  
  Render `CatalogApp(onOpenLegacyShowcase = {}, layoutMode = OneUiLayoutMode.Desktop)` under `OneUiTheme`. Assert `oneui-shell-desktop`, `oneui-shell-desktop-navigation`, `oneui-shell-desktop-content`, and `catalog-desktop-context-bar` are displayed. Navigate to `Navigation` through the permanent pane and assert `tabs-rounded-text` and `navigation-rail-control` remain displayed. Assert the compact navigation content description is absent.

- [ ] **Step 2: Run the focused demo instrumentation test and confirm the intended failure.**
  
  Run: `./gradlew :demo:connectedDebugAndroidTest --tests org.oneui.compose.demo.CatalogDesktopModeTest`
  
  Expected: compilation/test failure because the catalog override/context slot and desktop demo surface do not yet exist.

- [ ] **Step 3: Implement the catalog integration.**
  
  Add the optional layout override, pass the selected title and a domain-neutral context bar into `OneUiAppShell`, and keep all existing screen branches untouched. Use only reusable One UI icons/buttons/text and theme tokens in the context bar. Do not add Flux paths, storage semantics, or a second navigation state.

- [ ] **Step 4: Document the reusable pattern.**
  
  Add a concise `GETTING_STARTED.md` example showing automatic compact/desktop selection, explicit test/preview override, and a domain-neutral desktop context slot. State that application content remains shared between modes.

- [ ] **Step 5: Run the focused demo test and confirm it passes.**
  
  Run: `./gradlew :demo:connectedDebugAndroidTest --tests org.oneui.compose.demo.CatalogDesktopModeTest`
  
  Expected: the desktop shell and shared catalog screens pass.

- [ ] **Step 6: Commit the slice.**
  
  Commit through the GitHub connector with message `demo: showcase shared One UI phone and desktop modes`.

## Task 5: Run integrated verification and review the public surface

**Files:**
- Review: all files modified or created by Tasks 1–4
- Potentially modify: only files proven necessary by verification

- [ ] **Step 1: Run library unit tests and demo unit tests.**
  
  Run: `./gradlew :lib:testDebugUnitTest :demo:testDebugUnitTest`
  
  Expected: exit code 0 with no failing tests.

- [ ] **Step 2: Run parity audit, lint, instrumentation compilation, and APK builds.**
  
  Run:
  
      python3 scripts/audit-oneui8-parity.py --strict
      ./gradlew :lib:testDebugUnitTest :lib:compileDebugAndroidTestKotlin :lib:lintDebug :lib:assembleRelease :demo:testDebugUnitTest :demo:compileDebugAndroidTestKotlin :demo:assembleDebug --stacktrace
  
  Expected: parity audit, compilation, lint, tests, release library build, and debug demo build all succeed.

- [ ] **Step 3: Run affected connected tests.**
  
  Run:
  
      ./gradlew :lib:connectedDebugAndroidTest --tests org.oneui.compose.patterns.shell.OneUiAppShellTest
      ./gradlew :demo:connectedDebugAndroidTest --tests org.oneui.compose.demo.CatalogDesktopModeTest
      ./gradlew :demo:connectedDebugAndroidTest --tests org.oneui.compose.demo.CatalogSmokeTest
      ./gradlew :demo:connectedDebugAndroidTest --tests org.oneui.compose.demo.CatalogParitySmokeTest
  
  Expected: desktop structure, compact navigation, all existing catalog destinations, and parity interactions pass.

- [ ] **Step 4: Review the integrated diff.**
  
  Confirm that public API names match the spec, no Flux imports entered `:lib`, no squared desktop highlights or blue preference-overlay cards were introduced, compact mode defaults remain source-compatible, and tokens have one clear owner. Classify any unavailable connected-device/render evidence as unverified rather than inferring success.

- [ ] **Step 5: Commit any verification-only correction and record evidence.**
  
  If corrections are required, rerun the smallest affected check, then the integrated verification command. Commit through the GitHub connector with message `verify: harden One UI desktop mode integration`. Do not claim completion until fresh command results are available.

## Execution notes

Each task is independently reviewable and commits through the connected GitHub workflow on
`feature/oneui8-compose-components`. Use the same branch and preserve unrelated user
changes. Implementation should follow the TDD order in each task: failing test,
focused failure evidence, smallest implementation, focused passing evidence, then commit.