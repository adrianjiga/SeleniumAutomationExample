package com.example.tests.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {
    protected static final String BASE_URI = "https://demoqa.com";
    protected static final String BASE_PATH = "/BookStore/v1";

    protected RequestSpecification requestSpec;

    @BeforeClass
    public void setUpApi() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setBasePath(BASE_PATH)
                .addHeader("Accept", "application/json")
                .build();

        RestAssured.baseURI = BASE_URI;
        RestAssured.basePath = BASE_PATH;
    }
}