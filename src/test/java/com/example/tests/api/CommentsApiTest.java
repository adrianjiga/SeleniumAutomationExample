package com.example.tests.api;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class CommentsApiTest extends BaseApiTest {

    @Test(description = "GET /comments?postId=1 returns comments only for that post, matching the schema")
    public void testFilterCommentsByPostId() {
        int targetPostId = 1;

        given()
                .spec(requestSpec)
                .queryParam("postId", targetPostId)
        .when()
                .get("/comments")
        .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("size()", greaterThan(0))
                .body(matchesJsonSchemaInClasspath("schemas/commentsArraySchema.json"))
                .body("findAll { it.postId != " + targetPostId + " }.size()", equalTo(0));
    }
}
