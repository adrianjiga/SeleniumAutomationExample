package com.example.tests.api;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class WebTablesPageApiTest extends BaseApiTest {

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
}
