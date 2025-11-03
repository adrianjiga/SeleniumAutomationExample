package com.example.tests.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ButtonsTest extends BaseUITest {

    @BeforeMethod
    public void navigateToPage() {
        driver.get(BASE_URL + "/buttons");
    }

    @Test(description = "Should interact with double click button")
    public void testDoubleClick() {
        WebElement doubleClickButton = driver.findElement(By.id("doubleClickBtn"));

        Actions actions = new Actions(driver);
        actions.doubleClick(doubleClickButton).perform();

        WebElement message = driver.findElement(By.id("doubleClickMessage"));
        Assert.assertTrue(message.getText().contains("You have done a double click"),
                "Double click message should be displayed");
    }

    @Test(description = "Should interact with right click button")
    public void testRightClick() {
        WebElement rightClickButton = driver.findElement(By.id("rightClickBtn"));

        Actions actions = new Actions(driver);
        actions.contextClick(rightClickButton).perform();

        WebElement message = driver.findElement(By.id("rightClickMessage"));
        Assert.assertTrue(message.getText().contains("You have done a right click"),
                "Right click message should be displayed");
    }

    @Test(description = "Should interact with dynamic button")
    public void testDynamicClick() {
        // Find the dynamic button (it's the third button that's not double or right click)
        WebElement dynamicButton = driver.findElement(
                By.xpath("//div[contains(@class,'mt-4')]//button[text()='Click Me']"));

        dynamicButton.click();

        WebElement message = driver.findElement(By.id("dynamicClickMessage"));
        Assert.assertTrue(message.getText().contains("You have done a dynamic click"),
                "Dynamic click message should be displayed");
    }
}