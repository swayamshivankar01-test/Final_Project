package org.utilities;

import java.io.IOException;
import java.time.Duration;
import org.base.BaseClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BrowserManage extends BaseClass {

    public BrowserManage() throws IOException {
        super();
    }

    @BeforeMethod
    public void setup() {
        initialization();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}