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
# Run all tests (API + UI in parallel)
mvn test

# Run only API tests
mvn test -DsuiteXmlFile=testng-api.xml

# Run only UI tests
mvn test -DsuiteXmlFile=testng-ui.xml

# Run a single test class
mvn test -Dtest=QaHelpersApiTest
mvn test -Dtest=WebTablesTest

# Run a single test method
mvn test -Dtest=WebTablesTest#testAddNewRecord
```

## Project Structure

```
SeleniumAutomationExample/
├── .github/
│   ├── dependabot.yml
│   └── workflows/
│       ├── run-ci.yml
│       └── run-tests.yml
├── src/test/java/com/example/tests/
│   ├── api/
│   │   ├── BaseApiTest.java
│   │   └── QaHelpersApiTest.java
│   └── ui/
│       ├── BaseUITest.java
│       ├── ButtonsTest.java
│       ├── WebTablesTest.java
│       └── PracticeFormTest.java
├── pom.xml
├── testng.xml
├── testng-api.xml
└── testng-ui.xml
```

## Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Selenium WebDriver | 4.40.0 | Browser automation |
| REST Assured | 6.0.0 | API / HTTP testing |
| TestNG | 7.12.0 | Test framework |
| WebDriverManager | 6.3.3 | Automatic ChromeDriver management |
| Java | 17 | Runtime |
| Maven | 3.6+ | Build & dependency management |

## Test Coverage

### API Tests (`QaHelpersApiTest`) — 12 tests

HTTP-level tests against the QA Helpers site (`https://adrianjiga.github.io/qa/helpers`):

- **Index page** — loads with 200 and `text/html` content type
- **Buttons page** — correct status, all three button elements present, all message elements present
- **Web Tables page** — correct status, table structure, pagination controls, correct column headers
- **Practice Form page** — correct status, all required form fields, gender radios, hobby checkboxes, country options
- **Error handling** — non-existent page returns a 4xx response

### UI Tests

#### `ButtonsTest` — 5 tests

Tests against the [Buttons page](https://adrianjiga.github.io/qa/helpers/buttons):

- **Double click** — validates double-click interaction and message display
- **Right click** — validates context menu interaction and message display
- **Dynamic click** — validates standard click with JS executor fallback
- **Messages hidden by default** — verifies no messages are shown before any interaction
- **Only triggered message shown** — verifies other messages stay hidden after a single interaction

#### `WebTablesTest` — 15 tests

Tests against the [Web Tables page](https://adrianjiga.github.io/qa/helpers/webtables):

- **Default data** — 3 default records rendered on load, correct values and departments
- **Search** — filter by first name, filter by department, no results, restore on clear
- **Add record** — fills registration modal and verifies new row appears
- **Cancel modal** — closing without saving keeps row count unchanged
- **Delete record** — row count decreases, correct row removed
- **Edit record** — values updated in table, modal pre-populated with existing data
- **Pagination** — default page state, rows-per-page selector

#### `PracticeFormTest` — 13 tests

Tests against the [Automation Practice Form](https://adrianjiga.github.io/qa/helpers/automation-practice-form):

- **Full form submission** — fills all fields and validates success modal
- **Modal close** — close button hides the success modal
- **Gender radios** — select Male, select Female, switch between selections
- **Hobby checkboxes** — check one, check multiple, uncheck
- **Date picker** — opens popup, selects a date and verifies input value
- **Country / city cascade** — city options populate after country selection, select a city
- **Text fields** — all inputs accept and retain typed values

## CI/CD

### CI Workflow (`run-ci.yml`)

Lightweight validation on every pull request:

- Verifies Java and Chrome setup
- Resolves Maven dependencies
- Compiles source and test code
- Validates TestNG suite XML files

Fast feedback (~2 min) without running actual tests.

### Test Workflow (`run-tests.yml`)

Full test execution:

- **Triggers:** Pull requests to master, weekday schedule (07:00 UTC), manual dispatch
- **Jobs:** `test-api` and `test-ui` run as separate parallel jobs on `ubuntu-latest`
- **Features:** Test summaries, artifact uploads (reports retained 14 days), automatic retries (2x for flaky tests)
- **Manual dispatch:** supports `all`, `api`, or `ui` test group selection

### Dependabot

Automated dependency updates configured for:
- Maven dependencies (weekly, Tuesdays)
- GitHub Actions (weekly, Tuesdays)

## Configuration

### Headless Mode

UI tests run in headless Chrome by default. To see the browser locally:

```java
// In BaseUITest.java, comment out:
options.addArguments("--headless=new");
```

### Parallel Execution

The main `testng.xml` runs API and UI test groups in parallel:

```xml
<suite name="Test Suite" parallel="tests" thread-count="2">
```

### Test Retries

Failed tests automatically retry up to 2 times (configured in `pom.xml`):

```xml
<rerunFailingTestsCount>2</rerunFailingTestsCount>
```

## Reports

TestNG generates reports in `target/surefire-reports/` after test execution. GitHub Actions also provides test summaries directly in the workflow run.

## License

MIT
