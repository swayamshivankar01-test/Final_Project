package com.qa.tests;

import com.qa.base.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("DummyJSON nopCommerce QA Capstone")
@Feature("Checkout & Payment")
public class CheckoutPaymentTest extends BaseTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        loginIfNeeded();
    }

    @Test(priority = 1, description = "API_TC_013 - Checkout cart with multiple products")
    @Story("Checkout") @Severity(SeverityLevel.CRITICAL)
    public void tc013_checkoutCartWithMultipleProducts() {
        Map<String, Object> p1 = new HashMap<>(); p1.put("id", 1); p1.put("quantity", 1);
        Map<String, Object> p2 = new HashMap<>(); p2.put("id", 2); p2.put("quantity", 1);

        Map<String, Object> body = new HashMap<>();
        body.put("userId", String.valueOf(USER_ID));
        body.put("products", Arrays.asList(p1, p2));

        given()
                .spec(authSpec())
                .body(body)
                .when()
                .post("/carts/add")
                .then()
                .spec(responseSpec)
                .statusCode(anyOf(is(200), is(201)))
                .header("Content-Type", containsString("application/json"))
                .body("total", instanceOf(Number.class));
    }

    @Test(priority = 2, description = "API_TC_NEG_012 - Checkout with empty products array")
    @Story("Checkout") @Severity(SeverityLevel.NORMAL)
    public void tcNeg012_checkoutWithEmptyProductsArray() {
        Map<String, Object> body = new HashMap<>();
        body.put("userId", String.valueOf(USER_ID));
        body.put("products", Collections.emptyList());

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

    @Test(priority = 3, description = "API_TC_014 - Update cart quantity for checkout")
    @Story("Checkout") @Severity(SeverityLevel.CRITICAL)
    public void tc014_updateCartQuantityForCheckout() {
        Map<String, Object> product = new HashMap<>();
        product.put("id", 1); product.put("quantity", 4);

        Map<String, Object> body = new HashMap<>();
        body.put("merge", true);
        body.put("products", Collections.singletonList(product));

        given()
                .spec(authSpec())
                .body(body)
                .when()
                .put("/carts/" + CART_ID)
                .then()
                .spec(responseSpec)
                .statusCode(anyOf(is(200), is(201)))
                .header("Content-Type", containsString("application/json"))
                .time(lessThan(3000L));
    }

    @Test(priority = 4, description = "API_TC_NEG_013 - Update cart with non-existent cart ID")
    @Story("Checkout") @Severity(SeverityLevel.NORMAL)
    public void tcNeg013_updateCartWithNonExistentCartId() {
        Map<String, Object> product = new HashMap<>();
        product.put("id", 1); product.put("quantity", 2);

        Map<String, Object> body = new HashMap<>();
        body.put("merge", true);
        body.put("products", Collections.singletonList(product));

        given()
                .spec(authSpec())
                .body(body)
                .when()
                .put("/carts/999999")
                .then()
                .spec(responseSpec)
                .statusCode(404)
                .time(lessThan(3000L));
    }
}
