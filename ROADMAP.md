# ROADMAP — Cowlsly Simple Settings

## Mission

Provide one safe, accessible Android settings hub for the Cowlsly suite: frequently used controls first, regular Android settings shortcuts next, and sensitive/developer functions behind explicit gates.

Active and only branch: `root`.

---

## What this app is and where it fits

Simple Settings is the **master settings hub** for every Cowlsly product — Android app, website console, and future platforms. When a user changes volume, security preferences, or CASMEA medical information here, those changes propagate outward to the entire suite via `cowlsly_website_settings_sync.json` and the `CasmeaContentProvider`. No other Cowlsly app rebuilds its own settings screens; all consume or deep-link into this one.

### Cowlsly suite map (2026)

| Product | Role | Settings relationship |
|---------|------|-----------------------|
| **console-simple.settings-app** (this repo) | Master settings hub — Android | Source of truth for all user prefs |
| **Cowlsly.com** | Main web property — console + community | Receives settings sync JSON; hosts web console UI |
| **Cowlsly.CC** | Cowlsly Chats platform | Inherits console UI prefs; links to settings via web console |
| **Cowlsly.Net** | Advertising and sales property | Reads suite identity/profile for personalisation |
| **Cowlsly Vault** | Secret/key storage | Reads security tier from P3; never stores API keys in settings |
| **CASMEA** | Medical information consumer | Reads P5 fields read-only; all edits are here only |
| **Cowlsly Authenticator** *(future)* | Identity and authentication | Reads P3–P5 identity + security layer |

---

## Current status

Phase 1 Android app exists in `app/` with Compose pages, search, usage-based ranking, system-setting intents, volume controls, CASMEA information entry, and a gated developer shortcut. The last recorded successful build used Java 21.

The complete reusable Simple Settings asset set is canonical and byte-verified in `Cowlsly/cowlsly-ui-assets-and-data-files`. `UI_ASSET_SOURCE.md` and `ui_asset_dependencies.json` define canonical paths and generated Android destinations. Existing local copies remain protected until deterministic sync and a fresh build pass.

---

## Phase 1 — Foundation ✅ complete

- [x] Compose application scaffold.
- [x] Paged settings catalogue and search.
- [x] Usage-based ordering (open ×3, search ×2 rank scoring).
- [x] Volume steps, mute, and hearing warning (0 / 25 / 50 / 75 / 90 %).
- [x] CASMEA information entry provider.
- [x] Explicitly gated developer shortcut (PIN re-entry required each visit).
- [x] Suite settings sync foundation.
- [x] Canonical UI asset set imported and byte-verified (16 reusable assets).
- [x] Pinned immutable canonical release `acf7ffaab736283daccd286d01abf60a8f2a80a2`.
- [x] Java 21 CI with build, test, lint, and APK artifact steps.
- [x] All non-root branches merged and deleted; `root` is the only branch.

---

## Phase 2 — Canonical assets and build health

Goal: deterministic, reproducible builds from the canonical UI asset source.

- [x] Import shared branding, icons, volume controls, and animated background into the canonical UI repository.
- [x] Verify source/canonical SHA-256 and byte identity for all 16 reusable assets.
- [x] Pin immutable canonical release `acf7ffaab736283daccd286d01abf60a8f2a80a2` on branch `root`.
- [ ] Run canonical sync tool and write `.cowlsly_ui_generated.json`.
- [ ] Copy generated Android resources into valid `res/drawable-nodpi` and `res/raw` paths.
- [ ] Repair Compose/XML resource references only where generated paths differ from current.
- [ ] Pass `./gradlew assembleDebug`, `./gradlew test`, and `./gradlew lint` with Java 21.
- [ ] Add or confirm checksum drift validation in CI (fail build if canonical hash mismatches).
- [ ] Remove obsolete local masters only after sync receipt, reference checks, and Java 21 validation pass.

---

## Phase 3 — Settings reliability and coverage

Goal: every setting panel works correctly, degrades gracefully, and is tested.

- [ ] Review all Android system-settings intents against current Android API levels; add unavailable-screen fallbacks for each.
- [ ] Complete the full P1–P13 panel scroll as described in `SIMPLE_SETTINGS_VISION.md`.
- [ ] Add unit tests: ranking algorithm, search tokenisation, settings serialisation, and access-tier enforcement.
- [ ] Add instrumentation tests: system-intent launch behaviour, volume control feedback, tint persistence.
- [ ] Add accessibility checks: TalkBack labels, reduced-motion support, hearing-warning on volume steps.
- [ ] Version the suite settings contract (`cowlsly_suite_settings_v1.json`) and add migration tests for future schema changes.

---

## Phase 4 — Security and privacy hub

Goal: PIN / biometric / session unlock integrated properly; privacy controls surfaced clearly.

