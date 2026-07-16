# Repository Guidelines

## Project Structure & Architecture

This Maven project contains 10 Ozon Favorites UI tests. `src/main/java/{core,elements,pages}` holds base abstractions, controls, and Page Objects. `src/test/java/core` contains setup and authentication; `src/test/java/tests` contains JUnit scenarios. `checklist.pdf` defines cases and test data.

## Mandatory UI-Test Design Rules

- Preserve `tests -> pages -> elements`. Tests use page APIs only, never Selenide elements, locators, or element classes.
- Model reusable controls as `BaseElement` subclasses. Similar controls share a parent; unrelated controls may share behavior through an interface.
- Expose only user actions or observable state, such as `press()`, `fill()`, and `isActive()`. Add locator factories only when needed.
- All Page Objects extend `BasePage`. Keep page-specific selectors as private constants in that page; shared selectors belong to their component, such as `elements/Header.java`. Prefer semantic attributes such as `data-widget`.
- Navigate only through user clicks after `AuthService` opens the home page. Never open a feature page by URL or extract a link's `href`.
- Use Selenide conditions for dynamic waits. `sleep`, polling loops, and fixed delays are prohibited.
- Actions that open or refresh a page return the resulting Page Object.
- Assertions and expected-value comparisons belong only in tests. Pages and elements perform actions or return state.
- Keep browser lifecycle in `BaseTest`; extract shared workflows such as authentication into services.
- Give each test one independent functional check. Never rely on execution order.
- Reset Favorites and Cart through `AccountStateService` before and after every UI test. Create fixtures through UI/Page Objects and record product names read from the page.
- Keep JUnit execution sequential because all tests share one authenticated profile and account. Never add `@Order` or make one test prepare another.

## Build and Test Commands

Use Java 17+ and Google Chrome. Run the checked-in Maven Wrapper so every environment uses Maven 3.9.15.

```bash
./mvnw -DskipTests package
./mvnw test
./mvnw -Dtest=FavoritesPageTest test
./mvnw -Dtest=FavoritesProductManagementTest#shouldRemoveProductFromFavorites test
```

These compile without UI execution, run the suite, one class, or one method. Tests need visible Chrome and an authenticated `chrome-profile/`; never share that profile between parallel runs.

## Style and Testing Conventions

Use UTF-8 and four-space indentation. Follow `PascalCase` for classes, `camelCase` for members, and `UPPER_SNAKE_CASE` for constants. Name pages `*Page`, element fields by role (`searchInput`), and tests `*Test`. Extend `BaseTest`; use `@Test`, a checklist-numbered Russian `@DisplayName`, and descriptive AssertJ `.as(...)` messages. No formatter, linter, or coverage threshold is configured.

Tests use a dedicated account and may completely clear Favorites and Cart. They prepare and clean their own data; only authentication in `chrome-profile/` is manual.

## Commits, Pull Requests, and Security

Use focused `feat:` or `test:` commits. Pull requests list checklist cases, prerequisites, and verification commands; attach evidence for UI failures. Never commit `chrome-profile/`, `.idea/`, `target/`, `build/`, logs, or `.DS_Store`.
