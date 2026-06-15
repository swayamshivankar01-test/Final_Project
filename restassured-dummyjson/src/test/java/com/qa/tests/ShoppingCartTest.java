package com.qa.tests;

import com.qa.base.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("DummyJSON nopCommerce QA Capstone")
@Feature("Shopping Cart")
public class ShoppingCartTest extends BaseTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        loginIfNeeded();
    }

    @Test(priority = 1, description = "API_TC_009 - Get all carts")
    @Story("Cart Retrieval") @Severity(SeverityLevel.CRITICAL)
    public void tc009_getAllCarts() {
        Response response = given()
                .spec(requestSpec)
                .when()
                .get("/carts")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .header("Content-Type", containsString("application/json"))
                .body("carts", not(empty()))
                .body("carts[0].id", notNullValue())
                .time(lessThan(1500L))
                .extract().response();

        System.out.println("[INFO] Captured cartId: " + response.jsonPath().getInt("carts[0].id"));
    }

    @Test(priority = 2, description = "API_TC_NEG_009 - Get cart with non-existent ID")
    @Story("Cart Retrieval") @Severity(SeverityLevel.NORMAL)
    public void tcNeg009_getCartWithNonExistentId() {
        given()
                .spec(authSpec())
                .when()
                .get("/carts/999999")
                .then()
                .spec(responseSpec)
                .statusCode(404)
                .body(anyOf(
                        containsStringIgnoringCase("not found"),
                        containsStringIgnoringCase("cart")
                ));
    }

    @Test(priority = 3, description = "API_TC_010 - Get cart by ID")
    @Story("Cart Retrieval") @Severity(SeverityLevel.CRITICAL)
    public void tc010_getCartById() {
        given()
                .spec(authSpec())
                .when()
                .get("/carts/" + CART_ID)
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("id", equalTo(CART_ID))
                .body("totalQuantity", instanceOf(Number.class))
                .time(lessThan(3000L));
    }

    @Test(priority = 4, description = "API_TC_NEG_010 - Add to cart with missing fields")
    @Story("Cart Operations") @Severity(SeverityLevel.NORMAL)
    public void tcNeg010_addToCartWithMissingFields() {
        Map<String, Object> body = new HashMap<>();
        body.put("userId", "");

        given()
                .spec(requestSpec)
                .body(body)
                .when()
                .post("/carts/add")
                .then()
                .spec(responseSpec)
                .statusCode(anyOf(is(400), is(422)))
                .time(lessThan(3000L));
    }

    @Test(priority = 5, description = "API_TC_011 - Get carts by user")
    @Story("Cart Retrieval") @Severity(SeverityLevel.CRITICAL)
    public void tc011_getCartsByUser() {
        given()
                .spec(authSpec())
                .when()
                .get("/carts/user/" + USER_ID)
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .header("Content-Type", containsString("application/json"))
                .time(lessThan(3000L));
    }

    @Test(priority = 6, description = "API_TC_NEG_011 - Add to cart with negative quantity")
    @Story("Cart Operations") @Severity(SeverityLevel.NORMAL)
    public void tcNeg011_addToCartWithNegativeQuantity() {
        Map<String, Object> product = new HashMap<>();
        product.put("id", String.valueOf(PRODUCT_ID));
        product.put("quantity", -5);

        Map<String, Object> body = new HashMap<>();
        body.put("userId", String.valueOf(USER_ID));
        body.put("products", Collections.singletonList(product));

        given()
                .spec(authSpec())
                .body(body)
                .when()
                .post("/carts/add")
                .then()
                .spec(responseSpec)
                .statusCode(anyOf(is(200), is(201), is(400), is(422)))
                .time(lessThan(3000L));
    }

    @Test(priority = 7, description = "API_TC_012 - Add product to cart")
    @Story("Cart Operations") @Severity(SeverityLevel.CRITICAL)
    public void tc012_addProductToCart() {
        Map<String, Object> product = new HashMap<>();
        product.put("id", String.valueOf(PRODUCT_ID));
        product.put("quantity", 5);

        Map<String, Object> body = new HashMap<>();
        body.put("userId", String.valueOf(USER_ID));
        body.put("products", Collections.singletonList(product));

        given()
                .spec(authSpec())
                .body(body)
                .when()
                .post("/carts/add")
                .then()
                .spec(responseSpec)
                .statusCode(anyOf(is(200), is(201)))
                .header("Content-Type", containsString("application/json"))
                .time(lessThan(3000L));
    }
}
