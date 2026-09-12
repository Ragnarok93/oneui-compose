# One UI 8 Sample Parity Implementation Plan

> **Execution contract:** Implement the approved `2026-09-11-oneui8-sample-parity-design.md` spec test-first. The reference sample in `Ragnarok93/oneui-design` is the completeness contract. The public One UI Figma design kit is the visual language reference; the connected Figma account currently cannot read the Community file nodes, so exact Figma node/vector exports must not be claimed. Until readable access exists, measurable geometry/state/motion comes from the MIT `oneui-design`/SESL8 source and visually accessible One UI kit guidance.

**Goal:** Recreate every reusable item demonstrated by the OneUI8 sample app as Compose-native APIs, with particular emphasis on complete icon/drawable coverage and source-traceable animations.

**Branch:** `feature/oneui8-compose-components-tdd`

**Rules:**
- No `AndroidView` implementation for target components.
- Red/green/refactor for behavior changes. Every production behavior starts with a failing test or parity audit.
- Demo code consumes public library APIs; demo-only facsimiles do not satisfy parity.
- Exact Figma exports only after connector-readable access; otherwise use documented, source-backed reconstruction.
- Do not claim completion until all parity audits, JVM/UI tests, build, route smoke tests, and emulator/device smoke gates pass.

---

## Task 1 — Establish machine-checkable reference manifests

**Files**
- Create `reference/oneui8-demo/routes.txt`
- Create `reference/oneui8-demo/components.txt`
- Create `reference/oneui8-demo/drawables.txt`
- Create `reference/oneui8-demo/motion.txt`
- Create `lib/src/test/java/org/oneui/compose/parity/ReferenceManifestTest.kt`

**RED**
1. Add a test requiring all four manifests to exist, contain no duplicates, and include the eight top-level reference destinations: ProgressBars, SeekBars, Pickers, QRCodes, Navigation, RecyclerViews, MiscWidgets, CustomAbout.
2. Run `./gradlew :lib:testDebugUnitTest --tests '*ReferenceManifestTest*'`; verify failure because manifests are missing.

**GREEN**
3. Populate manifests directly from the `oneui-design` sample/navigation/preferences/icon catalogue/resource source. Each line must include the source path or provenance key where practical.
4. Re-run the focused test and then all library JVM tests.
5. Commit: `test: establish OneUI8 reference parity manifests`.

## Task 2 — Build drawable/icon parity auditing before importing assets

**Files**
- Create `lib/src/main/java/org/oneui/compose/oneui8/icons/OneUI8AssetCatalog.kt`
- Create `lib/src/test/java/org/oneui/compose/parity/OneUI8AssetCatalogTest.kt`
- Update `reference/oneui8-demo/drawables.txt`

**RED**
1. Test that every required reference drawable/icon ID has exactly one Compose asset entry and that catalogue IDs are unique.
2. Test that all reference `IconsRepo` non-`sesl_` entries are represented.
3. Verify expected failure against the current small `OneUI8Icons` set.

**GREEN**
4. Introduce the catalogue API with explicit asset kinds: vector, stateful-vector, animated, raster/painter, generated-shape.
5. Add placeholders only as compile-time catalogue records if needed, but keep the test red until the actual asset can render; `implemented=true` must be required for parity.
6. Commit the audit infrastructure separately from asset implementations.

## Task 3 — Source-backed motion registry and animation parity tests

**Files**
- Update `lib/src/main/java/org/oneui/compose/oneui8/motion/OneUI8Motion.kt`
- Update/bridge `lib/src/main/java/org/oneui/compose/motion/OneUiMotion.kt`
- Create `lib/src/test/java/org/oneui/compose/parity/OneUI8MotionParityTest.kt`
- Update `reference/oneui8-demo/motion.txt`

**RED**
1. Add tests for every verified SESL8 timing/easing/state transition found in the reference source, including selection 110/180 ms stages and bottom-sheet 300/165 enter + 200/140 exit timings.
2. Require provenance metadata for each named motion token.
3. Verify current implementation lacks the complete registry/provenance and fails.

