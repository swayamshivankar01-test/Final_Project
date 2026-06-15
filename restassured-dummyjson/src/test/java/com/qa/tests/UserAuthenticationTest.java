package com.qa.tests;

import com.qa.base.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("DummyJSON nopCommerce QA Capstone")
@Feature("User Authentication")
public class UserAuthenticationTest extends BaseTest {

    @Test(priority = 1, description = "API_TC_001 - Login with valid credentials")
    @Story("Login") @Severity(SeverityLevel.BLOCKER)
    public void tc001_loginWithValidCredentials() {
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
                .spec(responseSpec)
                .statusCode(anyOf(is(200), is(201)))
                .header("Content-Type", containsString("application/json"))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .time(lessThan(3000L))
                .extract().response();

        accessToken  = response.jsonPath().getString("accessToken");
        refreshToken = response.jsonPath().getString("refreshToken");
    }

    @Test(priority = 2, description = "API_TC_NEG_001 - Login with empty credentials")
    @Story("Login") @Severity(SeverityLevel.NORMAL)
    public void tcNeg001_loginWithEmptyCredentials() {
        Map<String, Object> body = new HashMap<>();
        body.put("username", "");
        body.put("password", "");
        body.put("expiresInMins", 30);

        given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/auth/login")
                .then()
                .spec(responseSpec)
                .statusCode(anyOf(is(400), is(401)))
                .time(lessThan(3000L));
    }

    @Test(priority = 3, description = "API_TC_002 - Login with invalid password")
    @Story("Login") @Severity(SeverityLevel.CRITICAL)
    public void tc002_loginWithInvalidPassword() {
        Map<String, Object> body = new HashMap<>();
        body.put("username", USERNAME);
        body.put("password", "WrongPassword@123");
        body.put("expiresInMins", 30);

        given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/auth/login")
                .then()
                .spec(responseSpec)
                .statusCode(anyOf(is(400), is(401)))
                .body(containsStringIgnoringCase("invalid credentials"));
    }

    @Test(priority = 4, description = "API_TC_NEG_002 - Login with invalid username")
    @Story("Login") @Severity(SeverityLevel.NORMAL)
    public void tcNeg002_loginWithInvalidUsername() {
        Map<String, Object> body = new HashMap<>();
        body.put("username", "nonexistentuser999");
        body.put("password", "somepassword");
        body.put("expiresInMins", 30);

        given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/auth/login")
                .then()
                .spec(responseSpec)
                .statusCode(anyOf(is(400), is(401)))
                .body(containsStringIgnoringCase("invalid"));
    }

    @Test(priority = 5, description = "API_TC_003 - Get authenticated user profile")
    @Story("Profile") @Severity(SeverityLevel.CRITICAL)
    public void tc003_getAuthenticatedUserProfile() {
        loginIfNeeded();
        given()
                .spec(authSpec())
                .when()
                .get("/auth/me")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("username", notNullValue())
                .body("email", notNullValue())
                .time(lessThan(3000L));
    }

    @Test(priority = 6, description = "API_TC_NEG_003 - Access protected route without token")
    @Story("Profile") @Severity(SeverityLevel.NORMAL)
    public void tcNeg003_accessProtectedRouteWithoutToken() {
        given()
                .spec(requestSpec)
                .when()
                .get("/auth/me")
                .then()
                .spec(responseSpec)
                .statusCode(401)
                .time(lessThan(3000L));
    }

    @Test(priority = 7, description = "API_TC_NEG_004 - Access protected route with invalid token")
    @Story("Profile") @Severity(SeverityLevel.NORMAL)
    public void tcNeg004_accessProtectedRouteWithInvalidToken() {
        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer invalid.token.here")
                .when()
                .get("/auth/me")
                .then()
                .spec(responseSpec)
                .statusCode(anyOf(is(401), is(403)))
                .body(anyOf(
                        containsStringIgnoringCase("invalid"),
                        containsStringIgnoringCase("token"),
                        containsStringIgnoringCase("unauthorized"),
                        containsStringIgnoringCase("expired")
                ));
    }
}
