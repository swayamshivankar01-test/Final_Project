package org.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import org.base.BaseClass;
import org.utilities.ExtentReport;
import org.utilities.ScreenshotUtil;

public class TestListener
        implements ITestListener {

    ExtentReports extent =
            ExtentReport.getReport();

    ExtentTest test;

    @Override
    public void onTestStart(
            ITestResult result) {

        test =
                extent.createTest(
                        result.getMethod()
                        .getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.pass("Test Passed");
        
        String screenshotPath = ScreenshotUtil.captureScreenshot(
                        BaseClass.driver, 
                        result.getMethod().getMethodName() + "_Passed");
        
        test.addScreenCaptureFromPath(screenshotPath);
    }

    @Override
    public void onTestFailure(
            ITestResult result) {

        test.fail(result.getThrowable());

        String screenshotPath =
                ScreenshotUtil.captureScreenshot(
                        BaseClass.driver,
                        result.getMethod()
                        .getMethodName());

        test.addScreenCaptureFromPath(
                screenshotPath);
    }

    @Override
    public void onFinish(
            ITestContext context) {

        extent.flush();
    }
}