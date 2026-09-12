# One UI 8 Sample Parity Implementation Plan

> **Execution:** Implement on `feature/oneui8-compose-components`. This plan supersedes the narrower component-task sequence wherever the approved sample-parity contract is stricter.

**Goal:** Recreate the complete `Ragnarok93/oneui-design` One UI 8 sample as a Compose-native library/catalog, including every reusable sample UI, state, icon/drawable catalogue entry, and observable source-backed animation. No target component may use `AndroidView` as its implementation.

**Pinned reference:** `Ragnarok93/oneui-design@ce4f2cae8c0d712acd2f0b2926ee909fd5d8a434` (`sample-app/` plus MIT-licensed `lib/`).

**Existing green foundations to preserve:** `OneUiTheme`, `OneUiMotion`, `oneUiInteractive`, curated/animated icons, buttons/FAB, sliders/range slider, progress facade, switches/checkbox/radio, and existing compatibility wrappers.

## Global completion rules

- [ ] Keep `:lib` Compose-native; no SESL/View dependency and no `AndroidView` target-component implementation.
- [ ] Preserve current legacy and `OneUI8*` signatures where practical by forwarding to stable `OneUi*` APIs.
- [ ] Every reference destination/item is listed in a checked-in parity manifest with a stable library API, demo destination/test tag, and status.
- [ ] Every non-`sesl_` drawable returned by the reference `IconsRepo` contract has an explicit Compose mapping or an explicit non-visual mapping.
- [ ] Every source-observable animation used by the sample is represented by a named motion/animated-asset mapping; approximations are explicitly marked.
- [ ] Every top-level and nested reference demo destination opens in the Compose catalog and exercises the corresponding public API.
- [ ] Unit tests, Compose UI tests, lint, drawable/route audits, release AAR, demo APK, and emulator traversal are green before completion is claimed.

---

## Task 1 — Make parity machine-checkable and replace the one-page demo shell

**Create**
- `parity/reference.json`
- `parity/drawables.txt`
- `parity/motion.json`
- `scripts/audit-oneui8-parity.py`
- `lib/src/main/java/org/oneui/compose/patterns/shell/OneUiAppShell.kt`
- `lib/src/main/java/org/oneui/compose/patterns/appbar/OneUiAppBar.kt`
- `demo/src/main/java/org/oneui/compose/demo/CatalogDestination.kt`
- `demo/src/main/java/org/oneui/compose/demo/CatalogApp.kt`
- `demo/src/main/java/org/oneui/compose/demo/CatalogSection.kt`
- `demo/src/main/java/org/oneui/compose/demo/screens/ParityStatusScreen.kt`
- `demo/src/androidTest/java/org/oneui/compose/demo/CatalogSmokeTest.kt`

**Modify**
- `demo/src/main/java/org/oneui/compose/demo/MainActivity.kt`
- `demo/build.gradle.kts`
- `.github/workflows/oneui8-demo-apk.yml`

### Steps
- [ ] Add a reference manifest pinned to `oneui-design@ce4f2cae...` containing top-level routes `ProgressBars`, `SeekBars`, `Pickers`, `QRCodes`, `Navigation`, `RecyclerViews`, `Misc. Widgets`, `Custom About`, plus nested `Icons`, `Stargazers`, `Apps`, Preferences, About, popup/search/action-mode surfaces.
- [ ] Add `scripts/audit-oneui8-parity.py` that validates unique IDs, non-empty public API/demo tags, status values, drawable mappings, motion mappings, and zero missing items for final mode.
- [ ] Add a source-sync mode that clones/fetches the pinned reference in CI and verifies the checked-in manifest has not silently omitted new reference navigation/layout/menu/drawable entries.
- [ ] Implement `OneUiAppShell` with large-title top app bar, drawer, popup action slot, preference/settings header action, compact/expanded drawer behavior, focus/D-pad traversal, and RTL.
- [ ] Replace the long demo `MainActivity` body with `CatalogApp`; keep the current showcase only as a temporary `LegacyShowcase` destination until every old example exists elsewhere.
- [ ] Add registry smoke tests that open every `CatalogDestination` and assert its test tag.

