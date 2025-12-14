package com.example.tests.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ButtonsTest extends BaseUITest {

    private static final Logger log = LoggerFactory.getLogger(ButtonsTest.class);

    @BeforeMethod
    public void navigateToPage() {
        driver.get(BASE_URL + "/buttons");
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
        removeAds();
    }

    @Test(description = "Should interact with double click button")
    public void testDoubleClick() {
        WebElement doubleClickButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("doubleClickBtn"))
        );

        scrollToElement(doubleClickButton);
        wait.until(ExpectedConditions.elementToBeClickable(doubleClickButton));
        Actions actions = new Actions(driver);
        actions.doubleClick(doubleClickButton).perform();

        WebElement message = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("doubleClickMessage"))
        );

        Assert.assertTrue(message.getText().contains("You have done a double click"),
                "Double click message should be displayed. Actual: " + message.getText());
    }

    @Test(description = "Should interact with right click button")
    public void testRightClick() {
        WebElement rightClickButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("rightClickBtn"))
        );

        scrollToElement(rightClickButton);
        wait.until(ExpectedConditions.elementToBeClickable(rightClickButton));
        Actions actions = new Actions(driver);
        actions.contextClick(rightClickButton).perform();

        WebElement message = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("rightClickMessage"))
        );

        Assert.assertTrue(message.getText().contains("You have done a right click"),
                "Right click message should be displayed. Actual: " + message.getText());
    }

    @Test(description = "Should interact with dynamic button")
    public void testDynamicClick() {
        WebElement dynamicButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//div[contains(@class,'mt-4')]//button[text()='Click Me']")
                )
        );

        scrollToElement(dynamicButton);
        wait.until(ExpectedConditions.elementToBeClickable(dynamicButton));

        try {
            dynamicButton.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dynamicButton);
        }

        WebElement message = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("dynamicClickMessage"))
        );

        Assert.assertTrue(message.getText().contains("You have done a dynamic click"),
                "Dynamic click message should be displayed. Actual: " + message.getText());
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                element
        );
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void removeAds() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                    "var ads = document.querySelectorAll('[id*=\"google_ads\"], [id*=\"ad-\"], [class*=\"adsbygoogle\"], iframe[src*=\"googlesyndication\"]');" +
                            "ads.forEach(function(ad) { ad.remove(); });"
            );
            Thread.sleep(500);
        } catch (Exception e) {
            log.error("e: ", e);
        }
    }
}