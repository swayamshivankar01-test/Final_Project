package org.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReport {

    public static ExtentReports extent;

    public static ExtentReports getReport() {

        if (extent == null) {

        	String path = System.getProperty("user.dir")+ "/src/test/resources/Reports/Extent.html";

            ExtentSparkReporter spark =
                    new ExtentSparkReporter(path);

            spark.config().setTheme(Theme.DARK);
            spark.config().setReportName("Hybrid Automation Framework");

            spark.config().setDocumentTitle("Automation Test Report");
            extent = new ExtentReports();

            extent.attachReporter(spark);

            extent.setSystemInfo(
                    "Tester",
                    "Swayam Shivankar");

            extent.setSystemInfo(
                    "Browser",
                    "Chrome");
        }

        return extent;
    }
}