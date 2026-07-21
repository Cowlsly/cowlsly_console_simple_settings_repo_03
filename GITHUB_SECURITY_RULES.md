# Mandatory GitHub Security Rules

**Applies to every human, AI agent, coding assistant, bot, workflow, and contributor working in this repository.**

**Last reviewed:** 2026-07-21  
**Status:** Mandatory security baseline  
**Rule:** Read this file before changing workflows, dependencies, repository settings, authentication, secrets, releases, or deployment code.

GitHub security behaviour changed materially in July 2026. Do not rely on older assumptions or copy an old workflow without reviewing it against this file and current official GitHub guidance.

## 1. Protected branches and safe work method

1. Discover the repository's current default and protected branch. Never assume it is named `main`; Cowlsly repositories may use `root` or another name.
2. Do not commit generated work directly to a protected/default/production branch unless the repository owner explicitly requests that exact action.
3. Work on a dedicated branch, run checks, push the branch, and open a pull request.
4. Do not merge, deploy, change live infrastructure, rotate credentials, or alter external accounts without explicit owner approval.
5. Before editing, run or inspect the equivalent of:
   - current branch and upstream
   - `git status`
   - pending changes
   - repository instructions, `SECURITY.md`, `CONTRIBUTING.md`, `AGENTS.md`, roadmap, and task files when present
6. Never claim that a commit, push, check, deployment, or security control succeeded unless its result was actually verified.

## 2. GitHub Actions: untrusted code must never gain privilege

### Required trigger rule

- Use `pull_request` for building, installing, linting, testing, type-checking, packaging, or otherwise executing pull-request code.
- Use `pull_request_target` only for trusted, metadata-only work such as labels, reviewers, comments, or triage.
- A `pull_request_target`, privileged `workflow_run`, `issue_comment`, or other privileged workflow must never retrieve and execute untrusted fork or pull-request code.

### July 2026 `actions/checkout` protection

GitHub released `actions/checkout` v7 and backported safer checkout behaviour to other supported versions. It blocks common “pwn request” patterns involving fork code in `pull_request_target` and certain `workflow_run` jobs.

This protection is not complete. It does not make manual retrieval safe. Agents must also reject or carefully redesign workflows that use:

- `github.event.pull_request.head.sha`
- `github.event.pull_request.head.repo.full_name`
- `refs/pull/*/head` or `refs/pull/*/merge`
- `git fetch` of pull-request refs
- `gh pr checkout`
- `curl`, `wget`, package scripts, or custom code that downloads and executes untrusted content
- executable artifacts or caches originating from untrusted pull requests
- unrelated third-party repositories that are checked out and executed in privileged jobs

**Never add `allow-unsafe-pr-checkout: true`.** If a task appears to require it, stop and request a security review instead.

### Action pinning and token permissions

- Use supported action releases.
- Pin third-party actions to a verified full commit SHA where practical and add a comment naming the human-readable release version.
- Never invent a SHA. Verify it against the action's official repository or release.
- If pinning `actions/checkout`, ensure the pinned commit includes the July 2026 protection. A stale exact SHA does not receive a floating-tag backport.
- Set the workflow-level default to:

```yaml
permissions:
  contents: read
```

- Add only the minimum extra permission required at the individual job level.
- Workflows that execute untrusted code must not receive write tokens, repository secrets, production environments, privileged caches, or trusted artifacts.

### Shell-injection rule

Never place attacker-controlled values directly inside a `run:` command. Pull-request titles, branch names, commit messages, issue bodies, labels, usernames, and event payload fields are untrusted input. Pass required values through environment variables, quote them correctly, validate them, and avoid `eval` or dynamically assembled shell commands.

### Cache and artifact rule

Treat caches and artifacts produced by fork or pull-request jobs as untrusted. A privileged workflow must not execute files from them. Do not allow an attacker-controlled cache to be restored into a later privileged job.

## 3. Dependabot supply-chain cooldown

GitHub now applies a default **three-day cooldown** before Dependabot opens ordinary version-update pull requests for newly released packages. Security updates still open immediately.

- Do not disable or reduce this cooldown merely to receive releases faster.
- Consider a longer cooldown for high-risk or sensitive dependencies.
- A green Dependabot pull request is not automatic approval.
- Review publisher identity, ownership changes, release age, changelog, install scripts, dependency tree, lockfile changes, unexpected binaries, and newly introduced network activity before merging.

