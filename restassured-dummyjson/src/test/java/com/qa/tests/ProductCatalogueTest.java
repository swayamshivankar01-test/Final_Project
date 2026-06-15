package com.qa.tests;

import com.qa.base.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("DummyJSON nopCommerce QA Capstone")
@Feature("Product Catalogue & Search")
public class ProductCatalogueTest extends BaseTest {

    @Test(priority = 1, description = "API_TC_004 - Get all products")
    @Story("Product Listing") @Severity(SeverityLevel.CRITICAL)
    public void tc004_getAllProducts() {
        given()
                .spec(requestSpec)
                .when()
                .get("/products")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .header("Content-Type", containsString("application/json"))
                .body("products", notNullValue());
    }

    @Test(priority = 2, description = "API_TC_NEG_005 - Get product with non-existent ID")
    @Story("Product Listing") @Severity(SeverityLevel.NORMAL)
    public void tcNeg005_getProductWithNonExistentId() {
        given()
                .spec(requestSpec)
                .when()
                .get("/products/999999")
                .then()
                .spec(responseSpec)
                .statusCode(anyOf(is(400), is(404)))
                .body(anyOf(
                        containsStringIgnoringCase("not found"),
                        containsStringIgnoringCase("404"),
                        containsStringIgnoringCase("product"),
                        containsStringIgnoringCase("invalid")
                ));
    }

    @Test(priority = 3, description = "API_TC_005 - Get product by ID")
    @Story("Product Listing") @Severity(SeverityLevel.CRITICAL)
    public void tc005_getProductById() {
        given()
                .spec(requestSpec)
                .when()
                .get("/products/" + PRODUCT_ID)
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("id", equalTo(PRODUCT_ID))
                .body("price", instanceOf(Number.class))
                .body("stock", instanceOf(Number.class))
                .time(lessThan(3000L));
    }

    @Test(priority = 4, description = "API_TC_NEG_006 - Get product with invalid string ID")
    @Story("Product Listing") @Severity(SeverityLevel.NORMAL)
    public void tcNeg006_getProductWithInvalidStringId() {
        given()
                .spec(requestSpec)
                .when()
                .get("/products/invalid-id")
                .then()
                .spec(responseSpec)
                .statusCode(anyOf(is(400), is(404)))
                .time(lessThan(3000L));
    }

    @Test(priority = 5, description = "API_TC_006 - Search products by keyword")
    @Story("Product Search") @Severity(SeverityLevel.CRITICAL)
    public void tc006_searchProductsByKeyword() {
        given()
                .spec(requestSpec)
                .queryParam("q", SEARCH_KEYWORD)
                .when()
                .get("/products/search")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .header("Content-Type", containsString("application/json"))
                .body("total", instanceOf(Number.class));
    }

    @Test(priority = 6, description = "API_TC_NEG_007 - Search products with empty keyword")
    @Story("Product Search") @Severity(SeverityLevel.NORMAL)
    public void tcNeg007_searchProductsWithEmptyKeyword() {
        given()
                .spec(requestSpec)
                .queryParam("q", "")
                .when()
                .get("/products/search")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("total", instanceOf(Number.class))
                .time(lessThan(3000L));
    }

    @Test(priority = 7, description = "API_TC_007 - Sort and paginate products by price")
    @Story("Product Listing") @Severity(SeverityLevel.NORMAL)
    public void tc007_sortAndPaginateProductsByPrice() {
        given()
                .spec(requestSpec)
                .queryParam("limit", 10)
                .queryParam("skip", 0)
                .queryParam("sortBy", "price")
                .queryParam("order", "asc")
                .when()
                .get("/products")
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .header("Content-Type", notNullValue())
                .body("products.size()", lessThanOrEqualTo(10))
                .time(lessThan(2000L));
    }

    @Test(priority = 8, description = "API_TC_NEG_008 - Get products by non-existent category")
    @Story("Product Listing") @Severity(SeverityLevel.NORMAL)
    public void tcNeg008_getProductsByNonExistentCategory() {
        io.restassured.response.Response response = given()
                .spec(requestSpec)
                .when()
                .get("/products/category/invalidcategory999")
                .then()
                .spec(responseSpec)
                .statusCode(anyOf(is(200), is(404)))
                .extract().response();

        if (response.statusCode() == 200) {
            response.then().body("products", instanceOf(java.util.List.class));
        }
    }

    @Test(priority = 9, description = "API_TC_008 - Get products by category")
    @Story("Product Listing") @Severity(SeverityLevel.CRITICAL)
    public void tc008_getProductsByCategory() {
        given()
                .spec(requestSpec)
                .when()
                .get("/products/category/" + CATEGORY_NAME)
                .then()
                .spec(responseSpec)
                .statusCode(200)
                .body("products", instanceOf(java.util.List.class))
                .time(lessThan(1000L));
    }
}
