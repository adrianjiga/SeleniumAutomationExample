package com.example.tests.ui.webtables;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WebTablesCrudTest extends BaseWebTablesTest {

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

        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("[data-cy^='table-row-']"), 4));
        boolean found = driver.findElements(By.cssSelector("[data-cy^='cell-first-name-']"))
                .stream().anyMatch(el -> el.getText().equals("Jane"));
        Assert.assertTrue(found, "Newly added record 'Jane' should appear in the table");
    }

    @Test(description = "Should close registration modal without saving when cancel is clicked")
    public void testCancelModalDoesNotAddRecord() {
        driver.findElement(By.id("addNewRecordButton")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-cy='modal-first-name']")));
        driver.findElement(By.cssSelector("[data-cy='modal-first-name']")).sendKeys("Ghost");
        driver.findElement(By.cssSelector("[data-cy='modal-cancel-btn']")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("[data-cy='modal-overlay']")));
        Assert.assertEquals(driver.findElements(By.cssSelector("[data-cy^='table-row-']")).size(), 3,
                "Row count should remain 3 after cancel");
    }

    @Test(description = "Should delete a record from the table")
    public void testDeleteRecord() {
        driver.findElement(By.cssSelector("[data-cy='delete-btn-1']")).click();
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("[data-cy^='table-row-']"), 2));
        Assert.assertEquals(driver.findElements(By.cssSelector("[data-cy^='table-row-']")).size(), 2,
                "Table should have 2 rows after deleting one");
    }

    @Test(description = "Should delete the correct record by row position")
    public void testDeleteSpecificRecord() {
        String secondRowName = driver.findElement(By.cssSelector("[data-cy='cell-first-name-2']")).getText();
        driver.findElement(By.cssSelector("[data-cy='delete-btn-1']")).click();
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("[data-cy^='table-row-']"), 2));

        String newFirstRowName = driver.findElement(By.cssSelector("[data-cy='cell-first-name-1']")).getText();
        Assert.assertEquals(newFirstRowName, secondRowName,
                "After deleting row 1, the old row 2 should shift up to row 1");
    }

    @Test(description = "Should edit an existing record via the registration modal")
    public void testEditRecord() {
        driver.findElement(By.cssSelector("[data-cy='edit-btn-1']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-cy='modal-first-name']")));

        WebElement firstNameInput = driver.findElement(By.cssSelector("[data-cy='modal-first-name']"));
        firstNameInput.clear();
        firstNameInput.sendKeys("UpdatedName");
        driver.findElement(By.cssSelector("[data-cy='modal-submit-btn']")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("[data-cy='modal-overlay']")));
        Assert.assertEquals(driver.findElement(By.cssSelector("[data-cy='cell-first-name-1']")).getText(),
                "UpdatedName", "First name should be updated after editing");
    }

    @Test(description = "Edit modal should be pre-populated with existing record values")
    public void testEditModalPrePopulated() {
        driver.findElement(By.cssSelector("[data-cy='edit-btn-1']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-cy='modal-first-name']")));

        Assert.assertEquals(
                driver.findElement(By.cssSelector("[data-cy='modal-first-name']")).getAttribute("value"),
                "Cierra", "Modal should be pre-filled with existing first name");
        Assert.assertEquals(
                driver.findElement(By.cssSelector("[data-cy='modal-last-name']")).getAttribute("value"),
                "Vega", "Modal should be pre-filled with existing last name");
    }
}
