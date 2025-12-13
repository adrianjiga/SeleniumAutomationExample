# Selenium + REST Assured Example

A demo project showcasing Selenium WebDriver and REST Assured testing capabilities with CI/CD integration.

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
# Run all tests
mvn test

# Run only API tests
mvn test -DsuiteXmlFile=testng-api.xml

# Run only UI tests
mvn test -DsuiteXmlFile=testng-ui.xml
```

## Project Structure

```
SeleniumAutomationExample/
├── .github/
│   ├── dependabot.yml
│   └── workflows/
│       ├── ci.yml
│       └── run-tests.yml
├── src/test/java/com/example/tests/
│   ├── api/
│   │   ├── BaseApiTest.java
│   │   └── BookStoreApiTest.java
│   └── ui/
│       ├── BaseUITest.java
│       └── ButtonsTest.java
├── pom.xml
├── testng.xml
├── testng-api.xml
└── testng-ui.xml
```

## Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Selenium WebDriver | 4.39.0 | Browser automation |
| REST Assured | 6.0.0 | API testing |
| TestNG | 7.11.0 | Test framework |
| WebDriverManager | 6.3.3 | Automatic driver management |
| Java | 17 | Runtime |
| Maven | 3.6+ | Build & dependency management |

## Test Coverage

### API Tests (`BookStoreApiTest`)

Tests against the DemoQA BookStore API (`https://demoqa.com/BookStore/v1`):

- **List all books** - Validates response structure, data types, and publisher values
- **Fetch book by ISBN** - Retrieves specific book details with full field validation
- **Invalid ISBN handling** - Verifies proper 400 error response for invalid requests

### UI Tests (`ButtonsTest`)

Tests against the DemoQA Buttons page (`https://demoqa.com/buttons`):

- **Double click** - Validates double-click interaction and message display
- **Right click** - Validates context menu interaction and message display
- **Dynamic click** - Validates standard click with fallback to JavaScript execution

## CI/CD

### CI Workflow (`ci.yml`)

Lightweight validation on every pull request:

- Verifies Java and Chrome setup
- Resolves Maven dependencies
- Compiles source and test code
- Validates TestNG suite files exist

Fast feedback (~2 min) without running actual tests.

### Test Workflow (`run-tests.yml`)

Full test execution:

- **Triggers**: Pull requests to master, weekday schedule (07:00 UTC), manual dispatch
- **Matrix strategy**: Parallel execution of API and UI test groups
- **Features**: Test summaries, artifact uploads, automatic retries (2x for flaky tests)

### Dependabot

Automated dependency updates configured for:
- Maven dependencies (weekly, Tuesdays)
- GitHub Actions (weekly, Tuesdays)

## Configuration

### Headless Mode

UI tests run in headless mode by default. To see the browser:

```java
// In BaseUITest.java, comment out:
options.addArguments("--headless=new");
```

### Parallel Execution

Tests run in parallel by default (`testng.xml`):

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