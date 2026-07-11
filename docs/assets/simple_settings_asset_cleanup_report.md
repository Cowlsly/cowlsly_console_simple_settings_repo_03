# Simple Settings Asset Cleanup Report

Date: 2026-07-11
Branch: `maintenance/simple-settings-asset-cleanup`
Visibility: Public
Status: Non-destructive audit and manifest foundation

## Confirmed findings

- The repository is a compact Android application with a small reusable asset collection.
- Reusable masters include settings icons, volume-step badges, an animated cogs background, and small Cowlsly logo exports.
- The small PNG logo appears in three locations: GitBook documentation, project branding assets, and Android runtime resources. SHA-256 comparison is required before classifying these as exact duplicates.
- No APK, AAB, Gradle build directory, or other build output was identified in the current tree map.

## Public-repository security review

- No secret values are printed or copied by this report.
- The application contains CASMEA and account-related code, so committed examples and defaults require careful inspection for personal information even though no private data file was identified in the path map.
- A generated value-redacted scan is required before this branch can claim the public tree is free of credentials or private records.

## Actions completed

- Created a maintenance branch.
- Added a consumer manifest.
- Recorded reusable master candidates and the three-path logo duplicate candidate.
- Preserved all current runtime and documentation assets.
- Used no Codespace.

## Pending

- Exact hashes and references.
- Public-tree secret and privacy scan.
- Canonical migration of reusable masters.
- Android build, lint, tests, and APK size after any later cleanup.

## Rollback

Close the maintenance PR or delete the maintenance branch. The public `main` branch has not been modified.