**Verify**
```bash
python scripts/audit-oneui8-parity.py --check
./gradlew :demo:assembleDebug :demo:compileDebugAndroidTestKotlin :lib:testDebugUnitTest :lib:lintDebug
```

**Commit:** `feat: add parity manifests and catalog shell`

---

## Task 2 — Complete icon/drawable and motion parity

**Create/modify**
- `lib/src/main/java/org/oneui/compose/icons/OneUiDrawableCatalog.kt`
- `lib/src/main/java/org/oneui/compose/icons/OneUiAnimatedIcons.kt`
- `lib/src/main/java/org/oneui/compose/motion/OneUiMotion.kt`
- `lib/src/test/java/org/oneui/compose/icons/OneUiDrawableParityTest.kt`
- `lib/src/test/java/org/oneui/compose/motion/OneUiMotionParityTest.kt`
- `demo/.../screens/IconsScreen.kt`

### Steps
- [ ] Derive the exact non-`sesl_` `dev.oneuiproject.oneui.R.drawable` catalogue used by `IconsRepo` from the pinned reference.
- [ ] Map dependency-provided `oneui-icons:1.1.0` resources first; map MIT reference resources that are legally reusable; implement stateful/animated drawables as Compose state machines rather than flattened images.
- [ ] Expose a searchable `OneUiDrawableCatalog.entries` model with stable name/type/source/rendering metadata.
- [ ] Expand animated icons beyond current Search/Close, Expand/Collapse, Play/Pause, CheckMorph to every stateful/animated reference asset surfaced by the catalogue/sample.
- [ ] Add named motion tokens for app bar, drawer selection/expansion, tabs, popup/menu, snackbar, dialog, picker wheels, list/action/search mode, preference reveal/dependency changes, and any animated-drawable transitions with traceable source metadata.
- [ ] Add deterministic-clock tests for representative animated icons and navigation/app-bar state transitions.
- [ ] `IconsScreen` supports search, progress/empty states, long-press action selection, select-all, and displays every visual catalogue entry.

**Verify**
```bash
python scripts/audit-oneui8-parity.py --check-drawables --check-motion
./gradlew :lib:testDebugUnitTest :lib:compileDebugAndroidTestKotlin :lib:lintDebug :demo:assembleDebug
```

**Commit:** `feat: complete One UI drawable and motion catalogue`

---

## Task 3 — ProgressBars and SeekBars exact sample parity

**Modify/create**
- `lib/src/main/java/org/oneui/compose/components/progress/OneUiProgress.kt`
- `lib/src/main/java/org/oneui/compose/components/slider/OneUiSlider.kt`
- `lib/src/main/java/org/oneui/compose/components/slider/OneUiLevelSlider.kt`
- `lib/src/main/java/org/oneui/compose/components/slider/OneUiSeekBarPlus.kt`
- `demo/.../screens/ProgressBarsScreen.kt`
- `demo/.../screens/SeekBarsScreen.kt`
- tests under `lib/src/test` and `lib/src/androidTest`

### Required demo parity
- [ ] Circular indeterminate Small/Medium/Large/XLarge.
- [ ] Circular determinate Small/Medium/Large/XLarge with animated 1..100 progress.
- [ ] Horizontal determinate and indeterminate.
- [ ] Standard horizontal seekbar.
- [ ] Expand horizontal seekbar.
- [ ] Expand dual-color/overlap seekbar at reference threshold 70.
- [ ] Level bar with arbitrary min/max.
- [ ] Seamless level bar.
- [ ] `SeekBarPlus` seamless and stepped variants.
- [ ] Standard vertical and expand-vertical seekbars.
- [ ] Split mode where exposed by the SESL implementation/reference resources.

