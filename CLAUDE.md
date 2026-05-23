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

Shared base classes (no tests): `ui.webtables.BaseWebTablesTest`, `ui.form.BasePracticeFormTest`. Both extend `BaseUITest` from the parent `ui` package and require `import com.example.tests.ui.BaseUITest;`.

### TestNG Configuration

- `testng.xml` — All tests (parallel execution with 2 threads)
- `testng-api.xml` — API tests only
- `testng-ui.xml` — UI tests only

### Key Conventions

- Failed tests automatically retry up to 2 times (configured in `pom.xml` `rerunFailingTestsCount`)
- UI tests use explicit waits via `WebDriverWait` (15 second default timeout)
- Page load timeout: 30 seconds
- UI locator preference: `id` first, then `data-cy` CSS attribute selectors (e.g. `[data-cy='submit-btn']`), XPath avoided
- `WebElement.clear()` does not fire the JS `input` event — use `JavascriptExecutor` to dispatch it manually when a JS listener depends on it

### Target Site Notes

- **Buttons page** (`/buttons`): dynamic click button has no `id`, use `[data-cy='dynamic-click-btn']`; message elements are hidden by CSS initially and shown via `style.display='block'` by JS
- **Web Tables page** (`/webtables`): state is stored in `localStorage`; each test gets a clean state because `BaseUITest` creates a fresh `ChromeDriver` per method; the add/edit modal is injected into the DOM dynamically (use `visibilityOfElementLocated`, not just `presenceOfElementLocated`)
- **Practice Form** (`/automation-practice-form`): required fields for submission are `firstName`, `lastName`, `userNumber` (mobile), and `gender`; country/city dropdowns are custom JS components (not native `<select>`) — click `.select-control` to open, then click the option; `getText()` returns `""` on elements inside a `display:none` container, so open dropdowns before asserting on their options
