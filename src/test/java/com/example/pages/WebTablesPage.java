package com.example.pages;

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

    private static final By SEARCH_BOX       = By.id("searchBox");
    private static final By TABLE_ROWS       = By.cssSelector("[data-cy^='table-row-']");
    private static final By ADD_RECORD_BTN   = By.id("addNewRecordButton");
    private static final By MODAL_FIRST_NAME = By.cssSelector("[data-cy='modal-first-name']");
    private static final By MODAL_LAST_NAME  = By.cssSelector("[data-cy='modal-last-name']");
    private static final By MODAL_EMAIL      = By.cssSelector("[data-cy='modal-email']");
    private static final By MODAL_AGE        = By.cssSelector("[data-cy='modal-age']");
    private static final By MODAL_SALARY     = By.cssSelector("[data-cy='modal-salary']");
    private static final By MODAL_DEPARTMENT = By.cssSelector("[data-cy='modal-department']");
    private static final By MODAL_SUBMIT_BTN = By.cssSelector("[data-cy='modal-submit-btn']");
    private static final By MODAL_CANCEL_BTN = By.cssSelector("[data-cy='modal-cancel-btn']");
    private static final By MODAL_OVERLAY    = By.cssSelector("[data-cy='modal-overlay']");
    private static final By CURRENT_PAGE     = By.id("current-page-num");
    private static final By TOTAL_PAGES      = By.id("total-pages-display");
    private static final By ROWS_PER_PAGE_SEL = By.id("rows-per-page-sel");

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

    public void navigate(String baseUrl) {
        driver.get(baseUrl + "/webtables");
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-cy='table-row-1']")));
    }

    public void search(String term) {
        driver.findElement(SEARCH_BOX).sendKeys(term);
    }

    public void clearSearch() {
        WebElement searchBox = driver.findElement(SEARCH_BOX);
        searchBox.clear();
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));", searchBox);
    }

    public int getRowCount() {
        return driver.findElements(TABLE_ROWS).size();
    }

    public String getCellText(String field, int row) {
        return driver.findElement(cellLocator(field, row)).getText();
    }

    public void clickAddRecord() {
        driver.findElement(ADD_RECORD_BTN).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(MODAL_FIRST_NAME));
    }

    public void fillModal(String firstName, String lastName, String email,
                          String age, String salary, String department) {
        driver.findElement(MODAL_FIRST_NAME).sendKeys(firstName);
        driver.findElement(MODAL_LAST_NAME).sendKeys(lastName);
        driver.findElement(MODAL_EMAIL).sendKeys(email);
        driver.findElement(MODAL_AGE).sendKeys(age);
        driver.findElement(MODAL_SALARY).sendKeys(salary);
        driver.findElement(MODAL_DEPARTMENT).sendKeys(department);
    }

    public void submitModal() {
        driver.findElement(MODAL_SUBMIT_BTN).click();
    }

    public void cancelModal() {
        driver.findElement(MODAL_CANCEL_BTN).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(MODAL_OVERLAY));
    }

    public void waitForModalToClose() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(MODAL_OVERLAY));
    }

    public boolean isModalVisible() {
        return !driver.findElements(MODAL_OVERLAY).isEmpty()
                && driver.findElement(MODAL_OVERLAY).isDisplayed();
    }

    public void clickEdit(int row) {
        driver.findElement(editBtnLocator(row)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(MODAL_FIRST_NAME));
    }

    public void clickDelete(int row) {
        driver.findElement(deleteBtnLocator(row)).click();
    }

    public void clearAndTypeModalField(String dataCy, String value) {
        WebElement field = driver.findElement(By.cssSelector("[data-cy='" + dataCy + "']"));
        field.clear();
        field.sendKeys(value);
    }

    public String getModalFieldValue(String dataCy) {
        return driver.findElement(By.cssSelector("[data-cy='" + dataCy + "']")).getAttribute("value");
    }

    public String getCurrentPage() {
        return driver.findElement(CURRENT_PAGE).getText();
    }

    public String getTotalPages() {
        return driver.findElement(TOTAL_PAGES).getText();
    }

    public void setRowsPerPage(int rows) {
        new Select(driver.findElement(ROWS_PER_PAGE_SEL)).selectByValue(String.valueOf(rows));
    }

    public void waitForRowCount(int n) {
        wait.until(ExpectedConditions.numberOfElementsToBe(TABLE_ROWS, n));
    }

    public void waitForTotalPages(String text) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(TOTAL_PAGES, text));
    }
}
