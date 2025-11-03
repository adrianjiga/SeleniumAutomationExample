package com.example.tests.api;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BookStoreApiTest extends BaseApiTest {

    @Test(description = "Should list all books with correct structure and data")
    public void testListAllBooks() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get("/Books")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("books", notNullValue())
                .body("books", not(empty()))
                .extract()
                .response();

        List<Map<String, Object>> books = response.jsonPath().getList("books");

        Assert.assertTrue(books.size() > 0, "Books list should not be empty");

        // Validate first book structure
        Map<String, Object> firstBook = books.get(0);
        validateBookStructure(firstBook);

        // Validate publishers
        for (Map<String, Object> book : books) {
            String publisher = (String) book.get("publisher");
            Assert.assertTrue(
                    publisher.equals("O'Reilly Media") || publisher.equals("No Starch Press"),
                    "Publisher should be O'Reilly Media or No Starch Press");
        }
    }

    @Test(description = "Should fetch a specific book by valid ISBN")
    public void testFetchBookByIsbn() {
        String validIsbn = "9781449325862";

        given()
                .spec(requestSpec)
                .queryParam("ISBN", validIsbn)
                .when()
                .get("/Book")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("isbn", equalTo(validIsbn))
                .body("title", equalTo("Git Pocket Guide"))
                .body("subTitle", equalTo("A Working Introduction"))
                .body("author", equalTo("Richard E. Silverman"))
                .body("publisher", equalTo("O'Reilly Media"))
                .body("pages", equalTo(234))
                .body("description", notNullValue())
                .body("website", notNullValue());
    }

    @Test(description = "Should handle invalid ISBN with proper error response")
    public void testInvalidIsbn() {
        given()
                .spec(requestSpec)
                .queryParam("ISBN", "invalid-isbn")
                .when()
                .get("/Book")
                .then()
                .statusCode(400)
                .body("message", notNullValue());
    }

    private void validateBookStructure(Map<String, Object> book) {
        Assert.assertNotNull(book.get("isbn"), "ISBN should not be null");
        Assert.assertNotNull(book.get("title"), "Title should not be null");
        Assert.assertNotNull(book.get("subTitle"), "SubTitle should not be null");
        Assert.assertNotNull(book.get("author"), "Author should not be null");
        Assert.assertNotNull(book.get("publish_date"), "Publish date should not be null");
        Assert.assertNotNull(book.get("publisher"), "Publisher should not be null");
        Assert.assertNotNull(book.get("pages"), "Pages should not be null");
        Assert.assertNotNull(book.get("description"), "Description should not be null");
        Assert.assertNotNull(book.get("website"), "Website should not be null");

        // Validate types
        Assert.assertTrue(book.get("isbn") instanceof String, "ISBN should be a string");
        Assert.assertTrue(book.get("pages") instanceof Integer, "Pages should be an integer");
        Assert.assertTrue((Integer) book.get("pages") > 0, "Pages should be greater than 0");

        // Validate ISBN format
        String isbn = (String) book.get("isbn");
        Assert.assertTrue(isbn.matches("^[0-9-]+$"), "ISBN should contain only numbers and hyphens");
    }
}