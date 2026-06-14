package org.utilities;
	
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
	
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
	
public class ScreenshotUtil {
	
	public static String captureScreenshot(WebDriver driver, String screenshotName) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());

        String destinationPath = System.getProperty("user.dir")
                + "/screenshots/"
                + screenshotName + "_" + timeStamp + ".png";

        try {

            File srcFile = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            File destFile = new File(destinationPath);
            destFile.getParentFile().mkdirs();

            FileUtils.copyFile(srcFile, destFile);

            System.out.println("Screenshot saved at: " + destinationPath);

        } catch (IOException e) {
	
            System.out.println("Failed to capture screenshot: "
                    + e.getMessage());
        }

        return destinationPath;
    }
}