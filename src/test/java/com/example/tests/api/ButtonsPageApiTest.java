package com.example.tests.api;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ButtonsPageApiTest extends BaseApiTest {

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
}
