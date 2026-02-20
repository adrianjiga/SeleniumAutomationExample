package com.example.tests.api;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class PracticeFormPageApiTest extends BaseApiTest {

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
}
