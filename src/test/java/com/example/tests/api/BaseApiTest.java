package com.example.tests.api;

import com.example.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class BaseApiTest {
    protected static final String BASE_URI = ConfigManager.get("api.base.uri");
    protected static final String BASE_PATH = ConfigManager.get("api.base.path");

    protected RequestSpecification requestSpec;

    @BeforeClass
    public void setUpApi() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setBasePath(BASE_PATH)
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .build();

        RestAssured.baseURI = BASE_URI;
        RestAssured.basePath = BASE_PATH;
    }
}
