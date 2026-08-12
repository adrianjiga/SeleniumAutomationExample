package com.example.pages;

import io.qameta.allure.Step;
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

    private static final By DOUBLE_CLICK_BTN      = By.cssSelector("[data-cy='doubleClickBtn']");
    private static final By RIGHT_CLICK_BTN       = By.cssSelector("[data-cy='rightClickBtn']");
    private static final By DYNAMIC_CLICK_BTN     = By.cssSelector("[data-cy='dynamicClickBtn']");
    private static final By DOUBLE_CLICK_MESSAGE  = By.cssSelector("[data-cy='doubleClickMessage']");
    private static final By RIGHT_CLICK_MESSAGE   = By.cssSelector("[data-cy='rightClickMessage']");
    private static final By DYNAMIC_CLICK_MESSAGE = By.cssSelector("[data-cy='dynamicClickMessage']");

    public ButtonsPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    @Step("Navigate to Buttons page")
    public ButtonsPage navigate(String baseUrl) {
        driver.get(baseUrl + "/buttons");
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
        return this;
    }

    @Step("Double-click the Double Click button")
    public ButtonsPage doubleClick() {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(DOUBLE_CLICK_BTN));
        scrollToElement(button);
        wait.until(ExpectedConditions.elementToBeClickable(button));
        new Actions(driver).doubleClick(button).perform();
        return this;
    }

    @Step("Right-click the Right Click button")
    public ButtonsPage rightClick() {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(RIGHT_CLICK_BTN));
        scrollToElement(button);
        wait.until(ExpectedConditions.elementToBeClickable(button));
        new Actions(driver).contextClick(button).perform();
        return this;
    }

    @Step("Click the Dynamic Click button")
    public ButtonsPage dynamicClick() {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(DYNAMIC_CLICK_BTN));
        scrollToElement(button);
        wait.until(ExpectedConditions.elementToBeClickable(button));
        try {
            button.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
        return this;
    }

    @Step("Check if double-click message is displayed")
    public boolean isDoubleClickMessageDisplayed() {
        return driver.findElement(DOUBLE_CLICK_MESSAGE).isDisplayed();
    }

    @Step("Check if right-click message is displayed")
    public boolean isRightClickMessageDisplayed() {
        return driver.findElement(RIGHT_CLICK_MESSAGE).isDisplayed();
    }

    @Step("Check if dynamic-click message is displayed")
    public boolean isDynamicClickMessageDisplayed() {
        return driver.findElement(DYNAMIC_CLICK_MESSAGE).isDisplayed();
    }

    @Step("Get double-click message text")
    public String getDoubleClickMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(DOUBLE_CLICK_MESSAGE)).getText();
    }

    @Step("Get right-click message text")
    public String getRightClickMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(RIGHT_CLICK_MESSAGE)).getText();
    }

    @Step("Get dynamic-click message text")
    public String getDynamicClickMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(DYNAMIC_CLICK_MESSAGE)).getText();
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }
}
