package com.example.listeners;

import com.example.tests.ui.BaseUITest;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

public class ScreenshotListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        Object instance = result.getInstance();
        if (instance instanceof BaseUITest baseUITest) {
            WebDriver driver = baseUITest.getDriver();
            if (driver != null) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(
                        "Screenshot on failure — " + result.getName(),
                        "image/png",
                        new ByteArrayInputStream(screenshot),
                        "png"
                );
            }
        }
    }
}
