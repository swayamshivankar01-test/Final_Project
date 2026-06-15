package com.qa.tests;

import com.qa.base.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("DummyJSON nopCommerce QA Capstone")
@Feature("Wishlist")
public class WishlistTest extends BaseTest {

    @BeforeClass(alwaysRun = true)
    public void setup() {
        loginIfNeeded();
    }

    @Test(priority = 1, description = "API_TC_016 - Get products for wishlist selection")
    @Story("Wishlist") @Severity(SeverityLevel.NORMAL)
    public void tc016_getProductsForWishlistSelection() {
        given()
                .spec(authSpec())
                .queryParam("limit", 5)
                .queryParam("select", "title,price,thumbnail,rating")
                .when()
                .get("/products")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .header("Content-Type", containsString("application/json"))
                .body("products[0].title", notNullValue())
                .body("products[0].price", notNullValue())
                .time(lessThan(2000L));
    }

    @Test(priority = 2, description = "API_TC_017 - Move wishlist product to cart")
    @Story("Wishlist") @Severity(SeverityLevel.NORMAL)
    public void tc017_moveWishlistProductToCartSimulation() {
        Map<String, Object> product = new HashMap<>();
        product.put("id", String.valueOf(PRODUCT_ID));
        product.put("quantity", 1);

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
