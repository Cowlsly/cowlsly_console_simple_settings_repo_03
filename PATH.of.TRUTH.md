# PATH.of.TRUTH — Cowlsly Simple Settings

## Authority order

1. `PATH.of.TRUTH.md`
2. `README.md`
3. `ROADMAP.md`
4. `TODO.md`
5. `UI_ASSET_SOURCE.md`
6. `ui_asset_dependencies.json`
7. `dev/dat/doc/SIMPLE_SETTINGS_VISION.md`

Historical local asset manifests remain migration records and never override the canonical UI repository.

## Repository ownership

Simple Settings owns the Android settings catalogue, search/ranking behavior, safe system-intent navigation, suite settings contracts, volume/hearing controls, CASMEA entry integration, and explicitly gated developer shortcuts.

It does not own API-key storage or reusable UI masters.

## Rules for agents

1. Read this file and the authority documents before work.
2. Do not store ALI-Key/API secrets, recovery data, or private vault material.
3. Do not create shared branding, icons, panels, backgrounds, controls, or UI audio as local masters.
4. Add reusable assets to `Cowlsly/cowlsly-ui-assets-and-data-files`, update its manifests/checksums, then sync generated Android copies from `ui_asset_dependencies.json`.
5. Existing files under `assets/branding/`, `assets/images/`, and Android `res/` remain protected generated copies or migration candidates until canonical checksums and a Java 21 build pass.
6. Android resources must be copied directly into valid resource-type directories; nested arbitrary folders under `res/drawable*` or `res/raw` are forbidden.
7. Settings remain user-consent-led. No hidden admin or developer access.
8. Record exact changed paths, tests, security effects, canonical source paths, and hashes.

## Canonical asset state

The complete reusable set is already canonical and byte-verified:

- eight transparent settings/zone icons;
- five volume-step controls;
- compact Cowlsly PNG and SVG branding;
- the animated cog-machine SVG background.

Existing local copies remain until deterministic sync and Java 21 validation confirm safe cleanup.

## Canonical structure

```text
app/
docs/
dev/dat/doc/
tests/
tools/
assets/                 # historical records or generated copies only
```

## Required validation

- Java 21 `./gradlew assembleDebug`
- unit/instrumentation tests where available
- resource and Compose/XML reference checks
- suite settings schema migration tests
- system-intent fallback checks
- accessibility, reduced-motion, and hearing-warning checks

## Branch policy

The canonical and only branch is `root`. Do not create a separate line of work without an explicit reviewed PR plan.

## Work log

2026-07-09 | initial repo checklist
- Recorded original local asset inventory and guarded API-key storage out of scope.

2026-07-12 | canonical asset and structure correction
- Added current README/ROADMAP/TODO authority chain.
- Replaced website-copy/local-master instructions with canonical UI dependency rules.
- Imported and byte-verified all 16 reusable assets in the canonical UI repository.

2026-07-12 | final branch closeout
- Integrated all reviewed histories into `root`.
- Deleted every non-root branch after ancestry verification.
- Confirmed `root` as the GitHub default branch.
- Retargeted dependency metadata and documentation from `main` to `root`.