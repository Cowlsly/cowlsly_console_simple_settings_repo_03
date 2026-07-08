# Simple Settings — What Needs to Be Done

**Repo:** `cowlsly_console_simple_settings_repo_03`  
**Date:** 2026-07-08  
**Status:** Planning / quick-win build — assets and docs are in place; **no application code exists yet**

---

## Executive summary

Cowlsly Simple Settings is meant to be a **single, shared settings module** that every Cowlsly Console app can point to or embed, instead of each app rebuilding its own settings screen.

Phase 1 is deliberately small: three features only (volume, CASMEA info entry, developer shortcut). The visual identity, icon set, and roadmap are ready. **The remaining work is almost entirely implementation** — scaffolding the app/module, building the three screens, wiring storage and permissions, and integrating with CASMEA and future suite apps.

---

## What is already done

| Area | Status | Notes |
|------|--------|-------|
| Roadmap & scope | Done | `ROADMAP.md` defines Phase 1 A/B/C and out-of-scope rules |
| Founding context | Done | `dev/dat/doc/ROADMAP.ORIGINAL.md` |
| Phase 1 SVG assets | Done | 6 icons + 5 volume-step badges in `assets/images/` |
| Asset catalog | Done | `assets/ASSET_MANIFEST.md` with labels and phase mapping |
| Branding kit | Done | Logo, cogs background, `Cowlsly/BRANDING.md` |
| Documentation | Done | README, GitBook structure under `Cowlsly/` |
| Application code | **Not started** | No Kotlin/Java, Gradle, or UI implementation in this repo |

---

## Phase 1 features to build

### A) Volume control

**Requirement:** Mute toggle plus stepped levels at **0% / 25% / 50% / 75% / 90%** only. No 100% option. Show a hearing-damage warning when headphones/earbuds are in use.

**Implementation tasks:**

- [ ] Create volume settings screen with cyberpunk glass-panel UI over the cogs background
- [ ] Wire mute control using `simple_settings_volume_mute_icon_transparent.svg`
- [ ] Implement stepped level selector using the five `volume_step_*_percent` badges
- [ ] Enforce the 90% ceiling in logic (no 100% path in UI or API)
- [ ] Show hearing-damage warning (`simple_settings_hearing_warning_icon_transparent.svg`) when appropriate (headphone/earbud detection or user acknowledgment flow — confirm platform behavior with Vault/CASMEA repos)
- [ ] Persist volume preference and expose it to consuming apps (shared prefs, content provider, or suite SDK — **decision needed**)
- [ ] Apply volume changes to system/media audio on the target platform

**Assets ready:** volume steps icon, mute icon, warning icon, five step badges (see `assets/ASSET_MANIFEST.md`).

---

### B) CASMEA information entry screen

**Requirement:** This repo is the **only** place personal/medical information is entered. The CASMEA emergency app (`cowlsly_console_medical_emergency_assistant_casmea_repo_02`) reads data from here but has **no data-entry screen** — by design, for security.

**Implementation tasks:**

- [ ] Design and build the info-entry form (fields TBD — align with CASMEA repo schema)
- [ ] Use `simple_settings_casmea_info_entry_icon_transparent.svg` for navigation/entry point
- [ ] Define data model for personal/medical fields (name, conditions, medications, emergency contacts, etc. — **must match CASMEA consumer expectations**)
- [ ] Implement secure local storage (encrypted at rest; scoped credentials per suite rules)
- [ ] Expose read API for CASMEA (content provider, shared module, or documented contract — **coordinate with repo 02**)
- [ ] Add validation, save/cancel flows, and clear user feedback
- [ ] Document the data contract so CASMEA integration is unambiguous

**Dependency:** `cowlsly_console_medical_emergency_assistant_casmea_repo_02` is a Phase 1 consumer; field names and access patterns must be agreed before shipping.

---

### C) Developer options shortcut

**Requirement:** A fast-access button to developer tools, **visible only after developer access has been explicitly granted** elsewhere in the suite.

**Implementation tasks:**

- [ ] Implement developer-access gate (read granted/denied state from suite auth or local flag — **align with Authenticator repo 04 when available**)
- [ ] Show shortcut button only when access is granted; hide completely otherwise
- [ ] Use `simple_settings_developer_shortcut_icon_transparent.svg`
- [ ] Deep-link or launch target developer tools screen (define destination — Vault repo, debug activity, or suite dev menu)
- [ ] Ensure shortcut cannot be enabled by tampering with local UI alone (trust the upstream grant mechanism)

