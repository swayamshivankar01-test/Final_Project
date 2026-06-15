package com.qa.tests;

import com.qa.base.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("DummyJSON nopCommerce QA Capstone")
@Feature("Token Refresh & Session")
public class TokenRefreshTest extends BaseTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        loginIfNeeded();
    }

    @Test(priority = 1, description = "API_TC_015 - Refresh access token")
    @Story("Token Refresh") @Severity(SeverityLevel.BLOCKER)
    public void tc015_refreshAccessToken() {
        Map<String, Object> body = new HashMap<>();
        body.put("refreshToken", refreshToken);
        body.put("expiresInMins", 30);

        Response response = given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/auth/refresh")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .header("Content-Type", containsString("application/json"))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .time(lessThan(3000L))
                .extract().response();

        accessToken  = response.jsonPath().getString("accessToken");
        refreshToken = response.jsonPath().getString("refreshToken");
    }

    @Test(priority = 2, description = "API_TC_NEG - Refresh with invalid token")
    @Story("Token Refresh") @Severity(SeverityLevel.NORMAL)
    public void tcNeg_refreshWithInvalidToken() {
        Map<String, Object> body = new HashMap<>();
        body.put("refreshToken", "invalid.refresh.token.xyz");
        body.put("expiresInMins", 30);

        given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/auth/refresh")
                .then()
                .spec(responseSpec)
                .statusCode(anyOf(is(401), is(403)))
                .time(lessThan(3000L));
    }
}
