package com.example.tests.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ButtonsTest extends BaseUITest {

    @BeforeMethod
    public void navigateToPage() {
        driver.get(BASE_URL + "/buttons");
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
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
                ExpectedConditions.visibilityOfElementLocated(By.id("doubleClickMessage"))
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
                ExpectedConditions.visibilityOfElementLocated(By.id("rightClickMessage"))
        );

        Assert.assertTrue(message.getText().contains("You have done a right click"),
                "Right click message should be displayed. Actual: " + message.getText());
    }

    @Test(description = "Should interact with dynamic click button")
    public void testDynamicClick() {
        WebElement dynamicButton = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-cy='dynamic-click-btn']"))
        );

        scrollToElement(dynamicButton);
        wait.until(ExpectedConditions.elementToBeClickable(dynamicButton));

        try {
            dynamicButton.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dynamicButton);
        }

        WebElement message = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("dynamicClickMessage"))
        );

        Assert.assertTrue(message.getText().contains("You have done a dynamic click"),
                "Dynamic click message should be displayed. Actual: " + message.getText());
    }

    @Test(description = "Should show only the triggered message, not others")
    public void testMessagesAreHiddenByDefault() {
        Assert.assertFalse(
                driver.findElement(By.id("doubleClickMessage")).isDisplayed(),
                "Double click message should be hidden before interaction"
        );
        Assert.assertFalse(
                driver.findElement(By.id("rightClickMessage")).isDisplayed(),
                "Right click message should be hidden before interaction"
        );
        Assert.assertFalse(
                driver.findElement(By.id("dynamicClickMessage")).isDisplayed(),
                "Dynamic click message should be hidden before interaction"
        );
    }

    @Test(description = "Should show only double-click message after double clicking")
    public void testOnlyDoubleClickMessageShownAfterDoubleClick() {
        WebElement doubleClickButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("doubleClickBtn"))
        );

        new Actions(driver).doubleClick(doubleClickButton).perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("doubleClickMessage")));

        Assert.assertTrue(driver.findElement(By.id("doubleClickMessage")).isDisplayed());
        Assert.assertFalse(driver.findElement(By.id("rightClickMessage")).isDisplayed());
        Assert.assertFalse(driver.findElement(By.id("dynamicClickMessage")).isDisplayed());
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                element
        );
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
