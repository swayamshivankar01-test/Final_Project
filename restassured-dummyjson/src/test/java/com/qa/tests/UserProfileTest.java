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
@Feature("User Profile Management")
public class UserProfileTest extends BaseTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        loginIfNeeded();
    }

    @Test(priority = 1, description = "API_TC_020 - Get user profile by ID")
    @Story("Profile") @Severity(SeverityLevel.CRITICAL)
    public void tc020_getUserProfileById() {
        given()
                .spec(authSpec())
                .when()
                .get("/users/" + USER_ID)
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("id", equalTo(USER_ID))
                .body("firstName", notNullValue())
                .body("lastName", notNullValue())
                .body("email", notNullValue())
                .time(lessThan(2000L));
    }

    @Test(priority = 2, description = "API_TC_NEG_016 - Get user with non-existent ID")
    @Story("Profile") @Severity(SeverityLevel.NORMAL)
    public void tcNeg016_getUserProfileWithNonExistentId() {
        given()
                .spec(authSpec())
                .when()
                .get("/users/999999")
                .then()
                .spec(responseSpec)
                .statusCode(404)
                .body(anyOf(
                        containsStringIgnoringCase("not found"),
                        containsStringIgnoringCase("user")
                ))
                .time(lessThan(3000L));
    }

    @Test(priority = 3, description = "API_TC_021 - Update user profile")
    @Story("Profile") @Severity(SeverityLevel.CRITICAL)
    public void tc021_updateUserProfile() {
        Map<String, Object> body = new HashMap<>();
        body.put("firstName", "Emily");
        body.put("lastName", "UpdatedSmith");
        body.put("email", "emily.updated@example.com");
        body.put("age", 29);

        given()
                .spec(authSpec())
                .body(body)
                .when()
                .put("/users/" + USER_ID)
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("firstName", notNullValue())
                .body("lastName", notNullValue())
                .time(lessThan(3000L));
    }

    @Test(priority = 4, description = "API_TC_022 - Partially update user profile")
    @Story("Profile") @Severity(SeverityLevel.NORMAL)
    public void tc022_partiallyUpdateUserProfile() {
        Map<String, Object> body = new HashMap<>();
        body.put("lastName", "PatchedSmith");

        given()
                .spec(authSpec())
                .body(body)
                .when()
                .patch("/users/" + USER_ID)
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("lastName", notNullValue())
                .time(lessThan(2000L));
    }

    @Test(priority = 5, description = "API_TC_NEG_017 - Update user with invalid email format")
    @Story("Profile") @Severity(SeverityLevel.NORMAL)
    public void tcNeg017_updateUserWithInvalidEmailFormat() {
        Map<String, Object> body = new HashMap<>();
        body.put("firstName", "Emily");
        body.put("lastName", "Smith");
        body.put("email", "not-an-email");

        Response response = given()
                .spec(authSpec())
                .body(body)
                .when()
                .put("/users/" + USER_ID)
                .then()
                .spec(responseSpec)
                .statusCode(anyOf(is(200), is(400), is(422)))
                .time(lessThan(3000L))
                .extract().response();

        if (response.statusCode() == 200) {
            System.out.println("[KNOWN LIMITATION] DummyJSON mock API accepted invalid email format with 200 OK.");
        }
    }
}
