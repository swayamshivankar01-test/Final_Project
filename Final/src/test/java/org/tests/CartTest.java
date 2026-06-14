package org.tests;

import org.pages.CartPage;
import org.pages.LoginPage;
import org.pages.SearchPage;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;

import org.utilities.BrowserManage;
import org.utilities.ExcelUtil;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

@Listeners(org.listeners.TestListener.class)
public class CartTest extends BrowserManage {

    public CartTest() throws IOException {
        super();
    }

    private void loginFirst() throws Exception {
        driver.get(pro.getProperty("url") + "login");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        LoginPage lp = new LoginPage(driver);
        String username = ExcelUtil.getExcelData("LoginData")[0][0].toString();
        String password = ExcelUtil.getExcelData("LoginData")[0][1].toString();
        lp.login(username, password);
        wait.until(ExpectedConditions.urlToBe(pro.getProperty("url")));
    }

    private void searchAndOpenProduct(String keyword) throws Exception {
        SearchPage sp = new SearchPage(driver);
        sp.searchProduct(keyword);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("search"));
        wait.until(ExpectedConditions.elementToBeClickable(sp.firstProductLink));
        sp.clickFirstProduct();

        CartPage cp = new CartPage(driver);
        cp.waitForProductPage();
    }

    @Test(priority = 1)
    public void addToCartTest() throws Exception {
        loginFirst();
        searchAndOpenProduct("laptop");

        CartPage cp = new CartPage(driver);
        cp.clickAddToCart();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(cp.successNotification));

        Assert.assertTrue(cp.isSuccessNotificationDisplayed(),
                "Success notification not shown after adding to cart");
        Assert.assertFalse(cp.getCartCount().equals("(0)"),
                "Cart count should be greater than 0 after adding item");
    }

    @Test(priority = 2)
    public void updateCartQuantityTest() throws Exception {
        loginFirst();

        driver.get(pro.getProperty("url") + "cart");
        CartPage cp = new CartPage(driver);
        cp.clearCart();

        searchAndOpenProduct("laptop");
        cp.clickAddToCart();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(cp.successNotification));

        driver.get(pro.getProperty("url") + "cart");
        wait.until(ExpectedConditions.urlContains("cart"));

        String qtyBefore = cp.getQuantityValue(0);
        System.out.println("Quantity Before = " + qtyBefore);
        Assert.assertEquals(qtyBefore, "1", "Initial quantity should be 1");

        cp.updateQuantity(0, "3");

        driver.get(pro.getProperty("url") + "cart");
        wait.until(ExpectedConditions.urlContains("cart"));

        String qtyAfter = cp.getQuantityValue(0);
        System.out.println("Quantity After = " + qtyAfter);
        Assert.assertEquals(qtyAfter, "3", "Quantity should be updated to 3");
    }

    @Test(priority = 3)
    public void removeFromCartTest() throws Exception {
        loginFirst();
        searchAndOpenProduct("laptop");

        CartPage cp = new CartPage(driver);
        cp.clickAddToCart();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(cp.successNotification));

        driver.get(pro.getProperty("url") + "cart");
        System.out.println("Items Before Remove = " + cp.getCartItemCount());

        cp.removeItem(0);

        wait.until(driver -> cp.emptyCartMessage.getText()
                .contains("Your Shopping Cart is empty!"));

        Assert.assertTrue(cp.isCartEmpty(),
                "Cart should be empty after removing all items");
    }
}