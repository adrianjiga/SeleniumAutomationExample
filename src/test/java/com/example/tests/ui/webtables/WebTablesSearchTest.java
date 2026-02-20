package com.example.tests.ui.webtables;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WebTablesSearchTest extends BaseWebTablesTest {

    @Test(description = "Should filter records by first name via search box")
    public void testSearchByFirstName() {
        driver.findElement(By.id("searchBox")).sendKeys("Cierra");
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("[data-cy^='table-row-']"), 1));
        Assert.assertEquals(driver.findElement(By.cssSelector("[data-cy='cell-first-name-1']")).getText(), "Cierra");
    }

    @Test(description = "Should filter records by department via search box")
    public void testSearchByDepartment() {
        driver.findElement(By.id("searchBox")).sendKeys("Legal");
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("[data-cy^='table-row-']"), 1));
        Assert.assertEquals(driver.findElement(By.cssSelector("[data-cy='cell-department-1']")).getText(), "Legal");
    }

    @Test(description = "Should show no rows when search matches nothing")
    public void testSearchWithNoResults() {
        driver.findElement(By.id("searchBox")).sendKeys("zzz_no_match_xyz");
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("[data-cy^='table-row-']"), 0));
        Assert.assertTrue(driver.findElements(By.cssSelector("[data-cy^='table-row-']")).isEmpty(),
                "No rows should be shown for unmatched search");
    }

    @Test(description = "Should restore all records when search is cleared")
    public void testClearSearchRestoresAllRows() {
        WebElement searchBox = driver.findElement(By.id("searchBox"));
        searchBox.sendKeys("Alden");
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("[data-cy^='table-row-']"), 1));

        searchBox.clear();
        // clear() doesn't fire the 'input' event that the JS filter listens to — dispatch it manually
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));", searchBox);

        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("[data-cy^='table-row-']"), 3));
        Assert.assertEquals(driver.findElements(By.cssSelector("[data-cy^='table-row-']")).size(), 3);
    }
}
