package com.qa.tests;

import com.qa.base.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("DummyJSON nopCommerce QA Capstone")
@Feature("Order Management")
public class OrderManagementTest extends BaseTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        loginIfNeeded();
    }

    @Test(priority = 1, description = "API_TC_018 - Retrieve user carts as order history")
    @Story("Order History") @Severity(SeverityLevel.CRITICAL)
    public void tc018_retrieveUserCartsAsOrderHistory() {
        given()
                .spec(authSpec())
                .when()
                .get("/users/" + USER_ID + "/carts")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .header("Content-Type", notNullValue())
                .body("total", instanceOf(Number.class));
    }

    @Test(priority = 2, description = "API_TC_NEG_014 - Retrieve carts for non-existent user")
    @Story("Order History") @Severity(SeverityLevel.NORMAL)
    public void tcNeg014_retrieveCartsForNonExistentUser() {
        given()
                .spec(authSpec())
                .when()
                .get("/users/999999/carts")
                .then()
                .spec(responseSpec)
                .statusCode(404)
                .time(lessThan(3000L));
    }

    @Test(priority = 3, description = "API_TC_019 - Cancel order by deleting cart")
    @Story("Order Cancellation") @Severity(SeverityLevel.CRITICAL)
    public void tc019_cancelOrderSimulationByDeletingCart() {
        given()
                .spec(authSpec())
                .when()
                .delete("/carts/" + CART_ID)
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("isDeleted", equalTo(true))
                .body("deletedOn", notNullValue())
                .time(lessThan(2000L));
    }

    @Test(priority = 4, description = "API_TC_NEG_015 - Delete non-existent cart")
    @Story("Order Cancellation") @Severity(SeverityLevel.NORMAL)
    public void tcNeg015_deleteNonExistentCart() {
        given()
                .spec(authSpec())
                .when()
                .delete("/carts/999999")
                .then()
                .spec(responseSpec)
                .statusCode(404)
                .time(lessThan(3000L));
    }
}
