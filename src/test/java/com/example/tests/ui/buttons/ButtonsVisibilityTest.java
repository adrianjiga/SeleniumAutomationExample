package com.example.tests.ui.buttons;

import com.example.tests.ui.BaseUITest;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ButtonsVisibilityTest extends BaseUITest {

    @BeforeMethod
    public void navigateToPage() {
        driver.get(BASE_URL + "/buttons");
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
    }

    @Test(description = "Should show only the triggered message, not others")
    public void testMessagesAreHiddenByDefault() {
        Assert.assertFalse(driver.findElement(By.id("doubleClickMessage")).isDisplayed(),
                "Double click message should be hidden before interaction");
        Assert.assertFalse(driver.findElement(By.id("rightClickMessage")).isDisplayed(),
                "Right click message should be hidden before interaction");
        Assert.assertFalse(driver.findElement(By.id("dynamicClickMessage")).isDisplayed(),
                "Dynamic click message should be hidden before interaction");
    }

    @Test(description = "Should show only double-click message after double clicking")
    public void testOnlyDoubleClickMessageShownAfterDoubleClick() {
        new Actions(driver)
                .doubleClick(wait.until(ExpectedConditions.elementToBeClickable(By.id("doubleClickBtn"))))
                .perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("doubleClickMessage")));

        Assert.assertTrue(driver.findElement(By.id("doubleClickMessage")).isDisplayed());
        Assert.assertFalse(driver.findElement(By.id("rightClickMessage")).isDisplayed());
        Assert.assertFalse(driver.findElement(By.id("dynamicClickMessage")).isDisplayed());
    }
}