**GREEN**
4. Implement named source-backed motion tokens and reusable specs.
5. Keep unavailable/private curves explicitly marked `approximation` rather than exact (e.g. `sine_out_80` if source remains unavailable).
6. Add press/recoil, selection drawing, navigation selection, app-bar collapse, search/action-mode, menu/sheet/dialog, progress, picker, and list-state motion as discovered.
7. Commit: `feat: add source-backed OneUI8 motion registry`.

## Task 4 — Figma/SESL visual token layer

**Files**
- Update `lib/src/main/java/org/oneui/compose/oneui8/theme/OneUI8Theme.kt`
- Create `OneUI8ColorTokens.kt`, `OneUI8ShapeTokens.kt`, `OneUI8SpacingTokens.kt`, `OneUI8TypeTokens.kt`
- Create `lib/src/test/java/org/oneui/compose/parity/OneUI8VisualTokensTest.kt`
- Update `docs/ONEUI8_DESIGN_GUIDELINES.md`

**RED**
1. Test required token families and critical values used repeatedly in reference layouts.
2. Add invariants for touch targets, corner hierarchy, content margins, nested surface radii, icon optical sizes, and disabled/selected alpha behavior.
3. Verify missing token families fail.

**GREEN**
4. Reconstruct token values from accessible One UI kit guidance and `oneui-design` dimensions/colors/styles, documenting source and whether exact or reconstructed.
5. Wire theme and existing components to tokens rather than ad-hoc constants.
6. Commit: `feat: align OneUI8 visual tokens with design kit`.

## Task 5 — Complete static and stateful icon/drawable catalogue

**Files**
- Expand `lib/src/main/java/org/oneui/compose/oneui8/icons/`
- Add generated vector files split by category to keep files maintainable.
- Add painter/state classes for non-vector assets.
- Update `OneUI8AssetCatalogTest.kt`

**RED/GREEN loop per asset family**
1. Select one reference family (navigation, actions, categories, controls, picker, drawer, FAB, about, list/widget).
2. Make parity test fail for that family.
3. Recreate/import the MIT-source geometry as Compose `ImageVector`/vector resource when permitted; use Compose drawing for stateful geometry.
4. Verify viewBox/intrinsic size, tint behavior, RTL mirroring where applicable, and selected/unselected state.
5. Repeat until every required non-`sesl_` drawable/icon is implemented and the asset audit is green.
6. Keep provenance alongside generated assets.
7. Commit per family, not as one giant commit.

## Task 6 — Animated icon library

**Files**
- Expand `lib/src/main/java/org/oneui/compose/icons/OneUiAnimatedIcons.kt`
- Create `lib/src/main/java/org/oneui/compose/oneui8/icons/OneUI8AnimatedIcon.kt`
- Create Compose UI tests under `lib/src/androidTest/.../icons/`

**RED**
1. Add deterministic clock tests for each animated icon/state transition demonstrated by the sample/reference assets.
2. Assert initial/mid/final geometry or semantic state rather than screenshot-only existence.

**GREEN**
3. Implement vector/path/rotation/scale/alpha morphs with source-backed durations/easing.
4. Ensure reverse transitions are animated where the reference animates them.
5. Commit per animation family.

## Task 7 — Progress indicator parity

**Files**
- Consolidate/extend `components/progress/` and `progress/` APIs.
- Create UI/JVM tests for determinate and indeterminate variants.

**Coverage**
- rounded determinate
- standard horizontal determinate
- circular indeterminate small/medium/large
- horizontal indeterminate small/medium/large
- progress-button state

**TDD**
1. Write variant/state tests first, including deterministic animation clock checks.
2. Implement Compose-native drawing and animation matching source behavior.
3. Add demo destination and route smoke test.
4. Commit: `feat: complete OneUI8 progress parity`.

## Task 8 — SeekBar/slider parity

**Files**
- Extend `components/slider/` and/or replace legacy `widgets/SeekBar.kt` internals.
- Add `OneUI8SeekBarMode.kt` and UI tests.

