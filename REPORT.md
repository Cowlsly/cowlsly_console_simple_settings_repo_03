# Simple Settings — Phase 1 Complete

**Repo:** `cowlsly_console_simple_settings_repo_03`  
**Date:** 2026-07-08  
**Status:** Phase 1 shipped — Android app in `app/`, docs and assets in place

---

## Executive summary

Cowlsly Simple Settings is the **master settings hub** for the Cowlsly Console suite. Phase 1 delivers the three core features (volume, CASMEA entry, developer shortcut) inside a full paged settings shell with search, usage-based ordering, suite sync, and Cowlsly cyberpunk branding.

---

## What is done

| Area | Status | Notes |
|------|--------|-------|
| Roadmap & scope | Done | `ROADMAP.md` — Phase 1 complete |
| Master vision | Done | `dev/dat/doc/SIMPLE_SETTINGS_VISION.md` |
| Founding context | Done | `dev/dat/doc/ROADMAP.ORIGINAL.md` |
| Phase 1 SVG assets | Done | Icons + volume-step badges in `assets/images/` |
| Asset catalog | Done | `assets/ASSET_MANIFEST.md` |
| Branding kit | Done | `assets/branding/`, `Cowlsly/BRANDING.md` |
| GitBook / docs tree | Done | `Cowlsly/SUMMARY.md`, README, REPORT |
| Android application | **Done** | `app/` — Jetpack Compose, 46 Kotlin sources |
| Build | **Done** | `JAVA_HOME=/path/to/java-21 ./gradlew assembleDebug` |

---

## Phase 1 features shipped

### A) Volume control

- Mute toggle and stepped levels: **0% / 25% / 50% / 75% / 90%** (no 100%).
- Hearing-damage warning when headphones are in use.
- `VolumePanel.kt` + `SettingsViewModel` persistence via `SecurePrefs`.

### B) CASMEA information entry

- Single source of truth for personal/medical emergency data.
- `CasmeaPanel.kt` form + `CasmeaContentProvider` (`com.cowlsly.simplesettings.casmea`).
- PIN-gated edits; CASMEA app reads via content provider (no entry screen in CASMEA).

### C) Developer options shortcut

- Gated section visible only when developer access is granted.
- PIN re-entry on each visit before opening Developer Options intents.
- Shizuku panel for privileged settings when binder is granted.

---

## Application structure

```
app/
├── build.gradle.kts
├── proguard-rules.pro
└── src/main/
    ├── AndroidManifest.xml
    ├── java/com/cowlsly/simplesettings/
    │   ├── MainActivity.kt
    │   ├── SimpleSettingsApplication.kt
    │   ├── account/          # Cowlsly.com AccountManager + SyncAdapter
    │   ├── audio/            # Page-turn UI sounds
    │   ├── data/             # Catalog, CASMEA + suite providers, secure prefs
    │   ├── shizuku/          # Privileged settings helper
    │   ├── sync/             # Suite sync engine, website client, permissions
    │   └── ui/
    │       ├── components/   # Glass panels, cogs background, page turner
    │       ├── panels/       # Volume, CASMEA, Shizuku, Credits, …
    │       └── theme/        # Cowlsly palette + panel tint
    └── res/                  # Launcher, strings, Cowlsly logo drawable
```

---

## Beyond Phase 1 (already scaffolded)

The app ships more than the three Phase 1 pillars — by design, per the master vision:

- Paged glass-panel UI over animated cogs background
- Search bar with usage/search ranking (`UsageTracker`, `SettingsSorter`)
- 50+ system setting intents (network, display, security, privacy, apps, …)
- Suite sync: local JSON + ContentProvider + cowlsly.com API pull/push
- Cowlsly Console Settings logo button → `https://cowlsly.com/console/settings`
- Credits page (Shizuku, Hidden Settings, Activity Launcher)

---

## Build instructions

Requires **Java 21** (Java 25 is not yet supported by the Kotlin Gradle plugin):

```bash
JAVA_HOME=/usr/local/sdkman/candidates/java/21.0.10-ms ./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk` (gitignored).

---

## Integration consumers

| Consumer repo | Contract |
|---------------|----------|
| `cowlsly_console_medical_emergency_assistant_casmea_repo_02` | Read `content://com.cowlsly.simplesettings.casmea/` |
| `cowlsly_console_authenticator_repo_04` | Developer grant (interim: local flag until repo 04 ships) |
| Suite apps | Read `content://com.cowlsly.simplesettings.sync/` |

---

## Later phases

See `ROADMAP.md` for Phases 2–6 (full security hub, live developer gate, website JSON export, embeddable SDK).

---

## Related documents

- `ROADMAP.md` — active scope and build log
- `dev/dat/doc/SIMPLE_SETTINGS_VISION.md` — full master settings design
- `dev/dat/doc/ROADMAP.ORIGINAL.md` — founding context
- `assets/ASSET_MANIFEST.md` — asset-to-feature mapping
- `Cowlsly/BRANDING.md` — UI palette and background rules
- `Cowlsly/SUMMARY.md` — table of contents for this repo