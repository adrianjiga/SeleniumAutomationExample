package com.example.tests.ui.form;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PracticeFormSubmissionTest extends BasePracticeFormTest {

    @Test(description = "Should submit form and show success modal with all fields")
    public void testFullFormSubmission() {
        driver.findElement(By.id("firstName")).sendKeys("John");
        driver.findElement(By.id("lastName")).sendKeys("Doe");
        driver.findElement(By.id("userEmail")).sendKeys("john.doe@example.com");
        driver.findElement(By.cssSelector("label[for='gender-radio-1']")).click();
        driver.findElement(By.id("userNumber")).sendKeys("1234567890");
        driver.findElement(By.id("currentAddress")).sendKeys("123 Main Street");
        selectCountry("Germany");
        selectCity("Berlin");
        driver.findElement(By.id("submit")).click();

        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-modal")));
        Assert.assertTrue(modal.isDisplayed(), "Success modal should be visible after submit");
        Assert.assertTrue(
                driver.findElement(By.cssSelector("[data-cy='modal-title']"))
                        .getText().contains("Thanks for submitting the form"),
                "Modal title should confirm submission");
    }

    @Test(description = "Should close success modal when Close button is clicked")
    public void testSuccessModalClose() {
        fillMinimalForm();
        driver.findElement(By.id("submit")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-modal")));

        driver.findElement(By.cssSelector("[data-cy='close-modal-btn']")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("success-modal")));

        Assert.assertFalse(driver.findElement(By.id("success-modal")).isDisplayed(),
                "Success modal should be hidden after closing");
    }

    @Test(description = "Should accept input in all text fields")
    public void testTextFieldsAcceptInput() {
        driver.findElement(By.id("firstName")).sendKeys("Alice");
        driver.findElement(By.id("lastName")).sendKeys("Wonderland");
        driver.findElement(By.id("userEmail")).sendKeys("alice@example.com");
        driver.findElement(By.id("userNumber")).sendKeys("9876543210");
        driver.findElement(By.id("currentAddress")).sendKeys("42 Fantasy Lane");

        Assert.assertEquals(driver.findElement(By.id("firstName")).getAttribute("value"), "Alice");
        Assert.assertEquals(driver.findElement(By.id("lastName")).getAttribute("value"), "Wonderland");
        Assert.assertEquals(driver.findElement(By.id("userEmail")).getAttribute("value"), "alice@example.com");
        Assert.assertEquals(driver.findElement(By.id("userNumber")).getAttribute("value"), "9876543210");
        Assert.assertEquals(driver.findElement(By.id("currentAddress")).getAttribute("value"), "42 Fantasy Lane");
    }

    private void fillMinimalForm() {
        driver.findElement(By.id("firstName")).sendKeys("Test");
        driver.findElement(By.id("lastName")).sendKeys("User");
        driver.findElement(By.id("userNumber")).sendKeys("1234567890");
        driver.findElement(By.cssSelector("label[for='gender-radio-1']")).click();
    }
}