**Coverage**
- normal
- expand
- vertical expand
- split
- level bar
- center-based/seamless preference modes
- ticks, labels, values, adjustable/read-only, continuous updates

**TDD**
1. Write geometry/value/gesture/semantics tests per mode.
2. Implement mode-specific track/thumb/tick behavior and source-backed thumb expansion/recoil motion.
3. Add demo page matching reference arrangement.
4. Commit by mode family.

## Task 9 — Selection controls and buttons/FAB parity

**Files**
- Extend buttons, checkbox, radio, switch, floating actions, switch bar.
- Add deterministic animation tests.

**Coverage**
- all enabled/disabled/pressed/selected/checked states
- checkmark two-stage draw
- radio dot transition
- switch thumb/track/recoil
- standard/tonal/outline/transparent buttons used in sample
- icon buttons
- floating action button layout and selected-state behavior
- switch bar

**TDD**
1. Test state semantics and motion before production changes.
2. Align shapes/spacing/icons to token layer.
3. Add reference-style demo sections.

## Task 10 — Pickers and picker dialogs

**Files**
- Extend `picker/`, `picker/time/`, `picker/color/`.
- Add tests for scrolling, selection, locale/RTL, dialog state, and adaptive layout.

**Coverage**
- number/string wheel picker
- date picker + dialog
- time picker + dialog
- color picker incl. alpha
- any sample date/time/color variants
- next/previous controls and selection animations

**TDD**
1. Add deterministic selection/scroll tests.
2. Reconcile existing Compose picker visuals/behavior with One UI 8 tokens/source.
3. Build matching Pickers destination.

## Task 11 — QR code patterns

**Files**
- Create `components/qrcode/` or `patterns/qrcode/`.
- Add rendering/data tests and demo route.

**TDD**
1. Test deterministic module matrix generation/accepted input state first.
2. Implement the One UI-styled QR container, action/label states, and any sample variants.
3. Match reference surface spacing/corners and iconography.

## Task 12 — Navigation, app shell, tabs, drawer, rail, menus

**Files**
- Extend `navigation/`, `navigation/drawer/`, `navigation/rail/`, `layout/toolbar/`, `widgets/menu/`.
- Create navigation UI tests and demo routes.

**Coverage**
- large-title app bar and collapse
- search mode and contextual/action mode
- navigation drawer category/header/item/separator
- popup menu
- text tabs/icon tabs/fixed tabs
- expanded selected-item behavior
- bottom navigation variants
- navigation rail
- generic navigation bar
- adaptive compact/wide behavior

**TDD**
1. Add state + deterministic transition tests.
2. Make demo shell itself consume these APIs.
3. Verify each reference navigation interaction through route smoke tests.

## Task 13 — Recycler/list patterns: Icons, Stargazers, Apps

**Files**
- Create `patterns/list/`, `patterns/search/`, `patterns/actionmode/`.
- Add demo destinations/tabs and fixture data.

**Coverage**
- Icons catalogue with every audited asset
- deterministic Stargazers fixture list
- deterministic Apps fixture list
- search
- selection/action mode
- list rows, dividers, indexed/fast-scroll behavior represented by sample
- dialogs/bottom sheets/detail actions used by those screens

**TDD**
1. Test filtering, selection model, index lookup, action-mode transitions.
2. Implement UI with One UI list/card/search tokens and motion.
3. The Icons tab becomes the human-visible completeness audit for Task 5/6.

## Task 14 — Misc. Widgets parity

**Files**
- Extend/create Compose-native widgets and a `WidgetsDemoScreen`.

**Method**
1. Enumerate every child/interaction from the reference fragment/layout into `components.txt` before coding.
2. For each missing widget, add a failing test, implement API, add it to the demo.
3. Cover progress button, edit/search fields, menus, dialogs/sheets, rounded surfaces, tips/cards, dividers/separators, and every other visible sample control.
4. Do not close the task until the manifest has zero unimplemented entries.

## Task 15 — Preferences parity

**Files**
- Extend `preference/` and demo preference screen.

