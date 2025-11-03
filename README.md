# Selenium + REST Assured Example

A demo project showcasing Selenium WebDriver and REST Assured testing capabilities.

##  Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Chrome browser (for Selenium tests)

##  Getting Started

### Installation

```bash
# Clone the repository
git clone git@github.com:adrianjiga/SeleniumAutomationExample.git
cd SeleniumAutomationExample

# Install dependencies
mvn clean install
```

##  Running Tests

### Run All Tests
```bash
mvn test
```

### Run Only API Tests
```bash
mvn test -DsuiteXmlFile=testng-api.xml
```

### Run Only UI Tests
```bash
mvn test -DsuiteXmlFile=testng-ui.xml
```

##  Project Structure

```
selenium-restassured-example/
├── src/
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── tests/
│                       ├── api/
│                       │   ├── BaseApiTest.java
│                       │   └── BookStoreApiTest.java
│                       └── ui/
│                           ├── BaseUITest.java
│                           └── ButtonsTest.java
├── pom.xml
├── testng.xml
├── testng-api.xml
├── testng-ui.xml
├── .gitignore
└── README.md
```

##  Key Technologies

- **Selenium WebDriver 4.26.0** - Browser automation
- **REST Assured 5.5.0** - API testing
- **TestNG 7.10.2** - Test framework
- **WebDriverManager 5.9.2** - Automatic driver management
- **Maven** - Build and dependency management

##  Test Details

### API Tests (BookStoreApiTest)
- ✅ List all books with correct structure and data
- ✅ Fetch a specific book by valid ISBN
- ✅ Handle invalid ISBN with proper error response

### UI Tests (ButtonsTest)
- ✅ Interact with double click button
- ✅ Interact with right click button
- ✅ Interact with dynamic button

##  Test Execution Flow

1. **Base Classes** handle common setup:
    - `BaseUITest` - WebDriver initialization and cleanup
    - `BaseApiTest` - REST Assured configuration

2. **Test Classes** contain actual test cases:
    - API tests use REST Assured for HTTP requests
    - UI tests use Selenium WebDriver for browser interactions

3. **TestNG** manages test execution and reporting

##  Reports

After running tests, TestNG generates reports in:
```
target/surefire-reports/
```

Open `index.html` or `emailable-report.html` to view the detailed test report.

##  Key Features

- Headless Chrome execution for CI/CD compatibility
- Automatic WebDriver management (no manual driver downloads)
- Parallel test execution support (configured in testng.xml)
- Comprehensive assertions and validations
- Clean test structure with base classes

## Customization

### Remove Headless Mode
To see the browser during test execution:
1. Open `src/test/java/com/example/tests/ui/BaseUITest.java`
2. Remove or comment out: `options.addArguments("--headless");`

### Add More Tests
1. Create new test class extending `BaseUITest` or `BaseApiTest`
2. Add `@Test` annotations to test methods
3. Update corresponding `testng.xml` file

### Enable Parallel Execution
Edit `testng.xml` and modify:
```xml
<suite name="Test Suite" parallel="methods" thread-count="4">
```

## License

MIT