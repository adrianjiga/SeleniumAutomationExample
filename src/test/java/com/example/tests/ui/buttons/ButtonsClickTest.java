package com.example.tests.ui.buttons;

import com.example.tests.ui.BaseUITest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ButtonsClickTest extends BaseUITest {

    @BeforeMethod
    public void navigateToPage() {
        driver.get(BASE_URL + "/buttons");
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
    }

    @Test(description = "Should interact with double click button")
    public void testDoubleClick() {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("doubleClickBtn")));
        scrollToElement(button);
        wait.until(ExpectedConditions.elementToBeClickable(button));
        new Actions(driver).doubleClick(button).perform();

        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("doubleClickMessage")));
        Assert.assertTrue(message.getText().contains("You have done a double click"),
                "Double click message should be displayed. Actual: " + message.getText());
    }

    @Test(description = "Should interact with right click button")
    public void testRightClick() {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rightClickBtn")));
        scrollToElement(button);
        wait.until(ExpectedConditions.elementToBeClickable(button));
        new Actions(driver).contextClick(button).perform();

        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("rightClickMessage")));
        Assert.assertTrue(message.getText().contains("You have done a right click"),
                "Right click message should be displayed. Actual: " + message.getText());
    }

    @Test(description = "Should interact with dynamic click button")
    public void testDynamicClick() {
        WebElement button = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-cy='dynamic-click-btn']")));
        scrollToElement(button);
        wait.until(ExpectedConditions.elementToBeClickable(button));

        try {
            button.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }

        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dynamicClickMessage")));
        Assert.assertTrue(message.getText().contains("You have done a dynamic click"),
                "Dynamic click message should be displayed. Actual: " + message.getText());
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
