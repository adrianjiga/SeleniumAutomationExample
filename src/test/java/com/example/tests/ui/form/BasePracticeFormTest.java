package com.example.tests.ui.form;

import com.example.tests.ui.BaseUITest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.BeforeMethod;

public class BasePracticeFormTest extends BaseUITest {

    @BeforeMethod
    public void navigateToPage() {
        driver.get(BASE_URL + "/automation-practice-form");
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("practice-form")));
    }

    protected void selectCountry(String countryName) {
        driver.findElement(By.cssSelector("#state .select-control")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("state-menu")));
        driver.findElements(By.cssSelector("#state-menu .select-option"))
                .stream()
                .filter(el -> el.getText().equals(countryName))
                .findFirst()
                .ifPresent(WebElement::click);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("state-display"), countryName));
    }

    protected void selectCity(String cityName) {
        driver.findElement(By.cssSelector("#city .select-control")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city-menu")));
        driver.findElements(By.cssSelector("#city-menu .select-option"))
                .stream()
                .filter(el -> el.getText().equals(cityName))
                .findFirst()
                .ifPresent(WebElement::click);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("city-display"), cityName));
    }
}
