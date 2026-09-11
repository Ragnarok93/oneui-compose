# OneUI Compose

Jetpack Compose components and design tokens inspired by Samsung One UI, now with an additive **One UI 8 / SESL8-style Compose layer**.

The project keeps the existing `OneUITheme` and legacy component API while adding reusable motion, shape, interaction, navigation and icon primitives under `org.oneui.compose.oneui8`.

## One UI 8 additions

- Compose-native SESL-style motion tokens, including the `0.22, 0.25, 0, 1` reference curve.
- Shared press-scale/ripple interaction behavior.
- One UI 8 buttons and icon buttons.
- Switch and slider wrappers.
- Rounded cards, sections and list rows.
- Two-stage animated selection indicator.
- Compact expanding navigation bar.
- Original rounded 24×24 vector icon set.
- Design-kit integration guidance and asset provenance rules.
- `:demo` application that interactively demonstrates every new component.

See [One UI 8 design guidelines](docs/ONEUI8_DESIGN_GUIDELINES.md) and [attribution](docs/ATTRIBUTION.md).

## Demo APK

Build locally with:

```bash
./gradlew :lib:testDebugUnitTest :demo:assembleDebug
```

The APK is generated at:

```text
demo/build/outputs/apk/debug/demo-debug.apk
```

The feature branch also contains a GitHub Actions workflow that builds and uploads `oneui-compose-oneui8-demo-debug` on every push.

## Getting started

Existing API documentation remains in [GETTING_STARTED.md](GETTING_STARTED.md). New projects can wrap screens in `OneUI8Theme` and use components from `org.oneui.compose.oneui8.components`.

```kotlin
OneUI8Theme {
    OneUI8Button(text = "Continue", onClick = { /* ... */ })
}
```

## Design-library scope

This remains a design/component library. Application state, persistence and business logic stay in the consuming app. The new One UI 8 APIs are intentionally additive so older consumers do not need a flag-day migration.

## Credits

- Samsung for the One UI design language.
- OneUIProject / `oneui-design` contributors for the open Android/SESL reference work.
- Original `oneui-compose` contributors.
