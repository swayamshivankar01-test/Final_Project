package org.tests;
import org.pages.LoginPage;
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
public class LoginTest extends BrowserManage {

    public LoginTest() throws IOException {
        super();
    }

    @Test(priority = 1)
    public void validLoginTest() throws Exception {
        driver.get(pro.getProperty("url"));
        LoginPage lp = new LoginPage(driver);
        lp.clickLoginLink();

        String username = ExcelUtil.getExcelData("LoginData")[0][0].toString();
        String password = ExcelUtil.getExcelData("LoginData")[0][1].toString();

        lp.login(username, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe(pro.getProperty("url")));

        Assert.assertEquals(driver.getCurrentUrl(), pro.getProperty("url"),
                "Valid login failed - URL mismatch");
    }

    @Test(priority = 2)
    public void invalidLoginTest() throws Exception {
        driver.get(pro.getProperty("url"));
        LoginPage lp = new LoginPage(driver);
        lp.clickLoginLink();

        String username = ExcelUtil.getExcelData("LoginData")[1][0].toString();
        String password = ExcelUtil.getExcelData("LoginData")[1][1].toString();

        lp.login(username, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(lp.loginErrorMessage));

        Assert.assertTrue(lp.isErrorMessageDisplayed(),
                "Error message not displayed for invalid login");
    }

    @Test(priority = 3)
    public void blankLoginTest() throws Exception {
        driver.get(pro.getProperty("url"));
        LoginPage lp = new LoginPage(driver);
        lp.clickLoginLink();

        lp.clickLoginWithoutCredentials();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(lp.loginErrorMessage));

        Assert.assertTrue(lp.isErrorMessageDisplayed(),
                "Validation error not shown for blank login");
    }
}