### Behavior
- [ ] Match source-backed geometry and current measured 250 ms `SINE_IN_OUT_80` track expansion and 100/300 ms expand-thumb motion.
- [ ] Match dual-color overlap, level/tick/seamless behavior, keyboard, D-pad, RTL, vertical direction, callbacks, and progress semantics.
- [ ] Audit current indeterminate progress implementation against SESL8 source; replace generic/fabricated easing with exact source-backed motion.

**Commit:** `feat: complete progress and seekbar sample parity`

---

## Task 4 — Misc Widgets parity: surfaces, rows, spinner, search, menus, feedback

**Create**
- `components/surface/OneUiSurface.kt`
- `components/list/OneUiListItem.kt`
- `components/list/OneUiSwitchItem.kt`
- `components/list/OneUiRadioItem.kt`
- `components/menu/OneUiMenu.kt`
- `components/input/OneUiSpinner.kt`
- `components/input/OneUiTextField.kt`
- `components/search/OneUiSearch.kt`
- `components/feedback/OneUiSnackbar.kt`
- `components/feedback/OneUiTipPopup.kt`
- `components/feedback/OneUiBottomTip.kt`
- `components/buttons/OneUiProgressButton.kt`
- `patterns/appbar/OneUiSuggestAppBar.kt`
- `demo/.../screens/WidgetsScreen.kt`

### Required reference items
- [ ] Switch, checkbox, two radio buttons.
- [ ] Seven reference button presentations: default, colored, transparent, outline, contained, contained-primary, contained-transparent.
- [ ] Spinner with four items.
- [ ] Search field/view with up/back action and clear state.
- [ ] Card row variants: divider control, end drawable, icon, summary/action.
- [ ] Separate-switch row and icon switch row.
- [ ] Three-row radio group.
- [ ] Relative links card.
- [ ] Bottom tip title/summary/link.
- [ ] Two-action floating action bar.
- [ ] Switch bar with transient progress state.
- [ ] Suggest app-bar with close/action.
- [ ] Popup menu, image button, bottom-sheet trigger, snackbar/icon/action, progress button, tip popup.

### Motion/lifetime
- [ ] Popup/menu content remains composed through exit transition.
- [ ] Snackbar/tip/menu/dialog motion uses named tokens and interruption-safe state.
- [ ] Search/action/suggest app-bar transitions support keyboard/focus/predictive-back hooks where applicable.

**Commit:** `feat: add One UI widget and feedback parity`

---

## Task 5 — Navigation, drawer, tabs, bottom navigation, rail, and app-bar modes

**Create**
- `components/navigation/OneUiTabs.kt`
- `components/navigation/OneUiNavigation.kt`
- `components/navigation/OneUiNavigationRail.kt`
- `components/navigation/OneUiDrawer.kt`
- `patterns/appbar/OneUiSearchMode.kt`
- `patterns/appbar/OneUiActionMode.kt`
- `demo/.../screens/NavigationScreen.kt`

### Reference parity
- [ ] Rounded two-tab layout.
- [ ] Scrollable subtab layout with 3 subtabs.
- [ ] Main 3-icon tab layout with auto-weight behavior.
- [ ] Bottom navigation icon style with overflow/text-only entries.
- [ ] Text-only bottom navigation.
- [ ] Bottom tab layout with the 13 reference destinations and scroll/overflow behavior.
- [ ] Navigation rail + drawer selection expansion used by the app shell.
- [ ] `alwaysExpanded=false`/collapsed selected-item presentation.
- [ ] Search mode, action mode, select-all state, badges, app-bar suggestion state, drawer lock behavior.

**Tests:** responsive widths, D-pad traversal, RTL physical motion, interrupted indicator animation, search/action-mode coexistence.

**Commit:** `feat: add One UI navigation and app bar parity`

---