**Note:** `cowlsly_console_authenticator_repo_04` is listed as a later consumer; interim grant mechanism may be needed for Phase 1.

---

## Platform & project scaffolding (not started)

Before feature work, the repo needs a real application skeleton. Based on Cowlsly suite conventions (Android, `CogVideoBackground` in Vault repo):

- [ ] **Confirm target platform** with suite owners (Android is implied by branding docs; verify before coding)
- [ ] Initialize project structure (Gradle, package naming, module layout)
- [ ] Import Phase 1 assets from `assets/images/` into app resources
- [ ] Implement shared Cowlsly UI shell:
  - Forever-turning cogs background (`cogs_background.mp4` loop via `CogVideoBackground`, or animated SVG fallback)
  - Dark scrim (40–60% opacity) over background
  - Neon glass panels, gold buttons, cyan accents per `Cowlsly/BRANDING.md` palette
- [ ] Set launcher/module icon to `simple_settings_app_icon_transparent.svg`
- [ ] Define how other apps **embed or launch** Simple Settings (standalone APK, library module, intent URI, or suite plugin — **architecture decision required**)
- [ ] Add basic navigation between the three Phase 1 areas
- [ ] CI/build pipeline and minimal smoke tests

---

## Integration & downstream consumers

| Consumer repo | Relationship | Action needed |
|---------------|--------------|---------------|
| `cowlsly_console_medical_emergency_assistant_casmea_repo_02` | Reads CASMEA data entered here | Agree schema + read contract; integration test |
| `cowlsly_console_authenticator_repo_04` | Later; may govern developer-access grant | Define grant API when repo 04 exists; interim stub if needed |
| Other Cowlsly Console apps | Future settings consumers | Keep module generic; add settings only when other apps need them |

---

## Explicitly out of scope (Phase 1)

Do **not** build these in the first release:

- Any settings beyond volume, CASMEA entry, and developer shortcut
- Data entry inside the CASMEA app itself
- 100% volume level
- Full Cowlsly Production layered logo (small embed kit only; full sigil lives in Vault repo)

Record new ideas in the roadmap; do not expand scope mid-build (`ROADMAP.ORIGINAL.md` rule).

---

## Suggested build order

1. **Scaffold** — project init, branding shell, navigation skeleton  
2. **A — Volume** — self-contained, no cross-repo contract; good first quick win  
3. **B — CASMEA entry** — coordinate with repo 02 on schema and read path  
4. **C — Developer shortcut** — depends on grant mechanism  
5. **Integration** — end-to-end test with CASMEA consumer  
6. **Docs** — update `ROADMAP.md` status from Planning → Building → Phase 1 complete  

---

## Open decisions (blockers or near-blockers)

| # | Question | Who / where to decide |
|---|----------|----------------------|
| 1 | Android app vs. shared library module vs. both? | Suite architecture |
| 2 | CASMEA field schema and read API shape | Coordinate with `casmea_repo_02` |
| 3 | How is developer access granted before Authenticator repo 04 exists? | Suite auth / interim flag |
| 4 | Volume: system-wide vs. app-suite-only? Headphone warning trigger? | Platform + UX |
| 5 | Where do developer tools deep-link to? | Vault or suite dev menu |
| 6 | Copy `cogs_background.mp4` from Vault for runtime, or SVG-only for Phase 1? | Vault repo |

---

## Success criteria for Phase 1

Phase 1 is complete when:

1. A user can set volume (mute + steps through 90%) with hearing warning where required  
2. A user can enter and update personal/medical info that CASMEA can read without its own entry screen  
3. Developer shortcut appears only when access is granted and opens the agreed dev destination  
4. UI matches Cowlsly cyberpunk branding (cogs background, palette, glass panels)  
5. CASMEA repo 02 can consume the data contract in a tested integration  
6. `ROADMAP.md` is updated to reflect shipped status  

---

## Related documents

- `ROADMAP.md` — active scope and asset log  
- `dev/dat/doc/ROADMAP.ORIGINAL.md` — rebuild context and AI assistant rules  
- `assets/ASSET_MANIFEST.md` — asset-to-feature mapping  
- `Cowlsly/BRANDING.md` — UI palette and background rules  
- `assets/README.md` — asset naming and folder layout