package com.qa.base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeSuite;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class BaseTest {

    protected static final String BASE_URL      = "https://dummyjson.com";
    protected static final String USERNAME       = "emilys";
    protected static final String PASSWORD       = "emilyspass";
    protected static final int    USER_ID        = 1;
    protected static final int    PRODUCT_ID     = 1;
    protected static final int    CART_ID        = 1;
    protected static final String CATEGORY_NAME  = "beauty";
    protected static final String SEARCH_KEYWORD = "phone";

    protected static volatile String accessToken  = "";
    protected static volatile String refreshToken = "";

    protected static RequestSpecification requestSpec;
    protected static ResponseSpecification responseSpec;

    @BeforeSuite(alwaysRun = true)
    public void globalSetup() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.useRelaxedHTTPSValidation();

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();
    }

    protected synchronized void loginIfNeeded() {
        if (accessToken != null && !accessToken.isEmpty()) return;

        Map<String, Object> body = new HashMap<>();
        body.put("username", USERNAME);
        body.put("password", PASSWORD);
        body.put("expiresInMins", 30);

        Response response = given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .extract().response();

        accessToken  = response.jsonPath().getString("accessToken");
        refreshToken = response.jsonPath().getString("refreshToken");
    }

    protected RequestSpecification authSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(requestSpec)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
    }
}
