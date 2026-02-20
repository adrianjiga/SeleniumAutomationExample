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
├── src/test/java/com/example/tests/
│   ├── api/
│   │   ├── BaseApiTest.java
│   │   ├── SiteApiTest.java
│   │   ├── ButtonsPageApiTest.java
│   │   ├── WebTablesPageApiTest.java
│   │   └── PracticeFormPageApiTest.java
│   └── ui/
│       ├── BaseUITest.java
│       ├── buttons/
│       │   ├── ButtonsClickTest.java
│       │   └── ButtonsVisibilityTest.java
│       ├── webtables/
│       │   ├── BaseWebTablesTest.java
│       │   ├── WebTablesDefaultDataTest.java
│       │   ├── WebTablesSearchTest.java
│       │   ├── WebTablesCrudTest.java
│       │   └── WebTablesPaginationTest.java
│       └── form/
│           ├── BasePracticeFormTest.java
│           ├── PracticeFormSubmissionTest.java
│           ├── PracticeFormGenderTest.java
│           ├── PracticeFormHobbiesTest.java
│           ├── PracticeFormDatePickerTest.java
│           └── PracticeFormLocationTest.java
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

### API Tests — 14 tests total

HTTP-level tests against the QA Helpers site (`https://adrianjiga.github.io/qa/helpers`):

| Class | Tests |
|---|---|
| `SiteApiTest` | Index page loads (200), non-existent page returns 4xx |
| `ButtonsPageApiTest` | Page loads, all button elements present, all message elements present |
| `WebTablesPageApiTest` | Page loads, table structure, pagination controls, correct column headers |
| `PracticeFormPageApiTest` | Page loads, required fields, gender radios, hobby checkboxes, country options |

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

#### Practice Form — 13 tests

Tests against the [Automation Practice Form](https://adrianjiga.github.io/qa/helpers/automation-practice-form):

| Class | Tests |
|---|---|
| `PracticeFormSubmissionTest` | Full form submission, modal close, text fields accept input |
| `PracticeFormGenderTest` | Select Male, select Female, switch between selections |
| `PracticeFormHobbiesTest` | Check one hobby, check multiple independently, uncheck |
| `PracticeFormDatePickerTest` | Opens popup, selects a date and verifies input value |
| `PracticeFormLocationTest` | Cities populate after country selection, select country and city |

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