- [ ] Complete PIN/biometric/session-unlock integration — no secrets stored in settings exports or logs.
- [ ] Add permissions overview page (P4) with plain-language explanations and direct Android intent links.
- [ ] Add privacy dashboard navigation (API 30+) with graceful fallback for older Android.
- [ ] Keep CASMEA fields scoped, consent-aware, and encrypted at rest; show only what the user permits on the lock screen.
- [ ] Keep developer access explicit, session-limited, and disabled by default; re-entry required every visit.
- [ ] Clipboard auto-clear preference wired to suite-wide setting.

---

## Phase 5 — Reusable module SDK

Goal: other Cowlsly apps embed or deep-link into Simple Settings without rebuilding anything.

- [ ] Publish Simple Settings as an embedded Compose module that Vault, CASMEA, and future apps can include.
- [ ] Define and document stable deep-link scheme (`cowlsly://settings/<page>`).
- [ ] Expose `ContentProvider` (`content://com.cowlsly.simplesettings.sync/document`) for suite settings sync read/write.
- [ ] Add API contract version header; bump on every breaking change.
- [ ] Document integration patterns in `docs/INTEGRATION.md` with examples for each consumer app.

---

## Phase 6 — Web console sync (Cowlsly.com / .CC / .Net)

Goal: settings changes on Android reflect live in the Cowlsly web console without user action.

- [ ] Define `cowlsly_website_settings_sync.json` schema v1: console UI prefs, profile basics, security flags.
- [ ] Implement push-on-change sync from Simple Settings to Cowlsly.com web console endpoint.
- [ ] Implement pull-on-open to reconcile web-side changes back to Android on app foreground.
- [ ] Handle offline/conflict cases: Android wins for security and personal fields; web wins for console display prefs when both have changed.
- [ ] Build the mirror web-console settings panel on Cowlsly.com so users on web can access the same P1–P5 controls.
- [ ] Cowlsly.CC (Chats) inherits console UI preferences from the sync JSON; no separate settings screen.
- [ ] Cowlsly.Net (advertising/sales) reads suite identity for personalisation only — never writes back to Simple Settings.
- [ ] Add sync status indicator in P12 (About & sync panel).

---

## Phase 7 — Cowlsly Authenticator integration *(future)*

Goal: Simple Settings becomes the identity layer for the full Cowlsly authentication stack.

- [ ] Expose stable identity fields from P5 (username, email, avatar) to the Authenticator via `ContentProvider`.
- [ ] Receive authentication events from the Authenticator to update the lock state in Simple Settings.
- [ ] Add MFA method preferences to P3 (Security) once the Authenticator is ready.
- [ ] Keep Simple Settings as the single place where users enrol or revoke biometric credentials.

---

## Phase 8 — Polish and platform expansion

Goal: best-in-class UX and reach beyond the first Android device.

- [ ] Animated panel transitions using the cog-machine motif; respect `prefers-reduced-motion` / `ANIMATOR_DURATION_SCALE`.
- [ ] Per-panel onboarding tips (dismissable, never repetitive).
- [ ] Tablet and foldable layout: two-column panel grid where width allows.
- [ ] Android Auto / TV stubs: volume and tint controls surfaced where appropriate.
- [ ] Localisation infrastructure: extract all strings to `res/values/strings.xml`, add RTL layout mirroring.
- [ ] Dark / light theme toggle wired to Android system theme, with cyberpunk-neon colour adaptations.

---

## Source-of-truth documents

| Document | Purpose |
|----------|---------|
| `PATH.of.TRUTH.md` | Repository authority order and safety rules |
| `README.md` | Quick-start and current status |
| `ROADMAP.md` | This file — active phase plan |
| `TODO.md` | Current work queue (tactical) |
| `UI_ASSET_SOURCE.md` | Canonical asset consumer policy and sync instructions |
| `ui_asset_dependencies.json` | Pinned canonical paths and generated Android destinations |
| `dev/dat/doc/SIMPLE_SETTINGS_VISION.md` | Master design vision — P0–P13 panel definitions |
| `dev/dat/doc/ROADMAP.ORIGINAL.md` | Founding context and rebuild notes |

Historical local asset catalogues remain migration records and do not override the canonical UI repository.

---

## Related Cowlsly repositories

| Repo | Status | Notes |
|------|--------|-------|
| `Cowlsly/cowlsly-ui-assets-and-data-files` | Active | Canonical source for all reusable UI assets |
| `Cowlsly/console-chat.app-cowlsly.cc` | Active | Cowlsly Chats web platform (Cowlsly.CC) |
| `Cowlsly/cowlsly.com` | Planned | Main web property and web console |
| `Cowlsly/cowlsly.net` | Planned | Advertising and sales web property |

Each web property will have its own `ROADMAP.md` aligned with the sync contract defined in Phase 6 above.