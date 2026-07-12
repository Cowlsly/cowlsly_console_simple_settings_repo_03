# TODO — Cowlsly Simple Settings

## Completed canonical asset work

- [x] Import the complete reusable Simple Settings asset set into the canonical UI repository.
- [x] Verify byte identity and SHA-256 for the PNG logo, SVG logo/background, eight icons, and five volume-step controls.
- [x] Pin verified immutable release `acf7ffaab736283daccd286d01abf60a8f2a80a2` and final canonical paths in `ui_asset_dependencies.json`.
- [x] Convert local asset paths to declared generated consumer copies.
- [x] Add main-only Java 21 build, tests, lint, and APK artifact CI.
- [x] Merge the known asset-audit branch into active `main` history.

## Remaining asset validation

- [ ] Run deterministic canonical sync and write `.cowlsly_ui_generated.json`.
- [ ] Synchronise vault-transition resources into valid `res/drawable-nodpi` and `res/raw` paths when the transition is enabled in the app.
- [ ] Confirm the newest Java 21 CI run passes after the final dependency and README updates.
- [ ] Repair Compose/XML references only where generated paths differ.
- [ ] Remove obsolete local masters only after sync receipt, reference checks, and Java 21 validation pass.

## Application validation

- [ ] Verify system-intent handling, unavailable-setting fallbacks, search, usage ranking, and access tiers on current Android.
- [ ] Add or run instrumentation tests where emulator/device behavior is required.
- [ ] Verify accessibility, reduced motion, hearing warnings, and safe volume behavior.
- [ ] Confirm no API keys or private medical data are exported through UI assets or suite-sync files.

## Product work

- [ ] Review every system-setting shortcut against current Android behavior and permissions.
- [ ] Complete the security/privacy hub without duplicating Android private-settings storage.
- [ ] Keep developer options explicitly gated and session-limited.
- [ ] Version the suite settings contract and add migration tests.

## Repository administration

- [x] Keep active development on `main`.
- [x] Convert README and asset docs to the canonical UI consumer model.
- [x] Retire the every-repository-carries-branding policy.
- [x] Close the known maintenance PR after integrating its work.
- [ ] Let the owner rename or remove the temporary `root` mirror and update the displayed default branch when convenient.
