# One UI 8 Sample Parity Design

**Status:** Proposed implementation contract

**Goal:** Turn `oneui-compose` into a Compose-native One UI 8 component library whose demo recreates every reusable UI element, variant, state, icon/drawable catalogue entry, and observable motion pattern demonstrated by the `Ragnarok93/oneui-design` One UI 8 sample app, without falling back to `AndroidView` wrappers for target components.

**Reference:** `Ragnarok93/oneui-design` `main`, especially `sample-app/` and the MIT-licensed `lib/` implementation/resources. The reference sample APK and its source are the completeness contract. The current `oneui-compose` demo is not the contract.

## 1. Definition of success

The work is complete only when all of the following are true:

1. Every reusable UI item and variant visible or interactable in the reference sample has a Compose-native equivalent in the library.
2. Every reference demo category has a corresponding navigable Compose demo destination, including nested tabs/subscreens used to demonstrate library behavior.
3. Every non-`sesl_` library drawable exposed by the reference sample's `IconsRepo` catalogue is represented in the Compose demo catalogue. Vector assets are exposed as Compose/vector resources or `ImageVector`-style APIs where appropriate; raster/stateful/animated assets use a Compose-appropriate painter or stateful/animated representation.
4. Reference sample-specific navigation/action icons used to demonstrate components are also represented where required by the recreated screens.
5. Motion is source-traceable. When the MIT source contains duration/easing/interpolator/state-transition behavior, the Compose version reproduces it. When the reference names behavior implemented only by unavailable/private code, the closest measured/source-backed equivalent is documented rather than falsely labeled exact.
6. All interactive states demonstrated by the sample are operable in the Compose demo: enabled/disabled, selected/unselected, checked/unchecked, expanded/collapsed, determinate/indeterminate, dialog/sheet open/closed, search/action mode, list selection, navigation selection, picker selection, progress state, and relevant landscape/adaptive variants.
7. No target One UI component is implemented by embedding the old View with `AndroidView`. Platform services that are not themselves UI components may still be invoked normally.
8. Existing public `oneui-compose` APIs remain source-compatible where practical; compatibility shims may delegate to new implementations.
9. CI passes library tests, demo compilation, lint/static checks, Compose UI tests, drawable parity auditing, route smoke tests, and emulator smoke coverage.
10. A final debug/release demo APK is built, installed/launched in emulator validation, and its complete route/component catalogue is exercised before completion is claimed.

Network/data acquisition itself is not part of the UI-library contract. For example, the Stargazers and Apps demonstrations may use deterministic local fixtures rather than GitHub/network or installed-app queries, but their list presentation, search, action mode, selection, index/fast-scroll behavior, menus, detail surfaces, bottom sheets, dialogs, and interactions must be recreated.

## 2. Architectural approach

Use a Compose-native component system split into focused public packages rather than one large showcase file. The library will have six layers:

- `theme`: colors, typography, dimensions, shapes, density-aware/adaptive tokens, light/dark behavior.
- `motion`: named One UI/SESL motion tokens and reusable transition specs.
- `icons`: generated/audited icon and drawable catalogue plus stateful/animated assets.
- `components`: controls and surfaces grouped by responsibility (buttons, selection, progress, sliders, text/input, feedback, navigation, dialogs/sheets, pickers, preferences, lists).
- `patterns`: higher-order Compose patterns such as large-title app bars, search/action mode, app-picker rows, indexed lists, about pages, and settings groups.
- `demo`: a navigable sample app that mirrors the reference sample's categories and demonstrates every public component/variant.

The demo is a consumer of the library. Demo-only recreation code must not hide missing library APIs.

## 3. Reference demo parity map

### App shell and navigation

The reference navigation graph contains ProgressBars, SeekBars, Pickers, QRCodes, Navigation, RecyclerViews, Misc. Widgets, and Custom About. The Compose demo will expose the same categories from a One UI navigation drawer/app shell, with large-title/action-bar behavior and a compact/adaptive navigation presentation where appropriate.

The navigation library must cover:

- One UI large-title/top app bar and collapsed state
- navigation drawer with category/header/item/separator states
- popup menu
- text tabs and icon tabs
- fixed tabs and expanded-item behavior
- bottom navigation variants
- navigation rail
- generic navigation bar
- selected-item expansion / `alwaysExpanded = false` behavior
- search mode and contextual/action mode app-bar states used by list demos

### ProgressBars

Compose equivalents must include every reference variant:

- rounded determinate progress bar (including custom tint/transparent track presentation)
- standard horizontal determinate progress
- circular indeterminate small, medium, and large
- horizontal indeterminate small, medium, and large
- progress-button state used by Misc. Widgets

Indeterminate motion must be animated rather than represented by a static Material indicator.

### SeekBars

The reference seek-bar page must be reproduced as distinct Compose modes with equivalent gesture/state semantics:

- normal
- expand
- vertical expand
- split
- level bar

Preference variants must additionally cover basic, expanded, level-bar, and center-based/seamless seek bars with label/value/tick/adjustability options.

