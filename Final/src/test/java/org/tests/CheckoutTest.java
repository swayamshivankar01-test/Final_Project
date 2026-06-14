package org.tests;

import org.pages.CartPage;
import org.pages.CheckoutPage;
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
public class CheckoutTest extends BrowserManage {

    public CheckoutTest() throws IOException {
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

    private void searchAndAddToCart() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        SearchPage sp = new SearchPage(driver);
        sp.searchProduct("laptop");
        wait.until(ExpectedConditions.urlContains("search"));
        wait.until(ExpectedConditions.elementToBeClickable(sp.firstProductLink));

        boolean added = false;
        int size = sp.getSearchResultCount();

        for (int i = 0; i < size; i++) {
            sp.clickFirstProduct();
            CartPage cp = new CartPage(driver);

            if (cp.isAddToCartPresent()) {
                cp.clickAddToCart();
                wait.until(ExpectedConditions.visibilityOf(cp.successNotification));
                added = true;
                break;
            } else {
                driver.navigate().back();
                wait.until(ExpectedConditions.urlContains("search"));
            }
        }

        if (!added) {
            driver.get(pro.getProperty("url") + "computing-and-internet");
            CartPage cp = new CartPage(driver);
            cp.clickAddToCart();
            wait.until(ExpectedConditions.visibilityOf(cp.successNotification));
        }
    }

    private void completeBillingStep(CheckoutPage chp) throws Exception {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOf(chp.billingFirstName));
            chp.fillBillingAddress(
                ExcelUtil.getExcelData("CheckoutData")[0][0].toString(),
                ExcelUtil.getExcelData("CheckoutData")[0][1].toString(),
                ExcelUtil.getExcelData("CheckoutData")[0][2].toString(),
                ExcelUtil.getExcelData("CheckoutData")[0][3].toString(),
                ExcelUtil.getExcelData("CheckoutData")[0][4].toString(),
                ExcelUtil.getExcelData("CheckoutData")[0][5].toString(),
                ExcelUtil.getExcelData("CheckoutData")[0][6].toString(),
                ExcelUtil.getExcelData("CheckoutData")[0][7].toString()
            );
            System.out.println("Billing filled");
        } catch (Exception e) {
            System.out.println("Using saved billing address");
        }
        chp.clickBillingContinue();
    }

    @Test(priority = 1)
    public void fullCheckoutCODTest() throws Exception {
        loginFirst();
        searchAndAddToCart();

        driver.get(pro.getProperty("url") + "cart");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        CheckoutPage chp = new CheckoutPage(driver);
        chp.waitForCartItems();
        chp.acceptTermsAndCheckout();

        wait.until(ExpectedConditions.urlContains("checkout"));

        completeBillingStep(chp);
        chp.clickShippingAddressContinue();
        chp.clickShippingMethodContinue();
        chp.selectCODPayment();
        chp.clickPaymentInfoContinue();
        chp.confirmOrder();

        wait.until(ExpectedConditions.urlContains("completed"));
        Assert.assertTrue(driver.getCurrentUrl().contains("completed"),
                "Order was not completed successfully");
    }

    @Test(priority = 2)
    public void validCouponTest() throws Exception {
        loginFirst();
        searchAndAddToCart();

        driver.get(pro.getProperty("url") + "cart");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        CheckoutPage chp = new CheckoutPage(driver);
        chp.waitForCartItems();

        Assert.assertTrue(chp.isCouponBoxPresent(),
                "Coupon field not present on cart page");

        String couponCode = ExcelUtil.getExcelData("CouponData")[0][0].toString();
        chp.applyCoupon(couponCode);

        String result = chp.getCouponResultMessage();
        System.out.println("Coupon result: " + result);

        Assert.assertFalse(result.toLowerCase().contains("couldn't"),
                "Valid coupon was not applied. Message: " + result);
    }

    @Test(priority = 3)
    public void invalidCouponTest() throws Exception {
        loginFirst();
        searchAndAddToCart();

        driver.get(pro.getProperty("url") + "cart");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        CheckoutPage chp = new CheckoutPage(driver);
        chp.waitForCartItems();

        Assert.assertTrue(chp.isCouponBoxPresent(),
                "Coupon field not present on cart page");

        String invalidCoupon = ExcelUtil.getExcelData("CouponData")[1][0].toString();
        chp.applyCoupon(invalidCoupon);

        String result = chp.getCouponResultMessage();
        System.out.println("Invalid coupon result: " + result);

        Assert.assertTrue(result.toLowerCase().contains("couldn't"),
                "No error message shown for invalid coupon. Message: " + result);
    }
}