## Task 6 — Dialogs, bottom sheets, QR and overlays

**Create**
- `components/dialog/OneUiDialog.kt`
- `components/sheet/OneUiSheet.kt`
- `components/qr/OneUiQrCode.kt`
- `patterns/qr/OneUiQrCard.kt`
- `demo/.../screens/QrCodesScreen.kt`

### Required parity
- [ ] Bottom sheet drag handle, title, divider, scrollable content, source-backed enter/exit channels.
- [ ] QR variants: icon+content, square frame/no icon, custom foreground+tinted anchor/frame+rounded, empty/default.
- [ ] QR-in-bottom-sheet/profile action presentation.
- [ ] Alert/options dialogs and list/multi-select dialog foundations used by pickers/preferences/list demos.
- [ ] Dialog/sheet focus trapping, back handling, reduced motion, large-screen side clearance.

**Commit:** `feat: add One UI dialogs sheets and QR components`

---

## Task 7 — Compose-native picker family

**Create**
- `components/picker/OneUiWheelPicker.kt`
- `components/picker/OneUiNumberPicker.kt`
- `components/picker/OneUiDatePicker.kt`
- `components/picker/OneUiTimePicker.kt`
- `components/picker/OneUiSpinningDatePicker.kt`
- `components/picker/OneUiSleepTimePicker.kt`
- `components/picker/OneUiOptionPicker.kt`
- `components/picker/OneUiColorPicker.kt`
- `demo/.../screens/PickersScreen.kt`

### Required reference parity
- [ ] Three inline number wheels.
- [ ] Inline time/date/spinning-date/sleep-time variants.
- [ ] Date dialog, time dialog, start/end-time dialog, color dialog.
- [ ] Multi-option and single-option pickers, adaptive landscape variant, custom top/bottom content.
- [ ] Number picker min/max, dividers, label, editable input, wrap/disabled behavior.
- [ ] Color swatches, custom/alpha controls.
- [ ] Wheel fling/snap/selection animations are source-backed and interruptible.

**Tests:** pure picker math/date boundaries, semantics, keyboard/D-pad, wheel snap, dialog hoisting, landscape layout.

**Commit:** `feat: add Compose-native One UI picker family`

---

## Task 8 — RecyclerViews parity as Compose list patterns

**Create**
- `components/list/OneUiIndexedList.kt`
- `components/list/OneUiFastScroller.kt`
- `components/list/OneUiSelectableList.kt`
- `patterns/apppicker/OneUiAppPicker.kt`
- `patterns/profile/OneUiProfile.kt`
- `demo/.../screens/RecyclerViewsScreen.kt`
- `demo/.../screens/recycler/IconsScreen.kt`
- `demo/.../screens/recycler/StargazersScreen.kt`
- `demo/.../screens/recycler/AppsScreen.kt`
- deterministic fixture models/data.

### Icons tab
- [ ] Search with 300 ms debounce-equivalent UI behavior, empty/progress states.
- [ ] Long-press action mode, multi-select, select-all, action menu, badge/tip feedback.
- [ ] Complete drawable catalogue.

### Stargazers tab
- [ ] Fixture list + avatars/placeholders, search, loading/empty/error, selection/action mode.
- [ ] Profile details with 100dp avatar, name/url, 46dp action icons, detail cards, QR/share bottom navigation.
- [ ] Options menu/dialog and QR sheet.

### Apps tab
- [ ] Exact seven `ListTypes`: list, action button, checkbox, radio, switch, grid, grid-checkbox.
- [ ] Spinner type switcher, search mode, list/grid, index tip/fast scroll, select-layout mode, all-selected state, empty/progress states.

**Commit:** `feat: add Compose-native list and app-picker parity`

---

## Task 9 — Preferences parity

