package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ButtonsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final By DOUBLE_CLICK_BTN      = By.id("doubleClickBtn");
    private static final By RIGHT_CLICK_BTN       = By.id("rightClickBtn");
    private static final By DYNAMIC_CLICK_BTN     = By.cssSelector("[data-cy='dynamic-click-btn']");
    private static final By DOUBLE_CLICK_MESSAGE  = By.id("doubleClickMessage");
    private static final By RIGHT_CLICK_MESSAGE   = By.id("rightClickMessage");
    private static final By DYNAMIC_CLICK_MESSAGE = By.id("dynamicClickMessage");

    public ButtonsPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void navigate(String baseUrl) {
        driver.get(baseUrl + "/buttons");
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
    }

    public void doubleClick() {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(DOUBLE_CLICK_BTN));
        scrollToElement(button);
        wait.until(ExpectedConditions.elementToBeClickable(button));
        new Actions(driver).doubleClick(button).perform();
    }

    public void rightClick() {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(RIGHT_CLICK_BTN));
        scrollToElement(button);
        wait.until(ExpectedConditions.elementToBeClickable(button));
        new Actions(driver).contextClick(button).perform();
    }

    public void dynamicClick() {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(DYNAMIC_CLICK_BTN));
        scrollToElement(button);
        wait.until(ExpectedConditions.elementToBeClickable(button));
        try {
            button.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
    }

    public boolean isDoubleClickMessageDisplayed() {
        return driver.findElement(DOUBLE_CLICK_MESSAGE).isDisplayed();
    }

    public boolean isRightClickMessageDisplayed() {
        return driver.findElement(RIGHT_CLICK_MESSAGE).isDisplayed();
    }

    public boolean isDynamicClickMessageDisplayed() {
        return driver.findElement(DYNAMIC_CLICK_MESSAGE).isDisplayed();
    }

    public String getDoubleClickMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(DOUBLE_CLICK_MESSAGE)).getText();
    }

    public String getRightClickMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(RIGHT_CLICK_MESSAGE)).getText();
    }

    public String getDynamicClickMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(DYNAMIC_CLICK_MESSAGE)).getText();
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