## 4. Secret scanning and credential handling

GitHub expanded secret scanning in July 2026, including new APIclub and Resend detectors, Resend public-leak reporting, VolcEngine Ark push protection, clearer `default` versus `generic` secret categories, and richer validity/ownership metadata for supported credentials.

These improvements do not replace safe handling:

- Enable secret scanning and push protection where available.
- Never commit, paste, log, screenshot, upload, or place in issues/PRs: passwords, API keys, access tokens, cookies, private keys, recovery codes, wallet seeds, signing files, connection strings, production endpoints containing credentials, or personal data.
- Use repository, Codespaces, Actions, or Environment secret stores as appropriate.
- Never expose secrets to untrusted pull-request code.
- Deleting a leaked secret from the newest file is not remediation. Revoke and rotate it, inspect history and logs, review account sessions and token use, then remove it from history when appropriate.
- If exposure is suspected, stop work and report it clearly. Do not continue using the credential.

## 5. GitHub Actions OIDC and cloud trust

For repositories created, renamed, or transferred after 2026-07-15, GitHub Actions OIDC subject claims use immutable owner and repository identifiers by default. Existing repositories are not automatically migrated.

- Do not assume a name-based cloud trust policy is safe after a repository or owner rename/transfer.
- Before opting an existing repository into immutable claims, preview the new subject, update the cloud provider trust policy, test safely, and only then remove the old trust rule.
- Do not change AWS, Azure, Google Cloud, or other live IAM/OIDC trust without explicit owner approval and a rollback plan.

## 6. Repository and environment settings checklist

Where the GitHub plan supports them, configure:

- protected default/production branch
- pull requests required before merge
- required successful checks
- blocked force pushes and branch deletion
- read-only default `GITHUB_TOKEN` permissions
- approval for workflows from forks
- secret scanning and push protection
- code scanning on pull requests, including AI-powered detections when available
- separate staging and production environments
- manual approval for production
- no production deployment from untrusted pull-request jobs
- tightly restricted `workflow_dispatch`, `repository_dispatch`, and privileged event workflows

Agents must document settings that require manual configuration rather than pretending repository files enforce them.

## 7. Mandatory pre-commit and pre-PR audit

Before committing or opening/updating a pull request:

1. Inspect every changed workflow and script.
2. Search for `pull_request_target`, `workflow_run`, `issue_comment`, `repository_dispatch`, `allow-unsafe-pr-checkout`, pull-request refs, manual `git`/`gh` retrieval, `curl`, `wget`, broad `permissions`, secrets, and executable artifacts.
3. Validate YAML and configuration syntax.
4. Run the repository's lint, type-check, tests, and build where available.
5. Inspect the complete diff and generated lockfiles.
6. Confirm no real secret, private data, deployment, or live-resource change was introduced.
7. Push only the intended working branch.
8. Open or update a pull request and report checks, risks, unresolved questions, and exact files changed.
9. Stop before merge unless explicit approval was given.

## 8. Stop conditions

Stop and ask for review when:

- instructions conflict
- a privileged workflow appears to require untrusted code
- an action SHA or release cannot be verified
- a secret may have been exposed
- cloud trust or production access would change
- a dependency or repository has suspicious provenance
- a security control would need to be bypassed
- the current branch, target branch, or intended deployment is uncertain

Security uncertainty is not permission to guess.

## Official GitHub sources

- Safer `pull_request_target` checkout defaults: https://github.blog/changelog/2026-06-18-safer-pull_request_target-defaults-for-github-actions-checkout/
- Dependabot default package cooldown: https://github.blog/changelog/2026-07-14-dependabot-version-updates-introduce-default-package-cooldown/
- Secret scanning and public-monitoring improvements: https://github.blog/changelog/2026-07-15-improvements-to-secret-scanning-and-public-monitoring/
- Secret-scanning extended metadata and multipart validation: https://github.blog/changelog/2026-07-07-secret-scanning-extended-metadata-and-multipart-validation/
- Immutable GitHub Actions OIDC subject claims: https://github.blog/changelog/2026-04-23-immutable-subject-claims-for-github-actions-oidc-tokens/
- AI security detections in pull requests: https://github.blog/changelog/2026-07-14-code-scanning-shows-ai-security-detections-on-pull-requests/

When official guidance changes, update this file and apply the safer rule before modifying security-sensitive automation.