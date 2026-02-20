package com.example.tests.ui.webtables;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WebTablesPaginationTest extends BaseWebTablesTest {

    @Test(description = "Should show page 1 of 1 by default with 3 records")
    public void testDefaultPaginationState() {
        Assert.assertEquals(driver.findElement(By.id("current-page-num")).getText(), "1");
        Assert.assertEquals(driver.findElement(By.id("total-pages-display")).getText(), "1");
    }

    @Test(description = "Should change rows-per-page and update pagination")
    public void testRowsPerPageSelector() {
        new Select(driver.findElement(By.id("rows-per-page-sel"))).selectByValue("5");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("total-pages-display"), "1"));
        Assert.assertEquals(driver.findElements(By.cssSelector("[data-cy^='table-row-']")).size(), 3,
                "All 3 rows still visible with page size 5");
    }
}
