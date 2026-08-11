# Selenium + REST Assured Example

A demo project showcasing Selenium WebDriver and REST Assured testing capabilities with CI/CD integration.

**Target site:** [adrianjiga.github.io/qa/helpers](https://adrianjiga.github.io/qa/helpers)

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Chrome browser (for UI tests)

## Getting Started

```bash
# Clone the repository
git clone git@github.com:adrianjiga/SeleniumAutomationExample.git
cd SeleniumAutomationExample

# Install dependencies
mvn clean install -DskipTests
```

## Running Tests

```bash
# Run all tests (classes in parallel, 6 threads)
mvn test

# Run only API tests
mvn test -DsuiteXmlFile=testng-api.xml

# Run only UI tests
mvn test -DsuiteXmlFile=testng-ui.xml

# Run a single test class
mvn test -Dtest=WebTablesCrudTest
mvn test -Dtest=PracticeFormSubmissionTest

# Run a single test method
mvn test -Dtest=WebTablesCrudTest#testAddNewRecord
```

## Project Structure

```
SeleniumAutomationExample/
├── .github/
│   ├── dependabot.yml
│   └── workflows/
│       ├── run-ci.yml
│       └── run-tests.yml
├── src/test/
│   ├── java/com/example/
│   │   ├── config/
│   │   │   └── ConfigManager.java
│   │   ├── listeners/
│   │   │   ├── RetryAnalyzer.java
│   │   │   ├── RetryListener.java
│   │   │   └── ScreenshotListener.java
│   │   ├── pages/
│   │   │   ├── ButtonsPage.java
│   │   │   ├── PracticeFormPage.java
│   │   │   └── WebTablesPage.java
│   │   └── tests/
│   │       ├── api/
│   │       │   ├── BaseApiTest.java
│   │       │   ├── PostsApiTest.java
│   │       │   ├── CommentsApiTest.java
│   │       │   ├── UsersApiTest.java
│   │       │   ├── TodosApiTest.java
│   │       │   ├── NestedRoutesApiTest.java
│   │       │   └── QueryFeaturesApiTest.java
│   │       └── ui/
│   │           ├── BaseUITest.java
│   │           ├── buttons/
│   │           │   ├── ButtonsClickTest.java
│   │           │   └── ButtonsVisibilityTest.java
│   │           ├── webtables/
│   │           │   ├── BaseWebTablesTest.java
│   │           │   ├── WebTablesDefaultDataTest.java
│   │           │   ├── WebTablesSearchTest.java
│   │           │   ├── WebTablesCrudTest.java
│   │           │   └── WebTablesPaginationTest.java
│   │           └── form/
│   │               ├── BasePracticeFormTest.java
│   │               ├── PracticeFormSubmissionTest.java
│   │               ├── PracticeFormGenderTest.java
│   │               ├── PracticeFormHobbiesTest.java
│   │               ├── PracticeFormDatePickerTest.java
│   │               └── PracticeFormLocationTest.java
│   └── resources/
│       ├── allure.properties
│       ├── config.properties
│       ├── logback.xml
│       ├── fixtures/
│       │   └── post.json
│       └── schemas/
│           ├── post-schema.json
│           ├── posts-array-schema.json
│           ├── comment-schema.json
│           ├── comments-array-schema.json
│           ├── user-schema.json
│           ├── users-array-schema.json
│           ├── todo-schema.json
│           ├── todos-array-schema.json
│           ├── photo-schema.json
│           └── photos-array-schema.json
├── CLAUDE.md                 # Repo conventions, gotchas and target-site notes
├── LICENSE
├── pom.xml
├── testng.xml                # All suites  (parallel="classes", 6 threads)
├── testng-api.xml            # API only    (sequential)
└── testng-ui.xml             # UI only     (parallel="classes", 4 threads)
```

## Tech Stack

| Technology | Purpose |
|------------|---------|
| Selenium WebDriver | Browser automation |
| REST Assured | JSON API testing, with JSON Schema (draft-07) validation |
| TestNG | Test framework — suites, data providers, listeners, retry analyzer |
| WebDriverManager | Automatic ChromeDriver resolution |
| Allure | Reporting — `@Step` traces and failure screenshots |
| AssertJ | Fluent assertions |
| Logback | Logging |

Versions are **not listed here on purpose.** They are declared as properties in
[`pom.xml`](pom.xml), Dependabot bumps them weekly, and a number transcribed into prose
drifts the moment it merges — this table carried three stale versions before #55 corrected
them. `pom.xml` is the single source of truth:

```bash
mvn help:evaluate -Dexpression=selenium.version -q -DforceStdout
```

Runtime requirements (Java 17, Maven 3.6+) are in [Prerequisites](#prerequisites) — those
are floors this project targets, not dependency versions that move on their own.

## Test Coverage

### API Tests — 21 tests total

JSON API tests against the [JSONPlaceholder](https://jsonplaceholder.typicode.com) mock API, with JSON Schema (Draft-07) validation:

| Class | Tests |
|---|---|
| `PostsApiTest` | List 100 posts, get-by-id (fixture), 404, create (201 echo), delete, **PUT replace**, **PATCH partial**, filter by `userId` |
| `CommentsApiTest` | Filter comments by `postId` query param (schema-validated, all items match the filter) |
| `UsersApiTest` | List 10 users, get-by-id with nested `address.geo` and `company` schema validation |
| `TodosApiTest` | List 200 todos, filter by `completed=true` boolean |
| `NestedRoutesApiTest` | `/posts/1/comments`, `/users/1/posts`, `/albums/1/photos` — each asserts parent-id integrity |
| `QueryFeaturesApiTest` | Pagination + `X-Total-Count` + RFC 5988 `Link` header parsing (all 4 rels), sort, slice, full-text `q=`, empty-filter returns `[]` not 404 |

### UI Tests

#### Buttons — 5 tests

Tests against the [Buttons page](https://adrianjiga.github.io/qa/helpers/buttons):

| Class | Tests |
|---|---|
| `ButtonsClickTest` | Double click, right click, dynamic click (with JS executor fallback) |
| `ButtonsVisibilityTest` | Messages hidden by default, only triggered message shown after interaction |

#### Web Tables — 15 tests

Tests against the [Web Tables page](https://adrianjiga.github.io/qa/helpers/webtables):

| Class | Tests |
|---|---|
| `WebTablesDefaultDataTest` | 3 default records rendered, correct values, correct departments |
| `WebTablesSearchTest` | Filter by name, filter by department, no results, restore on clear |
| `WebTablesCrudTest` | Add record, cancel modal, delete record, delete specific row, edit record, modal pre-populated |
| `WebTablesPaginationTest` | Default page state, rows-per-page selector |

#### Practice Form — 14 test runs (12 `@Test` methods)

`PracticeFormGenderTest.testGenderRadioSelection` is data-driven via a `@DataProvider` with
three rows, so it counts as one method but three runs. Every other count in this README is
methods and runs alike.

Tests against the [Automation Practice Form](https://adrianjiga.github.io/qa/helpers/automation-practice-form):

| Class | Tests |
|---|---|
| `PracticeFormSubmissionTest` | Full form submission, modal close, text fields accept input |
| `PracticeFormGenderTest` | Select Male, select Female, select Other (data-driven), switch between selections |
| `PracticeFormHobbiesTest` | Check one hobby, check multiple independently, uncheck |
| `PracticeFormDatePickerTest` | Opens popup, selects a date and verifies input value |
| `PracticeFormLocationTest` | Cities populate after country selection, select country and city |

## CI/CD

Both workflows open with a `changes` job that runs `dorny/paths-filter` to decide whether the
API side, the UI side, or shared build files were touched. Downstream jobs gate on those
outputs, so a PR that only edits API tests never spins up Chrome.

### CI Workflow (`run-ci.yml`)

Lightweight validation on every pull request — fast feedback (~2 min) without running tests:

- Verifies Java, and Chrome **only if** the UI or shared paths changed
- Resolves Maven dependencies
- Compiles source and test code, then caches `target/classes` and `target/test-classes`
  under the commit SHA
- Validates all three TestNG suite XML files with `xmllint --noout --nonet`
  (`--nonet` skips the external DTD fetch, so the check doesn't depend on testng.org being up)
- Writes a summary table to `$GITHUB_STEP_SUMMARY`

### Test Workflow (`run-tests.yml`)

Full test execution:

- **Triggers:** Pull requests to master, weekday schedule (07:00 UTC), manual dispatch
- **Jobs:** `changes` → `build` → (`test-api`, `test-ui`) in parallel on `ubuntu-latest`
- **Build caching:** the `build` job compiles once and publishes a cache key; both test jobs
  restore the compiled classes instead of recompiling
- **Failure handling:** the `mvn test` step uses `continue-on-error: true` so reports and
  logs still upload, then an explicit `Fail if Tests Failed` step re-raises the failure.
  Without this, a red suite loses its own artifacts
- **Concurrency:** in-progress runs on the same ref are cancelled
- **Features:** test summaries, artifact uploads, automatic retries (2x, via `RetryAnalyzer`
  — see [Test Retries](#test-retries))
- **Artifact retention:** 30 days. Surefire XML is the input to flake analytics, so retention
  is the hard limit on how far back that history can reach — once an artifact expires the run
  is unrecoverable. 30 days matches the sibling Cypress and Playwright projects, so a
  cross-project comparison covers the same window
- **Manual dispatch:** supports `all`, `api`, or `ui` test group selection

All `actions/*` references are pinned to full commit SHAs with a trailing `# vX.Y.Z` comment.
SHAs are immutable, so a compromised tag cannot silently re-point at different code.

### Dependabot

Automated dependency updates configured for:
- Maven dependencies (weekly, Tuesdays)
- GitHub Actions (weekly, Tuesdays)

## Configuration

### Properties and overrides

Settings live in `src/test/resources/config.properties`:

| Key | Default | Purpose |
|---|---|---|
| `base.url` | `https://adrianjiga.github.io/qa/helpers` | UI test target |
| `api.base.uri` | `https://jsonplaceholder.typicode.com` | API test target |
| `wait.timeout.seconds` | `15` | `WebDriverWait` timeout |
| `page.load.timeout.seconds` | `30` | Page load and script timeout |
| `headless` | `true` | Chrome headless mode |

`ConfigManager.get()` reads `System.getProperty(key, props.getProperty(key))` — a JVM system
property wins over the file. So **no source edit is needed to override anything**:

```bash
# Watch the browser drive the tests
mvn test -DsuiteXmlFile=testng-ui.xml -Dheadless=false

# Point the UI suite at a local copy of the helper site
mvn test -DsuiteXmlFile=testng-ui.xml -Dbase.url=http://localhost:3000/qa/helpers
```

One caveat: `BaseUITest.BASE_URL` is `static final`, so it is resolved once at class load.
That is fine for a per-run override like the above, but it means the base URL cannot change
between tests within a single JVM.

### Parallel Execution

Parallelism is set per suite, and the three suites differ:

| Suite | Setting | Why |
|---|---|---|
| `testng.xml` | `parallel="classes" thread-count="6"` | Full run — API and UI classes interleave across 6 threads. |
| `testng-ui.xml` | `parallel="classes" thread-count="4"` | UI only. Lower than 6 because each class holds its own `ChromeDriver`, and browsers are the memory constraint. |
| `testng-api.xml` | *(none — sequential)* | REST Assured calls are fast enough that thread setup costs more than it saves, and it keeps JSONPlaceholder rate limits out of play. |

`parallel="classes"` is the ceiling for the current design. `BaseUITest` holds `driver` and
`wait` as **instance fields**, which is safe when TestNG gives each class its own instance
but would break under `parallel="methods"`. Moving to method-level parallelism requires a
`ThreadLocal<WebDriver>` first.

### Test Retries

Failed tests automatically retry up to 2 times. This is **not** Surefire's
`rerunFailingTestsCount` — it is a TestNG retry analyzer, wired in two parts:

- `RetryAnalyzer` (`listeners/RetryAnalyzer.java`) implements `IRetryAnalyzer` and returns
  `true` for the first 2 failures of a test.
- `RetryListener` (`listeners/RetryListener.java`) implements `IAnnotationTransformer` and
  attaches that analyzer to every `@Test` that does not already declare one, so individual
  tests never need to opt in.

The listener is registered in each of the three suite XML files:

```xml
<listeners>
    <listener class-name="com.example.listeners.RetryListener"/>
</listeners>
```

The trade-off is worth stating plainly: blanket runner-level retries make a flaky suite look
green. They are here to absorb network jitter against a public API and a GitHub Pages host,
not to paper over genuine races in the tests.

## Reports

Three layers, produced by every run:

| Output | Location | Notes |
|---|---|---|
| Surefire XML/TXT | `target/surefire-reports/` | Consumed by the CI test-summary step and by any JUnit-XML reader. |
| Allure results | `target/allure-results/` | Raw result files; directory set in `src/test/resources/allure.properties`. |
| CI job summary | GitHub Actions run page | Rendered by `test-summary/action` from the Surefire XML. |

### Allure

Page objects annotate their methods with `@Step`, so a failure renders as a readable
sequence of actions rather than a bare stack trace:

```java
@Step("Click Edit button for row {row}")
public WebTablesPage clickEdit(int row) { ... }
```

`ScreenshotListener` implements `ITestListener` and attaches a PNG to the Allure result on
any UI test failure. It resolves the driver by checking `result.getInstance() instanceof
BaseUITest`, so API test failures are skipped without special-casing.

Allure requires the AspectJ weaver to be on the JVM's `-javaagent` path; `pom.xml` wires
this into Surefire's `argLine`. Removing that argument silently disables `@Step` capture —
the tests still pass, the report just goes blank.

To view the report locally you need the Allure CLI (`brew install allure`), then:

```bash
allure serve target/allure-results
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