### Pickers

The Compose picker set must recreate:

- inline date picker
- date-picker dialog
- inline time picker
- time-picker dialog
- multi-option picker
- single-option picker
- landscape/adaptive single-option presentation
- option picker with custom top/bottom content
- color picker
- number picker with min/max, dividers, label, and editable input
- basic/data number picker with a custom row/data adapter model

Picker headers, custom action content, selected/enabled states, wheel/selection motion, dialog presentation, and orientation/adaptive layout are part of parity.

### QRCode

Recreate the QR presentation patterns used by the sample, including the normal QR screen and QR-in-bottom-sheet/custom-content usage where demonstrated. QR data generation can use deterministic demo data; the UI presentation and sheet interactions are the contract.

### Navigation

Recreate all controls present in `fragment_navi.xml` rather than only one bottom bar:

- text tab layout and expansion toggle
- icon tab layout and expansion toggle
- navigation rail
- standard bottom navigation
- generic navigation bar
- bottom navigation with non-always-expanded behavior

All selection transitions and indicator/content motion must use the shared motion layer.

### RecyclerViews -> Compose list patterns

The reference RecyclerViews page has three tabs: Icons, Stargazers, and Apps. Compose uses lazy lists/pagers rather than RecyclerView/ViewPager wrappers while reproducing behavior.

**Icons**

- searchable icon/drawable catalogue
- list/grid presentation used by the sample
- contextual/action-mode selection if exposed by the reference screen
- complete non-`sesl_` drawable coverage derived from the reference `IconsRepo` contract

**Stargazers**

- list rows and avatars/placeholders
- selection/action mode
- search/filter mode
- refresh/loading/empty/error visual states demonstrated by the sample
- index/fast-scroll behavior where present
- profile/details composition
- options dialog/menu
- QR bottom sheet and share/action surfaces demonstrated by the sample
- deterministic local fixtures may replace remote GitHub data

**Apps**

- app-picker list styles and list-type variants from the reference `ListTypes`
- single/multi selection behavior
- menu/sort/action controls demonstrated by the sample
- app-icon/title/subtitle/action-row patterns
- deterministic fixture apps may replace package-manager enumeration in tests/demo

### Misc. Widgets

Every widget shown by the reference `fragment_widgets.xml` must have a Compose-native counterpart and interactive sample:

- switch
- switch bar
- checkbox
- radio button
- spinner/dropdown
- edit text / text field
- button
- image button
- popup menu
- bottom sheet trigger/content
- tip popup
- snackbar with icon/action
- progress button

These must use One UI styling and behavior; replacing them with unmodified Material 3 controls is not parity.

### Preferences

Recreate all preference types in the reference `app_preferences.xml` as Compose preference components/patterns:

- suggestion card preference + action button
- inset preference category
- tips card preference
- switch-bar preference
- horizontal radio preference with image entries
- switch preference
- switch preference screen
- updatable-widget preference
- checkbox preference
- edit-text preference
- dropdown preference
- list preference
- multi-select list preference
- color-picker preference
- basic seek-bar preference
- Pro expanded seek-bar preference
- Pro level-bar preference
- Pro center-based/seamless seek-bar preference with left/right labels
- normal preference row (About)
- custom layout preference / related-links card pattern
- dependency/enabled-state behavior such as dark-mode option disabled by system-default switch

### About / Custom About

Recreate the reusable One UI About/custom-about page structures: app identity/header, version/details, link/action cards, contributors/people rows or equivalent sample structures, bottom content, and menu/actions used in the reference app. Personal/network content can be represented by deterministic fixture data; layout and interaction patterns must remain faithful.

## 4. Icons and drawable parity

Icons are a first-class acceptance criterion, not decorative extras.

The reference sample's `IconsRepo` reflects over `dev.oneuiproject.oneui.R.drawable` and exposes every drawable whose resource name does not start with `sesl_`. Therefore the Compose parity test will derive a reference drawable manifest from `oneui-design/lib/src/main/res` and compare it against a checked-in Compose catalogue manifest.

Each entry is classified as one of:

- vector/icon: port to a Compose-compatible vector resource and expose through the catalogue; frequently consumed icons receive a stable `OneUI8Icons` accessor.
- shape/background/state list: reproduce as Compose `Shape`, `Brush`, state colors, or a resource-backed painter where that is the faithful representation.
- raster: retain/adapt the MIT-licensed resource through a painter API when it is actually part of the reference component/demo contract.
- animated/stateful drawable: implement a state-driven Compose animation or animated painter rather than flattening to a static image.

A generated test fails when a reference catalogue entry lacks an explicit Compose mapping or a documented non-visual implementation mapping. The demo Icon Catalogue displays all mapped visual entries and supports search so the user can inspect parity directly.

No Figma-only proprietary vector will be claimed or shipped unless a source with redistribution rights is explicitly available. MIT resources/code in `oneui-design` are the primary implementation reference.

## 5. Motion parity

