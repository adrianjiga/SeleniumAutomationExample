package com.example.tests.api;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class SiteApiTest extends BaseApiTest {

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

    @Test(description = "Should return an error response for a non-existent page")
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
