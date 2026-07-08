# README

<div align="center"><img src="Cowlsly/.gitbook/assets/cowlsly_logo_small.png" alt="Cowlsly Production" width="220"></div>

## cowlsly\_console\_simple\_settings\_repo\_03

Cowlsly Simple Settings is the shared settings module for the Cowlsly Console suite.

Cyberpunk UI in this repo follows the Cowlsly design language: neon glass panels over the **forever-turning cog machine** background (`assets/branding/cowlsly_cogs_background_animated.svg`). See `Cowlsly/BRANDING.md`.

### Current status

**Phase 1 complete** — Android app in `app/` with paged settings panels, search, usage-based ordering, and Phase 1 features (volume, CASMEA entry, developer gate). Build with **Java 21** (Java 25 is not supported by the current Kotlin Gradle plugin):

```bash
JAVA_HOME=/path/to/java-21 ./gradlew assembleDebug
```

### What Simple Settings is

One app. One scroll. The settings you need most are **closest to you** at the top — volume, tint, security, privacy — then every regular device setting category, then developer options (opened only after access is granted).

* **Closest** — volume, mute, panel tint, hearing-safe steps
* **Security & privacy** — PIN, biometrics, permissions, privacy dashboard
* **Personal** — profile + CASMEA medical info entry (edits only here)
* **Device** — Wi‑Fi, display, sound, storage, battery, apps, notifications, accounts (system intents)
* **Developer** — gated section; PIN re-entry; opens Android Developer Options

See `dev/dat/doc/SIMPLE_SETTINGS_VISION.md` for the full design.

### Phase 1 (shipped)

* Volume control with safe-listening steps (0–90%, hearing warning).
* CASMEA information entry screen (`CasmeaContentProvider` for repo 02).
* Developer shortcut (gated; PIN re-entry; Shizuku + system dev intents).

### Documents

* `dev/dat/doc/SIMPLE_SETTINGS_VISION.md` — **master settings app vision** (full scroll, all zones).
* `ROADMAP.md` — active build phases.
* `dev/dat/doc/ROADMAP.ORIGINAL.md` — founding context and rebuild notes.
* `assets/README.md` — asset naming, folder layout, and Phase 1 catalog.
* `assets/ASSET_MANIFEST.md` — labeled asset index with phase mapping.
* `assets/branding/README.md` — shared Cowlsly logo and cogs background kit.
* `Cowlsly/BRANDING.md` — suite-wide branding rules (logo + forever-turning machine).
* `Cowlsly/SUMMARY.md` — table of contents for this repo.
