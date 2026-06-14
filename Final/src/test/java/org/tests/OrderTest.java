package org.tests;

import org.pages.CartPage;
import org.pages.CheckoutPage;
import org.pages.LoginPage;
import org.pages.OrderPage;
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
public class OrderTest extends BrowserManage {

    public OrderTest() throws IOException {
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

    private void completeCheckout() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.get(pro.getProperty("url") + "cart");

        CheckoutPage chp = new CheckoutPage(driver);
        chp.waitForCartItems();
        chp.acceptTermsAndCheckout();

        wait.until(ExpectedConditions.urlContains("checkout"));

        try {
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
        } catch (Exception e) {
            System.out.println("Using saved billing address");
        }

        chp.clickBillingContinue();
        chp.clickShippingAddressContinue();
        chp.clickShippingMethodContinue();
        chp.selectCODPayment();
        chp.clickPaymentInfoContinue();
        chp.confirmOrder();

        wait.until(ExpectedConditions.urlContains("completed"));
        System.out.println("Order placed: " + driver.getCurrentUrl());
    }

    private void openFirstOrderDetails() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(pro.getProperty("url") + "customer/orders");

        OrderPage op = new OrderPage(driver);
        op.waitForOrders();
        op.clickFirstOrderDetails();

        wait.until(ExpectedConditions.urlContains("orderdetails"));
        System.out.println("Opened order details: " + driver.getCurrentUrl());
    }

    @Test(priority = 1)
    public void viewOrderHistoryTest() throws Exception {
        loginFirst();

        driver.get(pro.getProperty("url") + "customer/orders");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        OrderPage op = new OrderPage(driver);
        op.waitForOrders();

        Assert.assertTrue(op.getOrderCount() > 0,
                "No orders found in order history");

        openFirstOrderDetails();

        Assert.assertTrue(driver.getCurrentUrl().contains("orderdetails"),
                "Order details page did not open");
    }

    @Test(priority = 2)
    public void cancelOrderTest() throws Exception {
        loginFirst();
        searchAndAddToCart();
        completeCheckout();

        openFirstOrderDetails();

        OrderPage op = new OrderPage(driver);
        String status = op.getOrderStatus();
        System.out.println("Order status: " + status);

        Assert.assertNotNull(status, "Order status should be displayed");
        Assert.assertTrue(status.length() > 0, "Order status should not be empty");
    }
}