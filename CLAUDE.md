# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Install dependencies (skip tests)
mvn clean install -DskipTests

# Run all tests (API + UI in parallel)
mvn test

# Run only API tests
mvn test -DsuiteXmlFile=testng-api.xml

# Run only UI tests
mvn test -DsuiteXmlFile=testng-ui.xml

# Run a single test class
mvn test -Dtest=PostsApiTest
mvn test -Dtest=WebTablesCrudTest
mvn test -Dtest=PracticeFormSubmissionTest
mvn test -Dtest=ButtonsClickTest

# Run a single test method
mvn test -Dtest=WebTablesCrudTest#testAddNewRecord
mvn test -Dtest=PostsApiTest#testGetPostByIdMatchesFixture
```

## Architecture

This is a Maven-based test automation project using TestNG as the test framework. API tests target the public JSONPlaceholder mock API (`https://jsonplaceholder.typicode.com`); UI tests target the static QA Helpers site at `https://adrianjiga.github.io/qa/helpers`.

### Test Structure

- **API Tests** (`src/test/java/com/example/tests/api/`)
  - Extend `BaseApiTest` which configures REST Assured with base URI `https://jsonplaceholder.typicode.com`, `Accept: application/json`, and `Content-Type: application/json`
  - JSON body assertions via Hamcrest matchers and JSON Schema (Draft-07) validation via `matchesJsonSchemaInClasspath`
  - Schemas live in `src/test/resources/schemas/`; the fixture for the get-by-id test lives in `src/test/resources/fixtures/post.json`

- **UI Tests** (`src/test/java/com/example/tests/ui/`)
  - Extend `BaseUITest` which handles WebDriver lifecycle (setup/teardown per method)
  - Chrome runs in headless mode by default (`--headless=new`)
  - WebDriverManager handles ChromeDriver binary management automatically
  - Base URL: `https://adrianjiga.github.io/qa/helpers`
  - Chrome is hardcoded — `setUp()` constructs `ChromeDriver` directly with no browser
    switch. Cross-browser support needs a driver factory reading a `-Dbrowser=` property

- **Configuration** — `ConfigManager` reads `System.getProperty(key, props.getProperty(key))`,
  so any key in `config.properties` is overridable at the command line without editing
  source: `mvn test -Dheadless=false`, `mvn test -Dbase.url=http://localhost:3000/qa/helpers`.
  Note `BaseUITest.BASE_URL` is `static final`, so it freezes at class load — fine for a
  per-run override, not for switching environments mid-JVM

- **Reporting** — Allure. Page objects carry `@Step` annotations; `ScreenshotListener`
  (an `ITestListener` registered via `@Listeners` on `BaseUITest`) attaches a PNG to the
  Allure result on UI failure, guarding with `result.getInstance() instanceof BaseUITest` so
  API failures fall through. `@Step` capture depends on the AspectJ weaver wired into
  Surefire's `argLine` in `pom.xml` — drop that and the report silently goes blank

### Test Files

**API** (`src/test/java/com/example/tests/api/`):

| Class | Endpoint | Tests |
|---|---|---|
| `PostsApiTest` | `/posts`, `/posts/{id}` (GET, POST, PUT, PATCH, DELETE, `?userId=` filter) | 8 |
| `CommentsApiTest` | `/comments?postId=` | 1 |
| `UsersApiTest` | `/users`, `/users/{id}` (with nested `address.geo` + `company` schemas) | 2 |
| `TodosApiTest` | `/todos`, `/todos?completed=` | 2 |
| `NestedRoutesApiTest` | `/posts/{id}/comments`, `/users/{id}/posts`, `/albums/{id}/photos` | 3 |
| `QueryFeaturesApiTest` | `_page`, `_limit`, `_sort`, `_order`, `_start`, `_end`, `q=`, `X-Total-Count`, RFC 5988 `Link` header | 5 |

**UI** — split into subpackages under `src/test/java/com/example/tests/ui/`:

| Package | Class | Page | Tests |
|---|---|---|---|
| `ui.buttons` | `ButtonsClickTest` | `/buttons` | 3 |
| `ui.buttons` | `ButtonsVisibilityTest` | `/buttons` | 2 |
| `ui.webtables` | `WebTablesDefaultDataTest` | `/webtables` | 3 |
| `ui.webtables` | `WebTablesSearchTest` | `/webtables` | 4 |
| `ui.webtables` | `WebTablesCrudTest` | `/webtables` | 6 |
| `ui.webtables` | `WebTablesPaginationTest` | `/webtables` | 2 |
| `ui.form` | `PracticeFormSubmissionTest` | `/automation-practice-form` | 3 |
| `ui.form` | `PracticeFormGenderTest` | `/automation-practice-form` | 4 |
| `ui.form` | `PracticeFormHobbiesTest` | `/automation-practice-form` | 3 |
| `ui.form` | `PracticeFormDatePickerTest` | `/automation-practice-form` | 2 |
| `ui.form` | `PracticeFormLocationTest` | `/automation-practice-form` | 2 |

Counts are **test runs**, not `@Test` methods. The only place these differ is
`PracticeFormGenderTest`: 2 methods, but `testGenderRadioSelection` is data-driven via a
`@DataProvider` with 3 rows, so it contributes 4 runs.

Shared base classes (no tests): `ui.webtables.BaseWebTablesTest`, `ui.form.BasePracticeFormTest`. Both extend `BaseUITest` from the parent `ui` package and require `import com.example.tests.ui.BaseUITest;`.

### TestNG Configuration

- `testng.xml` — All tests. `parallel="classes" thread-count="6"`
- `testng-api.xml` — API tests only. No `parallel` attribute, so it runs sequentially
- `testng-ui.xml` — UI tests only. `parallel="classes" thread-count="4"`

All three register `com.example.listeners.RetryListener` in a `<listeners>` block.

`parallel="classes"` is a hard ceiling right now: `BaseUITest` keeps `driver` and `wait` as
instance fields, which is safe per-class but would race under `parallel="methods"`. Switching
to method-level parallelism means introducing a `ThreadLocal<WebDriver>` first.

### Key Conventions

- Failed tests automatically retry up to 2 times. This is **not** Surefire's
  `rerunFailingTestsCount` (that element is not in `pom.xml`). `RetryAnalyzer` implements
  `IRetryAnalyzer`; `RetryListener` implements `IAnnotationTransformer` and attaches it to
  every `@Test` that hasn't declared its own, so tests never opt in individually
- UI tests use explicit waits via `WebDriverWait` (15 second default timeout)
- Page load timeout: 30 seconds
- UI locator preference: `id` first, then `data-cy` CSS attribute selectors (e.g. `[data-cy='submit-btn']`), XPath avoided
- `WebElement.clear()` does not fire the JS `input` event — use `JavascriptExecutor` to dispatch it manually when a JS listener depends on it

### Target Site Notes

- **Buttons page** (`/buttons`): dynamic click button has no `id`, use `[data-cy='dynamic-click-btn']`; message elements are hidden by CSS initially and shown via `style.display='block'` by JS
- **Web Tables page** (`/webtables`): state is stored in `localStorage`; each test gets a clean state because `BaseUITest` creates a fresh `ChromeDriver` per method; the add/edit modal is injected into the DOM dynamically (use `visibilityOfElementLocated`, not just `presenceOfElementLocated`)
- **Practice Form** (`/automation-practice-form`): required fields for submission are `firstName`, `lastName`, `userNumber` (mobile), and `gender`; country/city dropdowns are custom JS components (not native `<select>`) — click `.select-control` to open, then click the option; `getText()` returns `""` on elements inside a `display:none` container, so open dropdowns before asserting on their options
