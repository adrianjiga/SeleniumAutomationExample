package com.example.tests.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PracticeFormTest extends BaseUITest {

    @BeforeMethod
    public void navigateToPage() {
        driver.get(BASE_URL + "/automation-practice-form");
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("practice-form")));
    }

    // ── Full form submission ───────────────────────────────────────────────────

    @Test(description = "Should submit form and show success modal with all fields")
    public void testFullFormSubmission() {
        driver.findElement(By.id("firstName")).sendKeys("John");
        driver.findElement(By.id("lastName")).sendKeys("Doe");
        driver.findElement(By.id("userEmail")).sendKeys("john.doe@example.com");

        // Select Male gender via label click
        driver.findElement(By.cssSelector("label[for='gender-radio-1']")).click();

        driver.findElement(By.id("userNumber")).sendKeys("1234567890");
        driver.findElement(By.id("currentAddress")).sendKeys("123 Main Street");

        // Select country: Germany
        selectCountry("Germany");

        // Select city: Berlin
        selectCity("Berlin");

        driver.findElement(By.id("submit")).click();

        WebElement modal = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("success-modal")));

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

    // ── Gender radio buttons ──────────────────────────────────────────────────

    @Test(description = "Should select Male gender radio button")
    public void testSelectMaleGender() {
        driver.findElement(By.cssSelector("label[for='gender-radio-1']")).click();

        Assert.assertTrue(driver.findElement(By.id("gender-radio-1")).isSelected(),
                "Male radio should be selected");
        Assert.assertFalse(driver.findElement(By.id("gender-radio-2")).isSelected(),
                "Female radio should not be selected");
    }

    @Test(description = "Should select Female gender radio button")
    public void testSelectFemaleGender() {
        driver.findElement(By.cssSelector("label[for='gender-radio-2']")).click();

        Assert.assertTrue(driver.findElement(By.id("gender-radio-2")).isSelected(),
                "Female radio should be selected");
        Assert.assertFalse(driver.findElement(By.id("gender-radio-1")).isSelected(),
                "Male radio should not be selected");
    }

    @Test(description = "Should switch gender selection when a different radio is clicked")
    public void testGenderRadioSwitching() {
        driver.findElement(By.cssSelector("label[for='gender-radio-1']")).click();
        Assert.assertTrue(driver.findElement(By.id("gender-radio-1")).isSelected());

        driver.findElement(By.cssSelector("label[for='gender-radio-3']")).click();
        Assert.assertTrue(driver.findElement(By.id("gender-radio-3")).isSelected());
        Assert.assertFalse(driver.findElement(By.id("gender-radio-1")).isSelected());
    }

    // ── Hobby checkboxes ──────────────────────────────────────────────────────

    @Test(description = "Should check Sports hobby checkbox")
    public void testCheckSportsHobby() {
        driver.findElement(By.cssSelector("label[data-cy='hobby-sports-label']")).click();
        Assert.assertTrue(driver.findElement(By.id("hobbies-checkbox-1")).isSelected(),
                "Sports checkbox should be checked");
    }

    @Test(description = "Should check multiple hobby checkboxes independently")
    public void testCheckMultipleHobbies() {
        driver.findElement(By.cssSelector("label[data-cy='hobby-sports-label']")).click();
        driver.findElement(By.cssSelector("label[data-cy='hobby-reading-label']")).click();

        Assert.assertTrue(driver.findElement(By.id("hobbies-checkbox-1")).isSelected(),
                "Sports should be checked");
        Assert.assertTrue(driver.findElement(By.id("hobbies-checkbox-2")).isSelected(),
                "Reading should be checked");
        Assert.assertFalse(driver.findElement(By.id("hobbies-checkbox-3")).isSelected(),
                "Music should remain unchecked");
    }

    @Test(description = "Should uncheck a previously checked hobby")
    public void testUncheckHobby() {
        driver.findElement(By.cssSelector("label[data-cy='hobby-reading-label']")).click();
        Assert.assertTrue(driver.findElement(By.id("hobbies-checkbox-2")).isSelected());

        driver.findElement(By.cssSelector("label[data-cy='hobby-reading-label']")).click();
        Assert.assertFalse(driver.findElement(By.id("hobbies-checkbox-2")).isSelected(),
                "Reading should be unchecked after clicking again");
    }

    // ── Date picker ───────────────────────────────────────────────────────────

    @Test(description = "Should open date picker popup when input is clicked")
    public void testDatePickerOpens() {
        driver.findElement(By.id("dateOfBirthInput")).click();

        WebElement popup = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("datepicker-popup")));

        Assert.assertTrue(popup.isDisplayed(), "Date picker popup should be visible");
    }

    @Test(description = "Should select a date from the date picker")
    public void testSelectDateFromPicker() {
        driver.findElement(By.id("dateOfBirthInput")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("datepicker-popup")));

        // Select January 1990
        new Select(driver.findElement(By.id("dp-month"))).selectByValue("0");
        new Select(driver.findElement(By.id("dp-year"))).selectByVisibleText("1990");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-cy='day-15']")));
        driver.findElement(By.cssSelector("[data-cy='day-15']")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("datepicker-popup")));

        String selectedDate = driver.findElement(By.id("dateOfBirthInput")).getAttribute("value");
        Assert.assertNotNull(selectedDate);
        Assert.assertFalse(selectedDate.isEmpty(), "Date input should have a value after selection");
        Assert.assertTrue(selectedDate.contains("1990"),
                "Selected date should contain the year 1990. Got: " + selectedDate);
    }

    // ── Country / City cascade ────────────────────────────────────────────────

    @Test(description = "Should populate city dropdown after selecting a country")
    public void testCitiesLoadAfterCountrySelection() {
        selectCountry("Germany");

        // Open the city dropdown so the menu becomes visible — getText() returns "" on hidden elements
        driver.findElement(By.cssSelector("#city .select-control")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city-menu")));

        boolean berlinVisible = driver.findElements(By.cssSelector("#city-menu .select-option"))
                .stream()
                .anyMatch(el -> el.getText().equals("Berlin"));
        Assert.assertTrue(berlinVisible, "Berlin should be available in city dropdown after selecting Germany");
    }

    @Test(description = "Should select a city after selecting a country")
    public void testSelectCountryAndCity() {
        selectCountry("France");
        selectCity("Paris");

        String cityDisplay = driver.findElement(By.id("city-display")).getText();
        Assert.assertEquals(cityDisplay, "Paris", "City display should show selected city");
    }

    // ── Text fields ───────────────────────────────────────────────────────────

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

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void fillMinimalForm() {
        driver.findElement(By.id("firstName")).sendKeys("Test");
        driver.findElement(By.id("lastName")).sendKeys("User");
        driver.findElement(By.id("userNumber")).sendKeys("1234567890");
        driver.findElement(By.cssSelector("label[for='gender-radio-1']")).click();
    }

    private void selectCountry(String countryName) {
        // Open the custom country dropdown
        driver.findElement(By.cssSelector("#state .select-control")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("state-menu")));

        // Click the matching option by text
        driver.findElements(By.cssSelector("#state-menu .select-option"))
                .stream()
                .filter(el -> el.getText().equals(countryName))
                .findFirst()
                .ifPresent(WebElement::click);

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.id("state-display"), countryName));
    }

    private void selectCity(String cityName) {
        // Open city dropdown (populated after country is chosen)
        driver.findElement(By.cssSelector("#city .select-control")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("city-menu")));

        driver.findElements(By.cssSelector("#city-menu .select-option"))
                .stream()
                .filter(el -> el.getText().equals(cityName))
                .findFirst()
                .ifPresent(WebElement::click);

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.id("city-display"), cityName));
    }
}
