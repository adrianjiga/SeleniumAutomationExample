package com.example.pages;

import io.qameta.allure.Step;
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

    private static final By FORM_CONTAINER      = By.cssSelector("[data-cy='practiceForm']");
    private static final By FIRST_NAME          = By.cssSelector("[data-cy='firstNameInput']");
    private static final By LAST_NAME           = By.cssSelector("[data-cy='lastNameInput']");
    private static final By USER_EMAIL          = By.cssSelector("[data-cy='emailInput']");
    private static final By USER_NUMBER         = By.cssSelector("[data-cy='mobileInput']");
    private static final By CURRENT_ADDRESS     = By.cssSelector("[data-cy='addressInput']");
    private static final By SUBMIT_BTN          = By.cssSelector("[data-cy='submitBtn']");
    private static final By DATE_OF_BIRTH_INPUT = By.cssSelector("[data-cy='dateOfBirthInput']");
    private static final By DATEPICKER_POPUP    = By.cssSelector("[data-cy='datepickerPopup']");
    private static final By DP_MONTH_SELECT     = By.cssSelector("[data-cy='monthSelect']");
    private static final By DP_YEAR_SELECT      = By.cssSelector("[data-cy='yearSelect']");
    private static final By COUNTRY_CONTROL     = By.cssSelector("[data-cy='stateControl']");
    private static final By COUNTRY_MENU        = By.cssSelector("[data-cy='stateMenu']");
    private static final By COUNTRY_OPTIONS     = By.cssSelector("[data-cy^='stateOption']");
    private static final By COUNTRY_DISPLAY     = By.cssSelector("[data-cy='stateDisplay']");
    private static final By CITY_CONTROL        = By.cssSelector("[data-cy='cityControl']");
    private static final By CITY_MENU           = By.cssSelector("[data-cy='cityMenu']");
    private static final By CITY_OPTIONS        = By.cssSelector("[data-cy^='cityOption']");
    private static final By CITY_DISPLAY        = By.cssSelector("[data-cy='cityDisplay']");
    private static final By SUCCESS_MODAL       = By.cssSelector("[data-cy='successModal']");
    private static final By MODAL_TITLE         = By.cssSelector("[data-cy='modalTitle']");
    private static final By CLOSE_MODAL_BTN     = By.cssSelector("[data-cy='closeModalBtn']");

    public PracticeFormPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * The gender and hobby hooks are named ({@code genderMale}) while the radios and
     * checkboxes are positional in the markup ({@code genderRadio1}). The 1-based index
     * stays in the method signature on purpose: the tests that use it are positional by
     * nature — {@code PracticeFormGenderTest} drives a {@code @DataProvider} of
     * "select radio N, assert radio M is deselected" — and naming those rows would obscure
     * the relationship rather than clarify it. The index-to-name mapping lives here instead.
     */
    private static final String[] GENDER_NAMES = {"Male", "Female", "Other"};
    private static final String[] HOBBY_NAMES = {"Sports", "Reading", "Music"};

    private static String genderName(int radioIndex) {
        return GENDER_NAMES[radioIndex - 1];
    }

    private static By genderLabel(int radioIndex) {
        return By.cssSelector("[data-cy='gender" + genderName(radioIndex) + "Label']");
    }

    private static By genderRadio(int radioIndex) {
        return By.cssSelector("[data-cy='gender" + genderName(radioIndex) + "']");
    }

    private static By hobbyLabel(String dataCyLabel) {
        return By.cssSelector("label[data-cy='" + dataCyLabel + "']");
    }

    private static By hobbyCheckbox(int index) {
        return By.cssSelector("[data-cy='hobby" + HOBBY_NAMES[index - 1] + "']");
    }

    private static By dayCell(int day) {
        return By.cssSelector("[data-cy='day" + String.format("%02d", day) + "']");
    }

    @Step("Navigate to Practice Form page")
    public PracticeFormPage navigate(String baseUrl) {
        driver.get(baseUrl + "/automation-practice-form");
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(FORM_CONTAINER));
        return this;
    }

    @Step("Set first name to '{value}'")
    public PracticeFormPage setFirstName(String value) {
        driver.findElement(FIRST_NAME).sendKeys(value);
        return this;
    }

    @Step("Set last name to '{value}'")
    public PracticeFormPage setLastName(String value) {
        driver.findElement(LAST_NAME).sendKeys(value);
        return this;
    }

    @Step("Set email to '{value}'")
    public PracticeFormPage setEmail(String value) {
        driver.findElement(USER_EMAIL).sendKeys(value);
        return this;
    }

    @Step("Set phone to '{value}'")
    public PracticeFormPage setPhone(String value) {
        driver.findElement(USER_NUMBER).sendKeys(value);
        return this;
    }

    @Step("Set address to '{value}'")
    public PracticeFormPage setAddress(String value) {
        driver.findElement(CURRENT_ADDRESS).sendKeys(value);
        return this;
    }

    @Step("Select gender radio index {radioIndex}")
    public PracticeFormPage selectGender(int radioIndex) {
        driver.findElement(genderLabel(radioIndex)).click();
        return this;
    }

    @Step("Check if gender radio index {radioIndex} is selected")
    public boolean isGenderSelected(int radioIndex) {
        return driver.findElement(genderRadio(radioIndex)).isSelected();
    }

    @Step("Check hobby '{dataCyLabel}'")
    public PracticeFormPage checkHobby(String dataCyLabel) {
        driver.findElement(hobbyLabel(dataCyLabel)).click();
        return this;
    }

    @Step("Check if hobby checkbox {checkboxIndex} is checked")
    public boolean isHobbyChecked(int checkboxIndex) {
        return driver.findElement(hobbyCheckbox(checkboxIndex)).isSelected();
    }

    @Step("Open date picker")
    public PracticeFormPage openDatePicker() {
        driver.findElement(DATE_OF_BIRTH_INPUT).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(DATEPICKER_POPUP));
        return this;
    }

    @Step("Check if date picker is visible")
    public boolean isDatePickerVisible() {
        return !driver.findElements(DATEPICKER_POPUP).isEmpty()
                && driver.findElement(DATEPICKER_POPUP).isDisplayed();
    }

    @Step("Select date: month {month}, year {year}, day {day}")
    public PracticeFormPage selectDate(int month, String year, int day) {
        new Select(driver.findElement(DP_MONTH_SELECT)).selectByValue(String.valueOf(month));
        new Select(driver.findElement(DP_YEAR_SELECT)).selectByVisibleText(year);
        wait.until(ExpectedConditions.presenceOfElementLocated(dayCell(day)));
        driver.findElement(dayCell(day)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(DATEPICKER_POPUP));
        return this;
    }

    @Step("Get date of birth input value")
    public String getDateValue() {
        return driver.findElement(DATE_OF_BIRTH_INPUT).getAttribute("value");
    }

    @Step("Select country '{countryName}'")
    public PracticeFormPage selectCountry(String countryName) {
        driver.findElement(COUNTRY_CONTROL).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(COUNTRY_MENU));
        driver.findElements(COUNTRY_OPTIONS)
                .stream()
                .filter(el -> el.getText().equals(countryName))
                .findFirst()
                .ifPresent(WebElement::click);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(COUNTRY_DISPLAY, countryName));
        return this;
    }

    @Step("Select city '{cityName}'")
    public PracticeFormPage selectCity(String cityName) {
        driver.findElement(CITY_CONTROL).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(CITY_MENU));
        driver.findElements(CITY_OPTIONS)
                .stream()
                .filter(el -> el.getText().equals(cityName))
                .findFirst()
                .ifPresent(WebElement::click);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(CITY_DISPLAY, cityName));
        return this;
    }

    @Step("Get selected country display text")
    public String getCountryDisplay() {
        return driver.findElement(COUNTRY_DISPLAY).getText();
    }

    @Step("Get selected city display text")
    public String getCityDisplay() {
        return driver.findElement(CITY_DISPLAY).getText();
    }

    @Step("Open city dropdown")
    public PracticeFormPage openCityDropdown() {
        driver.findElement(CITY_CONTROL).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(CITY_MENU));
        return this;
    }

    @Step("Get available city options")
    public List<String> getCityOptions() {
        return driver.findElements(CITY_OPTIONS).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    @Step("Submit the practice form")
    public PracticeFormPage submit() {
        driver.findElement(SUBMIT_BTN).click();
        return this;
    }

    @Step("Check if success modal is visible")
    public boolean isSuccessModalVisible() {
        List<WebElement> modals = driver.findElements(SUCCESS_MODAL);
        return !modals.isEmpty() && modals.get(0).isDisplayed();
    }

    @Step("Get success modal title text")
    public String getSuccessModalTitleText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(MODAL_TITLE)).getText();
    }

    @Step("Close the success modal")
    public PracticeFormPage closeSuccessModal() {
        driver.findElement(CLOSE_MODAL_BTN).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(SUCCESS_MODAL));
        return this;
    }

    /**
     * Reads an input's current value by its {@code data-cy} hook, e.g. {@code "first-name-input"}.
     */
    @Step("Get field value for '{dataCy}'")
    public String getFieldValue(String dataCy) {
        return driver.findElement(By.cssSelector("[data-cy='" + dataCy + "']")).getAttribute("value");
    }
}
