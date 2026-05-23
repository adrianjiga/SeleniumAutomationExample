package com.example.tests.api;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

public class QueryFeaturesApiTest extends BaseApiTest {

    private static final Pattern LINK_PART = Pattern.compile("<([^>]+)>\\s*;\\s*rel=\"([^\"]+)\"");

    @Test(description = "GET /posts?_page=2&_limit=10 returns the page, X-Total-Count, and all four Link rels")
    public void testPaginationReturnsPageAndAllLinkHeaderRels() {
        Response response = given()
                .spec(requestSpec)
                .queryParam("_page", 2)
                .queryParam("_limit", 10)
        .when()
                .get("/posts")
        .then()
                .statusCode(200)
                .header("X-Total-Count", equalTo("100"))
                .body("size()", equalTo(10))
                .extract().response();

        Map<String, String> links = parseLinkHeader(response.getHeader("Link"));

        assertThat(links.keySet(), containsInAnyOrder("first", "prev", "next", "last"));
        assertThat(links.get("first"), containsString("_page=1"));
        assertThat(links.get("prev"), containsString("_page=1"));
        assertThat(links.get("next"), containsString("_page=3"));
        assertThat(links.get("last"), containsString("_page=10"));
    }

    @Test(description = "GET /posts?_sort=id&_order=desc returns posts ordered by id descending")
    public void testSortByIdDescending() {
        List<Integer> ids = given()
                .spec(requestSpec)
                .queryParam("_sort", "id")
                .queryParam("_order", "desc")
                .queryParam("_limit", 3)
        .when()
                .get("/posts")
        .then()
                .statusCode(200)
                .extract().jsonPath().getList("id", Integer.class);

        assertThat(ids, equalTo(List.of(100, 99, 98)));
    }

    @Test(description = "GET /posts?_start=10&_end=15 returns the half-open slice [10, 15)")
    public void testSliceWithStartAndEnd() {
        List<Integer> ids = given()
                .spec(requestSpec)
                .queryParam("_start", 10)
                .queryParam("_end", 15)
        .when()
                .get("/posts")
        .then()
                .statusCode(200)
                .body("size()", equalTo(5))
                .extract().jsonPath().getList("id", Integer.class);

        assertThat(ids, equalTo(List.of(11, 12, 13, 14, 15)));
    }

    @Test(description = "GET /posts?q=qui performs full-text search over title and body")
    public void testFullTextSearch() {
        String term = "qui";

        given()
                .spec(requestSpec)
                .queryParam("q", term)
                .queryParam("_limit", 5)
        .when()
                .get("/posts")
        .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("findAll { !(it.title.toLowerCase().contains('" + term + "') "
                        + "|| it.body.toLowerCase().contains('" + term + "')) }.size()", equalTo(0));
    }

    @Test(description = "GET /posts?userId=999 returns 200 with an empty array (not 404)")
    public void testFilterWithNoMatchesReturnsEmptyArray() {
        given()
                .spec(requestSpec)
                .queryParam("userId", 999)
        .when()
                .get("/posts")
        .then()
                .statusCode(200)
                .contentType(containsString("application/json"))
                .body("size()", equalTo(0));
    }

    private static Map<String, String> parseLinkHeader(String header) {
        assertThat("Link header missing", header, notNullValue());
        Map<String, String> rels = new HashMap<>();
        for (String part : header.split(",")) {
            Matcher m = LINK_PART.matcher(part.trim());
            if (m.find()) {
                rels.put(m.group(2), m.group(1));
            }
        }
        return rels;
    }
}