**Coverage**
- SuggestionCardPreference
- TipsCardPreference
- SwitchBarPreference
- HorizontalRadioPreference (including image style)
- SwitchPreference
- SwitchPreferenceScreen
- UpdatableWidgetPreference
- CheckBoxPreference
- EditTextPreference
- DropDownPreference
- ListPreference
- MultiSelectListPreference
- ColorPickerPreference
- SeekBarPreference basic/expanded/level-bar/center-based
- inset/category/layout/relative-links cards

**TDD**
1. Add behavior tests for each preference state and dependency relationship.
2. Implement missing Compose APIs.
3. Build the complete reference-like preferences page.

## Task 16 — About and Custom About parity

**Files**
- Reconcile `layout/app/` with sample Custom About.
- Add demo route/tests.

**TDD**
1. Test version/update/link/button states first.
2. Implement exact reference information hierarchy and One UI 8 surface treatment using deterministic sample data.

## Task 17 — Full demo app navigation and parity route audit

**Files**
- Replace monolithic `demo/.../MainActivity.kt` showcase with route-driven demo architecture under `demo/src/main/java/org/oneui/compose/demo/`.
- Create `DemoDestination.kt`, `DemoNavHost.kt`, screen files.
- Add `demo/src/test/.../DemoRouteParityTest.kt` and android tests.

**RED**
1. Test that every route in `reference/oneui8-demo/routes.txt` has a demo destination.
2. Test every component manifest entry maps to at least one demo showcase ID.
3. Verify current single-page demo fails.

**GREEN**
4. Implement the complete destination hierarchy.
5. Add semantic test tags for every catalogue item.
6. Ensure adaptive portrait/tablet/desktop arrangements preserve reference visual hierarchy.

## Task 18 — Automated design and provenance audits

**Files**
- Add `scripts/verify-oneui8-parity.py`
- Add generated report `build/reports/oneui8-parity/` (not committed except schema/sample as appropriate)
- Update docs/attribution.

**Checks**
- zero missing manifest components
- zero missing icon/drawable IDs
- every motion token has source/provenance and exact/approximate flag
- no `AndroidView` usage in target OneUI8 packages
- no duplicate catalogue IDs
- every demo route reachable
- every public component has a showcase entry

**TDD**
1. Add fixture/unit tests for audit script or equivalent JVM audit logic.
2. Make CI fail on any parity regression.

## Task 19 — CI matrix and emulator smoke validation

**Files**
- Update `.github/workflows/oneui8-demo-apk.yml`
- Add/extend connected/emulator test workflow.

**Gates**
- `:lib:testDebugUnitTest`
- `:lib:lintDebug`
- `:lib:connectedDebugAndroidTest` where runner/emulator supports it
- demo unit/UI tests
- parity audit script
- `:lib:assembleRelease`
- `:demo:assembleDebug`
- route smoke on emulator/device
- artifact upload

Run failures are blockers; diagnose rather than bypassing a gate.

## Task 20 — Final visual review, APK validation, and branch completion

1. Build final debug APK from the fully green parity branch.
2. Install/launch in emulator or available Android runner/device.
3. Exercise every top-level route and nested Icons/Stargazers/Apps tabs; verify controls change state and animated items visibly animate.
4. Capture parity report totals (required/implemented/missing = missing must be zero).
5. Download and hash the APK artifact.
6. Use `verification-before-completion` and then `finishing-a-development-branch` before claiming success or proposing merge.

---

## Visual review standard

For every recreated component/screen, compare against the Figma One UI visual language and the `oneui-design` reference using this order of authority:

1. **Readable exact Figma node/vector data**, if connector access becomes available.
2. **MIT `oneui-design`/SESL8 source and resources** for measurable dimensions, drawable geometry, states, and motion.
3. **Publicly visible One UI design-kit guidance** for composition, optical balance, spacing hierarchy, shape language, and visual intent.
4. Custom reconstruction only for gaps, clearly documented as reconstructed/approximate.

A passing compiler is not a visual parity result. Components must also satisfy the asset, motion, state, and demo-manifest audits above.