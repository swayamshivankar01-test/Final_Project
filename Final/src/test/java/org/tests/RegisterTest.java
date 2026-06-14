package org.tests;
import org.pages.RegisterPage;
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
public class RegisterTest extends BrowserManage {

    public RegisterTest() throws IOException {
        super();
    }

    @Test(priority = 1)
    public void validRegistrationTest() throws Exception {
        driver.get(pro.getProperty("url"));
        RegisterPage rp = new RegisterPage(driver);
        rp.clickRegisterLink();

        String firstName  = ExcelUtil.getExcelData("RegisterData")[0][0].toString();
        String lastName   = ExcelUtil.getExcelData("RegisterData")[0][1].toString();
        String email      = ExcelUtil.getExcelData("RegisterData")[0][2].toString();
        String password   = ExcelUtil.getExcelData("RegisterData")[0][3].toString();
        String confirmPwd = ExcelUtil.getExcelData("RegisterData")[0][4].toString();

        rp.register(firstName, lastName, email, password, confirmPwd);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(rp.registrationResult));

        String result = rp.getRegistrationResultText();
        Assert.assertEquals(result, "Your registration completed");
    }

    @Test(priority = 2)
    public void registrationMissingFieldsTest() throws Exception {
        driver.get(pro.getProperty("url"));
        RegisterPage rp = new RegisterPage(driver);
        rp.clickRegisterLink();

        rp.clickRegister();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("register"));

        Assert.assertTrue(driver.getCurrentUrl().contains("register"),
                "Should remain on register page when fields are missing");
    }
}