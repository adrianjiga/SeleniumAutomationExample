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
        JsonPath expected = JsonPath.from(loadFixture("fixtures/post.json"));

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

    private String loadFixture(String classpathResource) throws IOException {
        try (InputStream in = Objects.requireNonNull(
                getClass().getClassLoader().getResourceAsStream(classpathResource),
                "fixture not found on classpath: " + classpathResource)) {
            return new String(in.readAllBytes());
        }
    }
}
