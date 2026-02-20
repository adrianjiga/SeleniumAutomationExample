package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

public class PracticeFormPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final By FORM_CONTAINER      = By.id("practice-form");
    private static final By FIRST_NAME          = By.id("firstName");
    private static final By LAST_NAME           = By.id("lastName");
    private static final By USER_EMAIL          = By.id("userEmail");
    private static final By USER_NUMBER         = By.id("userNumber");
    private static final By CURRENT_ADDRESS     = By.id("currentAddress");
    private static final By SUBMIT_BTN          = By.id("submit");
    private static final By DATE_OF_BIRTH_INPUT = By.id("dateOfBirthInput");
    private static final By DATEPICKER_POPUP    = By.id("datepicker-popup");
    private static final By DP_MONTH_SELECT     = By.id("dp-month");
    private static final By DP_YEAR_SELECT      = By.id("dp-year");
    private static final By COUNTRY_CONTROL     = By.cssSelector("#state .select-control");
    private static final By COUNTRY_MENU        = By.id("state-menu");
    private static final By COUNTRY_OPTIONS     = By.cssSelector("#state-menu .select-option");
    private static final By COUNTRY_DISPLAY     = By.id("state-display");
    private static final By CITY_CONTROL        = By.cssSelector("#city .select-control");
    private static final By CITY_MENU           = By.id("city-menu");
    private static final By CITY_OPTIONS        = By.cssSelector("#city-menu .select-option");
    private static final By CITY_DISPLAY        = By.id("city-display");
    private static final By SUCCESS_MODAL       = By.id("success-modal");
    private static final By MODAL_TITLE         = By.cssSelector("[data-cy='modal-title']");
    private static final By CLOSE_MODAL_BTN     = By.cssSelector("[data-cy='close-modal-btn']");

    public PracticeFormPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    private static By genderLabel(int radioIndex) {
        return By.cssSelector("label[for='gender-radio-" + radioIndex + "']");
    }

    private static By genderRadio(int radioIndex) {
        return By.id("gender-radio-" + radioIndex);
    }

    private static By hobbyLabel(String dataCyLabel) {
        return By.cssSelector("label[data-cy='" + dataCyLabel + "']");
    }

    private static By hobbyCheckbox(int index) {
        return By.id("hobbies-checkbox-" + index);
    }

    private static By dayCell(int day) {
        return By.cssSelector("[data-cy='day-" + day + "']");
    }

    public void navigate(String baseUrl) {
        driver.get(baseUrl + "/automation-practice-form");
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(FORM_CONTAINER));
    }

    public void setFirstName(String value) {
        driver.findElement(FIRST_NAME).sendKeys(value);
    }

    public void setLastName(String value) {
        driver.findElement(LAST_NAME).sendKeys(value);
    }

    public void setEmail(String value) {
        driver.findElement(USER_EMAIL).sendKeys(value);
    }

    public void setPhone(String value) {
        driver.findElement(USER_NUMBER).sendKeys(value);
    }

    public void setAddress(String value) {
        driver.findElement(CURRENT_ADDRESS).sendKeys(value);
    }

    public void selectGender(int radioIndex) {
        driver.findElement(genderLabel(radioIndex)).click();
    }

    public boolean isGenderSelected(int radioIndex) {
        return driver.findElement(genderRadio(radioIndex)).isSelected();
    }

    public void checkHobby(String dataCyLabel) {
        driver.findElement(hobbyLabel(dataCyLabel)).click();
    }

    public boolean isHobbyChecked(int checkboxIndex) {
        return driver.findElement(hobbyCheckbox(checkboxIndex)).isSelected();
    }

    public void openDatePicker() {
        driver.findElement(DATE_OF_BIRTH_INPUT).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(DATEPICKER_POPUP));
    }

    public boolean isDatePickerVisible() {
        return !driver.findElements(DATEPICKER_POPUP).isEmpty()
                && driver.findElement(DATEPICKER_POPUP).isDisplayed();
    }

    public void selectDate(int month, String year, int day) {
        new Select(driver.findElement(DP_MONTH_SELECT)).selectByValue(String.valueOf(month));
        new Select(driver.findElement(DP_YEAR_SELECT)).selectByVisibleText(year);
        wait.until(ExpectedConditions.presenceOfElementLocated(dayCell(day)));
        driver.findElement(dayCell(day)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(DATEPICKER_POPUP));
    }

    public String getDateValue() {
        return driver.findElement(DATE_OF_BIRTH_INPUT).getAttribute("value");
    }

    public void selectCountry(String countryName) {
        driver.findElement(COUNTRY_CONTROL).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(COUNTRY_MENU));
        driver.findElements(COUNTRY_OPTIONS)
                .stream()
                .filter(el -> el.getText().equals(countryName))
                .findFirst()
                .ifPresent(WebElement::click);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(COUNTRY_DISPLAY, countryName));
    }

    public void selectCity(String cityName) {
        driver.findElement(CITY_CONTROL).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(CITY_MENU));
        driver.findElements(CITY_OPTIONS)
                .stream()
                .filter(el -> el.getText().equals(cityName))
                .findFirst()
                .ifPresent(WebElement::click);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(CITY_DISPLAY, cityName));
    }

    public String getCountryDisplay() {
        return driver.findElement(COUNTRY_DISPLAY).getText();
    }

    public String getCityDisplay() {
        return driver.findElement(CITY_DISPLAY).getText();
    }

    public void openCityDropdown() {
        driver.findElement(CITY_CONTROL).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(CITY_MENU));
    }

    public List<String> getCityOptions() {
        return driver.findElements(CITY_OPTIONS).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public void submit() {
        driver.findElement(SUBMIT_BTN).click();
    }

    public boolean isSuccessModalVisible() {
        List<WebElement> modals = driver.findElements(SUCCESS_MODAL);
        return !modals.isEmpty() && modals.get(0).isDisplayed();
    }

    public String getSuccessModalTitleText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(MODAL_TITLE)).getText();
    }

    public void closeSuccessModal() {
        driver.findElement(CLOSE_MODAL_BTN).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(SUCCESS_MODAL));
    }

    public String getFieldValue(String id) {
        return driver.findElement(By.id(id)).getAttribute("value");
    }
}
