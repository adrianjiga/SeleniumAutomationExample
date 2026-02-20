package com.example.tests.ui.form;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PracticeFormDatePickerTest extends BasePracticeFormTest {

    @Test(description = "Should open date picker popup when input is clicked")
    public void testDatePickerOpens() {
        driver.findElement(By.id("dateOfBirthInput")).click();
        WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("datepicker-popup")));
        Assert.assertTrue(popup.isDisplayed(), "Date picker popup should be visible");
    }

    @Test(description = "Should select a date from the date picker")
    public void testSelectDateFromPicker() {
        driver.findElement(By.id("dateOfBirthInput")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("datepicker-popup")));

        new Select(driver.findElement(By.id("dp-month"))).selectByValue("0");
        new Select(driver.findElement(By.id("dp-year"))).selectByVisibleText("1990");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-cy='day-15']")));
        driver.findElement(By.cssSelector("[data-cy='day-15']")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("datepicker-popup")));

        String selectedDate = driver.findElement(By.id("dateOfBirthInput")).getAttribute("value");
        Assert.assertNotNull(selectedDate);
        Assert.assertFalse(selectedDate.isEmpty(), "Date input should have a value after selection");
        Assert.assertTrue(selectedDate.contains("1990"), "Selected date should contain the year 1990. Got: " + selectedDate);
    }
}
