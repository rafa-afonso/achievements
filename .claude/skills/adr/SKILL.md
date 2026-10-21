---
name: adr
description: Record an architecture decision as a MADR (minimal) file in docs/adr/. Use when the user asks to record, document or write an ADR / architecture decision, or when a significant technical choice (framework, database, library, protocol, infrastructure, project structure) has just been made and should be documented.
---

# Record an Architecture Decision (MADR minimal)

ADRs live in `docs/adr/`. The template is `docs/adr/adr-template.md` and the conventions are in `docs/adr/README.md`.

## Steps

1. **Gather the decision.** From the conversation (or by asking the user) collect:
   - the problem / context that forced a decision,
   - the options that were considered (at least two when possible),
   - the chosen option and why,
   - good and bad consequences.
   If the chosen option or its justification is unknown, ask before writing. Do not invent rationale.
2. **Pick the number.** List `docs/adr/` and take the highest `NNNN-` prefix + 1, zero-padded to 4 digits.
3. **Name the file** `docs/adr/NNNN-<short-title-in-kebab-case>.md`. The title should state the solution to the problem, e.g. `0002-use-postgresql-as-database.md`.
4. **Write the ADR** by filling in `docs/adr/adr-template.md`:
   - Replace every `{...}` placeholder; remove the template's HTML comments.
   - The `#` title matches the file name in sentence case.
   - Keep "Context and Problem Statement" to 2–3 sentences; phrasing it as a question is encouraged.
   - "Decision Outcome" must start with `Chosen option: "<option title>", because <justification>.` and the option title must match one in "Considered Options" exactly.
   - "Consequences" is optional; drop the section if there is nothing meaningful to say.
   - Write in English.
5. **Supersede if needed.** If the new decision replaces an earlier ADR, add `Superseded by [ADR-NNNN](NNNN-....md)` as the first line under the old ADR's title, and mention the old ADR in the new one's context. Never otherwise rewrite an accepted ADR.
6. **Update the index** in `docs/adr/README.md` with `* [ADR-NNNN](NNNN-....md) – <Title>`.
7. Suggest a commit message such as `docs(adr): record decision to <title>`.
