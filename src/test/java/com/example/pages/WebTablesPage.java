package com.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebTablesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final By SEARCH_BOX        = By.id("searchBox");
    private static final By TABLE_ROWS        = By.cssSelector("[data-cy^='table-row-']");
    private static final By ADD_RECORD_BTN    = By.id("addNewRecordButton");
    private static final By MODAL_FIRST_NAME  = By.cssSelector("[data-cy='modal-first-name']");
    private static final By MODAL_LAST_NAME   = By.cssSelector("[data-cy='modal-last-name']");
    private static final By MODAL_EMAIL       = By.cssSelector("[data-cy='modal-email']");
    private static final By MODAL_AGE         = By.cssSelector("[data-cy='modal-age']");
    private static final By MODAL_SALARY      = By.cssSelector("[data-cy='modal-salary']");
    private static final By MODAL_DEPARTMENT  = By.cssSelector("[data-cy='modal-department']");
    private static final By MODAL_SUBMIT_BTN  = By.cssSelector("[data-cy='modal-submit-btn']");
    private static final By MODAL_CANCEL_BTN  = By.cssSelector("[data-cy='modal-cancel-btn']");
    private static final By MODAL_OVERLAY     = By.cssSelector("[data-cy='modal-overlay']");
    private static final By CURRENT_PAGE      = By.id("current-page-num");
    private static final By TOTAL_PAGES       = By.id("total-pages-display");
    private static final By ROWS_PER_PAGE_SEL = By.id("rows-per-page-sel");
    private static final By NEXT_PAGE_BTN     = By.cssSelector("[data-cy='next-page-btn']");
    private static final By PREV_PAGE_BTN     = By.cssSelector("[data-cy='prev-page-btn']");

    public WebTablesPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    private static By cellLocator(String field, int row) {
        return By.cssSelector("[data-cy='cell-" + field + "-" + row + "']");
    }

    private static By deleteBtnLocator(int row) {
        return By.cssSelector("[data-cy='delete-btn-" + row + "']");
    }

    private static By editBtnLocator(int row) {
        return By.cssSelector("[data-cy='edit-btn-" + row + "']");
    }

    @Step("Navigate to Web Tables page")
    public WebTablesPage navigate(String baseUrl) {
        driver.get(baseUrl + "/webtables");
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-cy='table-row-1']")));
        return this;
    }

    @Step("Search for '{term}'")
    public WebTablesPage search(String term) {
        driver.findElement(SEARCH_BOX).sendKeys(term);
        return this;
    }

    @Step("Clear the search box")
    public WebTablesPage clearSearch() {
        WebElement searchBox = driver.findElement(SEARCH_BOX);
        searchBox.clear();
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));", searchBox);
        return this;
    }

    @Step("Get visible row count")
    public int getRowCount() {
        return driver.findElements(TABLE_ROWS).size();
    }

    @Step("Get cell text for field '{field}' at row {row}")
    public String getCellText(String field, int row) {
        return driver.findElement(cellLocator(field, row)).getText();
    }

    @Step("Click Add Record button")
    public WebTablesPage clickAddRecord() {
        driver.findElement(ADD_RECORD_BTN).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(MODAL_FIRST_NAME));
        return this;
    }

    @Step("Fill modal with: {firstName} {lastName}, {email}, age {age}, salary {salary}, dept {department}")
    public WebTablesPage fillModal(String firstName, String lastName, String email,
                                   String age, String salary, String department) {
        driver.findElement(MODAL_FIRST_NAME).sendKeys(firstName);
        driver.findElement(MODAL_LAST_NAME).sendKeys(lastName);
        driver.findElement(MODAL_EMAIL).sendKeys(email);
        driver.findElement(MODAL_AGE).sendKeys(age);
        driver.findElement(MODAL_SALARY).sendKeys(salary);
        driver.findElement(MODAL_DEPARTMENT).sendKeys(department);
        return this;
    }

    @Step("Submit the modal form")
    public WebTablesPage submitModal() {
        driver.findElement(MODAL_SUBMIT_BTN).click();
        return this;
    }

    @Step("Cancel and close the modal")
    public WebTablesPage cancelModal() {
        driver.findElement(MODAL_CANCEL_BTN).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(MODAL_OVERLAY));
        return this;
    }

    @Step("Wait for modal to close")
    public WebTablesPage waitForModalToClose() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(MODAL_OVERLAY));
        return this;
    }

    @Step("Check if modal is visible")
    public boolean isModalVisible() {
        return !driver.findElements(MODAL_OVERLAY).isEmpty()
                && driver.findElement(MODAL_OVERLAY).isDisplayed();
    }

    @Step("Click Edit button for row {row}")
    public WebTablesPage clickEdit(int row) {
        driver.findElement(editBtnLocator(row)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(MODAL_FIRST_NAME));
        return this;
    }

    @Step("Click Delete button for row {row}")
    public WebTablesPage clickDelete(int row) {
        driver.findElement(deleteBtnLocator(row)).click();
        return this;
    }

    @Step("Clear and type '{value}' in modal field '{dataCy}'")
    public WebTablesPage clearAndTypeModalField(String dataCy, String value) {
        WebElement field = driver.findElement(By.cssSelector("[data-cy='" + dataCy + "']"));
        field.clear();
        field.sendKeys(value);
        return this;
    }

    @Step("Get modal field value for '{dataCy}'")
    public String getModalFieldValue(String dataCy) {
        return driver.findElement(By.cssSelector("[data-cy='" + dataCy + "']")).getAttribute("value");
    }

    @Step("Get current page number")
    public String getCurrentPage() {
        return driver.findElement(CURRENT_PAGE).getText();
    }

    @Step("Get total pages")
    public String getTotalPages() {
        return driver.findElement(TOTAL_PAGES).getText();
    }

    @Step("Set rows per page to {rows}")
    public WebTablesPage setRowsPerPage(int rows) {
        new Select(driver.findElement(ROWS_PER_PAGE_SEL)).selectByValue(String.valueOf(rows));
        return this;
    }

    @Step("Click the Next Page button")
    public WebTablesPage clickNextPage() {
        driver.findElement(NEXT_PAGE_BTN).click();
        return this;
    }

    @Step("Click the Previous Page button")
    public WebTablesPage clickPrevPage() {
        driver.findElement(PREV_PAGE_BTN).click();
        return this;
    }

    @Step("Wait for row count to be {n}")
    public WebTablesPage waitForRowCount(int n) {
        wait.until(ExpectedConditions.numberOfElementsToBe(TABLE_ROWS, n));
        return this;
    }

    @Step("Wait for total pages to show '{text}'")
    public WebTablesPage waitForTotalPages(String text) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(TOTAL_PAGES, text));
        return this;
    }
}
