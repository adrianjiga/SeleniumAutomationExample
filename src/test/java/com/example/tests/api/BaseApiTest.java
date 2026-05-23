package com.example.tests.api;

import com.example.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {
    protected static final String BASE_URI = ConfigManager.get("api.base.uri");

    protected RequestSpecification requestSpec;

    @BeforeClass
    public void setUpApi() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .addHeader("Accept", "application/json")
                .setContentType(ContentType.JSON)
                .build();

        RestAssured.baseURI = BASE_URI;
    }
}
