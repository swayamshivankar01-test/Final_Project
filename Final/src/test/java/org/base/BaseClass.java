package org.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;

public class BaseClass {

    public static Properties pro;  
    public static WebDriver driver;        

    public BaseClass() throws IOException {

        FileInputStream opn = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/Config/Application.properties"
        );
        pro = new Properties();
        pro.load(opn);
    }

    public void initialization() {
        String browser = pro.getProperty("Browser");

        if (browser.equals("Chrome")) {

            ChromeOptions options = new ChromeOptions();

            options.addArguments(
                    "--disable-notifications");

            options.addArguments(
                    "--disable-save-password-bubble");

            options.addArguments(
                    "--disable-features=AutofillServerCommunication");

            options.addArguments(
                    "--guest");

            driver =
                    new ChromeDriver(options);
        } else if (browser.equals("Edge")) {
            driver = new EdgeDriver();
        } else {
            System.out.println("No such browser configured: " + browser);
        }
    }
}