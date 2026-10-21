# Commit Message Instructions

Generate commit messages following the [Conventional Commits 1.0.0](https://www.conventionalcommits.org/en/v1.0.0/) specification. Always write in English.

## Format

```
<type>(<optional scope>): <description>

<optional body>

<optional footer(s)>
```

## Types

| Type       | When to use                                                        |
| ---------- | ------------------------------------------------------------------ |
| `feat`     | A new feature for the user                                         |
| `fix`      | A bug fix                                                          |
| `docs`     | Documentation only (README, comments, guides)                      |
| `style`    | Formatting, whitespace, missing semicolons; no logic change        |
| `refactor` | Code change that neither fixes a bug nor adds a feature            |
| `perf`     | Performance improvement                                            |
| `test`     | Adding or fixing tests                                             |
| `build`    | Build system or dependencies (Gradle, Docker images, versions)     |
| `ci`       | CI configuration (GitHub Actions workflows)                        |
| `chore`    | Maintenance that doesn't touch source or tests (gitignore, config) |
| `revert`   | Reverts a previous commit                                          |

## Scopes

Use the area of the repository affected. Omit the scope if the change spans several areas.

- `backend` – Java backend service
- `android` – Android app
- `docker` – docker-compose and container setup
- `ci` – GitHub workflows
- `deps` – dependency updates
- `adr` – architecture decision records in `docs/adr/`
- `repo` – repository-level files (gitignore, license, templates, editor settings)

## Rules

1. **Description**
   - Imperative, present tense: "add", not "added" or "adds".
   - Lowercase first letter, no trailing period.
   - Max 72 characters for the whole header line.
   - Say *what* changed, concisely.
2. **Body** (optional)
   - Separate from the header with one blank line.
   - Explain *why* the change was made and any relevant context, not *how*.
   - Wrap lines at 72 characters. Bullet points (`-`) are allowed.
3. **Footer** (optional)
   - Breaking changes: start with `BREAKING CHANGE: ` followed by a description.
4. **Breaking changes** may also be flagged with `!` after the type/scope: `feat(backend)!: ...`.
5. One logical change per commit. If the diff mixes unrelated changes, describe the dominant one.

## Examples

```
feat(backend): add endpoint to list user achievements
```

```
fix(android): prevent crash when achievement list is empty
```

```
build(docker): add postgres service to docker-compose
```

```
chore(repo): ignore Gradle wrapper files except the jar
```

```
feat(backend)!: rename achievement "points" field to "score"

Aligns the API naming with the mobile app terminology.

BREAKING CHANGE: clients must read `score` instead of `points`.
```
