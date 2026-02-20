package com.example.tests.api;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class QaHelpersApiTest extends BaseApiTest {

    // ── Index page ───────────────────────────────────────────────────────────

    @Test(description = "Should load the helpers index page with 200 status")
    public void testHelpersIndexLoads() {
        given()
                .spec(requestSpec)
        .when()
                .get("/")
        .then()
                .statusCode(200)
                .header("Content-Type", containsString("text/html"));
    }

    // ── Buttons page ─────────────────────────────────────────────────────────

    @Test(description = "Should load the buttons page with correct status and content-type")
    public void testButtonsPageLoads() {
        given()
                .spec(requestSpec)
        .when()
                .get("/buttons")
        .then()
                .statusCode(200)
                .header("Content-Type", containsString("text/html"));
    }

    @Test(description = "Buttons page should contain all three interaction button elements")
    public void testButtonsPageHasAllButtons() {
        given()
                .spec(requestSpec)
        .when()
                .get("/buttons")
        .then()
                .statusCode(200)
                .body(containsString("id=\"doubleClickBtn\""))
                .body(containsString("id=\"rightClickBtn\""))
                .body(containsString("data-cy=\"dynamic-click-btn\""));
    }

    @Test(description = "Buttons page should include confirmation message elements")
    public void testButtonsPageHasMessageElements() {
        given()
                .spec(requestSpec)
        .when()
                .get("/buttons")
        .then()
                .statusCode(200)
                .body(containsString("id=\"doubleClickMessage\""))
                .body(containsString("id=\"rightClickMessage\""))
                .body(containsString("id=\"dynamicClickMessage\""));
    }

    // ── Web Tables page ───────────────────────────────────────────────────────

    @Test(description = "Should load the web tables page with correct status and content-type")
    public void testWebTablesPageLoads() {
        given()
                .spec(requestSpec)
        .when()
                .get("/webtables")
        .then()
                .statusCode(200)
                .header("Content-Type", containsString("text/html"));
    }

    @Test(description = "Web tables page should contain table structure and toolbar elements")
    public void testWebTablesPageHasTableStructure() {
        given()
                .spec(requestSpec)
        .when()
                .get("/webtables")
        .then()
                .statusCode(200)
                .body(containsString("id=\"searchBox\""))
                .body(containsString("id=\"addNewRecordButton\""))
                .body(containsString("id=\"table-body\""));
    }

    @Test(description = "Web tables page should contain pagination controls")
    public void testWebTablesPageHasPagination() {
        given()
                .spec(requestSpec)
        .when()
                .get("/webtables")
        .then()
                .statusCode(200)
                .body(containsString("data-cy=\"prev-page-btn\""))
                .body(containsString("data-cy=\"next-page-btn\""))
                .body(containsString("id=\"current-page-num\""))
                .body(containsString("id=\"rows-per-page-sel\""));
    }

    @Test(description = "Web tables page should list all expected column headers")
    public void testWebTablesPageHasCorrectColumns() {
        Response response = given()
                .spec(requestSpec)
        .when()
                .get("/webtables")
        .then()
                .statusCode(200)
                .extract()
                .response();

        String body = response.getBody().asString();
        for (String col : new String[]{"First Name", "Last Name", "Age", "Email", "Salary", "Department", "Actions"}) {
            assert body.contains(col) : "Column header missing: " + col;
        }
    }

    // ── Automation Practice Form page ─────────────────────────────────────────

    @Test(description = "Should load the automation practice form page with correct status and content-type")
    public void testPracticeFormPageLoads() {
        given()
                .spec(requestSpec)
        .when()
                .get("/automation-practice-form")
        .then()
                .statusCode(200)
                .header("Content-Type", containsString("text/html"));
    }

    @Test(description = "Practice form page should contain all required form fields")
    public void testPracticeFormHasRequiredFields() {
        given()
                .spec(requestSpec)
        .when()
                .get("/automation-practice-form")
        .then()
                .statusCode(200)
                .body(containsString("id=\"firstName\""))
                .body(containsString("id=\"lastName\""))
                .body(containsString("id=\"userEmail\""))
                .body(containsString("id=\"userNumber\""))
                .body(containsString("id=\"dateOfBirthInput\""))
                .body(containsString("id=\"currentAddress\""))
                .body(containsString("id=\"submit\""));
    }

    @Test(description = "Practice form page should contain gender radio buttons")
    public void testPracticeFormHasGenderOptions() {
        given()
                .spec(requestSpec)
        .when()
                .get("/automation-practice-form")
        .then()
                .statusCode(200)
                .body(containsString("id=\"gender-radio-1\""))
                .body(containsString("id=\"gender-radio-2\""))
                .body(containsString("id=\"gender-radio-3\""));
    }

    @Test(description = "Practice form page should contain hobby checkboxes")
    public void testPracticeFormHasHobbies() {
        given()
                .spec(requestSpec)
        .when()
                .get("/automation-practice-form")
        .then()
                .statusCode(200)
                .body(containsString("id=\"hobbies-checkbox-1\""))
                .body(containsString("id=\"hobbies-checkbox-2\""))
                .body(containsString("id=\"hobbies-checkbox-3\""));
    }

    @Test(description = "Practice form page should contain country dropdown with expected options")
    public void testPracticeFormHasCountryOptions() {
        given()
                .spec(requestSpec)
        .when()
                .get("/automation-practice-form")
        .then()
                .statusCode(200)
                .body(containsString("Germany"))
                .body(containsString("France"))
                .body(containsString("Spain"))
                .body(containsString("Italy"))
                .body(containsString("Netherlands"));
    }

    // ── Error handling ────────────────────────────────────────────────────────

    @Test(description = "Should return 404 for a non-existent page")
    public void testNonExistentPageReturns404() {
        given()
                .spec(requestSpec)
                .redirects().follow(false)
        .when()
                .get("/this-page-does-not-exist-xyz")
        .then()
                .statusCode(anyOf(equalTo(301), equalTo(302), equalTo(404)));
    }
}
