
# Repository Guidelines

## Project Structure & Module Organization

This repository is a documentation-first requirements package. All deliverables live under `docs/requirement/`.

- `docs/requirement/requirement.html`: entry page and index for the full requirement set
- `docs/requirement/user-story.html`: user stories, actors, and NFR overview
- `docs/requirement/traceability.html`: cross-reference matrix for requirements, BRs, and NFRs
- `docs/requirement/uc*.html`: individual use case specifications, for example `uc01用户注册.html`
- `CLAUDE.md`: repository-specific working notes and constraints

Keep new files in `docs/requirement/`. Do not introduce subdirectories unless the document set is explicitly reorganized.

## Build, Test, and Development Commands

There is no app build pipeline in this repository. Work is reviewed directly in the generated HTML files.

- `open docs/requirement/requirement.html`: open the main index locally
- `rg --files docs/requirement`: list the current document set
- `git diff -- docs/requirement`: review changes before commit

When editing HTML, verify navigation links, table layouts, and section anchors in a browser.

## Coding Style & Naming Conventions

Use UTF-8 HTML files with inline CSS/JS, matching the existing documents. Follow the current compact style:

- 2-space indentation in HTML, CSS, and inline JavaScript
- Descriptive Chinese filenames for use cases: `ucNN用例名称.html`
- Stable anchors and section IDs such as `#s1`, `#s12`, `#nfr-list`
- Business-facing wording in requirement content; avoid implementation detail unless the document explicitly calls for it

Preserve existing visual patterns, badges, table structure, and sidebar navigation.

## Testing Guidelines

Testing is manual and document-oriented.

- Open `requirement.html`, `traceability.html`, and any changed `uc*.html`
- Check broken links, anchor jumps, sortable tables, and PlantUML rendering fallbacks
- Confirm traceability consistency across `user-story.html`, `traceability.html`, and affected use cases

## Commit & Pull Request Guidelines

Recent history uses conventional prefixes such as `fix:`, `feat:`, `refactor:`, and scoped docs commits like `docs(uc03):`.

- Prefer messages like `docs(uc25): 去技术化备注与异常处理`
- Keep each commit focused on one document set or one cross-cutting cleanup
- In PRs, summarize changed files, rationale, affected use cases, and any manual verification performed
- Include screenshots only when layout, navigation, or table behavior changed

## Agent-Specific Notes

Treat `traceability.html` as a high-sensitivity artifact: update BR/NFR mappings whenever a use case changes. Keep requirement documents aligned with the current soft-specifier rules and avoid drifting back into architecture-level detail.

## Architectural Direction (Future)

The planned frontend is **AI Agent-driven** rather than a traditional Vue.js SPA.

- Admin operations will be expressed as natural-language intents; the Agent translates them into API calls against the JavaEE backend.
- Use case descriptions already accommodate this: steps like "管理员访问创建角色页面" are interface-agnostic and map equally to a form page or an Agent conversation turn.
- The JavaEE REST API surface is the stable contract; the frontend technology choice does not require changes to any use case.
- Batch operations (e.g., Q-12-01 batch delete, deferred to a later iteration) become trivially expressible in natural language, which is one motivation for this direction.
- This decision should be captured in the architecture baseline (`arch-vYYYYMMDD`) when the soft-architect phase begins.
