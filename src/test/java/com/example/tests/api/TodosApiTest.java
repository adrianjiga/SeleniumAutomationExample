package com.example.tests.api;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class TodosApiTest extends BaseApiTest {

    @Test(description = "GET /todos returns 200 todos matching the todo list schema")
    public void testGetAllTodosReturns200WithValidSchema() {
        given()
                .spec(requestSpec)
        .when()
                .get("/todos")
        .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("size()", equalTo(200))
                .body(matchesJsonSchemaInClasspath("schemas/todosArraySchema.json"));
    }

    @Test(description = "GET /todos?completed=true returns only completed todos")
    public void testFilterTodosByCompletedTrue() {
        given()
                .spec(requestSpec)
                .queryParam("completed", true)
        .when()
                .get("/todos")
        .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("findAll { it.completed != true }.size()", equalTo(0));
    }
}
