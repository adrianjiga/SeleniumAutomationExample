package com.example.tests.api;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class NestedRoutesApiTest extends BaseApiTest {

    @Test(description = "GET /posts/{id}/comments returns comments that all belong to that post")
    public void testGetCommentsForPost() {
        int postId = 1;

        given()
                .spec(requestSpec)
        .when()
                .get("/posts/" + postId + "/comments")
        .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("size()", greaterThan(0))
                .body(matchesJsonSchemaInClasspath("schemas/commentsArraySchema.json"))
                .body("findAll { it.postId != " + postId + " }.size()", equalTo(0));
    }

    @Test(description = "GET /users/{id}/posts returns the 10 posts authored by that user")
    public void testGetPostsForUser() {
        int userId = 1;

        given()
                .spec(requestSpec)
        .when()
                .get("/users/" + userId + "/posts")
        .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("size()", equalTo(10))
                .body(matchesJsonSchemaInClasspath("schemas/postsArraySchema.json"))
                .body("findAll { it.userId != " + userId + " }.size()", equalTo(0));
    }

    @Test(description = "GET /albums/{id}/photos returns the 50 photos in that album")
    public void testGetPhotosForAlbum() {
        int albumId = 1;

        given()
                .spec(requestSpec)
        .when()
                .get("/albums/" + albumId + "/photos")
        .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("size()", equalTo(50))
                .body(matchesJsonSchemaInClasspath("schemas/photosArraySchema.json"))
                .body("findAll { it.albumId != " + albumId + " }.size()", equalTo(0));
    }
}