Create `OneUI8Motion` as the single motion contract and migrate all components to it. Each public motion token records its source name/reference and timing.

Already verified source-backed values remain baseline requirements:

- standard One UI easing from `oui_des_interpolator_22_25_0_100`: cubic Bézier `(0.22, 0.25, 0, 1)`
- selection check: first stroke 110 ms + second stroke 180 ms
- bottom-sheet enter: translation 300 ms, alpha 165 ms
- bottom-sheet exit: translation 200 ms, alpha 140 ms

The implementation audit must additionally extract and reproduce motion used by switches, checkboxes/radios, seek-bar modes, progress indicators, tabs/navigation indicators, drawer selection/expansion, popups, snackbars, dialogs, pickers, list selection/recoil equivalents, app bars, search/action mode, and any animated drawable surfaced by the icon catalogue.

If the reference invokes a named SESL/private interpolator whose implementation is unavailable (for example an unavailable exact `sine_out_80` implementation), the Compose token is explicitly marked as an approximation with the exact available duration/state behavior retained. Such cases are listed in the parity manifest so they cannot be silently misrepresented.

Motion tests assert duration/easing/token mapping. Emulator/UI tests assert state transitions occur and settle in the correct final states. Representative animated controls receive screenshot/frame-sequence or deterministic-clock Compose tests where practical.

## 6. Component API principles

Public APIs are Compose-native and hoist state. Controls accept `value`/`checked`/`selected` plus change callbacks, support `enabled`, expose semantic roles, meet touch-target requirements, and allow modifiers without making callers reproduce One UI internals.

Visual variants are explicit enums/configuration objects when behavior differs (for example `OneUI8SeekBarMode.Normal`, `Expand`, `VerticalExpand`, `Split`, `Level`). Avoid a single mega-component with dozens of unrelated flags when separate focused components produce clearer APIs.

Accessibility semantics, keyboard/D-pad focus, font scaling, RTL, touch targets, and light/dark presentation are mandatory library behavior, not demo-only polish.

## 7. Demo architecture

Replace the current single scrolling `MainActivity.kt` showcase with a real catalog application:

- app shell / drawer mirroring the reference categories
- one focused destination per top-level reference category
- nested tabs/routes for Icons, Stargazers, and Apps
- independent interactive state per screen
- optional internal Parity Status screen showing route count, component mapping count, drawable mapping count, and failing/missing items; this screen is diagnostic and does not substitute for recreated screens

The demo must only import public library APIs for target UI. If a demo screen needs a reusable visual behavior and no library API exists, that is considered a missing library component and must be implemented in `lib` first.

## 8. Testing and parity enforcement

Testing occurs at four levels:

**Unit/state tests:** clamping, selection, preference dependencies, picker ranges, navigation selection, icon manifest mapping, motion token constants.

**Compose UI tests:** every control's click/toggle/selection/input semantics; menus/dialogs/sheets; search/action mode; list selection; picker interactions; navigation transitions.

**Parity audits:** a source-derived manifest records every reference demo item and every icon/drawable catalogue entry. CI fails on a missing mapping or a mapped item without a demo route/test reference.

**Emulator smoke:** launch demo, traverse every top-level route and nested RecyclerViews tab, open/close every dialog/sheet/menu/popup, exercise each core control state, and capture evidence for representative screens/motion. No success claim is made from compilation alone.

## 9. CI/build contract

CI must run at minimum:

- library unit tests
- Compose UI/instrumentation tests where the runner supports them
- parity manifest/audit tests
- `:lib:assembleRelease`
- `:demo:assembleDebug`
- lint/static analysis
- emulator smoke workflow on an Android image compatible with the project's minimum/target constraints
- artifact upload for the final demo APK and relevant test reports

The existing demo artifact workflow is expanded rather than treated as sufficient evidence by itself.

## 10. Compatibility and migration

Current public APIs remain usable while internals are moved into the expanded architecture. Existing `org.oneui.compose.oneui8.*` APIs may become forwarding wrappers/deprecated aliases where a cleaner canonical package is introduced. Breaking removals are deferred until a separately approved major-version migration.

The current demo regression is corrected as part of the catalog rewrite; the old one-page demo is removed only after all of its existing demonstrations have equivalents in the new routes.

## 11. Explicit non-goals

- Duplicating GitHub/network backend logic from the sample.
- Duplicating package-manager querying merely to populate the Apps demo.
- Embedding SESL Views through `AndroidView` to claim Compose parity.
- Claiming unavailable private Samsung/Figma assets or interpolator internals are exact.
- Matching only screenshots while omitting interaction, state, animation, or accessibility behavior.

## 12. Completion gate

Before declaring this effort complete, the parity manifest must report zero missing reference items and zero missing drawable mappings, all automated checks must pass, the final demo APK must build and launch, every route must pass the emulator smoke traversal, and the final review must explicitly compare the Compose demo against the OneUI-Design sample category-by-category. Any missing icon, animation, variant, menu, picker, preference type, list behavior, or reusable screen pattern keeps the project in progress.
