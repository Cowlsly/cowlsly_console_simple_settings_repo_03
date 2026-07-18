# Cowlsly Console Simple Settings

<div align="center"><img src="Cowlsly/.gitbook/assets/cowlsly_logo_small.png" alt="Cowlsly Production" width="220"></div>

Repository: `Cowlsly/cowlsly-simple-settings-console`  
Active and only branch: `root`

Cowlsly Simple Settings is the shared Android settings module for the Cowlsly Console suite.

Cyberpunk UI follows the Cowlsly design language: neon glass panels over the **forever-turning cog machine** background. Reusable artwork is consumed from the canonical UI asset repository; local copies are generated platform resources.

## Current status

**Phase 1 complete**: Android app in `app/` with paged settings panels, search, usage-based ordering, and Phase 1 features for volume, CASMEA entry, and the developer gate. Build with **Java 21**:

```bash
JAVA_HOME=/path/to/java-21 ./gradlew assembleDebug
```

The complete reusable asset set is canonical in `Cowlsly/cowlsly-ui-assets-and-data-files`: eight icons, five volume-step controls, compact PNG and SVG logos, and the animated cog-machine SVG background. Original SHA-256 values and byte identity are recorded there. Existing local paths are generated consumer copies and remain until deterministic sync and a final Java 21 build confirm safe cleanup.

## What Simple Settings is

One app. One scroll. The settings you need most are closest to you at the top: volume, tint, security, and privacy. Regular device setting categories follow, with developer options opened only after access is granted.

- **Closest**: volume, mute, panel tint, hearing-safe steps
- **Security & privacy**: PIN, biometrics, permissions, privacy dashboard
- **Personal**: profile plus CASMEA medical-information entry, edited only here
- **Device**: Wi-Fi, display, sound, storage, battery, apps, notifications, accounts through system intents
- **Developer**: gated section with PIN re-entry and Android Developer Options

See `dev/dat/doc/SIMPLE_SETTINGS_VISION.md` for the full design.

## Phase 1 shipped

- Volume control with safe-listening steps from 0 to 90 percent and a hearing warning.
- CASMEA information entry screen using `CasmeaContentProvider` for repo 02.
- Developer shortcut with PIN re-entry, Shizuku guidance, and system developer intents.

## Canonical UI asset source

Reusable Cowlsly UI assets come from `Cowlsly/cowlsly-ui-assets-and-data-files` on active branch `root`. This repository declares pinned canonical paths in `ui_asset_dependencies.json`; generated assets and Android resources must not be edited manually. See `UI_ASSET_SOURCE.md`.

## Documents

- `PATH.of.TRUTH.md` — current repository authority and safety rules.
- `ROADMAP.md` — active build phases.
- `TODO.md` — current work queue.
- `dev/dat/doc/SIMPLE_SETTINGS_VISION.md` — master settings-app vision.
- `dev/dat/doc/ROADMAP.ORIGINAL.md` — founding context and rebuild notes.
- `UI_ASSET_SOURCE.md` — canonical asset consumer policy and sync instructions.
- `ui_asset_dependencies.json` — pinned canonical paths and generated destinations.
- `assets/README.md` — legacy local-path classification during migration.
- `assets/ASSET_MANIFEST.md` — historical Phase 1 asset index and canonical migration mapping.
- `Cowlsly/BRANDING.md` — suite-wide branding design rules; canonical binaries live in the UI repository.
- `Cowlsly/SUMMARY.md` — table of contents for this repo.