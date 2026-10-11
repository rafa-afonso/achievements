# CLAUDE.md

Context for Claude Code when working in this repository.

## What this is

**Achievements App**: a personal app to log achievements (things already done) by category, shown on a month-grouped timeline. It is a **study and portfolio project** for Rafael to learn and practice software development, back end and front end. The repo is public.

- Career focus: **Java back end first**, with a full-stack view. A web client comes later (not in v0).
- Approach: grow the app in small increments, **limited by features, not by technology**. Good engineering practices (CI, tests, static analysis, ADRs) are in from the start.
- Rafael writes in Portuguese; the planning docs are in Portuguese. **App UI text is in English.** Code, commits and identifiers are in English.

## v0 scope (what to build now)

In: create, list (Timeline grouped by month, newest first, pull to refresh, loading/empty/error states), detail, edit and delete achievements. Fields: title (required, 1–120), category (one of 5 fixed), date (defaults to today, not in the future), optional plain-text description (up to 2000, empty becomes `null`).

Out (later increments): tabs and bottom nav, category CRUD, progress and goals, follow-up chains, Look back, search/filters, login, offline, deploy.

Decisions already made:
- Java/Spring backend integrated with the Android app from day one. Backend is the source of truth; the app is online-only in v0.
- Everything runs **locally** in dev: Postgres via `docker compose`, API on `localhost:8080`, emulator reaches it at `http://10.0.2.2:8080`.
- **One fixed user, no auth.** Every achievement has a `user_id`; a Flyway migration seeds the default user and a `CurrentUserProvider` with a fixed implementation resolves it. Endpoints never take a `userId`. Auth (Keycloak + Spring Security) replaces only that provider later.
- Fixed categories seeded by migration: Career `#0E6E61`, Health `#B23C0A`, Learning `#6337C9`, Personal `#A3204F`, Finance `#7A5600`.
- Optimistic locking with `@Version`: PUT sends the `version` it read; the API answers 409 if it changed.
- Cheap quality additions belong in v0: Dockerfile, ADRs, static analysis, coverage, Dependabot, conventional commits, PR template, SonarCloud.

## Stack and layout

Monorepo with two folders, each with its own path-filtered CI workflow:

```
backend/   Java 21, Spring Boot 3, Gradle (Kotlin DSL), Spring Web, Data JPA,
           Bean Validation, Flyway, PostgreSQL, springdoc-openapi, Actuator.
           Tests: JUnit 5, MockMvc, Testcontainers. Spotless + Checkstyle/PMD, JaCoCo.
android/   Kotlin, Jetpack Compose (Material 3), Navigation Compose, MVVM (ViewModel + StateFlow),
           Hilt, Retrofit + OkHttp + kotlinx.serialization, version catalog.
           Tests: JUnit, Turbine, MockWebServer, Compose UI test. Spotless (ktlint), Detekt, Kover.
docs/      Planning and design notes (see below); ADRs go in docs/adr/.
```

Backend conventions: `controller` → `service` → `repository` layers, DTOs separate from entities, errors as RFC 9457 Problem Details (`ProblemDetail`): 400 with invalid fields, 404 for missing or another user's achievement, 409 on version conflict.

## API contract v0

| Method | Path | Notes |
|---|---|---|
| GET | `/api/v1/categories` | id, name, color |
| GET | `/api/v1/achievements` | current user's, ordered by date desc |
| GET | `/api/v1/achievements/{id}` | detail |
| POST | `/api/v1/achievements` | `{title, description, categoryId, achievedOn}` → 201 + `Location` |
| PUT | `/api/v1/achievements/{id}` | same fields + `version` → 409 on conflict |
| DELETE | `/api/v1/achievements/{id}` | 204 |

Model: `app_user(id, display_name, created_at)`, `category(id, name, color)`, `achievement(id, user_id, category_id, title, description, achieved_on, version, created_at, updated_at)`, index on `(user_id, achieved_on)`.

## Work tracking

Stories are GitHub Issues **#1–#10** (H0–H9) on `rafa-afonso/achievements`, on a GitHub Project board. Suggested order: H0 → (H1 → H2 → H3) in parallel with (H4 → H5) → H6 → H7 → H8 → H9, then tag `v0.1.0`. Read the issue before starting a story; its checklist is the definition of done.

## Reference docs

- [docs/planning/v0-plano.md](docs/planning/v0-plano.md): full v0 plan (rev 4, approved), stories, post-v0 roadmap. Portuguese.
- [docs/planning/brainstorm-aprendizado.md](docs/planning/brainstorm-aprendizado.md): learning goals and why the roadmap is ordered as it is. Portuguese.
- [docs/design/resumo-design.md](docs/design/resumo-design.md): written summary of the v1 design (screens, data model, colors, fonts Space Grotesk + DM Sans). Portuguese.
- Design artifacts on claude.ai (private links, Claude Code cannot open them; ask Rafael for screenshots if needed):
  - v0 design, the reference for implementation: https://claude.ai/artifact/7BxWyghkZ7txRR4TPHbsFo
  - v1 design, long-term vision (8 screens): https://claude.ai/artifact/QtF4UMs6MRNT8vLR6rvibB

These docs are copies of the planning files kept in the claude.ai project; if they disagree with an issue, the issue wins.

## Gotchas

- The root `.gitignore` ignores `*.jar` and `.vscode/`. Add `!gradle/wrapper/gradle-wrapper.jar` (or equivalent) when the Gradle wrapper is added, or the wrapper won't be committed.
