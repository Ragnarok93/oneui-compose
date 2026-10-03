# One UI 8 Compose Desktop Mode Design

Date: 2026-10-02
Status: Approved direction; implementation pending spec review
Repository: Ragnarok93/oneui-compose
Target branch: feature/oneui8-compose-components

## Context

oneui-compose is a Compose-native port of the SESL8 / One UI 8 design library. Its
desktop mode must therefore be a reusable library pattern, not a Flux-specific
file-browser implementation. Phone and desktop presentations must share the same
component contracts, semantic colors, typography, shapes, interaction feedback,
and motion tokens. A future application should be able to adopt the shell and
components without importing Flux state, storage, or navigation code.

The current branch already contains OneUiAppShell with a compact overlay drawer
and a wide permanent drawer. Flux's desktop-oneui8-file-browser branch provides
the reference behavior for window classification, a stable navigation pane, a
desktop top bar, breadcrumb/address affordances, and a bounded content surface.
Those behaviors are reference architecture only; this library owns the generic
Compose API and One UI 8 visual language.

## Goals

1. Provide a reusable compact/desktop shell for Compose applications.
2. Select the presentation from the current usable window bounds, not device
   brand, device type, or orientation.
3. Keep existing phone-mode behavior and public component APIs compatible.
4. Make desktop navigation, app-bar actions, context/tool bars, and content
   surfaces reusable with arbitrary application state.
5. Preserve SESL8 semantic color mapping, One UI 8 rounded geometry, touch
   targets, press feedback, reduced-motion behavior, and dark-theme behavior.
6. Make the catalog itself a template demonstrating how future applications can
   share content components across modes.
7. Provide deterministic tests for mode classification and shell structure.

## Non-goals

- Porting Flux file operations, storage providers, breadcrumbs, or network
  state into this library.
- Creating a second desktop-only component family with different visual tokens.
- Replacing the existing SESL8 color/theme implementation with Material defaults.
- Adding platform-specific Samsung or DeX APIs to the public library contract.
- Making every individual component aware of desktop mode. Components should
  remain usable inside either shell unless a component has a documented,
  interaction-specific reason to adapt.

## Design principles

### Shared component, adaptive presentation

The same One UI button, list item, card, picker, control, navigation item, and
screen content must render in both modes. Desktop adaptation belongs primarily
to the shell, arrangement, available action affordances, and content surface
insets. A component may consume the same tokens at a different size or density
only when the SESL8 reference behavior requires it.

### Window-first classification

The default classifier uses the current Compose window constraints. Desktop is
selected when the window is at least 840 dp wide and 480 dp high, or at least
720 dp wide and 600 dp high. Otherwise the window is compact. This matches the
Flux reference while avoiding a phone forced into a desktop layout merely by
rotation. A nullable explicit layout-mode override remains available for
previews, tests, and applications with an external window policy.

The pure classifier is exposed as a small stable API so it can be unit-tested
without rendering Compose content.

### One UI 8 over generic desktop conventions

Desktop adds persistent navigation and pointer/keyboard-friendly affordances,
but it does not become a generic Material 3 dashboard. Surfaces remain tonal,
rounded, and calm; selected navigation uses the existing accent overlay and
rounded interaction shape; controls keep SESL8 press scaling and feedback; dark
mode uses the mapped SESL palette.

## Proposed public API

Add a reusable layout-mode contract in the library patterns package:

    enum class OneUiLayoutMode {
        Compact,
        Desktop;

        companion object {
            fun fromWindow(width: Dp, height: Dp): OneUiLayoutMode
        }
    }

OneUiAppShell gains an optional layoutMode override and an optional desktop
context-bar slot. Automatic mode selection remains the default:

    @Composable
    fun OneUiAppShell(
        destinations: List<OneUiAppShellDestination>,
        selectedId: String,
        onDestinationSelected: (String) -> Unit,
        title: String,
        modifier: Modifier = Modifier,
        subtitle: String? = null,
        layoutMode: OneUiLayoutMode? = null,
        headerAction: (@Composable () -> Unit)? = null,
        desktopContextBar: (@Composable RowScope.() -> Unit)? = null,
        actions: @Composable RowScope.() -> Unit = {},
        content: @Composable () -> Unit,
    )

The existing overload shape and default behavior remain source-compatible for
callers that do not use the new parameters. desktopContextBar is generic:
applications can use it for breadcrumbs, a search field, tabs, filters, or an
address/action row without the library knowing the application's domain.

Add a reusable OneUiDesktopTopBar pattern for applications that need a
standalone desktop header. It owns title hierarchy, optional subtitle,
context-bar placement, actions, window insets, and One UI interaction metrics.
It must be built from the same OneUiTheme values and use the existing
OneUiLargeTitleAppBar typography contract rather than introducing a second
unrelated title system.

## Shell behavior

### Compact mode

- Preserve the current overlay drawer behavior and drawer motion.
- Keep the large-title app bar and compact navigation affordance.
- Render content full-width with the existing phone-oriented insets.
- Do not expose desktop-only address, breadcrumb, or persistent-pane elements
  unless the caller explicitly includes them inside its screen content.

