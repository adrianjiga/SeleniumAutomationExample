package com.example.tests.api;

import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PostsApiTest extends BaseApiTest {

    @Test(description = "GET /posts returns 100 posts matching the post list schema")
    public void testListAllPostsReturns100WithValidSchema() {
        given()
                .spec(requestSpec)
        .when()
                .get("/posts")
        .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("size()", equalTo(100))
                .body(matchesJsonSchemaInClasspath("schemas/posts-array-schema.json"));
    }

    @Test(description = "GET /posts/{id} returns a post matching the fixture and schema")
    public void testGetPostByIdMatchesFixture() throws IOException {
        JsonPath expected = JsonPath.from(loadFixture());

        given()
                .spec(requestSpec)
        .when()
                .get("/posts/" + expected.getInt("id"))
        .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"))
                .body("id", equalTo(expected.getInt("id")))
                .body("userId", equalTo(expected.getInt("userId")))
                .body("title", equalTo(expected.getString("title")))
                .body("body", equalTo(expected.getString("body")));
    }

    @Test(description = "GET /posts/{id} for a non-existent post returns 404")
    public void testGetNonExistentPostReturns404() {
        given()
                .spec(requestSpec)
        .when()
                .get("/posts/999")
        .then()
                .statusCode(404);
    }

    @Test(description = "POST /posts creates a new post and returns 201 echoing the submitted body")
    public void testCreatePostReturns201WithEchoedBody() {
        Map<String, Object> newPost = Map.of(
                "title", "test title",
                "body", "test body",
                "userId", 1
        );

        given()
                .spec(requestSpec)
                .body(newPost)
        .when()
                .post("/posts")
        .then()
                .statusCode(201)
                .body("title", equalTo("test title"))
                .body("body", equalTo("test body"))
                .body("userId", equalTo(1))
                .body("id", notNullValue());
    }

    @Test(description = "DELETE /posts/{id} returns 200")
    public void testDeletePostReturns200() {
        given()
                .spec(requestSpec)
        .when()
                .delete("/posts/1")
        .then()
                .statusCode(200);
    }

    @Test(description = "PUT /posts/{id} replaces the full post and returns 200")
    public void testUpdatePostWithPut() {
        Map<String, Object> replacement = Map.of(
                "id", 1,
                "userId", 1,
                "title", "put title",
                "body", "put body"
        );

        given()
                .spec(requestSpec)
                .body(replacement)
        .when()
                .put("/posts/1")
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", equalTo("put title"))
                .body("body", equalTo("put body"));
    }

    @Test(description = "PATCH /posts/{id} updates only the supplied field and preserves the rest")
    public void testPartialUpdatePostWithPatch() {
        given()
                .spec(requestSpec)
                .body(Map.of("title", "patched title"))
        .when()
                .patch("/posts/1")
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", equalTo("patched title"))
                .body("body", notNullValue());
    }

    @Test(description = "GET /posts?userId=1 returns only posts belonging to that user")
    public void testFilterPostsByUserId() {
        int targetUserId = 1;

        given()
                .spec(requestSpec)
                .queryParam("userId", targetUserId)
        .when()
                .get("/posts")
        .then()
                .statusCode(200)
                .body("size()", equalTo(10))
                .body("findAll { it.userId != " + targetUserId + " }.size()", equalTo(0));
    }

    private String loadFixture() throws IOException {
        try (InputStream in = Objects.requireNonNull(
                getClass().getClassLoader().getResourceAsStream("fixtures/post.json"),
                "fixture not found on classpath: " + "fixtures/post.json")) {
            return new String(in.readAllBytes());
        }
    }
}
