package com.example.tests.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class WebTablesTest extends BaseUITest {

    @BeforeMethod
    public void navigateToPage() {
        driver.get(BASE_URL + "/webtables");
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
        // Wait for default records to be rendered by JS
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-cy='table-row-1']")));
    }

    // ── Default data ──────────────────────────────────────────────────────────

    @Test(description = "Should display three default records on page load")
    public void testDefaultRecordsAreDisplayed() {
        List<WebElement> rows = driver.findElements(By.cssSelector("[data-cy^='table-row-']"));
        Assert.assertEquals(rows.size(), 3, "Table should show 3 default records");
    }

    @Test(description = "Should display Cierra Vega as the first default record")
    public void testFirstDefaultRecord() {
        Assert.assertEquals(
                driver.findElement(By.cssSelector("[data-cy='cell-first-name-1']")).getText(), "Cierra");
        Assert.assertEquals(
                driver.findElement(By.cssSelector("[data-cy='cell-last-name-1']")).getText(), "Vega");
        Assert.assertEquals(
                driver.findElement(By.cssSelector("[data-cy='cell-age-1']")).getText(), "39");
        Assert.assertEquals(
                driver.findElement(By.cssSelector("[data-cy='cell-department-1']")).getText(), "Insurance");
    }

    @Test(description = "Should display all default department values correctly")
    public void testDefaultDepartments() {
        Assert.assertEquals(
                driver.findElement(By.cssSelector("[data-cy='cell-department-1']")).getText(), "Insurance");
        Assert.assertEquals(
                driver.findElement(By.cssSelector("[data-cy='cell-department-2']")).getText(), "Compliance");
        Assert.assertEquals(
                driver.findElement(By.cssSelector("[data-cy='cell-department-3']")).getText(), "Legal");
    }

    // ── Search ────────────────────────────────────────────────────────────────

    @Test(description = "Should filter records by first name via search box")
    public void testSearchByFirstName() {
        WebElement searchBox = driver.findElement(By.id("searchBox"));
        searchBox.sendKeys("Cierra");

        wait.until(ExpectedConditions.numberOfElementsToBe(
                By.cssSelector("[data-cy^='table-row-']"), 1));

        Assert.assertEquals(
                driver.findElement(By.cssSelector("[data-cy='cell-first-name-1']")).getText(), "Cierra");
    }

    @Test(description = "Should filter records by department via search box")
    public void testSearchByDepartment() {
        driver.findElement(By.id("searchBox")).sendKeys("Legal");

        wait.until(ExpectedConditions.numberOfElementsToBe(
                By.cssSelector("[data-cy^='table-row-']"), 1));

        Assert.assertEquals(
                driver.findElement(By.cssSelector("[data-cy='cell-department-1']")).getText(), "Legal");
    }

    @Test(description = "Should show no rows when search matches nothing")
    public void testSearchWithNoResults() {
        driver.findElement(By.id("searchBox")).sendKeys("zzz_no_match_xyz");

        wait.until(ExpectedConditions.numberOfElementsToBe(
                By.cssSelector("[data-cy^='table-row-']"), 0));

        Assert.assertTrue(
                driver.findElements(By.cssSelector("[data-cy^='table-row-']")).isEmpty(),
                "No rows should be shown for unmatched search");
    }

    @Test(description = "Should restore all records when search is cleared")
    public void testClearSearchRestoresAllRows() {
        WebElement searchBox = driver.findElement(By.id("searchBox"));
        searchBox.sendKeys("Alden");

        wait.until(ExpectedConditions.numberOfElementsToBe(
                By.cssSelector("[data-cy^='table-row-']"), 1));

        searchBox.clear();
        // clear() doesn't fire the 'input' event that the JS filter listens to — dispatch it manually
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));", searchBox);

        wait.until(ExpectedConditions.numberOfElementsToBe(
                By.cssSelector("[data-cy^='table-row-']"), 3));

        Assert.assertEquals(driver.findElements(By.cssSelector("[data-cy^='table-row-']")).size(), 3);
    }

    // ── Add record ────────────────────────────────────────────────────────────

    @Test(description = "Should add a new record via the registration modal")
    public void testAddNewRecord() {
        driver.findElement(By.id("addNewRecordButton")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-cy='modal-first-name']")));

        driver.findElement(By.cssSelector("[data-cy='modal-first-name']")).sendKeys("Jane");
        driver.findElement(By.cssSelector("[data-cy='modal-last-name']")).sendKeys("Smith");
        driver.findElement(By.cssSelector("[data-cy='modal-email']")).sendKeys("jane@example.com");
        driver.findElement(By.cssSelector("[data-cy='modal-age']")).sendKeys("28");
        driver.findElement(By.cssSelector("[data-cy='modal-salary']")).sendKeys("75000");
        driver.findElement(By.cssSelector("[data-cy='modal-department']")).sendKeys("Engineering");

        driver.findElement(By.cssSelector("[data-cy='modal-submit-btn']")).click();

        wait.until(ExpectedConditions.numberOfElementsToBe(
                By.cssSelector("[data-cy^='table-row-']"), 4));

        boolean found = driver.findElements(By.cssSelector("[data-cy^='cell-first-name-']"))
                .stream()
                .anyMatch(el -> el.getText().equals("Jane"));
        Assert.assertTrue(found, "Newly added record 'Jane' should appear in the table");
    }

    @Test(description = "Should close registration modal without saving when cancel is clicked")
    public void testCancelModalDoesNotAddRecord() {
        driver.findElement(By.id("addNewRecordButton")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-cy='modal-first-name']")));
        driver.findElement(By.cssSelector("[data-cy='modal-first-name']")).sendKeys("Ghost");

        driver.findElement(By.cssSelector("[data-cy='modal-cancel-btn']")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[data-cy='modal-overlay']")));

        Assert.assertEquals(
                driver.findElements(By.cssSelector("[data-cy^='table-row-']")).size(), 3,
                "Row count should remain 3 after cancel");
    }

    // ── Delete record ─────────────────────────────────────────────────────────

    @Test(description = "Should delete a record from the table")
    public void testDeleteRecord() {
        driver.findElement(By.cssSelector("[data-cy='delete-btn-1']")).click();

        wait.until(ExpectedConditions.numberOfElementsToBe(
                By.cssSelector("[data-cy^='table-row-']"), 2));

        Assert.assertEquals(
                driver.findElements(By.cssSelector("[data-cy^='table-row-']")).size(), 2,
                "Table should have 2 rows after deleting one");
    }

    @Test(description = "Should delete the correct record by row position")
    public void testDeleteSpecificRecord() {
        String secondRowName = driver.findElement(
                By.cssSelector("[data-cy='cell-first-name-2']")).getText();

        driver.findElement(By.cssSelector("[data-cy='delete-btn-1']")).click();

        wait.until(ExpectedConditions.numberOfElementsToBe(
                By.cssSelector("[data-cy^='table-row-']"), 2));

        // What was row-2 is now row-1 after deletion
        String newFirstRowName = driver.findElement(
                By.cssSelector("[data-cy='cell-first-name-1']")).getText();
        Assert.assertEquals(newFirstRowName, secondRowName,
                "After deleting row 1, the old row 2 should shift up to row 1");
    }

    // ── Edit record ───────────────────────────────────────────────────────────

    @Test(description = "Should edit an existing record via the registration modal")
    public void testEditRecord() {
        driver.findElement(By.cssSelector("[data-cy='edit-btn-1']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-cy='modal-first-name']")));

        WebElement firstNameInput = driver.findElement(By.cssSelector("[data-cy='modal-first-name']"));
        firstNameInput.clear();
        firstNameInput.sendKeys("UpdatedName");

        driver.findElement(By.cssSelector("[data-cy='modal-submit-btn']")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector("[data-cy='modal-overlay']")));

        Assert.assertEquals(
                driver.findElement(By.cssSelector("[data-cy='cell-first-name-1']")).getText(),
                "UpdatedName",
                "First name should be updated after editing");
    }

    @Test(description = "Edit modal should be pre-populated with existing record values")
    public void testEditModalPrePopulated() {
        driver.findElement(By.cssSelector("[data-cy='edit-btn-1']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-cy='modal-first-name']")));

        String firstName = driver.findElement(By.cssSelector("[data-cy='modal-first-name']"))
                .getAttribute("value");
        String lastName = driver.findElement(By.cssSelector("[data-cy='modal-last-name']"))
                .getAttribute("value");

        Assert.assertEquals(firstName, "Cierra", "Modal should be pre-filled with existing first name");
        Assert.assertEquals(lastName, "Vega", "Modal should be pre-filled with existing last name");
    }

    // ── Pagination ────────────────────────────────────────────────────────────

    @Test(description = "Should show page 1 of 1 by default with 3 records")
    public void testDefaultPaginationState() {
        Assert.assertEquals(
                driver.findElement(By.id("current-page-num")).getText(), "1");
        Assert.assertEquals(
                driver.findElement(By.id("total-pages-display")).getText(), "1");
    }

    @Test(description = "Should change rows-per-page and update pagination")
    public void testRowsPerPageSelector() {
        Select rowsSelect = new Select(driver.findElement(By.id("rows-per-page-sel")));
        rowsSelect.selectByValue("5");

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.id("total-pages-display"), "1"));

        Assert.assertEquals(
                driver.findElements(By.cssSelector("[data-cy^='table-row-']")).size(), 3,
                "All 3 rows still visible with page size 5");
    }
}