**Create**
- `components/preference/OneUiPreference.kt`
- `components/preference/OneUiPreferenceCategory.kt`
- `components/preference/OneUiPreferenceCards.kt`
- `components/preference/OneUiPreferenceDialogs.kt`
- `components/preference/OneUiSeekBarPreference.kt`
- `patterns/preferences/OneUiPreferencesScreen.kt`
- `demo/.../screens/PreferencesScreen.kt`

### Required reference items
- [ ] Suggestion card + action.
- [ ] Tips card and inset category.
- [ ] Switch-bar preference.
- [ ] Horizontal radio image entries with dependency state.
- [ ] Switch preference and switch preference screen.
- [ ] Updatable-widget preference.
- [ ] Checkbox preference.
- [ ] Edit-text, dropdown, list, multi-select, color-picker preferences.
- [ ] Basic seekbar preference.
- [ ] Expanded seekbar preference.
- [ ] Level-bar preference with ticks/min/max/adjustable state.
- [ ] Center-based/seamless preference with left/right labels and continuous updates.
- [ ] About row + related-links/custom-layout preference.

**Tests:** dependency graph, state restoration, dialogs, keyboard navigation, disabled semantics, slider snapping.

**Commit:** `feat: add Compose-native One UI preferences`

---

## Task 10 — About, custom-about and reusable large-title/adaptive patterns

**Create**
- `patterns/about/OneUiAppInfo.kt`
- `patterns/about/OneUiCustomAbout.kt`
- `patterns/cards/OneUiRelatedLinksCard.kt`
- `demo/.../screens/AboutScreen.kt`
- `demo/.../screens/CustomAboutScreen.kt`

### Required parity
- [ ] Basic AppInfo layout with Status/GitHub actions.
- [ ] Portrait 0.5-height collapsing header with 72dp identity icon, app/version text, 48dp actions.
- [ ] Collapsed 38dp identity row.
- [ ] Predictive-back/app-bar progress behavior and content alpha transition.
- [ ] Landscape/freeform collapsed presentation.
- [ ] Contributor rows, OSS license rows, related links, app-info menu action.
- [ ] Compose-native swipe-up hint animation (do not embed Lottie View).

**Commit:** `feat: add One UI about and adaptive page patterns`

---

## Task 11 — Finish catalog, compatibility, docs and hard completion gates

**Modify/create**
- all `demo/screens/*`
- `oneui8/Compatibility.kt`
- `docs/MIGRATION_ONEUI8.md`
- `README.md`, `GETTING_STARTED.md`, `docs/ATTRIBUTION.md`, `NOTICE`
- `.github/workflows/oneui8-demo-apk.yml`
- `.github/workflows/oneui8-parity-emulator.yml`
- `demo/src/androidTest/.../CatalogParitySmokeTest.kt`

### Steps
- [ ] Remove `LegacyShowcase` only after every old demo element has a new catalog home.
- [ ] Update README claim from “demonstrates every component” only when manifest reports zero missing.
- [ ] Add compile compatibility fixture for legacy and `OneUI8*` APIs.
- [ ] CI clones the pinned reference SHA, runs manifest/drawable/motion audits, unit tests, Android-test compilation, lint, release AAR and debug APK.
- [ ] Emulator job traverses every top-level and nested route, opens all menus/dialogs/sheets, exercises selection/search/action mode and captures representative screenshots.
- [ ] Run emulator sizes 1080×2340, 1600×2560, 1920×1080 plus RTL and dark mode smoke.
- [ ] Upload catalog APK, reports, parity manifest result and screenshots.
- [ ] Final audit requires `missingRoutes=0`, `missingItems=0`, `missingDrawables=0`, `missingMotionMappings=0`.

**Final verification**
```bash
python scripts/audit-oneui8-parity.py --strict
./gradlew clean :lib:testDebugUnitTest :lib:lintDebug :lib:assembleRelease :demo:assembleDebug
# emulator workflow must also be green
```

**Final commit:** `test: verify complete One UI 8 Compose sample parity`
