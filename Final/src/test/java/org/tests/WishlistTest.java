package org.tests;

import org.pages.LoginPage;
import org.pages.SearchPage;
import org.pages.WishlistPage;
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
public class WishlistTest extends BrowserManage {

    public WishlistTest() throws IOException {
        super();
    }

    @Test(priority = 1)
    public void addToWishlistTest() throws Exception {
        driver.get(pro.getProperty("url") + "login");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        LoginPage lp = new LoginPage(driver);
        String username = ExcelUtil.getExcelData("LoginData")[0][0].toString();
        String password = ExcelUtil.getExcelData("LoginData")[0][1].toString();
        lp.login(username, password);
        wait.until(ExpectedConditions.urlToBe(pro.getProperty("url")));

        SearchPage sp = new SearchPage(driver);
        sp.searchProduct("blue and green sneaker");
        wait.until(ExpectedConditions.urlContains("search"));

        wait.until(ExpectedConditions.elementToBeClickable(sp.firstProductLink));
        sp.clickFirstProduct();

        WishlistPage wp = new WishlistPage(driver);
        wait.until(ExpectedConditions.elementToBeClickable(wp.addToWishlistButton));
        wp.clickAddToWishlist();

        wait.until(ExpectedConditions.visibilityOf(wp.successNotification));
        Assert.assertTrue(wp.isSuccessNotificationDisplayed(),
                "Success notification not shown after adding to wishlist");

        String wishlistCount = wp.getWishlistCount();
        Assert.assertFalse(wishlistCount.equals("(0)"),
                "Wishlist count should be greater than 0 after adding item");

        System.out.println("Wishlist count: " + wishlistCount);
    }
}