### Desktop mode

- Use a permanent navigation pane on the leading edge.
- Keep the pane visually separate from the main workspace using the SESL
  elevated surface, not a hard divider or a saturated color block.
- Use a stable pane width of 292 dp, with a minimum usable width of 280 dp and
  bounded growth only if a future API explicitly opts into it.
- Preserve the existing destination list, selected state, semantic labels, and
  header action. Selected rows retain the rounded One UI interaction shape.
- Render a desktop top bar in the workspace. It contains the title/subtitle
  hierarchy, optional context bar, and actions.
- Render content in a bounded workspace surface with 12 dp outer desktop
  gutters and an 18 dp surface radius. The content composable remains
  authoritative for its own scrolling and state.
- Apply navigation-bar insets to the workspace without adding redundant nested
  inset padding to the content screen.
- Keep the desktop pane and top bar stable while the selected destination
  changes; only the content region should transition.

### Intermediate windows

The same two-mode contract is intentionally used for the first reusable slice:
windows below the desktop threshold remain compact and preserve the current
touch-first layout. A future medium/tablet-specific mode can be added only
after the compact and desktop contracts are stable; it must not silently alter
the meaning of the current thresholds.

## Token and ownership rules

Extend OneUiDimensions, OneUiSpacing, or a focused desktop metrics token
group with desktop geometry that is not already represented. Desktop token
ownership must cover:

- navigation pane width;
- desktop workspace gutter;
- desktop toolbar height and horizontal inset;
- desktop content surface radius;
- desktop pane row height and horizontal inset.

Do not place these values as unrelated literals across shell files. Existing
SESL8 colors remain the source for background, elevated surface, text, accent,
divider, and pressed states. Desktop-specific values must be documented as
library layout metrics, not claimed as extracted Samsung measurements.

Pointer and keyboard affordances use the same oneUiInteractive contract and
semantic roles as phone controls. A larger click target is allowed, but a
second desktop-only ripple/highlight system is not.

## Catalog template

CatalogApp continues to own destination selection and screen state. It passes
the same screen composables to OneUiAppShell in both modes. The demo will add a
small desktop context-bar example that is domain-neutral: a breadcrumb-like
destination trail plus search/action affordances demonstrating the slot
contract. It must not copy Flux's file semantics.

Representative screens, especially NavigationScreen, PreferencesScreen,
PickersScreen, and WidgetsScreen, should remain the same screen implementations.
Only their container arrangement may become width-aware where an existing
screen has a clear multi-column opportunity. Width-aware arrangements must
preserve a single-column compact presentation and share the same child
components.

The catalog must continue to expose the theme mode selector and verify that
light/dark mode changes affect both compact and desktop shells.

## Motion and state

- Compact drawer animation continues to use OneUiMotion.drawer().
- Desktop pane visibility is structural and does not animate on every
  recomposition.
- Destination changes may use the existing content transition only if it does
  not resize or recompose the permanent pane/top bar unnecessarily.
- Reduced-motion mode removes or shortens shell transitions consistently with
  the existing OneUiTheme.reducedMotion contract.
- Navigation state remains hoisted in the caller; the shell must not create a
  second destination state model.

## Testing and verification

Add pure unit tests for:
- both desktop threshold branches;
- compact classification below each threshold;
- explicit mode override behavior where applicable;
- boundary values at 720, 840, 480, and 600 dp.

Add Compose/instrumentation coverage for:
- compact mode showing the navigation affordance and overlay drawer behavior;
- desktop mode showing a permanent navigation pane and hiding the overlay
  affordance;
- selected destination and header action remaining functional in both modes;
- desktop context-bar content appearing only in desktop mode;
- dark theme and theme-mode changes preserving readable pane/workspace
  surfaces;
- reduced-motion behavior not introducing a second transition path.

Build verification must include the repository's unit tests, affected
instrumentation tests, and the demo debug APK. The implementation is not
complete until the public library compiles independently of the demo and the
catalog exercises both layout modes through a deterministic override or
preview/test harness.

## Implementation slices

1. Add the pure layout-mode contract and token ownership, with unit tests.
2. Refactor OneUiAppShell's existing wide/compact branch to use the contract
   without changing its default appearance.
3. Add the reusable desktop top-bar/context-bar structure and bounded workspace
   surface.
4. Update CatalogApp and representative screens to demonstrate shared content
   in both modes.
5. Add Compose tests and verify light/dark, reduced-motion, and boundary cases.
6. Review the public API and touched UI files for accidental Flux coupling,
   duplicated tokens, squared interaction highlights, and regressions to phone
   geometry.

## Acceptance criteria

- A future Compose application can adopt the desktop shell without depending
  on Flux classes or file-browser state.
- Compact and desktop presentations use the same destination/content contracts.
- The current phone/catalog behavior remains intact when the window is compact.
- Desktop mode provides permanent navigation, a reusable desktop top/context
  bar, and a bounded content workspace.
- SESL8/One UI 8 colors, shapes, motion, semantics, and dark-mode behavior
  remain the authority.
- Threshold behavior and shell structure are covered by fresh tests.
- No implementation is described as complete without a successful build/test
  result.