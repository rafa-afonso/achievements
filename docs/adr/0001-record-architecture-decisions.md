# Record architecture decisions using MADR

## Context and Problem Statement

Architecture decisions made on this project (backend, Android app, infrastructure) need to be recorded so that future contributors understand why things are the way they are. How should we document these decisions?

## Considered Options

* MADR (Markdown Architectural Decision Records), minimal template
* No formal records (decisions live in PRs and chat)

## Decision Outcome

Chosen option: "MADR (Markdown Architectural Decision Records), minimal template", because it is plain Markdown that lives next to the code, is lightweight enough to actually be written, and is a well-known open standard.

### Consequences

* Good, because decisions are versioned and reviewed together with the code in PRs.
* Good, because the minimal template keeps the cost of writing an ADR low.
* Bad, because ADRs must be kept up to date manually (e.g. superseded decisions).
