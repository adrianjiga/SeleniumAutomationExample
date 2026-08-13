package com.example.tests.api;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class UsersApiTest extends BaseApiTest {

    @Test(description = "GET /users returns 10 users matching the user list schema")
    public void testGetAllUsersReturns10WithValidSchema() {
        given()
                .spec(requestSpec)
        .when()
                .get("/users")
        .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("size()", equalTo(10))
                .body(matchesJsonSchemaInClasspath("schemas/usersArraySchema.json"));
    }

    @Test(description = "GET /users/{id} returns a user with nested address.geo and company objects")
    public void testGetUserByIdHasNestedAddressAndCompany() {
        given()
                .spec(requestSpec)
        .when()
                .get("/users/1")
        .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body(matchesJsonSchemaInClasspath("schemas/userSchema.json"))
                .body("id", equalTo(1))
                .body("address.geo.lat", not(equalTo("")))
                .body("address.geo.lng", not(equalTo("")))
                .body("company.name", not(equalTo("")));
    }
}
